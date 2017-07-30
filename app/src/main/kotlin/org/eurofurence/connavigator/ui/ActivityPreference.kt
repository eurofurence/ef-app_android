package org.eurofurence.connavigator.ui

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.text.InputType
import android.widget.TextView
import com.pawegio.kandroid.textWatcher
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.pref.AnalyticsPreferences
import org.eurofurence.connavigator.pref.AppPreferences
import org.eurofurence.connavigator.pref.DebugPreferences
import org.jetbrains.anko.*
import org.jetbrains.anko.appcompat.v7.toolbar

class ActivitySettings : AppCompatActivity(), AnkoLogger {
    val ui by lazy { SettingsUi() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ui.setContentView(this)
    }
}

class SettingsUi : AnkoComponent<ActivitySettings> {
    override fun createView(ui: AnkoContext<ActivitySettings>) = with(ui) {
        scrollView {
            verticalLayout {
                toolbar {
                    title = "Settings"
                    setTitleTextColor(ContextCompat.getColor(ctx, R.color.textWhite))
                    backgroundResource = R.color.primary
                }
                verticalLayout {
                    padding = dip(10)
                    textView("UI settings") {
                        padding = dip(15)
                    }

                    checkBox {
                        text = "Show old announcements"
                        isChecked = AppPreferences.showOldAnnouncements
                        setOnClickListener { AppPreferences.showOldAnnouncements = !AppPreferences.showOldAnnouncements }
                    }

                    checkBox {
                        text = "Use shortened dates (Wed, Fri) instead of complete dates (Aug 18)"
                        isChecked = AppPreferences.shortenDates
                        setOnClickListener { AppPreferences.shortenDates = !AppPreferences.shortenDates }
                    }

                    linearLayout {
                        weightSum = 10F

                        editText {
                            hint = "The count of minutes before an event that we'll send a notification"
                            setText(AppPreferences.notificationMinutesBefore.toString(), TextView.BufferType.EDITABLE)
                            textWatcher {
                                afterTextChanged { text ->
                                    if (text!!.isNotEmpty()) AppPreferences.notificationMinutesBefore = text.toString().toInt()
                                }
                            }
                            inputType = InputType.TYPE_CLASS_NUMBER
                        }.lparams(dip(0), wrapContent) {
                            weight = 2F
                        }

                        textView("The amount of minutes before an event you want to get notified") {
                            textColor = ContextCompat.getColor(ctx, R.color.textBlack)
                        }.lparams(dip(0), wrapContent) {
                            weight = 8F
                        }
                    }



                    textView("Analytics settings") {
                        padding = dip(15)
                    }

                    checkBox {
                        text = "Track usage with analytics"
                        isChecked = AnalyticsPreferences.enabled
                        setOnCheckedChangeListener { _, value -> AnalyticsPreferences.enabled = value }
                    }

                    checkBox {
                        text = "Track performance statistics"
                        isChecked = AnalyticsPreferences.performanceTracking
                        setOnCheckedChangeListener { _, value -> AnalyticsPreferences.performanceTracking = value }
                    }
                }

                verticalLayout {
                    padding = dip(10)

                    view {
                        lparams(matchParent, dip(1)) {
                            verticalMargin = dip(5)
                        }
                        backgroundResource = R.color.primary
                    }

                    textView("Debug settings") {
                        padding = dip(15)
                    }

                    checkBox {
                        text = "Tweak event days so it seems like it's the current date"
                        isChecked = DebugPreferences.debugDates
                        setOnClickListener { DebugPreferences.debugDates = !DebugPreferences.debugDates }
                    }

                    linearLayout {
                        weightSum = 10F

                        editText {
                            hint = "The amount of days to offset by. Must be integer"
                            setText(DebugPreferences.eventDateOffset.toString(), TextView.BufferType.EDITABLE)
                            textWatcher {
                                afterTextChanged { text ->
                                    if (text!!.isNotEmpty()) DebugPreferences.eventDateOffset = text.toString().toInt()
                                }
                            }
                            inputType = InputType.TYPE_CLASS_NUMBER
                        }.lparams(dip(0), wrapContent) {
                            weight = 2F
                        }

                        textView("Amount of days to offset the event schedule by") {
                            textColor = ContextCompat.getColor(ctx, R.color.textBlack)
                        }.lparams(dip(0), wrapContent) {
                            weight = 8F
                        }
                    }

                    checkBox {
                        text = "Schedule notifications in 5 minutes instead of event time"
                        isChecked = DebugPreferences.scheduleNotificationsForTest
                        setOnClickListener { DebugPreferences.scheduleNotificationsForTest = !DebugPreferences.scheduleNotificationsForTest }
                    }
                }

                lparams(matchParent, matchParent)
            }
        }
    }
}

