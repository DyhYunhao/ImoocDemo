package com.example.autoupdate.update

/*********************************************
 * @author daiyh
 * 创建日期：
 * 描述： 事件监听回调
 *********************************************
 */
interface UpdateDownloadListener {
    /**
     * 下载请求开始
     */
    public fun onStarted()

    /**
     * 进度更新
     */
    public fun onProgressChanged(progress: Int, downloadUrl: String)

    /**
     * 下载完成
     */
    public fun onFinished(completeSize: Int, downloadUrl: String)

    /**
     * 下载失败
     */
    public fun onFailure()
}