package com.aiemail.superemail.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.aiemail.superemail.databinding.LayoutSearchBinding
import com.aiemail.superemail.Activities.MainActivity
import com.aiemail.superemail.feature.utilis.ViewPagerAdapter

class SearchFragment : Fragment() {

    private lateinit var binding: LayoutSearchBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LayoutSearchBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupData()
        setupViewPager()
    }

    private fun setupData() {
        binding.back.setOnClickListener {

            ( getActivity() as MainActivity)!!.fm.popBackStack();
          //
        //  activity!!.finish()
           // activity!!.overridePendingTransition(R.anim.animate_slide_down_exit,R.anim.animate_slide_down_enter)
        }
        // binding.txtMain.text = getString(R.string.ap)
        // binding.imgMain.setImageResource(R.mipmap.ic_launcher)
    }
    private fun setupViewPager() {
        val viewPager = binding.viewPager
        viewPager.adapter = ViewPagerAdapter(activity!!.supportFragmentManager)
        binding.tabs.setupWithViewPager(viewPager)

    }

}