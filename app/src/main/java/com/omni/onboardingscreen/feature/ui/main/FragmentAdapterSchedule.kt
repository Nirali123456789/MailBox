package com.omni.onboardingscreen.feature.ui.main

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.omni.onboardingscreen.Fragments.SchedulingFragment
import com.omni.onboardingscreen.Fragments.Smart2Fragment
import com.omni.onboardingscreen.Fragments.SmartFragment

class FragmentAdapterSchedule (fragmentManager: FragmentManager, lifcycle: Lifecycle):
    FragmentStateAdapter(fragmentManager,lifcycle) {
    override fun getItemCount(): Int {
        return 2;
    }

    override fun createFragment(position: Int): Fragment {
        return SchedulingFragment()

    }
}