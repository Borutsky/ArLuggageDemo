package com.example.bottomsheetapptest

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.widget.NestedScrollView
import com.borutsky.arluggagedemo.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class ArBottomSheetFragment : BottomSheetDialogFragment() {

    companion object {
        const val PEEK_HEIGHT = 150
    }
    private lateinit var behavior: BottomSheetBehavior<FrameLayout>
    private lateinit var blabla: MaxHeightLinearLayout
    private lateinit var scrollView: NestedScrollView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.ar_bag_bottom_sheet_view, container, false)
    }

    override fun getTheme(): Int {
        return R.style.AppBottomSheetDialogTheme
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        blabla = getView()!!.findViewById(R.id.cardView)
        scrollView = view.findViewById(R.id.nestedScrollView)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(activity!!, theme)
        dialog.setOnShowListener { dialogInterface: DialogInterface ->
            val bottomSheetDialog = dialogInterface as BottomSheetDialog
            val bottomSheet =
                bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as
                        FrameLayout?
            behavior = BottomSheetBehavior.from(bottomSheet!!)
            behavior.state = BottomSheetBehavior.STATE_COLLAPSED
            behavior.peekHeight = PEEK_HEIGHT

        }
        return dialog
    }
}