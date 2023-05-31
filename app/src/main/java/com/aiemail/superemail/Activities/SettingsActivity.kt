package com.aiemail.superemail.Activities

import android.content.Intent
import android.os.Bundle
import android.transition.Explode
import android.view.Window
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.aiemail.superemail.Bottomsheet.ActionBottomDialogFragment
import com.aiemail.superemail.Bottomsheet.ActionBottomDialogFragment.ItemClickListener
import com.aiemail.superemail.Dialog.MyCustomDialog
import com.aiemail.superemail.databinding.SettingAccountLayoutBinding
import com.aiemail.superemail.R


class SettingsActivity : AppCompatActivity(),ItemClickListener {
    private lateinit var binding: SettingAccountLayoutBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        with(window) {
            requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)

            // Set an exit transition
            exitTransition = Explode()
        }
        val windowInsetsController =
            WindowCompat.getInsetsController(window, window.decorView)
        // Configure the behavior of the hidden system bars.
        windowInsetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        // Add a listener to update the behavior of the toggle fullscreen button when
        // the system bars are hidden or revealed.
        window.decorView.setOnApplyWindowInsetsListener { view, windowInsets ->
            // You can hide the caption bar even when the other system bars are visible.
            // To account for this, explicitly check the visibility of navigationBars()
            // and statusBars() rather than checking the visibility of systemBars().
            if (windowInsets.isVisible(WindowInsetsCompat.Type.navigationBars())
                || windowInsets.isVisible(WindowInsetsCompat.Type.statusBars())
            ) {
                windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())

            } else {
            }
            view.onApplyWindowInsets(windowInsets)
        }
        super.onCreate(savedInstanceState)
        binding = SettingAccountLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Onclick()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }



    fun Onclick() {
        binding.root.findViewById<ImageView>(R.id.back).setOnClickListener {

                super.onBackPressed()
                overridePendingTransition(
                    R.anim.anim_slide_stay,
                    R.anim.slide_out_up
                );
            }

        binding.includelyout.iconmail.setOnClickListener {
            MyCustomDialog(R.layout.layout_language_dialog).show(supportFragmentManager, "MyCustomFragment")
        }
        binding.accountLayout.setOnClickListener {
          startActivity(Intent(this, SettingSubcomponent::class.java).putExtra("Value",1).putExtra("Title","Synchronization"))
            overridePendingTransition(R.anim.anim_right, R.anim.anim_left)
        }
        binding.sublayout.setOnClickListener {
            val addPhotoBottomDialogFragment = ActionBottomDialogFragment.newInstance(R.layout.purchase_detail_layout)

            addPhotoBottomDialogFragment.show(
                supportFragmentManager,
                ActionBottomDialogFragment.TAG

            )
        }
        binding.emailAcccount.setOnClickListener {
            startActivity(Intent(this, SettingSubcomponent::class.java).putExtra("Value",2).putExtra("Title","Accounts"))
            overridePendingTransition(R.anim.anim_right, R.anim.anim_left)
        }
        binding.teamlayout.setOnClickListener {
            startActivity(Intent(this, SettingSubcomponent::class.java).putExtra("Value",3).putExtra("Title","Teams"))
            overridePendingTransition(R.anim.anim_right, R.anim.anim_right)
        }
        binding.appearenceLayout.setOnClickListener {
            startActivity(Intent(this, SettingSubcomponent::class.java).putExtra("Value",4).putExtra("Title","Appearance"))
            overridePendingTransition(R.anim.anim_right, R.anim.anim_left)
        }
        binding.emailviewer.setOnClickListener {
            startActivity(Intent(this, SettingSubcomponent::class.java).putExtra("Value",5).putExtra("Title","Email viewer"))
            overridePendingTransition(R.anim.anim_right, R.anim.anim_left)
        }
        binding.swipelayout.setOnClickListener {
            startActivity(Intent(this, SettingSubcomponent::class.java).putExtra("Value",6).putExtra("Title","Swipes"))
            overridePendingTransition(R.anim.anim_right, R.anim.anim_left)
        }
        binding.shortcutlayout.setOnClickListener {
            startActivity(Intent(this, SettingSubcomponent::class.java).putExtra("Value",7).putExtra("Title","Shortcuts"))
            overridePendingTransition(R.anim.anim_right, R.anim.anim_left)
        }
        binding.signaturelayout.setOnClickListener {
            startActivity(Intent(this, SettingSubcomponent::class.java).putExtra("Value",8).putExtra("Title","Signatures"))
            overridePendingTransition(R.anim.anim_right, R.anim.anim_left)
        }
        binding.templatelayout.setOnClickListener {
            startActivity(Intent(this, SettingSubcomponent::class.java).putExtra("Value",9).putExtra("Title","Templates"))
            overridePendingTransition(R.anim.anim_right, R.anim.anim_left)
        }
        binding.soundlayout.setOnClickListener {
            startActivity(Intent(this, SettingSubcomponent::class.java).putExtra("Value",10).putExtra("Title","Sound preferences"))
            overridePendingTransition(R.anim.anim_right, R.anim.anim_left)
        }
        binding.securitylayout.setOnClickListener {
            startActivity(Intent(this, SettingSubcomponent::class.java).putExtra("Value",11).putExtra("Title","Security"))
            overridePendingTransition(R.anim.anim_right, R.anim.anim_left)
        }
        binding.supportlayout.setOnClickListener {
            startActivity(Intent(this, SettingSubcomponent::class.java).putExtra("Value",12).putExtra("Title","Support"))
            overridePendingTransition(R.anim.anim_right, R.anim.anim_left)
        }
        binding.privacylayout.setOnClickListener {
            startActivity(Intent(this, SettingSubcomponent::class.java).putExtra("Value",13).putExtra("Title","Privacy Policy"))
            overridePendingTransition(R.anim.anim_right, R.anim.anim_left)
        }
        binding.termslayout.setOnClickListener {
            startActivity(Intent(this, SettingSubcomponent::class.java).putExtra("Value",14).putExtra("Title","Terms Rules"))
            overridePendingTransition(R.anim.anim_right, R.anim.anim_left)
        }
        binding.smartinbox.setOnClickListener {
            startActivity(Intent(this, SettingSubcomponent::class.java).putExtra("Value",15).putExtra("Title","Inbox"))
            overridePendingTransition(R.anim.anim_right, R.anim.anim_left)
        }
        binding.cloudlayout.setOnClickListener {
            startActivity(Intent(this, SettingSubcomponent::class.java).putExtra("Value",16).putExtra("Title","Spark Cloud"))
            overridePendingTransition(R.anim.anim_right, R.anim.anim_left)
        }
        binding.badgeslayout.setOnClickListener {
            startActivity(Intent(this, SettingSubcomponent::class.java).putExtra("Value",17).putExtra("Title","Slidebar Badges"))
            overridePendingTransition(R.anim.anim_right, R.anim.anim_left)
        }
        binding.notificationLayout.setOnClickListener {
            startActivity(Intent(this, SettingSubcomponent::class.java).putExtra("Value",18).putExtra("Title","Notification"))
            overridePendingTransition(R.anim.anim_right, R.anim.anim_left)
        }
        binding.scedulinglayout.setOnClickListener {
            startActivity(Intent(this, SettingSubcomponent::class.java).putExtra("Value",19).putExtra("Title","Scheduling"))
            overridePendingTransition(R.anim.anim_right, R.anim.anim_left)
        }

    }

    override fun onItemClick(item: String?) {

    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(
            R.anim.anim_slide_stay,
            R.anim.slide_out_up
        );
    }
}


