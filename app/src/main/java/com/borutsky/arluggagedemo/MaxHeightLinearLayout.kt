package com.example.bottomsheetapptest

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout

class MaxHeightLinearLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    companion object {
        const val ONBOARDING_MESSAGE_HEIGHT_SCALE_PARAM = 0.8
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val newHeightMeasureSpec = MeasureSpec.makeMeasureSpec(calculateOnBoardingMessagePeekHeight(), MeasureSpec.AT_MOST)
        super.onMeasure(widthMeasureSpec, newHeightMeasureSpec)
    }

    private fun calculateOnBoardingMessagePeekHeight(): Int {
        val displayMetrics = context!!.resources.displayMetrics
        return (displayMetrics.heightPixels * ONBOARDING_MESSAGE_HEIGHT_SCALE_PARAM).toInt()
    }

}