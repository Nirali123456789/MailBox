package com.aiemail.superemail.FragmentAdapter.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.aiemail.superemail.Fragments.SchedulingFragment

class FragmentAdapterSchedule (fragmentManager: FragmentManager, lifcycle: Lifecycle):
    FragmentStateAdapter(fragmentManager,lifcycle) {
    override fun getItemCount(): Int {
        return 2;
    }

    override fun createFragment(position: Int): Fragment {
        return SchedulingFragment()

    }
}