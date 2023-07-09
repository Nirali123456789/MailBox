package com.aiemail.superemail.Activities


import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.aiemail.superemail.Adapters.DataAdapter
import com.aiemail.superemail.Models.Email
import com.aiemail.superemail.MyApplication
import com.aiemail.superemail.R
import com.aiemail.superemail.databinding.LayoutRecentBinding
import com.aiemail.superemail.databinding.LayoutSearchBinding
import com.aiemail.superemail.utilis.ViewPagerAdapter
import com.aiemail.superemail.viewmodel.EmailViewmodel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*


class SearchActivity : AppCompatActivity(), DataAdapter.RecyclerViewItemClickListener {
    private val items: ArrayList<String> = ArrayList()
    private val searchResults: ArrayList<Email> = ArrayList()
    private lateinit var viewpagerAdapter: ViewPagerAdapter
    private lateinit var binding: LayoutSearchBinding
    val model: EmailViewmodel by viewModels() {
        EmailViewmodel.Factory(
            (application as MyApplication),
            (application as MyApplication).repository
        )
    }
    lateinit var DataAdapter: DataAdapter
    public var mEmailList: ArrayList<Email> = arrayListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LayoutSearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        setupData()
        setupViewPager()
        SetUpSearch()
    }


    private fun SetUpSearch() {

        binding.searchView.setActivated(true)
        binding.searchView.setQueryHint("Type your keyword here")
        binding.searchView.onActionViewExpanded()
        binding.searchView.setIconified(false)
        binding.searchView.clearFocus()

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Called when the user changes the text in the search view
                // Called when the user changes the text in the search view
                searchResults.clear()
                if (newText!!.length != 0 && newText.length > 3) {
                    filter(newText!!.toLowerCase())


                }
                return false
            }
        })
    }

    private fun setupData() {
        binding.back.setOnClickListener {
            super.onBackPressed()
            overridePendingTransition(
                R.anim.slide_in_left,
                R.anim.slide_out
            );
            // overridePendingTransition(R.anim.animate_slide_down_exit,R.anim.animate_slide_down_enter)
        }

        // Initialize the email list and adapter
        val layoutManager1 = LinearLayoutManager(this)
        binding.list.apply {
            layoutManager = layoutManager1
            DataAdapter = DataAdapter(searchResults, this@SearchActivity)
            adapter = (DataAdapter);

        }
        val model: EmailViewmodel by viewModels() {
            EmailViewmodel.Factory(
                (application as MyApplication),
                (application as MyApplication).repository
            )
        }
        CoroutineScope(Dispatchers.Main).launch(Dispatchers.IO) {
            val list = model.getEmailList()
            withContext(Dispatchers.Main) {
                list.observe(this@SearchActivity)
                {
                    Log.d("TAG", "SearchEmail: " + mEmailList.size)

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

    private fun setupViewPager() {


    }

    public fun filter(text: String) {

        // creating a new array list to filter our data.
        val filteredlist: ArrayList<Email> = ArrayList<Email>()

        // running a for loop to compare elements.
        for (item in mEmailList.indices) {
           // Log.d("TAG", "dataterm: " + ">>" +mEmailList.get(item))
            // checking if the entered string matched with any item of our recycler view.
            if (mEmailList.get(item).sender.lowercase(Locale.getDefault())
                    .contains(text.lowercase(Locale.getDefault())) || mEmailList.get(item).senderEmail.lowercase(Locale.getDefault())
                    .contains(text.lowercase(Locale.getDefault()))
            ) {
                // if the item is matched we are
                // adding it to our filtered list.

                filteredlist.add(mEmailList.get(item))
                DataAdapter.addIteams(filteredlist)
                Log.d("TAG", "dataterm: " + ">>" +mEmailList.get(item))
            }
        }
        if (filteredlist.isEmpty()) {

        } else {

            // at last we are passing that filtered
            // list to our adapter class.
            DataAdapter.addIteams(filteredlist)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(
            R.anim.slide_in_left,
            R.anim.slide_out
        );
    }



    override fun clickOnItem(data: Email?) {
        model.MailObject.postValue(data)
        startActivity(
            Intent(
                this,
                ComposeActivity::class.java
            )
        )
        overridePendingTransition(
            R.anim.slide_in_up,
            R.anim.slide_out_up
        )
    }


}

