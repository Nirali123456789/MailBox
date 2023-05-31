package com.aiemail.superemail.feature.Slideshow.customView

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.viewpager2.widget.ViewPager2
import com.aiemail.superemail.Activities.LoginActivity
import com.aiemail.superemail.core.setParallaxTransformation
import com.aiemail.superemail.databinding.OnboardingViewBinding
import com.aiemail.superemail.domain.OnBoardingPrefManager
import com.aiemail.superemail.Activities.MainActivity
import com.aiemail.superemail.feature.Slideshow.OnBoardingActivity
import com.aiemail.superemail.feature.Slideshow.OnBoardingPagerAdapter
import com.aiemail.superemail.feature.Slideshow.entity.OnBoardingPage

import com.aiemail.superemail.prefs
import java.util.*


class OnBoardingView @JvmOverloads
constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) :
    FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

    private val numberOfPages by lazy { OnBoardingPage.values().size }
    private val prefManager: OnBoardingPrefManager
    var currentPage = 0
    var timer: Timer? = null
    val DELAY_MS: Long = 3000 //delay in milliseconds before task is to be executed

    val PERIOD_MS: Long = 3000

    init {
        val binding = OnboardingViewBinding.inflate(LayoutInflater.from(context), this, true)
        with(binding) {
            setUpSlider()
            addingButtonsClickListeners()
            prefManager = OnBoardingPrefManager(root.context)

            /*After setting the adapter use the timer */
            /*After setting the adapter use the timer */
            val handler = Handler()
            val Update = Runnable {
                if (currentPage === numberOfPages - 1) {
                    currentPage = 0
                }
//                slider.setCurrentItem(currentPage++, true)

                navigateToNextSlide(slider)
            }

            timer = Timer() // This will create a new Thread

            timer!!.schedule(object : TimerTask() {
                // task to be scheduled
                override fun run() {
                    handler.post(Update)
                }
            }, DELAY_MS, PERIOD_MS)
        }

    }

    private fun OnboardingViewBinding.setUpSlider() {
        with(slider) {
            adapter = OnBoardingPagerAdapter()

            setPageTransformer { page, position ->
                setParallaxTransformation(page, position)
            }
//
//            setPageTransformer(pageCompositePageTransformer)

            addSlideChangeListener()


            val wormDotsIndicator = pageIndicator
            wormDotsIndicator.attachTo(this)
        }
    }


    private fun OnboardingViewBinding.addSlideChangeListener() {
         var handler = Handler()
       var a = progress1.getProgress();
        Thread {
            while (a < 100) {
                a += 1
                handler.post {
                    progress1.setProgress(a)
                }
                try {
                    // Sleep for 50 ms to show progress you can change it as well.
                    Thread.sleep(30)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }.start()


        slider.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                if (position==1)
                {
                    a = progress2.getProgress();
                    //handler = Handler()
                    Thread {
                        while (a < 100) {
                            a += 1
                            handler.post {
                                progress2.setProgress(a)
                            }
                            try {
                                // Sleep for 50 ms to show progress you can change it as well.
                                Thread.sleep(30)
                            } catch (e: InterruptedException) {
                                e.printStackTrace()
                            }
                        }
                    }.start()
                }
                if (position==2)
                {
                    handler = Handler()
                    a = progress3.getProgress();
                    Thread {
                        while (a < 100) {
                            a += 1
                            handler.post {
                                progress3.setProgress(a)
                            }
                            try {
                                // Sleep for 50 ms to show progress you can change it as well.
                                Thread.sleep(30)
                            } catch (e: InterruptedException) {
                                e.printStackTrace()
                            }
                        }
                    }.start()
                }
                if (numberOfPages > 1) {
                    val newProgress = (position + positionOffset) / (numberOfPages - 1)
                    //onboardingRoot.progress = newProgress
                    if (position==2)
                    {
                        startBtn.visibility= VISIBLE
                        skipBtn.visibility= GONE
                        nextBtn.visibility= GONE
//                        context.startActivity(Intent(context,MainActivity1::class.java))
//                        (context as OnBoardingActivity).finish()
                    }
                }
            }
        })
    }

    private fun OnboardingViewBinding.addingButtonsClickListeners() {
        nextBtn.setOnClickListener { navigateToNextSlide(slider) }
        skipBtn.setOnClickListener {
            setFirstTimeLaunchToFalse()
        }
        startBtn.setOnClickListener {
            prefs.intExamplePref=true
            setFirstTimeLaunchToFalse()
            if (prefs.islogin) {
                context.startActivity(Intent(context, MainActivity::class.java))
                (context as OnBoardingActivity).finish()
            }else
            {
                context.startActivity(Intent(context, LoginActivity::class.java))
                (context as OnBoardingActivity).finish()
            }

        }
    }

    private fun setFirstTimeLaunchToFalse() {
        prefManager.isFirstTimeLaunch = false
    }

    private fun navigateToNextSlide(slider: ViewPager2?) {
        val nextSlidePos: Int = slider?.currentItem?.plus(1) ?: 0
        Log.d("TAG", "navigateToNextSlide: "+slider?.currentItem)
        slider?.setCurrentItem(nextSlidePos, true)

    }


}