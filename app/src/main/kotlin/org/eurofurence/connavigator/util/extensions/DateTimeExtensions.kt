package org.eurofurence.connavigator.util.extensions

import android.text.method.DateTimeKeyListener
import net.danlew.android.joda.JodaTimeAndroid
import org.joda.time.DateTime
import java.util.*

/**
 * Created by requinard on 7/9/17.
 */
fun Date.jodatime() = DateTime(this.time)

fun now() = DateTime.now()