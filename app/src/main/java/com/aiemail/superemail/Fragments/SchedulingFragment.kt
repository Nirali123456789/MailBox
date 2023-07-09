package com.aiemail.superemail.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.fragment.app.Fragment
import com.aiemail.superemail.R
import com.aiemail.superemail.utilis.PreferenceManager

/**
 * A simple [Fragment] subclass.
 * Use the [SchedulingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
class SchedulingFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var preferenceManager: PreferenceManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        preferenceManager = this.context?.let { PreferenceManager(it) }!!
        preferenceManager.SetUpPreference()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       var view= inflater.inflate(R.layout.layout_snooze_fragment, container, false)
        CheckEvents(view)
        return  view
    }

    private fun CheckEvents(view: View) {
        if (preferenceManager.getBoolean("check1")) {
            view!!.findViewById<CheckBox>(R.id.check1).isChecked = true
        } else {
            view!!.findViewById<CheckBox>(R.id.check1).isChecked = false
        }
        view!!.findViewById<CheckBox>(R.id.check1).setOnCheckedChangeListener { buttonview, checked ->
            if(checked)
            {
                preferenceManager.SetBoolean("check1",true)
            }else
                preferenceManager.SetBoolean("check1",false)


        }


        if (preferenceManager.getBoolean("check1")) {
            view!!.findViewById<CheckBox>(R.id.check2).isChecked = true
        } else {
            view!!.findViewById<CheckBox>(R.id.check2).isChecked = false
        }
        view!!.findViewById<CheckBox>(R.id.check2).setOnCheckedChangeListener { buttonview, checked ->
            if(checked)
            {
                preferenceManager.SetBoolean("check2",true)
            }else
                preferenceManager.SetBoolean("check2",false)


        }

        if (preferenceManager.getBoolean("check3")) {
            view!!.findViewById<CheckBox>(R.id.check3).isChecked = true
        } else {
            view!!.findViewById<CheckBox>(R.id.check3).isChecked = false
        }
        view!!.findViewById<CheckBox>(R.id.check3).setOnCheckedChangeListener { buttonview, checked ->
            if(checked)
            {
                preferenceManager.SetBoolean("check3",true)
            }else
                preferenceManager.SetBoolean("check3",false)


        }

        if (preferenceManager.getBoolean("check4")) {
            view!!.findViewById<CheckBox>(R.id.check4).isChecked = true
        } else {
            view!!.findViewById<CheckBox>(R.id.check4).isChecked = false
        }
        view!!.findViewById<CheckBox>(R.id.check4).setOnCheckedChangeListener { buttonview, checked ->
            if(checked)
            {
                preferenceManager.SetBoolean("check4",true)
            }else
                preferenceManager.SetBoolean("check4",false)


        }

        if (preferenceManager.getBoolean("check5")) {
            view!!.findViewById<CheckBox>(R.id.check5).isChecked = true
        } else {
            view!!.findViewById<CheckBox>(R.id.check5).isChecked = false
        }
        view!!.findViewById<CheckBox>(R.id.check5).setOnCheckedChangeListener { buttonview, checked ->
            if(checked)
            {
                preferenceManager.SetBoolean("check5",true)
            }else
                preferenceManager.SetBoolean("check5",false)


        }

        if (preferenceManager.getBoolean("check6")) {
            view!!.findViewById<CheckBox>(R.id.check6).isChecked = true
        } else {
            view!!.findViewById<CheckBox>(R.id.check6).isChecked = false
        }
        view!!.findViewById<CheckBox>(R.id.check6).setOnCheckedChangeListener { buttonview, checked ->
            if(checked)
            {
                preferenceManager.SetBoolean("check6",true)
            }else
                preferenceManager.SetBoolean("check6",false)


        }

        if (preferenceManager.getBoolean("check7")) {
            view!!.findViewById<CheckBox>(R.id.check7).isChecked = true
        } else {
            view!!.findViewById<CheckBox>(R.id.check7).isChecked = false
        }
        view!!.findViewById<CheckBox>(R.id.check7).setOnCheckedChangeListener { buttonview, checked ->
            if(checked)
            {
                preferenceManager.SetBoolean("check6",true)
            }else
                preferenceManager.SetBoolean("check6",false)


        }


    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SmartFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SchedulingFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}