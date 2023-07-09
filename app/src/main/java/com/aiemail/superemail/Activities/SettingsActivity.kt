package com.aiemail.superemail.Activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.aiemail.superemail.Bottomsheet.ActionBottomDialogFragment
import com.aiemail.superemail.Bottomsheet.ActionBottomDialogFragment.ItemClickListener
import com.aiemail.superemail.Dialog.MyCustomDialog
import com.aiemail.superemail.databinding.SettingAccountLayoutBinding
import com.aiemail.superemail.R
import com.aiemail.superemail.utilis.Helpers
import com.aiemail.superemail.utilis.Helpers.Companion.OpenLink
import com.google.api.services.gmail.model.Message


class SettingsActivity : AppCompatActivity(), ItemClickListener, MyCustomDialog.DialogListner {
    private lateinit var binding: SettingAccountLayoutBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        Helpers.SetUpFullScreen(window)
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
            MyCustomDialog(R.layout.layout_language_dialog, "", this, this, Message()).show(
                supportFragmentManager,
                "MyCustomFragment"
            )
        }
        binding.accountLayout.setOnClickListener {
            startActivity(
                Intent(this, SettingSubcomponentActivity::class.java).putExtra("Value", 1)
                    .putExtra("Title", "Synchronization")
            )
            overridePendingTransition(R.anim.anim_right, R.anim.anim_left)
        }
        binding.sublayout.setOnClickListener {
            val addPhotoBottomDialogFragment =
                ActionBottomDialogFragment.newInstance(R.layout.purchase_detail_layout)

            addPhotoBottomDialogFragment.show(
                supportFragmentManager,
                ActionBottomDialogFragment.TAG

            )
        }
        binding.emailAcccount.setOnClickListener {
            startActivity(
                Intent(this, SettingSubcomponentActivity::class.java).putExtra("Value", 2)
                    .putExtra("Title", "Accounts")
            )
            overridePendingTransition(R.anim.anim_right, R.anim.anim_left)
        }
        binding.teamlayout.setOnClickListener {
            startActivity(
                Intent(this, SettingSubcomponentActivity::class.java).putExtra("Value", 3)
                    .putExtra("Title", "Teams")
            )
            overridePendingTransition(R.anim.anim_right, R.anim.anim_right)
        }
        binding.appearenceLayout.setOnClickListener {
            startActivity(
                Intent(this, SettingSubcomponentActivity::class.java).putExtra("Value", 4)
                    .putExtra("Title", "Appearance")
            )
            overridePendingTransition(R.anim.anim_right, R.anim.anim_left)
        }
        binding.emailviewer.setOnClickListener {
            startActivity(
                Intent(this, SettingSubcomponentActivity::class.java).putExtra("Value", 5)
                    .putExtra("Title", "Email viewer")
            )
            overridePendingTransition(R.anim.anim_right, R.anim.anim_left)
        }
        binding.swipelayout.setOnClickListener {
            startActivity(
                Intent(this, SettingSubcomponentActivity::class.java).putExtra("Value", 6)
                    .putExtra("Title", "Swipes")
            )
            overridePendingTransition(R.anim.anim_right, R.anim.anim_left)
        }
        binding.shortcutlayout.setOnClickListener {
            startActivity(
                Intent(this, SettingSubcomponentActivity::class.java).putExtra("Value", 7)
                    .putExtra("Title", "Shortcuts")
            )
            overridePendingTransition(R.anim.anim_right, R.anim.anim_left)
        }
        binding.signaturelayout.setOnClickListener {
            startActivity(
                Intent(this, SettingSubcomponentActivity::class.java).putExtra("Value", 8)
                    .putExtra("Title", "Signatures")
            )
            overridePendingTransition(R.anim.anim_right, R.anim.anim_left)
        }
        binding.templatelayout.setOnClickListener {
            startActivity(
                Intent(this, SettingSubcomponentActivity::class.java).putExtra("Value", 9)
                    .putExtra("Title", "Templates")
            )
            overridePendingTransition(R.anim.anim_right, R.anim.anim_left)
        }
        binding.soundlayout.setOnClickListener {
            startActivity(
                Intent(this, SettingSubcomponentActivity::class.java).putExtra("Value", 10)
                    .putExtra("Title", "Sound preferences")
            )
            overridePendingTransition(R.anim.anim_right, R.anim.anim_left)
        }
        binding.securitylayout.setOnClickListener {
            startActivity(
                Intent(this, SettingSubcomponentActivity::class.java).putExtra("Value", 11)
                    .putExtra("Title", "Security")
            )
            overridePendingTransition(R.anim.anim_right, R.anim.anim_left)
        }
        binding.supportlayout.setOnClickListener {
            startActivity(
                Intent(this, SettingSubcomponentActivity::class.java).putExtra("Value", 12)
                    .putExtra("Title", "Support")
            )
            overridePendingTransition(R.anim.anim_right, R.anim.anim_left)
        }
        binding.privacylayout.setOnClickListener {
           Helpers. OpenLink(this)
//            startActivity(Intent(this, SettingSubcomponent::class.java).putExtra("Value",13).putExtra("Title","Privacy Policy"))
//            overridePendingTransition(R.anim.anim_right, R.anim.anim_left)
        }
        binding.termslayout.setOnClickListener {
//            startActivity(
//                Intent(this, SettingSubcomponent::class.java).putExtra("Value", 14)
//                    .putExtra("Title", "Terms Rules")
//            )
//            overridePendingTransition(R.anim.anim_right, R.anim.anim_left)
            OpenLink(this)
        }
        binding.smartinbox.setOnClickListener {
            startActivity(
                Intent(this, SettingSubcomponentActivity::class.java).putExtra("Value", 15)
                    .putExtra("Title", "Inbox")
            )
            overridePendingTransition(R.anim.anim_right, R.anim.anim_left)
        }
        binding.cloudlayout.setOnClickListener {
            startActivity(
                Intent(this, SettingSubcomponentActivity::class.java).putExtra("Value", 16)
                    .putExtra("Title", "Spark Cloud")
            )
            overridePendingTransition(R.anim.anim_right, R.anim.anim_left)
        }
        binding.badgeslayout.setOnClickListener {
            startActivity(
                Intent(this, SettingSubcomponentActivity::class.java).putExtra("Value", 17)
                    .putExtra("Title", "Slidebar Badges")
            )
            overridePendingTransition(R.anim.anim_right, R.anim.anim_left)
        }
        binding.notificationLayout.setOnClickListener {
            startActivity(
                Intent(this, SettingSubcomponentActivity::class.java).putExtra("Value", 18)
                    .putExtra("Title", "Notification")
            )
            overridePendingTransition(R.anim.anim_right, R.anim.anim_left)
        }
        binding.scedulinglayout.setOnClickListener {
            startActivity(
                Intent(this, SettingSubcomponentActivity::class.java).putExtra("Value", 19)
                    .putExtra("Title", "Scheduling")
            )
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

    override fun dialogClick(sender_id: String, dialog: MyCustomDialog) {
        TODO("Not yet implemented")
    }
}


