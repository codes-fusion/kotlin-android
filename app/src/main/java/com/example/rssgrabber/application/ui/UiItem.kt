package com.example.rssgrabber.application.ui

import android.content.Context
import android.support.constraint.ConstraintLayout.LayoutParams.*
import android.util.TypedValue
import android.view.Gravity
import android.view.ViewGroup
import android.view.ViewManager
import com.example.rssgrabber.R
import com.example.rssgrabber.application.widgets.ExpandableTextView
import com.wang.avi.AVLoadingIndicatorView
import org.jetbrains.anko.*
import org.jetbrains.anko.constraint.layout.constraintLayout
import org.jetbrains.anko.constraint.layout.guideline
import org.jetbrains.anko.custom.ankoView

class UiItem : UiComponent {
    fun ViewManager.expandableTextView(theme: Int = 0, init: ExpandableTextView.() -> Unit) = ankoView(::ExpandableTextView, theme, init)

    fun Context.attribute(value : Int) : TypedValue {
        val ret = TypedValue()
        theme.resolveAttribute(value, ret, true)
        return ret
    }

    fun Context.attrAsDimen(value : Int) : Int{
        return TypedValue.complexToDimensionPixelSize(attribute(value).data, resources.displayMetrics)
    }

    override fun createView(ui: AnkoContext<ViewGroup>) = with(ui) {
        constraintLayout {
            id = R.id.item_container
            padding = dip(15)

            guideline {
                id = R.id.guideline
            }.lparams(matchParent, wrapContent) {
                orientation = HORIZONTAL
            }

            textView {
                id = R.id.title
                textSize = 18f
                gravity = Gravity.TOP or Gravity.START
                includeFontPadding = false
            }.lparams(0, wrapContent) {
                topMargin = dip(0)
                rightMargin = dip(10)
                topToTop = PARENT_ID
                // matchConstraintDefaultWidth = MATCH_CONSTRAINT_SPREAD
            }

            textView {
                id = R.id.salary
                textSize = 16f
                gravity = Gravity.TOP or Gravity.START
                includeFontPadding = false
            }.lparams(0, wrapContent) {
                topMargin = dip(10)
                topToBottom = R.id.title
                // matchConstraintDefaultWidth = MATCH_CONSTRAINT_SPREAD
            }

            textView {
                id = R.id.description
                textSize = 15f
                textColor = 0x757575.opaque
                maxLines = 2
            }.lparams(0, wrapContent) {
                topMargin = dip(10)
                rightMargin = dip(10)
                rightToLeft = R.id.preview
                topToBottom = R.id.salary
                leftToLeft = PARENT_ID
                verticalChainStyle = CHAIN_PACKED
                matchConstraintDefaultHeight = MATCH_CONSTRAINT_WRAP
            }

            imageView {
                id = R.id.preview
                alpha = 0.8f
            }.lparams(dip(40), dip(40)) {
                topMargin = dip(10)
                topToBottom = R.id.salary
                rightToRight = PARENT_ID
                bottomToBottom = PARENT_ID
                verticalChainStyle = CHAIN_PACKED
            }
        }
    }
}