package com.aiemail.superemail.Dialog

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.google.android.material.timepicker.MaterialTimePicker
import com.aiemail.superemail.R
import com.aiemail.superemail.Activities.SnoozeActivity


class MyCustomDialog(layout1:Int): DialogFragment() {
  var layout=layout1
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        getDialog()!!.getWindow()?.setBackgroundDrawableResource(R.drawable.round_corner);


        var v = inflater.inflate(layout, container, false)
        if (layout==R.layout.layout_discard_dialog) {
            v.findViewById<TextView>(R.id.discard).setOnClickListener {
                dialog?.dismiss()
                activity!!.finish()
                activity!!. overridePendingTransition(R.anim.slide_in_left,
                    R.anim.slide_out);
            }

            v.findViewById<TextView>(R.id.cancel).setOnClickListener {
            dialog!!.dismiss()
            }
            v.findViewById<TextView>(R.id.save).setOnClickListener {
            dialog!!.dismiss()
                activity!!.finish()
                activity!!. overridePendingTransition(R.anim.slide_in_left,
                        R.anim.slide_out);

            }
        }
        if (layout==R.layout.layout_logout) {


            v.findViewById<TextView>(R.id.cancel).setOnClickListener {
                dialog!!.dismiss()
            }
            v.findViewById<TextView>(R.id.save).setOnClickListener {
                dialog!!.dismiss()
                activity!!.finish()
            }

        }
        if (layout==R.layout.layout_snooze_dialog) {
            v.findViewById<TextView>(R.id.pick).setOnClickListener {
               // dialog!!.dismiss()
                MaterialTimePicker
                    .Builder()
                    .setTitleText("Select a time")
                    .build()
                    .show(childFragmentManager, "TIME_PICKER")
            }
            v.findViewById<ImageView>(R.id.setting).setOnClickListener {
                dialog!!.dismiss()
                activity!!.startActivity(Intent(activity, SnoozeActivity::class.java))
               activity!!.overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up)
            }
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