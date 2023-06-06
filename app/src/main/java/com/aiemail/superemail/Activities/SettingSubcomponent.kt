package com.aiemail.superemail.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.aiemail.superemail.Dialog.MyCustomDialog
import com.aiemail.superemail.R
import com.aiemail.superemail.databinding.ActivitySettingSubcomponentBinding
import com.aiemail.superemail.Fragments.SettingSubcomponentFragment
import com.aiemail.superemail.feature.utilis.Constant

class SettingSubcomponent : AppCompatActivity() {
     var value:Int=0
    var title:String=""
    private lateinit var binding: ActivitySettingSubcomponentBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        Constant.SetUpFullScreen(window)
        super.onCreate(savedInstanceState)
        binding = ActivitySettingSubcomponentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        title= intent.getStringExtra("Title")!!
        findViewById<TextView>(R.id.title).setText(title)
        value=intent.getIntExtra("Value",0)
        if (savedInstanceState == null) {
            FragmentSetup()
        }
        binding.back.setOnClickListener {
            super.onBackPressed()
            overridePendingTransition(R.anim.slide_in_left,
                R.anim.slide_out);
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left,
            R.anim.slide_out);
    }

    private fun FragmentSetup() {432
        when(value)
        {
            1->
            {
                binding.share.setOnClickListener {
                    MyCustomDialog(R.layout.layout_logout).show(supportFragmentManager, "MyCustomFragment")
                }
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container, SettingSubcomponentFragment.newInstance(R.layout.fragment_main))
                    .commitNow()
            }
            2->
            {
                binding.share.visibility=View.GONE
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container, SettingSubcomponentFragment.newInstance(R.layout.layout_email))
                    .commitNow()
            }
            3->
            {
                binding.share.visibility=View.VISIBLE
                binding.share.setImageResource(R.drawable.ic_add)
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container, SettingSubcomponentFragment.newInstance(R.layout.layout_teams))
                    .commitNow()
            }
            4->
            {
                binding.share.visibility=View.GONE
                binding.share.setImageResource(R.drawable.ic_add)
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container, SettingSubcomponentFragment.newInstance(R.layout.layout_appearance))
                    .commitNow()
            }
            5->
            {
                binding.share.visibility=View.GONE
               // binding.share.setImageResource(R.drawable.ic_add)
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container, SettingSubcomponentFragment.newInstance(R.layout.layout_emailviewer))
                    .commitNow()
            }
            6->
            {
                binding.share.visibility=View.GONE
                // binding.share.setImageResource(R.drawable.ic_add)
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container, SettingSubcomponentFragment.newInstance(R.layout.layout_swipe))
                    .commitNow()
            }
            7->
            {
                binding.share.visibility=View.GONE
                // binding.share.setImageResource(R.drawable.ic_add)
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container, SettingSubcomponentFragment.newInstance(R.layout.layout_shortcuts))
                    .commitNow()
            }
            8->
            {
                binding.share.visibility=View.GONE
                // binding.share.setImageResource(R.drawable.ic_add)
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container, SettingSubcomponentFragment.newInstance(R.layout.layout_signature))
                    .commitNow()
            }
            9->
            {
                binding.share.visibility=View.GONE
                // binding.share.setImageResource(R.drawable.ic_add)
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container, SettingSubcomponentFragment.newInstance(R.layout.layout_template))
                    .commitNow()
            }
            10->
            {
                binding.share.visibility=View.GONE
                // binding.share.setImageResource(R.drawable.ic_add)
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container, SettingSubcomponentFragment.newInstance(R.layout.layout_prefrence))
                    .commitNow()
            }
            11->
            {
                binding.share.visibility=View.GONE
                // binding.share.setImageResource(R.drawable.ic_add)
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container, SettingSubcomponentFragment.newInstance(R.layout.layout_security))
                    .commitNow()
            }
            12->
            {
                binding.share.visibility=View.GONE
                // binding.share.setImageResource(R.drawable.ic_add)
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container, SettingSubcomponentFragment.newInstance(R.layout.layout_support))
                    .commitNow()
            }
            13->
            {
                binding.share.visibility=View.GONE
                // binding.share.setImageResource(R.drawable.ic_add)
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container, SettingSubcomponentFragment.newInstance(R.layout.layout_policy))
                    .commitNow()
            }
            14->
            {
                binding.share.visibility=View.GONE
                // binding.share.setImageResource(R.drawable.ic_add)
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container, SettingSubcomponentFragment.newInstance(R.layout.layout_policy))
                    .commitNow()
            }
            15->
            {
                binding.share.visibility=View.GONE
                // binding.share.setImageResource(R.drawable.ic_add)
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container, SettingSubcomponentFragment.newInstance(R.layout.layout_smartinbox))
                    .commitNow()
            }
            16->
            {
                binding.share.visibility=View.GONE
                // binding.share.setImageResource(R.drawable.ic_add)
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container, SettingSubcomponentFragment.newInstance(R.layout.layout_cloud))
                    .commitNow()
            }
            17->
            {
                binding.share.visibility=View.GONE
                // binding.share.setImageResource(R.drawable.ic_add)
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container, SettingSubcomponentFragment.newInstance(R.layout.layout_badges))
                    .commitNow()
            }
            18->
            {
                binding.share.visibility=View.GONE
                // binding.share.setImageResource(R.drawable.ic_add)
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container, SettingSubcomponentFragment.newInstance(R.layout.layout_notification))
                    .commitNow()
            }
            19->
            {
                binding.share.visibility=View.GONE
                // binding.share.setImageResource(R.drawable.ic_add)
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container, SettingSubcomponentFragment.newInstance(R.layout.layout_sceduling))
                    .commitNow()
            }



        }

    }
}