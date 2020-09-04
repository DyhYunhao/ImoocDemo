package com.example.autoupdate.update

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.autoupdate.R
import java.io.File

/*********************************************
 * @author daiyh
 * 创建日期：2020-9-4
 * 描述：app更新下载后台服务
 *********************************************
 */
class UpdateService : Service() {
    private lateinit var apkURL: String
    private lateinit var filePath: String
    private lateinit var notificationManager: NotificationManager
    private lateinit var mNotification: Notification

    override fun onCreate() {
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        filePath = getExternalFilesDir(null).toString() + "/update/AutoUpdate.apk"
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent == null) {
            notifyUser(
                getString(R.string.update_download_failed),
                getString(R.string.update_download_failed_msg), 0
            )
            stopSelf()
        }
        apkURL = intent?.getStringExtra("apkUrl").toString()
        notifyUser(
            getString(R.string.update_download_start),
            getString(R.string.update_download_start), 0
        )
        startDownload()
        return super.onStartCommand(intent, flags, startId)
    }

    private fun startDownload() {
        UpdateManager().getInstance()
            .startDownloads(apkURL, filePath, object : UpdateDownloadListener {
                override fun onStarted() {

                }

                override fun onProgressChanged(progress: Int, downloadUrl: String) {
                    notifyUser(
                        getString(R.string.update_download_processing),
                        getString(R.string.update_download_processing), progress
                    )
                }

                override fun onFinished(completeSize: Int, downloadUrl: String) {
                    notifyUser(
                        getString(R.string.update_download_finish),
                        getString(R.string.update_download_finish), 100
                    )
                    stopSelf()
                }

                override fun onFailure() {
                    notifyUser(
                        getString(R.string.update_download_failed),
                        getString(R.string.update_download_failed_msg), 0
                    )
                    stopSelf()
                }

            })
    }

    /**
     * 更新notification来告知用户当前的下载进度
     */
    private fun notifyUser(result: String, reason: String, progress: Int) {
        var builder: NotificationCompat.Builder =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationCompat.Builder(this, NotificationChannel.DEFAULT_CHANNEL_ID)
            } else {
                NotificationCompat.Builder(this)
            }
        builder.setSmallIcon(R.drawable.ic_launcher_foreground)
            .setLargeIcon(
                BitmapFactory.decodeResource(
                    resources,
                    R.drawable.ic_launcher_foreground
                )
            )
            .setContentTitle(getString(R.string.app_name))
        if (progress in 1..99) {
            builder.setProgress(100, progress, false)
        } else {
            builder.setProgress(0, 0, false)
        }
        builder.setAutoCancel(true)
        builder.setWhen(System.currentTimeMillis())
        builder.setTicker(result)
        builder.setContentIntent(
            if (progress >= 100) getContentIntent() else PendingIntent.getActivity(
                this,
                0,
                Intent(),
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        )
        mNotification = builder.build()
        notificationManager.notify(0, mNotification)
    }

    private fun getContentIntent(): PendingIntent? {
        var apkFile = File(filePath)
        var intent = Intent(Intent.ACTION_VIEW)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.setDataAndType(
            Uri.parse("file://" + apkFile.absolutePath),
            "application/vnd.android.package-archive"
        )
        return PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }
}