package com.aiemail.superemail.Dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.google.android.material.tabs.TabLayout
import com.aiemail.superemail.R


class ChooseDialog() : DialogFragment() {
    lateinit var tabLayout: TabLayout
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog()!!.getWindow()?.setBackgroundDrawableResource(R.drawable.round_corner);

        var v = inflater.inflate(R.layout.layout_choose, container, false)
        tabLayout = v.findViewById(R.id.tabLayout_choose)

        tabLayout.addTab(tabLayout.newTab())
        tabLayout.addTab(tabLayout.newTab())
        tabLayout.addTab(tabLayout.newTab())
        createTabIcons()
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab != null) {

                    if (tab.position == 0) {


                        (tabLayout.getTabAt(0)!!.customView as ImageView).setImageResource(R.drawable.smart2_selected)


                        // tabLayout.getTabAt(0)!!.customView=tabOne
                        (tabLayout.getTabAt(1)!!.customView!! as ImageView).setImageResource(R.drawable.smart)
                        (tabLayout.getTabAt(2)!!.customView!! as ImageView).setImageResource(R.drawable.classic)

                    }
                    if (tab!!.position == 1) {
                        (tabLayout.getTabAt(0)!!.customView!! as ImageView).setImageResource(R.drawable.smart_2)

                        // tabLayout.getTabAt(0)!!.customView=tabOne
                        (tabLayout.getTabAt(1)!!.customView!! as ImageView).setImageResource(R.drawable.smart_selection)
                        (tabLayout.getTabAt(2)!!.customView!! as ImageView).setImageResource(R.drawable.classic)

                    }
                    if (tab!!.position == 2) {
                        (tabLayout.getTabAt(0)!!.customView!! as ImageView).setImageResource(R.drawable.smart_2)

                        // tabLayout.getTabAt(0)!!.customView=tabOne
                        (tabLayout.getTabAt(1)!!.customView!! as ImageView).setImageResource(R.drawable.smart)
                        (tabLayout.getTabAt(2)!!.customView!! as ImageView).setImageResource(R.drawable.classic_selection)

                    }
                }
            }


            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })

        return v
    }
    override fun getTheme(): Int {
        return R.style.DialogStyle
    }

    private fun createTabIcons() {
        val layout =
            LayoutInflater.from(activity).inflate(R.layout.custom_tab, null)
        // tabOne.text = "Tab 1"
        var tabOne = layout.findViewById<ImageView>(R.id.image)
        tabOne.setImageResource(R.drawable.smart2_selected)
        var tabOnetext = layout.findViewById<TextView>(R.id.tab)
        tabOnetext.setText("Smart 2.0")
        tabLayout.getTabAt(0)!!.customView = layout

        val layout2 =
            LayoutInflater.from(activity).inflate(R.layout.custom_tab, null)
        var tabTwo = layout2.findViewById<ImageView>(R.id.image)
        tabTwo.setImageResource(R.drawable.smart)
        var tabTwotext = layout.findViewById<TextView>(R.id.tab)
        tabTwotext.setText("Smart 2.0")

        tabLayout.getTabAt(1)!!.customView = layout2
        val layout3 =
            LayoutInflater.from(activity).inflate(R.layout.custom_tab, null)
        var tabThree = layout3.findViewById<ImageView>(R.id.image)
        // tabTwo.text = "Tab 2"
        tabThree.setImageResource(R.drawable.classic)
        tabLayout.getTabAt(2)!!.customView = layout3
        var tabThreetext = layout.findViewById<TextView>(R.id.tab)
        tabThreetext.setText("Smart 2.0")


        // tabLayout.getTabAt(2)!!.customView = tabThree
    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
        val height = (resources.displayMetrics.heightPixels * 0.40).toInt()
        dialog!!.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

}