package com.aiemail.superemail.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.aiemail.superemail.R
import com.aiemail.superemail.feature.Slideshow.OnBoardingActivity
import com.aiemail.superemail.feature.utilis.Constant

import com.aiemail.superemail.prefs

class FirstScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

 Constant.SetUpFullScreen(window)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (!prefs.islogin) {
            Handler(Looper.myLooper()!!).postDelayed(Runnable {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }, 3100)
        }else
        {
            if( !prefs.intExamplePref) {
                Handler(Looper.myLooper()!!).postDelayed(Runnable {
                    startActivity(Intent(this, OnBoardingActivity::class.java))
                    finish()
                }, 3100)
            }
            else
            {
                Handler(Looper.myLooper()!!).postDelayed(Runnable {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }, 3100)
            }
        }

    }
}
