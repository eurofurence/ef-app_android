package org.eurofurence.connavigator.util.extensions

import org.joda.time.DateTime
import org.ocpsoft.prettytime.PrettyTime
import java.util.*

/**
 * Created by requinard on 7/9/17.
 */
fun Date.jodatime() = DateTime(this.time)

fun Date.toRelative(): String = PrettyTime().format(this)

fun now(): DateTime = DateTime.now()