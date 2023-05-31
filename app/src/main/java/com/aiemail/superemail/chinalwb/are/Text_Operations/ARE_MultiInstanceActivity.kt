package com.chinalwb.are.demo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.chinalwb.are.AREditor
import com.aiemail.superemail.R

class ARE_MultiInstanceActivity : AppCompatActivity() {

    var activeARE : AREditor? = null

   // private val areFocusChangeListener = ARE_FocusChangeListener { arEditor, hasFocus -> if (hasFocus && arEditor != null) activeARE = arEditor }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_are__multi_instance)

//        are_1.setAreFocusChangeListener(areFocusChangeListener)
//        are_2.setAreFocusChangeListener(areFocusChangeListener)
//        are_3.setAreFocusChangeListener(areFocusChangeListener)
//        are_4.setAreFocusChangeListener(areFocusChangeListener)

       // activeARE = are_1
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        this.activeARE?.onActivityResult(requestCode, resultCode, data)
    }
}
