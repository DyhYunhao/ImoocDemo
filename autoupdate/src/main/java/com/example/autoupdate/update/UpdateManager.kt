package com.example.autoupdate.update

import java.io.File
import java.lang.Exception
import java.net.URL
import java.util.concurrent.Executors
import java.util.concurrent.Future
import java.util.concurrent.ThreadPoolExecutor

/*********************************************
 * @author daiyh
 * 创建日期：2020-9-4
 * 描述：下载调度管理器，调用我们的UpdateDownloadRequest
 *********************************************
 */
class UpdateManager {
    private lateinit var threadPoolExecutor: ThreadPoolExecutor
    private lateinit var request: UpdateDownloadRequest

    constructor(){
        threadPoolExecutor = Executors.newCachedThreadPool() as ThreadPoolExecutor
    }
    companion object{
        var manager: UpdateManager = UpdateManager()
    }

    fun getInstance(): UpdateManager {
        return manager
    }

    fun startDownloads(downloadUrl: String, localPath: String, listener: UpdateDownloadListener) {
        if (request != null) {
            return
        }
        checkLocalFilePath(localPath)

        //开始真正的下载
        request = UpdateDownloadRequest(downloadUrl, localPath, listener)
        var future: Future<*> = threadPoolExecutor.submit(request)
    }

    private fun checkLocalFilePath(localPath: String) {
        var dir: File = File(localPath.substring(0, localPath.lastIndexOf("/") + 1))
        if (!dir.exists()) {
            dir.mkdir()
        }
        var file: File = File(localPath)
        if (!file.exists()) {
            try {
                file.createNewFile()
            } catch (e: Exception){
            }
        }
    }

}