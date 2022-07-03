package org.eurofurence.connavigator.util.extensions

import android.content.Context
import android.text.SpannableStringBuilder
import android.text.Spanned
import org.eurofurence.connavigator.dropins.fa.Fa
import org.eurofurence.connavigator.dropins.fa.FaSpan
import org.eurofurence.connavigator.dropins.fa.FaCache


fun SpannableStringBuilder.appendFa(
    context: Context,
    icon: String,
    px: Float = -1f,
    color: Int = Int.MAX_VALUE
) = append(
    icon,
    FaSpan(icon, FaCache.fontAwesome(context), px, color),
    Spanned.SPAN_INCLUSIVE_EXCLUSIVE
)