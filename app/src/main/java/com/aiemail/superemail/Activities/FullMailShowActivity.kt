package com.aiemail.superemail.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.aiemail.superemail.Adapters.ListEmailAdapter
import com.aiemail.superemail.Models.AllMails
import com.aiemail.superemail.Models.Email
import com.aiemail.superemail.MyApplication
import com.aiemail.superemail.R
import com.aiemail.superemail.databinding.ActivityDirectComposeBinding
import com.aiemail.superemail.databinding.ActivityFullMailShowBinding
import com.aiemail.superemail.viewmodel.AllEmailViewmodel
import com.aiemail.superemail.viewmodel.EmailViewmodel
import io.github.luizgrp.sectionedrecyclerviewadapter.Section

class FullMailShowActivity : AppCompatActivity(), ListEmailAdapter.ClickListenerData {

    lateinit var realObject: Section
    private lateinit var binding: ActivityFullMailShowBinding
    private lateinit var listAdapter: ListEmailAdapter
    private var listofmail: ArrayList<Email> = arrayListOf()
    private var Allmails: ArrayList<AllMails> = arrayListOf()
    var key:String=""

    val Allmodel: AllEmailViewmodel by viewModels() {
        AllEmailViewmodel.Factory(
            (application as MyApplication), (application as MyApplication).allrepository
        )
    }
    val model: EmailViewmodel by viewModels() {
        EmailViewmodel.Factory(
            (application as MyApplication),
            (application as MyApplication).repository
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFullMailShowBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (intent.extras!=null)
        key= intent.getStringExtra("id")!!
        binding.title.setText(key)
        ObservData()
        OnClick()
    }

    private fun OnClick() {
        binding.back.setOnClickListener {
            onBackPressed()
        }
    }

    private fun ObservData() {
        binding.list.apply {
            layoutManager = LinearLayoutManager(this@FullMailShowActivity)
            listAdapter =
                ListEmailAdapter(
                    this@FullMailShowActivity,
                    listofmail,
                    this@FullMailShowActivity
                ) { select ->
                    // visibleSelectall(select)
                }
            adapter = listAdapter
            Allmodel.maildata.observe(this@FullMailShowActivity) {
                if (it != null) {
                    Log.d("TAG", "ObservData: " + it.get(key))
                    listofmail = arrayListOf()
                    for (key in it.keys) {
                        Allmails = it.get(key)!!
                        for (emailField in Allmails) {
                            var email: Email = Email()
                            email.sender = emailField.sender
                            email.senderEmail = emailField.senderEmail
                            email.body = emailField.body
                            email.content = emailField.content
                            email.description = emailField.description
                            email.author = emailField.author
                            email.sender = emailField.sender
                            listofmail.add(email)
                            listAdapter.adddata(listofmail)
                        }
                    }


                    listAdapter.adddata(listofmail)

                }
            }
        }


    }

    override fun onItemRootViewClicked(section: Email, itemAdapterPosition: Int) {
      // position = itemAdapterPosition
        model.MailObject.postValue(section)
        startActivity(
            Intent(
                this,
                ComposeActivity::class.java
            ).putExtra("id", itemAdapterPosition)
        )
        overridePendingTransition(
            R.anim.slide_in_up,
            R.anim.slide_out_up
        )
    }
}