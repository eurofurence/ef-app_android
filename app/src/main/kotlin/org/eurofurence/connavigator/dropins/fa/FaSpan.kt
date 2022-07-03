package org.eurofurence.connavigator.dropins.fa

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Paint.FontMetricsInt
import android.graphics.Rect
import android.graphics.Typeface
import android.text.style.ReplacementSpan

class FaSpan(
    private val icon: String, private val type: Typeface,
    private val iconSizePx: Float = -1f, private val iconColor: Int = Int.MAX_VALUE
) : ReplacementSpan() {
    override fun getSize(
        paint: Paint, text: CharSequence,
        start: Int, end: Int, fm: FontMetricsInt?
    ): Int {
        LOCAL_PAINT.set(paint)
        applyCustomTypeFace(LOCAL_PAINT, type)
        LOCAL_PAINT.getTextBounds(icon, 0, 1, TEXT_BOUNDS)
        if (fm != null) {
            fm.descent = (TEXT_BOUNDS.height() * BASELINE_RATIO).toInt()
            fm.ascent = -(TEXT_BOUNDS.height() - fm.descent)
            fm.top = fm.ascent
            fm.bottom = fm.descent
        }
        return TEXT_BOUNDS.width()
    }

    override fun draw(
        canvas: Canvas, text: CharSequence,
        start: Int, end: Int, x: Float, top: Int, y: Int,
        bottom: Int, paint: Paint
    ) {
        applyCustomTypeFace(paint, type)
        paint.getTextBounds(icon, 0, 1, TEXT_BOUNDS)
        canvas.save()
        canvas.drawText(
            icon,
            x - TEXT_BOUNDS.left,
            y - TEXT_BOUNDS.bottom + TEXT_BOUNDS.height() * BASELINE_RATIO, paint
        )
        canvas.restore()
    }

    private fun applyCustomTypeFace(paint: Paint, tf: Typeface) {
        paint.isFakeBoldText = false
        paint.textSkewX = 0f
        paint.typeface = tf
        if (iconSizePx > 0) paint.textSize = iconSizePx
        if (iconColor < Int.MAX_VALUE) paint.color = iconColor
    }

    companion object {
        private val TEXT_BOUNDS = Rect()
        private val LOCAL_PAINT = Paint()
        private const val BASELINE_RATIO = 1 / 7f
    }
}
