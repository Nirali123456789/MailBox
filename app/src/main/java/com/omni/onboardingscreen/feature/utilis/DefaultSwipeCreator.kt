package com.omni.onboardingscreen.feature.utilis

import android.content.Context
import androidx.annotation.StringRes
import com.github.ygngy.multiswipe.Swipe
import com.omni.onboardingscreen.R

/**
 * This is a sample class for default and simple swipe creation.
 * For customized (or detailed) swipe creation see [SwipeCreator] class.
 *
 * @constructor
 * @param context Used to get resources from.
 * @param withLabel if true labels will be drawn otherwise only icons will be drawn
 */
class DefaultSwipeCreator(context: Context, private val withLabel: Boolean): SwipeCreatorBase(context) {

    override val shareSwipe = Swipe(
        context = context,
        id = SWIPE_TO_SHARE_ID,
        activeIcon = getDrawable(R.drawable.share),
        activeLabel = getLabel(R.string.search),
        inactiveIcon = getDrawable(R.drawable.ic_archive)
    )

    override val copySwipe = Swipe(
        context = context,
        id = SWIPE_TO_COPY_ID,
        activeIcon = getDrawable(R.drawable.share),
        activeLabel = getLabel(R.string.search),
        inactiveIcon = getDrawable(R.drawable.ic_archive)
    )

    override val cutSwipe = Swipe(
        context = context,
        id = SWIPE_TO_CUT_ID,
        activeIcon = getDrawable(R.drawable.ic_archive),
        activeLabel = getLabel(R.string.search),
        inactiveIcon = getDrawable(R.drawable.share)
    )




    private fun getLabel(@StringRes stringRes: Int) =
        if (withLabel) getString(stringRes) else null

}