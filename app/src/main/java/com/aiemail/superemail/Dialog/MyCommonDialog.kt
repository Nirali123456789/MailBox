package com.aiemail.superemail.Dialog

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.aiemail.superemail.R
import com.aiemail.superemail.Activities.SettingSubcomponent


class MyCommonDialog(var from:String): DialogFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        getDialog()!!.getWindow()?.setBackgroundDrawableResource(R.drawable.round_corner);

        var v = inflater.inflate(R.layout.layout_common_dialog, container, false)


        v.findViewById<TextView>(R.id.team).setOnClickListener {
            dialog?.dismiss()
            startActivity(Intent(activity, SettingSubcomponent::class.java).putExtra("Value",3).putExtra("Title","Teams"))
           activity!!. overridePendingTransition(R.anim.anim_right, R.anim.anim_right)
            //activity!!.finish()
        }

        v.findViewById<TextView>(R.id.cancel).setOnClickListener {
            dialog!!.dismiss()
        }

     if (from.equals("share"))
     {

     }
        else if(from.equals(""))
     {

     }

        return  v
    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
        val height = (resources.displayMetrics.heightPixels * 0.40).toInt()
        dialog!!.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

}