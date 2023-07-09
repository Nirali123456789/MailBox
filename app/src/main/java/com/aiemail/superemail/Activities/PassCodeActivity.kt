package com.aiemail.superemail.Activities


import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.aiemail.superemail.R
import com.aiemail.superemail.utilis.PreferenceManager
import com.hanks.passcodeview.PasscodeView
import com.hanks.passcodeview.PasscodeView.PasscodeViewListener


class PassCodeActivity : AppCompatActivity() {
    // initialize variable passcodeview
    lateinit var passcodeView: PasscodeView
    lateinit var preferenceManager: PreferenceManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pass_code)

        PasswordSetUp()
    }

    private fun PasswordSetUp() {
        preferenceManager=com.aiemail.superemail.utilis.PreferenceManager(this)
        preferenceManager.SetUpPreference()
        passcodeView = findViewById<PasscodeView>(R.id.passcodeView)


        //First Time Set password
//        if (preferenceManager.getString("Passcode").equals(""))
//        {
//
//        }

        // to set length of password as here
        // we have set the length as 5 digits

        // to set length of password as here
        // we have set the length as 5 digits
        passcodeView.listener = object : PasscodeViewListener {
            override fun onFail(wrongNumber: String) {
                Toast.makeText(application, "Wrong!!", Toast.LENGTH_SHORT).show()
            }

            override fun onSuccess(number: String) {
                preferenceManager.SetBoolean("PassLock",true)
                preferenceManager.SetString("Passcode",number)
                Toast.makeText(application, "finish", Toast.LENGTH_SHORT).show()
                onBackPressed()
            }
        }
    }
}