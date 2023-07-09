package com.aiemail.superemail.Activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.appcompat.app.AppCompatActivity
import com.aiemail.superemail.R
import com.aiemail.superemail.Slideshow.OnBoardingActivity
import com.aiemail.superemail.prefs
import com.aiemail.superemail.utilis.Helpers
import com.aiemail.superemail.utilis.PreferenceManager

class SplashScreenActivity : AppCompatActivity() {
    lateinit var preferenceManager: PreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {

        Helpers.SetUpFullScreen(window)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //Hide navigation bar and status bar.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.let {
                // Default behavior is that if navigation bar is hidden, the system will "steal" touches
                // and show it again upon user's touch. We just want the user to be able to show the
                // navigation bar by swipe, touches are handled by custom code -> change system bar behavior.
                // Alternative to deprecated SYSTEM_UI_FLAG_IMMERSIVE.
                it.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                // make navigation bar translucent (alternative to deprecated
                // WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
                // - do this already in hideSystemUI() so that the bar
                // is translucent if user swipes it up

                // Finally, hide the system bars, alternative to View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                // and SYSTEM_UI_FLAG_FULLSCREEN.
                it.hide(WindowInsets.Type.systemBars())
            }
        } else {
            Helpers.hideSystemUI(this.window.decorView)




        }
        SetUpUI()
    }

    private fun SetUpUI() {
        preferenceManager = com.aiemail.superemail.utilis.PreferenceManager(this)
        preferenceManager.SetUpPreference()
        if (!preferenceManager.getBoolean("PRIVACY_CHECKED")) {

            startActivity(Intent(this, PrivacyPolicyActivty::class.java))
            finish()
        } else {
            if (!prefs.islogin) {
                Handler(Looper.myLooper()!!).postDelayed(Runnable {
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }, 3100)
            } else {
                if (!prefs.intExamplePref) {
                    Handler(Looper.myLooper()!!).postDelayed(Runnable {
                        startActivity(Intent(this, OnBoardingActivity::class.java))
                        finish()
                    }, 3100)
                } else {

                    if (preferenceManager.getBoolean("PassLock")) {
                        if (!preferenceManager.getString("Passcode").equals("")) {
                            Handler(Looper.myLooper()!!).postDelayed(Runnable {

                                startActivity(Intent(this, PasscodeActivity3::class.java))
                                finish()

                            }, 3100)
                        }
                    } else {

                        Handler(Looper.myLooper()!!).postDelayed(Runnable {
                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
                        }, 3100)
                    }

                }
            }

        }

    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        }
    }
}

