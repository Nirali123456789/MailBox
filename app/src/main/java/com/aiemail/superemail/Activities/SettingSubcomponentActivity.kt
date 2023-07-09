package com.aiemail.superemail.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.aiemail.superemail.Dialog.MyCustomDialog
import com.aiemail.superemail.R
import com.aiemail.superemail.databinding.ActivitySettingSubcomponentBinding
import com.aiemail.superemail.utilis.Helpers
import com.aiemail.superemail.utilis.Helpers.Companion.ChangeFragment
import com.google.api.services.gmail.model.Message

class SettingSubcomponentActivity : AppCompatActivity(), MyCustomDialog.DialogListner {
    var value: Int = 0
    var title: String = ""
    private lateinit var binding: ActivitySettingSubcomponentBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        Helpers.SetUpFullScreen(window)
        super.onCreate(savedInstanceState)
        binding = ActivitySettingSubcomponentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (savedInstanceState == null) {
            FragmentSetup()
        }
        OnClick()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(
            R.anim.slide_in_left,
            R.anim.slide_out
        );
    }

    private fun FragmentSetup() {
        title = intent.getStringExtra("Title")!!
        findViewById<TextView>(R.id.title).setText(title)
        value = intent.getIntExtra("Value", 0)
        when (value) {
            1 -> {
                binding.share.setOnClickListener {
                    MyCustomDialog(R.layout.layout_logout, "", this, this, Message()).show(
                        supportFragmentManager,
                        "MyCustomFragment"
                    )
                }
                ChangeFragment(this, R.layout.fragment_main)

            }

            2 -> {
                binding.share.visibility = View.GONE
                ChangeFragment(this, R.layout.layout_email)

            }

            3 -> {
                binding.share.visibility = View.VISIBLE
                binding.share.setImageResource(R.drawable.ic_add)
                ChangeFragment(this, R.layout.layout_teams)

            }

            4 -> {
                binding.share.visibility = View.GONE
                binding.share.setImageResource(R.drawable.ic_add)
                ChangeFragment(this, R.layout.layout_appearance)

            }

            5 -> {
                binding.share.visibility = View.GONE
                ChangeFragment(this, R.layout.layout_emailviewer)

            }

            6 -> {
                binding.share.visibility = View.GONE

                ChangeFragment(this, R.layout.layout_swipe)
            }

            7 -> {
                binding.share.visibility = View.GONE

                ChangeFragment(this, R.layout.layout_shortcuts)
            }

            8 -> {
                binding.share.visibility = View.GONE

                ChangeFragment(this, R.layout.layout_signature)
            }

            9 -> {
                binding.share.visibility = View.GONE

                ChangeFragment(this, R.layout.layout_template)
            }

            10 -> {
                binding.share.visibility = View.GONE

                ChangeFragment(this, R.layout.layout_prefrence)
            }

            11 -> {
                binding.share.visibility = View.GONE

                ChangeFragment(this, R.layout.layout_security)
            }

            12 -> {
                binding.share.visibility = View.GONE

                ChangeFragment(this, R.layout.layout_support)
            }

            13 -> {
                binding.share.visibility = View.GONE
                ChangeFragment(this, R.layout.layout_policy)

            }

            14 -> {
                binding.share.visibility = View.GONE
                ChangeFragment(this, R.layout.layout_policy)

            }

            15 -> {
                binding.share.visibility = View.GONE
                ChangeFragment(this, R.layout.layout_smartinbox)

            }

            16 -> {
                binding.share.visibility = View.GONE
                ChangeFragment(this, R.layout.layout_cloud)
//                supportFragmentManager.beginTransaction()
//                    .replace(R.id.container, SettingSubcomponentFragment.newInstance(R.layout.layout_cloud))
//                    .commitNow()
            }

            17 -> {
                binding.share.visibility = View.GONE
                ChangeFragment(this, R.layout.layout_badges)
//                supportFragmentManager.beginTransaction()
//                    .replace(R.id.container, SettingSubcomponentFragment.newInstance(R.layout.layout_badges))
//                    .commitNow()
            }

            18 -> {
                binding.share.visibility = View.GONE
                ChangeFragment(this, R.layout.layout_notification)

            }

            19 -> {
                binding.share.visibility = View.GONE
                ChangeFragment(this, R.layout.layout_sceduling)

            }


        }

    }

    fun OnClick() {
        binding.back.setOnClickListener {
            super.onBackPressed()
            overridePendingTransition(
                R.anim.slide_in_left,
                R.anim.slide_out
            );
        }

    }

    override fun dialogClick(sender_id: String, dialog: MyCustomDialog) {

    }
}