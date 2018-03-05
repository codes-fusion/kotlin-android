package com.example.rssgrabber.application.widgets

import android.annotation.TargetApi
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.support.annotation.DrawableRes
import android.text.TextUtils
import android.util.AttributeSet
import android.util.SparseBooleanArray
import android.view.MotionEvent
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.Transformation
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.example.rssgrabber.R

class ExpandableTextView: LinearLayout, View.OnClickListener {
    private val TAG = ExpandableTextView::class.java.simpleName
    private val MAX_COLLAPSED_LINES = 5
    private val DEFAULT_ANIM_DURATION = 300
    private val DEFAULT_ANIM_ALPHA_START = 0.7f

    private var mTv: TextView? = null
    private var mButton: ImageView? = null
    private var mRelayout:Boolean = false
    private var mCollapsed = true
    private var mCollapsedHeight: Int = 0
    private var mTextHeightWithMaxLines: Int = 0
    private var mMaxCollapsedLines: Int = 0
    private var mMarginBetweenTxtAndBottom: Int = 0
    private var mExpandDrawable: Drawable? = null
    private var mCollapseDrawable: Drawable? = null
    private var mAnimationDuration:Int = 0
    private var mAnimAlphaStart:Float = 0.toFloat()
    private var mAnimating:Boolean = false
    private var mListener:OnExpandStateChangeListener? = null
    private var mCollapsedStatus: SparseBooleanArray? = null
    private var mPosition:Int = 0

    var text:CharSequence?
        get() = if (mTv == null) { "" } else mTv?.text
        set(text) {
            mRelayout = true
            mTv?.text = text
            visibility = if (TextUtils.isEmpty(text)) View.GONE else View.VISIBLE
        }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    constructor(context: Context, attrs: AttributeSet, defStyle:Int) : super(context, attrs, defStyle) {
        init(attrs)
    }

    constructor(context: Context) : super(context, null) {
        init(null)
    }

    constructor(context: Context, attrs: AttributeSet? = null) : super(context, attrs) {
        init(attrs)
    }

    override fun setOrientation(orientation:Int) {
        if (LinearLayout.HORIZONTAL == orientation) {
            throw IllegalArgumentException("ExpandableTextView only supports Vertical Orientation.")
        }
        super.setOrientation(orientation)
    }

    override fun onClick(view: View) {
        if (mButton?.visibility != View.VISIBLE) {
            return
        }

        mCollapsed = !mCollapsed
        mButton?.setImageDrawable(if (mCollapsed) mExpandDrawable else mCollapseDrawable)

        if (mCollapsedStatus != null) {
            mCollapsedStatus?.put(mPosition, mCollapsed)
        }

        // mark that the animation is in progress
        mAnimating = true

        val animation: Animation = if (mCollapsed) {
            ExpandCollapseAnimation(this, height, mCollapsedHeight)
        } else {
            ExpandCollapseAnimation(this, height, height + mTextHeightWithMaxLines - mTv!!.height)
        }

        animation.fillAfter = true
        animation.setAnimationListener(object: Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {
                applyAlphaAnimation(mTv, mAnimAlphaStart)
            }

            override fun onAnimationEnd(animation: Animation) {
                // clear animation here to avoid repeated applyTransformation() calls
                clearAnimation()

                // clear the animation flag
                mAnimating = false

                // notify the listener
                if (mListener != null) {
                    mListener?.onExpandStateChanged(mTv, !mCollapsed)
                }
            }
            override fun onAnimationRepeat(animation: Animation) {}
        })

