package com.example.animationdemo

import android.graphics.drawable.AnimationDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

/**
 * 帧动画：更多的依赖于完善的UI资源，他的原理就是将一张张单独的图片连贯的进行播放，从而在视觉上产生一种动画的效果
 * android：oneshot="false" ，这个oneshot 的含义就是动画执行一次（true）还是循环执行多次
 */
class FrameOne : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_frame_one)

        var animationFrameOne = findViewById<ImageView>(R.id.img_frame_1)
        (animationFrameOne.drawable as AnimationDrawable).start()
    }
}