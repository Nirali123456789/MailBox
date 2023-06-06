package com.aiemail.superemail.feature.utilis

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.aiemail.superemail.Fragments.RecentFrament
import com.aiemail.superemail.Fragments.SecondFragment

class ViewPagerAdapter(fm: FragmentManager): FragmentPagerAdapter(fm) {
 
    private val COUNT = 2
 
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> RecentFrament()
            1 -> SecondFragment()
            else -> RecentFrament()
        }
    }
 
    override fun getCount(): Int {
        return COUNT
    }
 
    override fun getPageTitle(position: Int): CharSequence? {
        if (position==0)
        {
            return "Recent"
        }
        else
            return "Saved"

    }
}
