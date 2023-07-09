package com.aiemail.superemail.Fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.aiemail.superemail.Activities.ComposeActivity
import com.aiemail.superemail.Adapters.ListEmailAdapter
import com.aiemail.superemail.Models.Email
import com.aiemail.superemail.MyApplication
import com.aiemail.superemail.R
import com.aiemail.superemail.databinding.LayoutAsideBinding
import com.aiemail.superemail.databinding.LayoutRecentBinding
import com.aiemail.superemail.databinding.LayoutSignatureBinding
import com.aiemail.superemail.viewmodel.EmailViewmodel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SignatureFragment : Fragment(), ListEmailAdapter.ClickListenerData {

    private lateinit var binding: LayoutSignatureBinding
    private lateinit var listAdapter: ListEmailAdapter
    private var listofmail: ArrayList<Email> = arrayListOf()
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    val model: EmailViewmodel by viewModels() {
        EmailViewmodel.Factory(
            (activity!!.application as MyApplication),
            (activity!!.application as MyApplication).repository
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LayoutSignatureBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchData()
        setupData()
    }

    private fun setupData() {


    }

    private fun fetchData() {


    }

    override fun onItemRootViewClicked(section: Email, itemAdapterPosition: Int) {

    }
}