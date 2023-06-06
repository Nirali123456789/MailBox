package com.aiemail.superemail.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.aiemail.superemail.databinding.LayoutRecentBinding

class RecentFrament : Fragment() {
 
    private lateinit var binding: LayoutRecentBinding
 
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LayoutRecentBinding.inflate(layoutInflater)
        return binding.root
    }
 
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
 
        setupData()
    }
 
    private fun setupData() {
       // binding.txtMain.text = getString(R.string.ap)
       // binding.imgMain.setImageResource(R.mipmap.ic_launcher)
    }
}