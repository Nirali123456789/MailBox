package com.aiemail.superemail.Activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat.animate
import com.aiemail.superemail.R
import com.chinalwb.are.styles.toolbar.IARE_Toolbar
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_AlignmentCenter
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_AlignmentLeft
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_AlignmentRight
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_At
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_BackgroundColor
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_Bold
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_Hr
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_Image
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_Italic
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_Link
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_ListBullet
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_ListNumber
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_Quote
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_Strikethrough
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_Subscript
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_Superscript
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_Underline
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_Video
import com.chinalwb.are.styles.toolitems.IARE_ToolItem
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.aiemail.superemail.Bottomsheet.ActionBottomDialogFragment
import com.aiemail.superemail.Bottomsheet.ActionBottomDialogFragment.ItemClickListener
import com.aiemail.superemail.Dialog.MyCustomDialog

import com.aiemail.superemail.chinalwb.are.Text_Operations.DemoUtil
import com.aiemail.superemail.databinding.ActivityDirectComposeBinding

class DirectComposeActivity : AppCompatActivity(),ItemClickListener {

    private lateinit var binding: ActivityDirectComposeBinding
    private val REQ_WRITE_EXTERNAL_STORAGE = 10000
    private lateinit var mToolbar: IARE_Toolbar
    var click:Boolean=false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDirectComposeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mToolbar=binding.areToolbar
        option1()
        binding.back.setOnClickListener {
         onBackPressed()
        }
        if (intent.extras!=null)
        {
            binding.email.setText(intent.extras!!.getString("data"))
        }else
        binding.email.setText("New message")
        binding.apply.setOnClickListener {
            binding.option.visibility=View.VISIBLE
            binding.tool.visibility = View.GONE
            val html: String = binding.sub.getHtml()
            DemoUtil.saveHtml(this, html)
        }
        binding.bb.setOnClickListener {
            if (!click) {
                click=!click
                binding.group1.visibility = (View.VISIBLE);
                binding.group2.visibility = (View.VISIBLE);
                animate(binding.bb)
                    .alpha(1f)
                    .setDuration(800)
                    .setListener(null);
            }else
            {
                click=!click
                binding.group1.visibility = (View.GONE);
                binding.group2.visibility = (View.GONE);
                animate(binding.bb)
                    .alpha(1f)
                    .setDuration(800)
                    .setListener(null);
            }
        }
        ItemClick()

       binding.bottombar.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.clock1->MyCustomDialog(R.layout.layout_snooze_dialog).show(supportFragmentManager, "MyCustomFragment")
                R.id.text1-> {
                    binding.bottombar.visibility=View.GONE
                   binding.tool.visibility = View.VISIBLE
                }
                R.id.folder->showBottomSheetpurchse()
                R.id.attach1->
                {
                   var bottomSheetDialog =  BottomSheetDialog(this,R.style.BottomSheetDialogcustom);
                    bottomSheetDialog.setContentView(R.layout.layout_attachment);
                  //  bottomSheetDialog!!.getWindow()?.setBackgroundDrawableResource(R.drawable.round_corner);
                    bottomSheetDialog.show()
                }


            }
            true
        }
