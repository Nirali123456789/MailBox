package com.aiemail.superemail.Fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.aiemail.superemail.Activities.ComposeActivity
import com.aiemail.superemail.Adapters.ListEmailAdapter
import com.aiemail.superemail.Adapters.ScrollAdapter
import com.aiemail.superemail.Models.Email
import com.aiemail.superemail.MyApplication
import com.aiemail.superemail.R
import com.aiemail.superemail.databinding.LayoutAsideBinding
import com.aiemail.superemail.databinding.LayoutFragmentBinding
import com.aiemail.superemail.databinding.SavedLayoutBinding
import com.aiemail.superemail.viewmodel.EmailViewmodel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FragmentChanger : Fragment(), ScrollAdapter.ClickListenerData {

    private lateinit var binding: LayoutFragmentBinding

    private lateinit var listAdapter: ScrollAdapter
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
        binding = LayoutFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchData()
        setupData()
    }

    private fun setupData() {

//        *Set EmailList Adapter To Recyclerview

        binding.recyclerview.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            listAdapter = ScrollAdapter(activity!!, listofmail, this@FragmentChanger) { select ->
//                visibleSelectall(select)
            }
            adapter = listAdapter
        }
    }

    private fun fetchData() {

            coroutineScope.launch(Dispatchers.IO) {
                // Fetch data from Room database on a background thread
                val userList = model.getUnreadEmailList()

                // Perform additional background fetch or processing
                // ...

                withContext(Dispatchers.Main) {
                    view?.let { safeView ->
                    userList.observe(viewLifecycleOwner)
                    {
//                    if (it.size < 6) {

                        listofmail = arrayListOf()
                        listofmail.addAll(it)
                        listAdapter.adddata(listofmail)
                        //}


                    }
                    // Update UI with the fetched data

                }
            }
            // Access the LifecycleOwner of the View as needed
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
