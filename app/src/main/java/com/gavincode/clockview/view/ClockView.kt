package com.gavincode.clockview.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.gavincode.clockview.R
import java.util.*


class ClockView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private var mSize = 0
    private val backgroundPaint = Paint()
    private var mInnerColor: Int = Color.WHITE
    private var mOuterColor: Int = Color.CYAN
    private var mNeedleSecondColor: Int = Color.WHITE
    private val secondRect = RectF()
    private val hourRect = RectF()
    private val minuteRect = RectF()
    private val needlePaint = Paint()
    private var secondDegrees = 0f
    private var hourDegrees = 0f
    private var minuteDegrees = 0f

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.ClockView,
            0, 0
        ).apply {
            try {
                mInnerColor = getColor(R.styleable.ClockView_innerColor, Color.WHITE)
                mOuterColor = getColor(R.styleable.ClockView_outerColor, Color.CYAN)
                mNeedleSecondColor = getColor(R.styleable.ClockView_needleSecondColor, Color.WHITE)
            } finally {
                recycle()
            }
        }

        val thread = Thread(Runnable {
            while (true) {
                val currentTime = Calendar.getInstance().time
                val step = 360f / 60f
                val hourStep = 360f / 12f
                secondDegrees = step * currentTime.seconds
                val minutesDegreesForHours =
                    currentTime.minutes / 2f
                hourDegrees = hourStep * currentTime.hours + minutesDegreesForHours
                minuteDegrees = step * currentTime.minutes

                postInvalidate()
                Thread.sleep(1000)
            }
        })
        thread.start()
    }



    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mSize = Math.min(measuredWidth, measuredHeight)
        setMeasuredDimension(mSize, mSize)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.apply {
            drawBackground(this)
            drawHourNeedle(this)
            drawMinuteNeedle(this)
            drawSecondNeedle(this)
            backgroundPaint.color = Color.WHITE
            canvas.drawCircle(mSize / 2f, mSize / 2f, mSize * 0.05f / 2f, backgroundPaint)
        }
    }

    private fun drawBackground(canvas: Canvas) {
        val centerX = mSize / 2f
        val centerY = mSize / 2f
        backgroundPaint.style = Paint.Style.STROKE
        backgroundPaint.color = Color.GRAY
        backgroundPaint.strokeWidth = 2f
        canvas.drawCircle(centerX, centerY, mSize / 2f, backgroundPaint)

        backgroundPaint.style = Paint.Style.FILL
        backgroundPaint.color = mOuterColor
        canvas.drawCircle(centerX, centerY, mSize * 0.95f / 2f, backgroundPaint)

        backgroundPaint.color = mInnerColor
        canvas.drawCircle(centerX, centerY, mSize * 0.55f / 2f, backgroundPaint)


        val dotPaint = Paint()
        dotPaint.color = Color.WHITE
        dotPaint.style = Paint.Style.FILL
//        canvas.drawCircle(centerX, centerY, mSize * 0.8f / 2.0f, dotPaint)

        for (i in 0..11) {
            val degrees = 360f / 12f * i
            canvas.save()
            canvas.rotate(degrees, centerX, centerY)
            canvas.drawCircle(centerX, mSize / 2f * 0.2f, 10f, dotPaint)
            canvas.restore()
        }
    }

    private fun drawSecondNeedle(canvas: Canvas) {
        val centerX = mSize / 2f
        val centerY = mSize / 2f
        val width = 10
        val height = mSize * 0.65f / 2f
        secondRect.left = centerX - width / 2f
        secondRect.right = centerX + width / 2f
        secondRect.top = centerY - height
        secondRect.bottom = centerY
        needlePaint.style = Paint.Style.FILL
        needlePaint.color = mNeedleSecondColor
        canvas.save()
        canvas.rotate(secondDegrees, centerX, centerY)
        canvas.drawRoundRect(secondRect, centerX, centerY, needlePaint)
        canvas.restore()
    }

    private fun drawHourNeedle(canvas: Canvas) {
        val centerX = mSize / 2f
        val centerY = mSize / 2f
        val width = 25
        val height = mSize * 0.35f / 2f
        hourRect.left = centerX - width / 2f
        hourRect.right = centerX + width / 2f
        hourRect.top = centerY - height
        hourRect.bottom = centerY
        needlePaint.style = Paint.Style.FILL
        needlePaint.color = Color.WHITE
        canvas.save()
        canvas.rotate(hourDegrees, centerX, centerY)
        canvas.drawRoundRect(hourRect, centerX, centerY, needlePaint)
        canvas.restore()
    }

    private fun drawMinuteNeedle(canvas: Canvas) {
        val centerX = mSize / 2f
        val centerY = mSize / 2f
        val width = 15
        val height = mSize * 0.60f / 2f
        minuteRect.left = centerX - width / 2f
        minuteRect.right = centerX + width / 2f
        minuteRect.top = centerY - height
        minuteRect.bottom = centerY
        needlePaint.style = Paint.Style.FILL
        needlePaint.color = Color.WHITE
        canvas.save()
        canvas.rotate(minuteDegrees, centerX, centerY)
        canvas.drawRoundRect(minuteRect, centerX, centerY, needlePaint)
        canvas.restore()
    }
}