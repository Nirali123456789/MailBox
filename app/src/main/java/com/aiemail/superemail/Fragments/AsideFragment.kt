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
import com.aiemail.superemail.viewmodel.EmailViewmodel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AsideFragment : Fragment(), ListEmailAdapter.ClickListenerData {

    private lateinit var binding: LayoutAsideBinding
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
        binding = LayoutAsideBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchData()
        setupData()
    }

    private fun setupData() {

//        *Set EmailList Adapter To Recyclerview

        binding.list.apply {
            layoutManager = LinearLayoutManager(activity)
            listAdapter = ListEmailAdapter(activity!!, listofmail, this@AsideFragment) { select ->
//                visibleSelectall(select)
            }
            adapter = listAdapter
        }
    }

    private fun fetchData() {

        coroutineScope.launch(Dispatchers.IO) {
            // Fetch data from Room database on a background thread
            val userList = model.FetchAsideList()

            // Perform additional background fetch or processing
            // ...

            withContext(Dispatchers.Main) {
                userList.observe(viewLifecycleOwner)
                {

                    Log.d("TAG", "fetchAndProcess: " + it)
                    listofmail = arrayListOf()
                    listofmail.addAll(it)
                    listAdapter.adddata(listofmail)


                }
                // Update UI with the fetched data

            }
        }
    }

    override fun onItemRootViewClicked(section: Email, itemAdapterPosition: Int) {
        model.MailObject.postValue(section)
        startActivity(
            Intent(
                activity,
                ComposeActivity::class.java
            ).putExtra("id", itemAdapterPosition)
        )
        activity?.overridePendingTransition(
            R.anim.slide_in_up,
            R.anim.slide_out_up
        )
    }
}