// MyCustomDialog(R.layout.layout_snooze_dialog).show(supportFragmentManager, "MyCustomFragment")
    }

    private fun ItemClick() {
        binding.attach.setOnClickListener {
            var bottomSheetDialog =  BottomSheetDialog(this,R.style.BottomSheetDialogcustom);
            bottomSheetDialog.setContentView(R.layout.layout_attachment);
            //  bottomSheetDialog!!.getWindow()?.setBackgroundDrawableResource(R.drawable.round_corner);
            bottomSheetDialog.show()
        }
        binding.file.setOnClickListener {
            showBottomSheetpurchse()
        }
        binding.text.setOnClickListener {
            binding.option.visibility=View.GONE
            binding.tool.visibility = View.VISIBLE
        }
        binding.clock.setOnClickListener {
            MyCustomDialog(R.layout.layout_snooze_dialog).show(supportFragmentManager, "MyCustomFragment"
            )

        }
        binding.send.setOnClickListener {
            MyCustomDialog(R.layout.layout_send_later).show(supportFragmentManager, "MyCustomFragment")
        }
        binding.add.setOnClickListener {
            MyCustomDialog(R.layout.layout_common_dialog).show(supportFragmentManager, "MyCustomFragment")
        }


    }

    fun showBottomSheetpurchse() {

        val addPhotoBottomDialogFragment = ActionBottomDialogFragment.newInstance(R.layout.purchase_detail_layout)
        addPhotoBottomDialogFragment.show(
            supportFragmentManager,
            ActionBottomDialogFragment.TAG

        )

    }

    override fun onBackPressed() {
        MyCustomDialog(R.layout.layout_discard_dialog).show(supportFragmentManager, "MyCustomFragment")
       // super.onBackPressed()
    }
    private fun option1() {
        mToolbar = findViewById(R.id.areToolbar)
        val bold: IARE_ToolItem = ARE_ToolItem_Bold()
        val italic: IARE_ToolItem = ARE_ToolItem_Italic()
        val underline: IARE_ToolItem = ARE_ToolItem_Underline()
        val strikethrough: IARE_ToolItem = ARE_ToolItem_Strikethrough()
        val quote: IARE_ToolItem = ARE_ToolItem_Quote()
        val listNumber: IARE_ToolItem = ARE_ToolItem_ListNumber()
        val color: IARE_ToolItem = ARE_ToolItem_BackgroundColor()
        val listBullet: IARE_ToolItem = ARE_ToolItem_ListBullet()
        val hr: IARE_ToolItem = ARE_ToolItem_Hr()
        val link: IARE_ToolItem = ARE_ToolItem_Link()
        val subscript: IARE_ToolItem = ARE_ToolItem_Subscript()
        val superscript: IARE_ToolItem = ARE_ToolItem_Superscript()
        val left: IARE_ToolItem = ARE_ToolItem_AlignmentLeft()
        val center: IARE_ToolItem = ARE_ToolItem_AlignmentCenter()
        val right: IARE_ToolItem = ARE_ToolItem_AlignmentRight()
        val image: IARE_ToolItem = ARE_ToolItem_Image()
        val video: IARE_ToolItem = ARE_ToolItem_Video()
        val at: IARE_ToolItem = ARE_ToolItem_At()
        mToolbar.addToolbarItem(bold)
        mToolbar.addToolbarItem(italic)
        mToolbar.addToolbarItem(underline)
        mToolbar.addToolbarItem(strikethrough)
        mToolbar.addToolbarItem(quote)
        mToolbar.addToolbarItem(color)
        mToolbar.addToolbarItem(listNumber)
        mToolbar.addToolbarItem(listBullet)
        mToolbar.addToolbarItem(hr)
        mToolbar.addToolbarItem(link)
        mToolbar.addToolbarItem(subscript)
        mToolbar.addToolbarItem(superscript)
        mToolbar.addToolbarItem(left)
        mToolbar.addToolbarItem(center)
        mToolbar.addToolbarItem(right)
        mToolbar.addToolbarItem(image)
        mToolbar.addToolbarItem(video)
        mToolbar.addToolbarItem(at)

        binding.sub.setToolbar(mToolbar)
            //initToolbarArrow()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQ_WRITE_EXTERNAL_STORAGE) {
            val html: String = binding.sub.getHtml()
            DemoUtil.saveHtml(this, html)
            return
        }
       // if (useOption1) {
            mToolbar.onActivityResult(requestCode, resultCode, data)
       // } else {
            //this.arEditor.onActivityResult(requestCode, resultCode, data)
      //  }
    }

    override fun onItemClick(item: String?) {
    }
}