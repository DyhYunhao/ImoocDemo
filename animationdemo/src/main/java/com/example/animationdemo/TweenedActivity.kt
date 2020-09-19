package com.example.animationdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import kotlinx.android.synthetic.main.activity_tweened.*

/**
 * 补间动画又可以分为四种形式，分别是 alpha（淡入淡出），translate（位移），scale（缩放大小），rotate（旋转）。
 */
class TweenedActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tweened)
        var animation1 = AnimationUtils.loadAnimation(this, R.anim.alpha_anim)
        iv_tween_lu.animation = animation1

        var animation2 = AnimationUtils.loadAnimation(this, R.anim.rotate_anim)
        iv_tween_2.animation = animation2

        var animation3 = AnimationUtils.loadAnimation(this, R.anim.scale_anim)
        iv_tween_3.animation = animation3

        var animation4 = AnimationUtils.loadAnimation(this, R.anim.translate_anim)
        iv_tween_4.animation = animation4

//        btn_alpha.setOnClickListener {
//            var animation = AnimationUtils.loadAnimation(this, R.anim.alpha_anim)
//            iv_tween_lu.animation = animation
//        }

//        btn_rotate.setOnClickListener {
//            var animation = AnimationUtils.loadAnimation(this, R.anim.rotate_anim)
//            iv_tween_lu.animation = animation
//        }
//
//        btn_scale.setOnClickListener {
//            var animation = AnimationUtils.loadAnimation(this, R.anim.scale_anim)
//            iv_tween_lu.animation = animation
//        }
//
//        btn_translate.setOnClickListener {
//            var animation = AnimationUtils.loadAnimation(this, R.anim.translate_anim)
//            iv_tween_lu.animation = animation
//        }
    }
}