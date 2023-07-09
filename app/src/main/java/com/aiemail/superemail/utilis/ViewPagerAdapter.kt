package com.aiemail.superemail.utilis

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.aiemail.superemail.Fragments.RecentFrament1
import com.aiemail.superemail.Fragments.SecondFragment


class ViewPagerAdapter(fm: FragmentManager): FragmentPagerAdapter(fm) {
 
    private val COUNT = 1
    private var fragments: List<Fragment> = ArrayList()
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> RecentFrament1()
            1 -> SecondFragment()
            else -> RecentFrament1()
        }
    }
 
    override fun getCount(): Int {
        return COUNT
    }
    fun setFragments(fragments: List<Fragment?>) {
        this.fragments = fragments as List<Fragment>
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
