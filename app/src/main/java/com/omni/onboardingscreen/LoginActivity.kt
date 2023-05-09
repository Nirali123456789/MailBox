package com.omni.onboardingscreen

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.omni.onboardingscreen.databinding.ActivityLoginBinding
import com.omni.onboardingscreen.databinding.SettingAccountLayoutBinding
import com.omni.onboardingscreen.feature.MainActivity1
import com.omni.onboardingscreen.feature.onboarding.OnBoardingActivity
import com.omni.onboardingscreen.global.prefs

class LoginActivity:AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
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
        binding= ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.login.setOnClickListener {
            prefs.islogin=true
            if( !prefs.intExamplePref) {
                Handler(Looper.myLooper()!!).postDelayed(Runnable {
                    startActivity(Intent(this, OnBoardingActivity::class.java))
                    finish()
                }, 3100)
            }
            else
            {
                Handler(Looper.myLooper()!!).postDelayed(Runnable {
                    startActivity(Intent(this, MainActivity1::class.java))
                    finish()
                }, 3100)
            }

        }
    }
}