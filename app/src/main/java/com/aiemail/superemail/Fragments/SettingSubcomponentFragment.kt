package com.aiemail.superemail.Fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.aiemail.superemail.Activities.ColorCodeActivity
import com.aiemail.superemail.R
import com.aiemail.superemail.Activities.TeamActivity
import com.aiemail.superemail.feature.FragmentAdapter.main.FragmentAdapterSchedule
import com.aiemail.superemail.feature.FragmentAdapter.main.FragmentpageAdapter
import com.aiemail.superemail.feature.viewmodel.MainViewModel


class SettingSubcomponentFragment : Fragment(),TabLayoutMediator.TabConfigurationStrategy {
    lateinit var tabLayout: TabLayout
    lateinit var viewPager2: ViewPager2
    lateinit var adapter: FragmentpageAdapter

    companion object {
        fun newInstance( fragmentMain: Int):Fragment {
            val args = Bundle()
            args.putInt("layout", fragmentMain)
            val fragment = SettingSubcomponentFragment()
            fragment.setArguments(args)
            return fragment
        }
    }
 var layout:Int=R.layout.fragment_main
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        val args = arguments
        layout = args?.getInt("layout", 0)!!
        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        var view= inflater.inflate(layout, container, false)
        if (layout==R.layout.layout_teams)
        {
            view.findViewById<TextView>(R.id.remove).setOnClickListener {
                startActivity(Intent(activity, TeamActivity::class.java))
                activity!!.overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up)
            }
        }
        if (layout==R.layout.layout_smartinbox) {
            tabLayout = view.findViewById(R.id.tabLayout)
            viewPager2 = view.findViewById(R.id.viewpager)

            adapter = FragmentpageAdapter(childFragmentManager, lifecycle)

            tabLayout.addTab(tabLayout.newTab())
            tabLayout.addTab(tabLayout.newTab())
            createTabIcons()
            viewPager2.adapter = adapter

            tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    if (tab != null) {
                        viewPager2.currentItem=tab.position
                        if (tab.position==0)


                            (tabLayout.getTabAt(0)!!.customView!! as ImageView).setImageResource(R.drawable.smart2_selected)

                            // tabLayout.getTabAt(0)!!.customView=tabOne
                            (tabLayout.getTabAt(1)!!.customView!! as ImageView).setImageResource(R.drawable.smart)

                        }
                        if (tab!!.position==1)
                        {
                            (tabLayout.getTabAt(0)!!.customView!! as ImageView).setImageResource(R.drawable.smart_2)

                            // tabLayout.getTabAt(0)!!.customView=tabOne
                            (tabLayout.getTabAt(1)!!.customView!! as ImageView).setImageResource(R.drawable.smart_selection)
                        }
                    }


                override fun onTabUnselected(tab: TabLayout.Tab?) {
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                }

            })

        }
        if (layout==R.layout.layout_email)
        {
           var add = view.findViewById<TextView>(R.id.add_txt)
            add.setOnClickListener {
                var bottomSheetDialog =  BottomSheetDialog(activity!!,R.style.BottomSheetDialogcustom);
                bottomSheetDialog.setContentView(R.layout.layout_bottom_email);
                //  bottomSheetDialog!!.getWindow()?.setBackgroundDrawableResource(R.drawable.round_corner);
                bottomSheetDialog.show()
            }
            var signin_layout = view.findViewById<RelativeLayout>(R.id.signin_layout)
            signin_layout.setOnClickListener {
                var bottomSheetDialog =  BottomSheetDialog(activity!!,R.style.BottomSheetDialogcustom);
                bottomSheetDialog.setContentView(R.layout.layout_bottom_email);
                //  bottomSheetDialog!!.getWindow()?.setBackgroundDrawableResource(R.drawable.round_corner);
                bottomSheetDialog.show()
            }
            var disable=view.findViewById<TextView>(R.id.disable).setOnClickListener {
                activity!!.startActivity(Intent( activity, ColorCodeActivity::class.java))
            }
        }
        if (layout==R.layout.layout_sceduling)
        {
            tabLayout = view.findViewById(R.id.sceduletab)
            viewPager2 = view.findViewById(R.id.sceduleviewpager)

           var adapter = FragmentAdapterSchedule(childFragmentManager, lifecycle)
            tabLayout.addTab(tabLayout.newTab().setText("Snoozes").setIcon(R.drawable.ic_clock))
            tabLayout.addTab(tabLayout.newTab().setText("Reminders").setIcon(R.drawable.ic_circle))
            tabLayout.addTab(tabLayout.newTab().setText("Send later").setIcon(R.drawable.ic_send))
            tabLayout.tabGravity = TabLayout.GRAVITY_FILL
            viewPager2.adapter = adapter
            TabLayoutMediator(tabLayout, viewPager2, this).attach()
            tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    if (tab != null) {
                        viewPager2.currentItem=tab.position


                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                    //( tab?.customView as ImageView) .setImageResource(R.drawable.transparent_bg)
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                }

            })
        }
        return  view;
    }
    private fun createTabIcons() {
        val tabOne =
            LayoutInflater.from(activity).inflate(R.layout.tab_layout, null) as ImageView
       // tabOne.text = "Tab 1"
        tabOne.setImageResource(R.drawable.smart2_selected)

        tabLayout.getTabAt(0)!!.customView=tabOne
        val tabTwo =
            LayoutInflater.from(activity).inflate(R.layout.tab_layout, null) as ImageView
       // tabTwo.text = "Tab 2"
        tabTwo.setImageResource(R.drawable.smart)
        tabLayout.getTabAt(1)!!.customView=tabTwo

       // tabLayout.getTabAt(2)!!.customView = tabThree
    }

    private fun createTabIconsscedule() {
        val tabOne =
            LayoutInflater.from(activity).inflate(R.layout.tab_layout, null) as ImageView
        // tabOne.text = "Tab 1"
        tabOne.setImageResource(R.drawable.pic4)
        tabLayout.getTabAt(0)!!.customView=tabOne
        val tabTwo =
            LayoutInflater.from(activity).inflate(R.layout.tab_layout, null) as ImageView
        // tabTwo.text = "Tab 2"
        tabTwo.setImageResource(R.drawable.pic4)
        tabLayout.getTabAt(1)!!.customView=tabTwo
        val tabthree =
            LayoutInflater.from(activity).inflate(R.layout.tab_layout, null) as ImageView
        // tabTwo.text = "Tab 2"
        tabTwo.setImageResource(R.drawable.pic4)
        tabLayout.getTabAt(2)!!.customView=tabTwo

        // tabLayout.getTabAt(2)!!.customView = tabThree
    }

    override fun onConfigureTab(tab: TabLayout.Tab, position: Int) {
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener
        {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (position==0)
                {
                    val tabOne =
                        LayoutInflater.from(activity).inflate(R.layout.custom_tab, null) as ImageView
                    // tabOne.text = "Tab 1"
                    tabOne.background=ContextCompat.getDrawable(activity!!,R.drawable.selection)
                    tabLayout.getTabAt(0)!!.customView!!.background=ContextCompat.getDrawable(activity!!,R.drawable.selection)
                    // tabLayout.getTabAt(0)!!.customView=tabOne
                    tabLayout.getTabAt(1)!!.customView!!.background=ContextCompat.getDrawable(activity!!,R.drawable.transparent_bg)

                }
                if (position==1)
                {
                    val two =
                        LayoutInflater.from(activity).inflate(R.layout.custom_tab, null) as ImageView
                    // tabOne.text = "Tab 1"
                    tabLayout.getTabAt(1)!!.customView!!.background=ContextCompat.getDrawable(activity!!,R.drawable.selection)
                    // tabLayout.getTabAt(0)!!.customView=tabOne
                    tabLayout.getTabAt(0)!!.customView!!.background=ContextCompat.getDrawable(activity!!,R.drawable.transparent_bg)
                    // tabLayout.getTabAt(0)!!.customView=tabOne
                }

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                // tabLayout.getTabAt(0)!!.customView=tabOne
                Log.d("TAG", "onTabUnselected: ")
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })

    }


}