        clearAnimation()
        startAnimation(animation)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent):Boolean {
        // while an animation is in progress, intercept all the touch events to children to
        // prevent extra clicks during the animation
        return mAnimating
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        findViews()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // If no change, measure and return
        if (!mRelayout || visibility == View.GONE) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
            return
        }

        mRelayout = false

        // Setup with optimistic case
        // i.e. Everything fits. No button needed
        mButton?.visibility = View.GONE
        mTv?.maxLines = Integer.MAX_VALUE

        // Measure
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        // If the text fits in collapsed mode, we are done.
        if (mTv?.lineCount ?: 0 <= mMaxCollapsedLines) {
            return
        }

        // Saves the text height w/ max lines
        mTv?.let {
            mTextHeightWithMaxLines = getRealTextViewHeight(it)
        }

        // Doesn't fit in collapsed mode. Collapse text view as needed. Show
        // button.
        if (mCollapsed) {
            mTv?.maxLines = mMaxCollapsedLines
        }

        mButton?.visibility = View.VISIBLE

        // Re-measure with new setup
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        if (mCollapsed) {
            // Gets the margin between the TextView's bottom and the ViewGroup's bottom
            mTv?.post { mMarginBetweenTxtAndBottom = height - mTv?.height!! }

            // Saves the collapsed height of this ViewGroup
            mCollapsedHeight = measuredHeight
        }
    }

    fun setOnExpandStateChangeListener(listener:OnExpandStateChangeListener?) {
        mListener = listener
    }

    fun setDefaultText(text: CharSequence?) {
        mRelayout = true
        mTv?.text = text
        visibility = if (TextUtils.isEmpty(text)) View.GONE else View.VISIBLE
    }

    fun setText(text: CharSequence?, collapsedStatus: SparseBooleanArray, position: Int) {
        mCollapsedStatus = collapsedStatus
        mPosition = position
        val isCollapsed = collapsedStatus.get(position, true)
        clearAnimation()
        mCollapsed = isCollapsed
        mButton?.setImageDrawable(if (mCollapsed) mExpandDrawable else mCollapseDrawable)

        mRelayout = true
        mTv?.text = text
        visibility = if (TextUtils.isEmpty(text)) View.GONE else View.VISIBLE

        layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT
        requestLayout()
    }

    private fun init(attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ExpandableTextView)
        mMaxCollapsedLines = typedArray.getInt(R.styleable.ExpandableTextView_maxCollapsedLines, MAX_COLLAPSED_LINES)
        mAnimationDuration = typedArray.getInt(R.styleable.ExpandableTextView_animDuration, DEFAULT_ANIM_DURATION)
        mAnimAlphaStart = typedArray.getFloat(R.styleable.ExpandableTextView_animAlphaStart, DEFAULT_ANIM_ALPHA_START)
        mExpandDrawable = typedArray.getDrawable(R.styleable.ExpandableTextView_expandDrawable)
        mCollapseDrawable = typedArray.getDrawable(R.styleable.ExpandableTextView_collapseDrawable)

        if (mExpandDrawable == null) {
            mExpandDrawable = getDrawable(context, R.drawable.ic_menu_camera)
        }
        if (mCollapseDrawable == null) {
            mCollapseDrawable = getDrawable(context, R.drawable.ic_menu_gallery)
        }

        typedArray.recycle()

        // enforces vertical orientation
        orientation = LinearLayout.VERTICAL

        // default visibility is gone
        visibility = View.GONE
    }

    fun findViews() {
        mTv = findViewById<View>(R.id.expandable_text) as TextView?
        mTv?.setOnClickListener(this)
        mButton = findViewById<View>(R.id.expand_collapse) as ImageView?
        mButton?.setImageDrawable(if (mCollapsed) mExpandDrawable else mCollapseDrawable)
        mButton?.setOnClickListener(this)
    }

    private fun isPostHoneycomb(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB
    }

    private fun isPostLolipop(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private fun applyAlphaAnimation(view: View?, alpha: Float) {
        if (isPostHoneycomb()) {
            view?.alpha = alpha
        } else {
            val alphaAnimation = AlphaAnimation(alpha, alpha)
            // make it instant
            alphaAnimation.duration = 0
            alphaAnimation.fillAfter = true
            view?.startAnimation(alphaAnimation)
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun getDrawable(context: Context, @DrawableRes resId: Int): Drawable {
        val resources = context.resources
        return if (isPostLolipop()) {
            resources.getDrawable(resId, context.theme)
        } else {
            resources.getDrawable(resId)
        }
    }

    private fun getRealTextViewHeight(textView: TextView): Int {
        val textHeight = textView.layout.getLineTop(textView.lineCount)
        val padding = textView.compoundPaddingTop + textView.compoundPaddingBottom
        return textView.measuredHeight + padding
    }

    inner class ExpandCollapseAnimation(private val mTargetView: View, private val mStartHeight: Int, private val mEndHeight: Int) : Animation() {
        init {
            duration = mAnimationDuration.toLong()
        }

        override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
            val newHeight = ((mEndHeight - mStartHeight) * interpolatedTime + mStartHeight).toInt()
            mTv?.maxHeight = newHeight - mMarginBetweenTxtAndBottom
            if (java.lang.Float.compare(mAnimAlphaStart, 1.0f) != 0) {
                applyAlphaAnimation(mTv, mAnimAlphaStart + interpolatedTime * (1.0f - mAnimAlphaStart))
            }
            mTargetView.layoutParams.height = newHeight
            mTargetView.requestLayout()
        }

        override fun initialize(width: Int, height: Int, parentWidth: Int, parentHeight: Int) {
            super.initialize(width, height, parentWidth, parentHeight)
        }

        override fun willChangeBounds(): Boolean {
            return true
        }
    }

    interface OnExpandStateChangeListener {
        /**
         * Called when the expand/collapse animation has been finished
         *
         * @param textView - TextView being expanded/collapsed
         * @param isExpanded - true if the TextView has been expanded
         */
        fun onExpandStateChanged(textView: TextView?, isExpanded: Boolean)
    }
}