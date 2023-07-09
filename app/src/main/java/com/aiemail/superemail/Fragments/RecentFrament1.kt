package com.aiemail.superemail.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.aiemail.superemail.Adapters.DataAdapter
import com.aiemail.superemail.Models.Email
import com.aiemail.superemail.MyApplication
import com.aiemail.superemail.databinding.LayoutRecentBinding
import com.aiemail.superemail.viewmodel.EmailViewmodel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*


class RecentFrament1 : Fragment(), DataAdapter.RecyclerViewItemClickListener {

    private val ARG_RESULT: String? = "ARG_RESULT"
    private var result: String? = "neha"
    private lateinit var binding: LayoutRecentBinding
    private var mainList = listOf<String>()
    lateinit var DataAdapter: DataAdapter
    public var mEmailList: ArrayList<Email> = arrayListOf()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LayoutRecentBinding.inflate(layoutInflater)
        setupData()
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (getArguments() != null) {
            result = getArguments()!!.getString(ARG_RESULT);



        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    public fun filter(text: String) {
        // creating a new array list to filter our data.
        val filteredlist: ArrayList<String> = ArrayList<String>()


    }


    private fun setupData() {
        // Initialize the email list and adapter
        // Initialize the email list and adapter
        mEmailList = ArrayList()
        DataAdapter = DataAdapter(mEmailList, this@RecentFrament1)
        val model: EmailViewmodel by viewModels() {
            EmailViewmodel.Factory(
                (requireActivity().application as MyApplication),
                (requireActivity().application as MyApplication).repository
            )
        }
        CoroutineScope(Dispatchers.Main).launch(Dispatchers.IO) {
            val list = model.getEmailList()
            withContext(Dispatchers.Main) {
                list.observe(viewLifecycleOwner)
                {
                    Log.d("TAG", "SearchEmail: " + mEmailList.size)
                    binding.list.setAdapter(DataAdapter);
                    for (list in it) {
                        Log.d("TAG", "SearchEmail: " + mEmailList.size)
                        mEmailList.add(list)
                    }
//        mEmailListAdapter = ArrayAdapter<String>(activity!!, R.layout.list_item, mEmailList)
//        binding.list.setAdapter(mEmailListAdapter);


                }
            }
        }
    }


    override fun clickOnItem(data: Email?) {

    }


}






