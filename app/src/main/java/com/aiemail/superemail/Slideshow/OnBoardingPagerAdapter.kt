package com.aiemail.superemail.Slideshow

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.aiemail.superemail.R
import com.aiemail.superemail.databinding.OnboardingPageItemBinding
import com.aiemail.superemail.Slideshow.entity.OnBoardingPage
import com.aiemail.superemail.Slideshow.entity.Typewriter


/**
 *OnBoardingPagerAdapter adapter for the viewpager2
 *  @param onBoardingPageList as Array */
class OnBoardingPagerAdapter(private val onBoardingPageList: Array<OnBoardingPage> = OnBoardingPage.values()) :
    RecyclerView.Adapter<OnBoardingPagerAdapter.PagerViewHolder>() {

    lateinit var rightSwipe: Animation

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): PagerViewHolder =
        LayoutInflater.from(parent.context).let {
            rightSwipe = AnimationUtils.loadAnimation(parent.context, R.anim.anim_left);
            OnboardingPageItemBinding.inflate(
                it, parent, false
            ).let { binding -> PagerViewHolder(binding) }
        }


    override fun getItemCount() = onBoardingPageList.size

    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
        holder.bind(onBoardingPageList[position])
    }

    /** PagerViewHolder viewHolder inner class
     * @param binding is OnboardingPageItemBinding to bind data */
    inner class PagerViewHolder(private val binding: OnboardingPageItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        /**
         * @param onBoardingPage is OnBoardingPage item
         * bind view **/
        fun bind(onBoardingPage: OnBoardingPage) {
            val res = binding.root.context.resources
            binding.titleTv.text = res.getString(onBoardingPage.titleResource)

            val writer =
                Typewriter(binding.root.context)
            //binding.root.context.setContentView(writer)

            //Add a character every 150ms

            //Add a character every 150ms

            binding.subTitleTv.text = res.getString(onBoardingPage.subTitleResource)
            binding.subTitleTv.startAnimation(rightSwipe);
            binding.descTV.text = res.getString(onBoardingPage.descriptionResource)
            binding.descTV.startAnimation(rightSwipe);
            binding.img.setImageResource(onBoardingPage.logoResource)
        }

    }
}