package com.aiemail.superemail.FragmentAdapter.main

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.aiemail.superemail.Fragments.Smart2Fragment
import com.aiemail.superemail.Fragments.SmartFragment

class FragmentpageAdapter(fragmentManager: FragmentManager,lifcycle:Lifecycle):FragmentStateAdapter(fragmentManager,lifcycle) {
    override fun getItemCount(): Int {
        return 2;
    }

    override fun createFragment(position: Int): Fragment {
     return if (position==0)
      {
          Log.d("TAG", "createFragment: "+position)
      SmartFragment()

      }else
      {
          Log.d("TAG", "createFragment: "+position)
          Smart2Fragment()
      }
    }
}