package com.aiemail.superemail.Activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.aiemail.superemail.R
import com.aiemail.superemail.databinding.ActivityMain1Binding
import com.aiemail.superemail.databinding.LayoutSearchBinding
import com.aiemail.superemail.feature.utilis.ViewPagerAdapter

class SearchActivity:AppCompatActivity() {

    private lateinit var binding: LayoutSearchBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = LayoutSearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupData()
        setupViewPager()
    }
    private fun setupData() {
        binding.back.setOnClickListener {


            //
           super.onBackPressed()
            overridePendingTransition(R.anim.slide_in_left,
                R.anim.slide_out);
          // overridePendingTransition(R.anim.animate_slide_down_exit,R.anim.animate_slide_down_enter)
        }
        // binding.txtMain.text = getString(R.string.ap)
        // binding.imgMain.setImageResource(R.mipmap.ic_launcher)
    }
    private fun setupViewPager() {
        val viewPager = binding.viewPager
        viewPager.adapter = ViewPagerAdapter(supportFragmentManager)
        binding.tabs.setupWithViewPager(viewPager)

    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left,
            R.anim.slide_out);
    }
}