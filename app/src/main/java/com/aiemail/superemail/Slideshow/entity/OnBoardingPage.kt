package com.aiemail.superemail.Slideshow.entity
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.aiemail.superemail.R

enum class OnBoardingPage(
    @StringRes val titleResource: Int,
    @StringRes val subTitleResource: Int,
    @StringRes val descriptionResource: Int,
    @DrawableRes val logoResource: Int
) {

    ONE(R.string.onboarding_slide1_title, R.string.onboarding_slide1_subtitle,R.string.onboarding_slide1_desc, R.drawable.ic_directions),
    TWO(R.string.onboarding_slide2_title, R.string.onboarding_slide2_subtitle,R.string.onboarding_slide2_desc, R.drawable.ic_hang_out),
    THREE(R.string.onboarding_slide2_title, R.string.onboarding_slide3_subtitle,R.string.onboarding_slide1_desc, R.drawable.ic_a_day_at_the_park)

}