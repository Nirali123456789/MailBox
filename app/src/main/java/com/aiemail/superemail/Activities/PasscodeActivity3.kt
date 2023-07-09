package com.aiemail.superemail.Activities

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.aiemail.superemail.R
import com.aiemail.superemail.utilis.PreferenceManager
import com.hanks.passcodeview.PasscodeView
import com.hanks.passcodeview.PasscodeView.PasscodeViewListener

class PasscodeActivity3 : AppCompatActivity() {
    lateinit var preferenceManager: PreferenceManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= 21) {
            val decorView = window.decorView
            val option = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
            decorView.systemUiVisibility = option
            window.statusBarColor = Color.TRANSPARENT
        }
        setContentView(R.layout.layout_passcode3)

        preferenceManager = com.aiemail.superemail.utilis.PreferenceManager(this)
        preferenceManager.SetUpPreference()
        val passcodeView = findViewById<View>(R.id.passcodeView) as PasscodeView
        passcodeView
            .setPasscodeLength(4)
            .setLocalPasscode(preferenceManager.getString("Passcode")).listener = object : PasscodeViewListener {
            override fun onFail(wrongNumber: String) {
                Toast.makeText(application, "Wrong!!", Toast.LENGTH_SHORT).show()
            }

            override fun onSuccess(number: String) {
                Toast.makeText(application, "finish", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@PasscodeActivity3, MainActivity::class.java))
                finish()
            }
        }
    }
}