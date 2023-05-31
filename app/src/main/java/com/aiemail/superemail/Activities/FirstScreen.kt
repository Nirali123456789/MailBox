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

import com.aiemail.superemail.prefs

class FirstScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        val windowInsetsController =
            WindowCompat.getInsetsController(window, window.decorView)
        // Configure the behavior of the hidden system bars.
        windowInsetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        // Add a listener to update the behavior of the toggle fullscreen button when
        // the system bars are hidden or revealed.
        window.decorView.setOnApplyWindowInsetsListener { view, windowInsets ->
            // You can hide the caption bar even when the other system bars are visible.
            // To account for this, explicitly check the visibility of navigationBars()
            // and statusBars() rather than checking the visibility of systemBars().
            if (windowInsets.isVisible(WindowInsetsCompat.Type.navigationBars())
                || windowInsets.isVisible(WindowInsetsCompat.Type.statusBars())
            ) {
                windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())

            } else {
            }
            view.onApplyWindowInsets(windowInsets)
        }
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
