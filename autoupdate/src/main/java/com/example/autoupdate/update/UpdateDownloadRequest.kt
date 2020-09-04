package com.example.autoupdate.update

import android.os.Handler
import android.os.Looper
import android.os.Message
import java.io.IOException
import java.io.InputStream
import java.io.RandomAccessFile
import java.net.HttpURLConnection
import java.net.URL
import java.text.DecimalFormat

/*********************************************
 * @author daiyh
 * 创建日期：2020-9-1
 * 描述：处理文件的下载和线程间的通信
 *********************************************
 */
class UpdateDownloadRequest : Runnable {

    private lateinit var downloadUrl: String
    private lateinit var localFilePath: String
    private lateinit var downloadListener: UpdateDownloadListener
    private var isDownloading = false
    private var currentLength: Long = 0
    private lateinit var downloadHandler: DownloadResponseHandler

    companion object {
        const val SUCCESS_MESSAGE: Int = 0
        const val FAILURE_MESSAGE: Int = 1
        const val START_MESSAGE: Int = 2
        const val FINISH_MESSAGE: Int = 3
        const val NETWORK_OFF: Int = 4
        const val PROGRESS_CHANGED: Int = 5
    }

    constructor(
        downloadUrl: String,
        localFilePath: String,
        downloadListener: UpdateDownloadListener
    ) {
        this.downloadUrl = downloadUrl
        this.downloadListener = downloadListener
        this.localFilePath = localFilePath
        this.isDownloading = true
        this.downloadHandler = DownloadResponseHandler()
    }

    //真正去建立连接的方法
    private fun makeRequest() {
        if (!Thread.currentThread().isInterrupted) {
            try {
                var url: URL = URL(downloadUrl)
                var connection: HttpURLConnection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.connectTimeout = 5000
                connection.setRequestProperty("Connection", "Keep-Alive")
                //阻塞我们当前的线程
                connection.connect()
                currentLength = connection.contentLength.toLong()
                if (!Thread.currentThread().isInterrupted) {
                    //真正完成文件的下载
                    downloadHandler.sendResponseMessage(connection.inputStream)
                }
            } catch (e: IOException) {
            }
        }
    }

    override fun run() {
        try {
            makeRequest()
        } catch (e: IOException) {
        } catch (e: InterruptedException) {
        }

    }

    /**
     * 格式化数字
     */
    private fun getTwoPointFloatStr(value: Float): String {
        return DecimalFormat("0.00").format(value)
    }

    /**
     * 下载过程中可能出现的异常情况
     */
    enum class FailureCode {
        UnknownHost, Socket, SocketTimeout, ConnectTimeout,
        IO, HttpResponse, JSON, Interrupted
    }

    /**
     * 用来真正下载文件并发送消息和回调的接口
     */
    inner class DownloadResponseHandler {
        private var mCompleteSize: Int = 0
        private var progress: Int = 0
        private lateinit var handler: Handler

        constructor() {
            handler = object : Handler(Looper.getMainLooper()) {
                override fun handleMessage(msg: Message) {
                    handleSelfMessage(msg)
                }
            }
        }

        /**
         * 发送不同消息的对象
         */
        private fun sendMessage(msg: Message) {
            if (handler != null) {
                handler.sendMessage(msg)
            } else {
                handleSelfMessage(msg)
            }
        }

        protected fun sendFinishMessage() {
            sendMessage(obtainMessage(FINISH_MESSAGE, null))
        }

        private fun sendProgressChangedMessage(progress: Int) {
            sendMessage(obtainMessage(PROGRESS_CHANGED, arrayOf(progress)))
        }

        protected fun sendFailureMessage(failureCode: FailureCode) {
            sendMessage(obtainMessage(FAILURE_MESSAGE, arrayOf(failureCode)))
        }

        protected fun obtainMessage(responseMessage: Int, response: Any?): Message {
            var msg: Message? = null
            if (handler != null) {
                msg = handler.obtainMessage(responseMessage, response)
            } else {
                msg = Message.obtain()
                msg.what = responseMessage
                msg.obj = response
            }
            return msg!!
        }

        protected fun handleSelfMessage(msg: Message) {
            var response: Array<Any>
            when (msg.what) {
                FAILURE_MESSAGE -> {
                    response = arrayOf(msg.obj as Array<*>)
                    handlerFailureMessage(response[0] as FailureCode)
                }
                PROGRESS_CHANGED -> {
                    response = arrayOf(msg.obj as Array<*>)
                    handlerProgressChangedMessage(response[0] as Int)
                }
                FINISH_MESSAGE -> {
                    onFinish()
                }
            }
        }

        /**
         * 各种消息的处理
         */
        protected fun handlerProgressChangedMessage(i: Int) {

        }

        protected fun handlerFailureMessage(failureCode: UpdateDownloadRequest.FailureCode) {
            onFailure(failureCode)
        }

        fun onFailure(failureCode: UpdateDownloadRequest.FailureCode) {
            downloadListener.onFailure()
        }

        fun onFinish() {
            downloadListener.onFinished(mCompleteSize, "")
        }

        /**
         * 文件下载方法，会发送各种类型的事件
         */
        fun sendResponseMessage(ins: InputStream) {
            var randomAccessFile: RandomAccessFile? = null
            mCompleteSize = 0
            try {
                var buffer = ByteArray(1024)
                var length: Int = -1
                var limit: Int = 1
                randomAccessFile = RandomAccessFile(localFilePath, "rwd")
                length = ins.read(buffer)
                while (length != -1) {
                    if (isDownloading) {
                        randomAccessFile.write(buffer, 0, length)
                        mCompleteSize += length
                        if (mCompleteSize < currentLength) {
                            progress =
                                getTwoPointFloatStr((mCompleteSize / currentLength) as Float).toInt()
                            if (limit / 30 == 0 || progress <= 100) {
                                //为了限制一下我们notification的更新频率
                                sendProgressChangedMessage(progress)
                            }
                            limit++
                        }
                    }
                }

                sendFinishMessage()
            } catch (e: Exception) {
                sendFailureMessage(FailureCode.IO)
            } finally {
                ins.close()
                randomAccessFile?.close()
            }
        }


    }
}
