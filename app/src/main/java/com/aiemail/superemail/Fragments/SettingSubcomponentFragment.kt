package com.aiemail.superemail.Fragments


import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.RelativeLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getDrawable
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.aiemail.superemail.Activities.ColorCodeActivity
import com.aiemail.superemail.Activities.PassCodeActivity
import com.aiemail.superemail.Activities.TeamActivity
import com.aiemail.superemail.Activities.TemplateActivity
import com.aiemail.superemail.FragmentAdapter.main.FragmentAdapterSchedule
import com.aiemail.superemail.FragmentAdapter.main.FragmentpageAdapter
import com.aiemail.superemail.R
import com.aiemail.superemail.prefs
import com.aiemail.superemail.utilis.Helpers
import com.aiemail.superemail.utilis.PreferenceManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class SettingSubcomponentFragment : Fragment(), TabLayoutMediator.TabConfigurationStrategy {
    lateinit var tabLayout: TabLayout
    lateinit var scedule_viewpager: ViewPager2
    lateinit var viewPager2: ViewPager2
    lateinit var adapter: FragmentpageAdapter
    lateinit var preferenceManager: PreferenceManager
    lateinit var snooze_tabLayout: TabLayout
    lateinit var myPref: SharedPreferences
    var username: String = ""

    companion object {
        fun newInstance(fragmentMain: Int): Fragment {
            val args = Bundle()
            args.putInt("layout", fragmentMain)
            val fragment = SettingSubcomponentFragment()
            fragment.setArguments(args)
            return fragment
        }
    }

    var layout: Int = R.layout.fragment_main


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val args = arguments
        layout = args?.getInt("layout", 0)!!
        preferenceManager = this.context?.let { PreferenceManager(it) }!!
        preferenceManager.SetUpPreference()
        myPref = context!!.getSharedPreferences("APP_SHARED_PREF", Context.MODE_PRIVATE);
        username = myPref.getString("username", "")!!;
        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        var view = inflater.inflate(layout, container, false)
        if (layout == R.layout.layout_teams) {
            view.findViewById<TextView>(R.id.remove).setOnClickListener {
                startActivity(Intent(activity, TeamActivity::class.java))
                activity!!.overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up)
            }
        }

        if (layout == R.layout.layout_appearance) {


//Radio button on click change
            val radioGroup = view.findViewById(R.id.radioGroup) as RadioGroup
            radioGroup.setOnCheckedChangeListener(
                RadioGroup.OnCheckedChangeListener { group, checkedId ->

                    val radio_langange1: RadioButton = view.findViewById(R.id.radioButton2)
                    val radio_langange2: RadioButton = view.findViewById(R.id.radioButton3)
                    if (radio_langange1.isChecked) {
                        preferenceManager.SetString("Theme", "Light theme")
                    } else if (radio_langange2.isChecked) {
                        preferenceManager.SetString("Theme", "Dark theme")
                    }


                    Log.d(
                        "TAG",
                        "onCreateView: " + radioGroup.isSelected + ">>" + radio_langange2.isSelected
                    )
                    // Check the current theme mode
                    val currentNightMode = AppCompatDelegate.getDefaultNightMode()
                    var lightTheme = view.findViewById<RadioButton>(R.id.radioButton2)
                    var darkTheme = view.findViewById<RadioButton>(R.id.radioButton3)




                    // Switch to the opposite theme mode
                    val newNightMode =
                        if (preferenceManager.getString("Theme").equals("Dark theme")) {
                            AppCompatDelegate.MODE_NIGHT_YES
                        } else {
                            AppCompatDelegate.MODE_NIGHT_NO
                        }

                    if (!currentNightMode.equals(newNightMode)) {

                        // Set the new theme mode
                        AppCompatDelegate.setDefaultNightMode(newNightMode)
                        activity?.recreate()
                        activity!!.finish()
                    }


                }
            )
            var theme = preferenceManager.getString("Theme")
            Log.d("TAG", "onCreateView: "+theme)

            if (theme.equals("Dark theme")) {
                radioGroup.findViewById<RadioButton>(R.id.radioButton3).isChecked = true


            } else if (theme.equals("Light theme")) {
                radioGroup.findViewById<RadioButton>(R.id.radioButton2).isChecked = true

            }
            //Body Text Num of Lines in ComposeActivity

            val Lines = view.findViewById(R.id.line) as TextView
            Lines.setText("" + prefs.NoOfLine ?: "" + 1)
            var LinesList: ArrayList<String> = arrayListOf()
            LinesList.add(getString(R.string.line1))
            LinesList.add(getString(R.string.line2))
            LinesList.add(getString(R.string.line3))
            view.findViewById<RelativeLayout>(R.id.listpreview).setOnClickListener {
                Helpers.showDialog(activity!!, LinesList)
            }

            val Switch = view.findViewById(R.id.dark_html) as SwitchCompat
            if (preferenceManager.getBoolean("dark_html")) {
                Switch.isChecked = true
            } else {
                Switch.isChecked = false
            }
            Switch.setOnCheckedChangeListener { buttonView, isChecked ->
                // Perform actions based on the checked state

                if (isChecked) {
                    preferenceManager.SetBoolean("dark_html", true)
                    // Switch is checked
                    // Do something
                } else {
                    preferenceManager.SetBoolean("dark_html", false)
                    // Switch is unchecked
                    // Do something else
                }
            }

            val avatars = view.findViewById(R.id.avatars) as SwitchCompat

            if (preferenceManager.getBoolean("avatars")) {
                avatars.isChecked = true
            } else {
                avatars.isChecked = false
            }
            avatars.setOnCheckedChangeListener { buttonView, isChecked ->
                // Perform actions based on the checked state

                if (isChecked) {
                    preferenceManager.SetBoolean("avatars", true)
                    // Switch is checked
                    // Do something
                } else {
                    preferenceManager.SetBoolean("avatars", false)
                    // Switch is unchecked
                    // Do something else
                }
            }


        }
        if (layout == R.layout.layout_prefrence) {
            val sound: SwitchCompat = view.findViewById(R.id.sound)
            val haptic: SwitchCompat = view.findViewById(R.id.haptic)
            Helpers.SwitchToggle(preferenceManager, sound, "sound", true, "")
            Helpers.SwitchToggle(preferenceManager, haptic, "haptic", true, "")

        }

        if (layout == R.layout.layout_security) {
            val passkey: SwitchCompat = view.findViewById(R.id.passkey)
            passkey.setOnCheckedChangeListener { buttonView, isChecked ->
                // Perform actions based on the checked state

                if (isChecked) {
                    if (preferenceManager.getString("Passcode").equals("")) {
                        activity!!.startActivity(Intent(context, PassCodeActivity::class.java))

                    } else {
                        preferenceManager.SetBoolean("PassLock", true)
                    }
                } else {
                    preferenceManager.SetBoolean("PassLock", false)
                }


                //Helpers.SwitchToggle(preferenceManager,passkey,"Passcode",false,"")


            }
        }
        if (layout == R.layout.layout_smartinbox) {
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
                        viewPager2.currentItem = tab.position
                        if (tab.position == 0)


                            (tabLayout.getTabAt(0)!!.customView!! as ImageView).setImageResource(
                                R.drawable.smart2_selected
                            )

                        // tabLayout.getTabAt(0)!!.customView=tabOne
                        (tabLayout.getTabAt(1)!!.customView!! as ImageView).setImageResource(
                            R.drawable.smart
                        )

                    }
                    if (tab!!.position == 1) {
                        (tabLayout.getTabAt(0)!!.customView!! as ImageView).setImageResource(
                            R.drawable.smart_2
                        )

                        // tabLayout.getTabAt(0)!!.customView=tabOne
                        (tabLayout.getTabAt(1)!!.customView!! as ImageView).setImageResource(
                            R.drawable.smart_selection
                        )
                    }
                }


                override fun onTabUnselected(tab: TabLayout.Tab?) {
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                }

            })

        }
        if (layout == R.layout.layout_email) {
            var add = view.findViewById<TextView>(R.id.add_txt)
            add.setOnClickListener {
                var bottomSheetDialog =
                    BottomSheetDialog(activity!!, R.style.BottomSheetDialogcustom);
                bottomSheetDialog.setContentView(R.layout.layout_bottom_email);
                //  bottomSheetDialog!!.getWindow()?.setBackgroundDrawableResource(R.drawable.round_corner);
                bottomSheetDialog.show()
            }
            var signin_layout = view.findViewById<RelativeLayout>(R.id.signin_layout)
            signin_layout.setOnClickListener {
                var bottomSheetDialog =
                    BottomSheetDialog(activity!!, R.style.BottomSheetDialogcustom);
                bottomSheetDialog.setContentView(R.layout.layout_bottom_email);
                //  bottomSheetDialog!!.getWindow()?.setBackgroundDrawableResource(R.drawable.round_corner);
                bottomSheetDialog.show()
            }
            var disable = view.findViewById<TextView>(R.id.disable).setOnClickListener {
                activity!!.startActivity(Intent(activity, ColorCodeActivity::class.java))
            }
        }
        if (layout == R.layout.layout_sceduling) {
            val animalsArray = arrayOf(
                "Snoozes",
                "Reminders",
                "Send later"
            )

            snooze_tabLayout = view.findViewById(R.id.sceduletab)
            scedule_viewpager = view.findViewById(R.id.sceduleviewpager)

            var adapter = FragmentAdapterSchedule(childFragmentManager, lifecycle)
//            snooze_tabLayout.addTab(snooze_tabLayout.newTab().setText("Snoozes").setIcon(R.drawable.ic_clock))
//            snooze_tabLayout.addTab(snooze_tabLayout.newTab().setText("Reminders").setIcon(R.drawable.ic_circle))
//            snooze_tabLayout.addTab(snooze_tabLayout.newTab().setText("Send later").setIcon(R.drawable.ic_send))
            snooze_tabLayout.tabGravity = TabLayout.GRAVITY_FILL
            scedule_viewpager.adapter = adapter
            TabLayoutMediator(snooze_tabLayout, scedule_viewpager) { tab, position ->
                tab.text = animalsArray[position]
            }.attach()
            snooze_tabLayout.addOnTabSelectedListener(object :
                TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    if (tab != null) {
                        scedule_viewpager.currentItem = tab.position


                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                    //( tab?.customView as ImageView) .setImageResource(R.drawable.transparent_bg)
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                }

            })
        }

        if (layout == R.layout.layout_signature) {
            var signature: String = preferenceManager.getString("signature")
            var supermail = preferenceManager!!.getBoolean("supermail")
            var signature_custom = preferenceManager!!.getBoolean("signature_custom")
            var signatureview = view.findViewById<TextView>(R.id.signature)
            var supermailview = view.findViewById<SwitchCompat>(R.id.supermail)
            var ssignatureview = view.findViewById<SwitchCompat>(R.id.signature_custom)
            Log.d("TAG", "onCreateView: " + supermail + "?" + signature_custom)
            if (supermail.equals(true)) {
                supermailview.isChecked = true

            }
            if (signature_custom.equals(true)) {
                ssignatureview.isChecked = true
            }
            if (!signature.equals("")) {
                signatureview.setText(signature)
                view.findViewById<RelativeLayout>(R.id.add_layout).visibility = View.GONE
            } else {

                view.findViewById<RelativeLayout>(R.id.add_layout).visibility = View.VISIBLE
            }
            view.findViewById<RelativeLayout>(R.id.add_layout).setOnClickListener {
                SetUpDialog(view)


            }
            supermailview.setOnCheckedChangeListener { buttonView, isChecked ->
                // Perform actions based on the checked state

                if (isChecked) {
                    preferenceManager.SetBoolean("supermail", true)
                    // Switch is checked
                    // Do something
                } else {
                    preferenceManager.SetBoolean("supermail", false)
                    // Switch is unchecked
                    // Do something else
                }
            }

            ssignatureview.setOnCheckedChangeListener { buttonView, isChecked ->
                // Perform actions based on the checked state
                Log.d("TAG", "onCreateView: " + isChecked)
                if (isChecked) {
                    preferenceManager.SetBoolean("signature_custom", true)
                    // Switch is checked
                    // Do something
                } else {
                    preferenceManager.SetBoolean("signature_custom", false)
                    // Switch is unchecked
                    // Do something else
                }
            }


        }
        if (layout == R.layout.layout_emailviewer) {
            // This overrides the radiogroup onCheckListener
            // This overrides the radiogroup onCheckListener
            view.findViewById<RadioGroup>(R.id.radioGroup)
                .setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
                    // This will get the radiobutton that has changed in its check state
                    val checkedRadioButton =
                        group.findViewById<View>(checkedId) as RadioButton
                    // This puts the value (true/false) into the variable
                    val isChecked = checkedRadioButton.isChecked
                    var opennext = group.findViewById<RadioButton>(R.id.opennext)
                    // If the radiobutton that has changed in check state is now checked...
                    if (isChecked) {
                        // Changes the textview's text to "Checked: example radiobutton text"
//                    tv.setText("Checked:" + checkedRadioButton.text)


                        if (opennext.isChecked) {
                            preferenceManager.SetBoolean("opennext", true)
                        } else {
                            preferenceManager.SetBoolean("opennext", false)
                        }
                    }
                    Log.d(
                        "TAG",
                        "onCreateView: " + checkedRadioButton.equals(R.id.opennext) + ">>" + preferenceManager.getBoolean(
                            "opennext"
                        )
                    )
                })

            view.findViewById<RadioGroup>(R.id.defaultbrowser)
                .setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
                    // This will get the radiobutton that has changed in its check state
                    val checkedRadioButton =
                        group.findViewById<View>(checkedId) as RadioButton
                    // This puts the value (true/false) into the variable
                    val isChecked = checkedRadioButton.isChecked
                    var opennext = group.findViewById<RadioButton>(R.id.browser)
                    // If the radiobutton that has changed in check state is now checked...
                    if (isChecked) {
                        // Changes the textview's text to "Checked: example radiobutton text"
//                    tv.setText("Checked:" + checkedRadioButton.text)


                        if (opennext.isChecked) {
                            preferenceManager.SetBoolean("browser", true)
                        } else {
                            preferenceManager.SetBoolean("browser", false)
                        }
                    }
                    Log.d(
                        "TAG",
                        "onCreateView: " + checkedRadioButton.equals(R.id.opennext) + ">>" + preferenceManager.getBoolean(
                            "browser"
                        )
                    )
                })


        }
        if (layout == R.layout.layout_badges) {
            if(preferenceManager.getBoolean("Allmessage"))
            {
                view.findViewById<RadioButton>(R.id.radioButton2).isChecked=true
            }else{
                view.findViewById<RadioButton>(R.id.radioButton2).isChecked=false
            }
            view.findViewById<RadioButton>(R.id.radioButton2)
                .setOnCheckedChangeListener { buttonview, checked ->


                    // Perform actions when the checked state of the radio button changes
                    if (checked) {
                        preferenceManager.SetBoolean("Allmessage", true)
                        // Radio button is checked
                        // Add your code here
                    } else {
                        preferenceManager.SetBoolean("Allmessage", false)
                        // Radio button is unchecked
                        // Add your code here
                    }
                }
        }

        if (layout == R.layout.layout_support) {
            view.findViewById<TextView>(R.id.version).setText(Helpers.getAppVersion(context!!))


        }

        if (layout == R.layout.layout_template) {
            view.findViewById<TextView>(R.id.button).setOnClickListener {
                startActivity(Intent(context, TemplateActivity::class.java))
            }

        }
        if (layout == R.layout.layout_notification) {
            view.findViewById<TextView>(R.id.account).setText(username)
            view.findViewById<TextView>(R.id.email).setText(username)
            // Create an ArrayAdapter using a list of options
            val options =
                listOf("Full preview", "Sender & Subject", "Sender only", "No preview")
            val options2 = listOf("Mark as read", "Done", "Delete")

            SpinnerSetup(view, R.id.spinner, options, 1)
            // Create an ArrayAdapter using a list of options

            SpinnerSetup(view, R.id.readspinner, options2, 2)

            // Create an ArrayAdapter using a list of options

            SpinnerSetup(view, R.id.donespinner, options2, 3)
        }

        return view;
    }

    fun SpinnerSetup(view1: View, item: Int, options: List<String>, i: Int) {
        val spinner: Spinner = view1.findViewById(item)


        val adapter = ArrayAdapter(context!!, android.R.layout.simple_spinner_item, options)

// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

// Apply the adapter to the spinner
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedItem = options[position]
                preferenceManager.SetString("selectedItem", selectedItem + i)
                if (i == 1 && selectedItem.equals("Full preview")) {

                    view1!!.findViewById<TextView>(R.id.content_text).visibility =
                        View.VISIBLE
                    view1!!.findViewById<TextView>(R.id.subject_text).visibility =
                        View.VISIBLE
                    view1!!.findViewById<TextView>(R.id.body).visibility = View.VISIBLE
                }
                if (i == 1 && selectedItem.equals("Sender & Subject")) {

                    view1!!.findViewById<TextView>(R.id.content_text).visibility =
                        View.VISIBLE
                    view1!!.findViewById<TextView>(R.id.subject_text).visibility =
                        View.VISIBLE
                    view1!!.findViewById<TextView>(R.id.body).visibility = View.GONE
                }
                if (i == 1 && selectedItem.equals("Sender only")) {

                    view1!!.findViewById<TextView>(R.id.content_text).visibility =
                        View.VISIBLE
                    view1!!.findViewById<TextView>(R.id.subject_text).visibility =
                        View.VISIBLE
                    view1!!.findViewById<TextView>(R.id.subject_text)
                        .setText("Notification")
                    view1!!.findViewById<TextView>(R.id.body).visibility = View.GONE
                }
                if (i == 1 && selectedItem.equals("No preview")) {

                    view1!!.findViewById<TextView>(R.id.content_text).visibility =
                        View.VISIBLE
                    view1!!.findViewById<TextView>(R.id.content_text)
                        .setText("Notification")
                    view1!!.findViewById<TextView>(R.id.subject_text).visibility = View.GONE
                    view1!!.findViewById<TextView>(R.id.body).visibility = View.GONE
                }

                Log.d(
                    "TAG",
                    "onItemSelected: " + preferenceManager.getString("selectedItem")
                )

                // Do something with the selected item
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle the case when nothing is selected
            }
        }


    }

    private fun SetUpDialog(view: View) {
        val builder = AlertDialog.Builder(this.activity)
        val customBackground: Drawable =
            getDrawable(context!!, R.drawable.edittext_border)!!

        // Create new LayoutParams
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )


        // Create LayoutParams and specify the desired margins

        layoutParams.setMargins(30, 10, 30, 10)
        // Apply the modified LayoutParams to the EditText
        val input = EditText(this.activity)
        input.layoutParams = layoutParams
        input.setBackgroundDrawable(customBackground)
        builder
            .setTitle("Signature")
            .setMessage("Add Signature")
            .setView(input)
            .setPositiveButton(android.R.string.ok,
                DialogInterface.OnClickListener { dialog, which ->
                    val value = input.text.toString()
                    if (input.text.toString().trim { it <= ' ' }.length == 0) {
                        Toast.makeText(
                            activity,
                            "Enter Signature",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        preferenceManager.SetString("signature", value)
                        var signature: String = preferenceManager.getString("signature")
                        if (!signature.equals("")) {
                            view.findViewById<TextView>(R.id.signature).setText(signature)
                            view.findViewById<RelativeLayout>(R.id.add_layout).visibility =
                                View.GONE
                        } else {

                            view.findViewById<RelativeLayout>(R.id.add_layout).visibility =
                                View.VISIBLE
                        }

                    }
                    val imm =
                        context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
                    imm!!.hideSoftInputFromWindow(input.windowToken, 0)
                })
            .setNegativeButton(android.R.string.cancel,
                DialogInterface.OnClickListener { dialog, which ->
                    val imm =
                        context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
                    imm!!.hideSoftInputFromWindow(input.windowToken, 0)
                })

        builder.show()
        input.requestFocus()
        val imm =
            context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        imm!!.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }

    private fun createTabIcons() {
        val tabOne =
            LayoutInflater.from(activity).inflate(R.layout.tab_layout, null) as ImageView
        // tabOne.text = "Tab 1"
        tabOne.setImageResource(R.drawable.smart2_selected)

        tabLayout.getTabAt(0)!!.customView = tabOne
        val tabTwo =
            LayoutInflater.from(activity).inflate(R.layout.tab_layout, null) as ImageView
        // tabTwo.text = "Tab 2"
        tabTwo.setImageResource(R.drawable.smart)
        tabLayout.getTabAt(1)!!.customView = tabTwo

        // tabLayout.getTabAt(2)!!.customView = tabThree
    }

    private fun createTabIconsscedule() {
        val tabOne =
            LayoutInflater.from(activity).inflate(R.layout.tab_layout, null) as ImageView
        // tabOne.text = "Tab 1"
        tabOne.setImageResource(R.drawable.pic4)
        tabLayout.getTabAt(0)!!.customView = tabOne
        val tabTwo =
            LayoutInflater.from(activity).inflate(R.layout.tab_layout, null) as ImageView
        // tabTwo.text = "Tab 2"
        tabTwo.setImageResource(R.drawable.pic4)
        tabLayout.getTabAt(1)!!.customView = tabTwo
        val tabthree =
            LayoutInflater.from(activity).inflate(R.layout.tab_layout, null) as ImageView
        // tabTwo.text = "Tab 2"
        tabTwo.setImageResource(R.drawable.pic4)
        tabLayout.getTabAt(2)!!.customView = tabTwo

        // tabLayout.getTabAt(2)!!.customView = tabThree
    }

    override fun onConfigureTab(tab: TabLayout.Tab, position: Int) {
        snooze_tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (position == 0) {
//                    val tabOne =
//                        LayoutInflater.from(activity)
//                            .inflate(R.layout.custom_tab, null) as ImageView
//                    // tabOne.text = "Tab 1"
//                    tabOne.background =
//                        ContextCompat.getDrawable(activity!!, R.drawable.selection)
                    snooze_tabLayout.getTabAt(0)!!.customView!!.background =
                        ContextCompat.getDrawable(activity!!, R.drawable.selection)
                    // tabLayout.getTabAt(0)!!.customView=tabOne
                    snooze_tabLayout.getTabAt(1)!!.customView!!.background =
                        ContextCompat.getDrawable(activity!!, R.drawable.transparent_bg)

                }
                if (position == 1) {

                    // tabOne.text = "Tab 1"
                    snooze_tabLayout.getTabAt(1)!!.customView!!.background =
                        ContextCompat.getDrawable(activity!!, R.drawable.selection)
                    // tabLayout.getTabAt(0)!!.customView=tabOne
                    snooze_tabLayout.getTabAt(0)!!.customView!!.background =
                        ContextCompat.getDrawable(activity!!, R.drawable.transparent_bg)
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