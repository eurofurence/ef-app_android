package org.eurofurence.connavigator.ui.activities

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.text.InputType
import android.view.View
import android.widget.TextView
import com.pawegio.kandroid.textWatcher
import org.eurofurence.connavigator.BuildConfig
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.pref.AnalyticsPreferences
import org.eurofurence.connavigator.pref.AppPreferences
import org.eurofurence.connavigator.pref.BackgroundPreferences
import org.eurofurence.connavigator.pref.DebugPreferences
import org.jetbrains.anko.*
import org.jetbrains.anko.appcompat.v7.titleResource
import org.jetbrains.anko.appcompat.v7.toolbar

class SettingsActivity : AppCompatActivity(), AnkoLogger {
    val ui by lazy { SettingsUi() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ui.setContentView(this)
    }
}

class SettingsUi : AnkoComponent<SettingsActivity> {
    override fun createView(ui: AnkoContext<SettingsActivity>) = with(ui) {
        scrollView {
            verticalLayout {
                toolbar {
                    titleResource = R.string.settings
                    setTitleTextColor(ContextCompat.getColor(ctx, R.color.textWhite))
                    backgroundResource = R.color.primary
                }
                verticalLayout {
                    padding = dip(10)
                    textView {
                        textResource = R.string.settings_ui_settings
                        padding = dip(15)
                    }

                    checkBox {
                        textResource = R.string.settings_show_irrelevant_announcements
                        isChecked = AppPreferences.showOldAnnouncements
                        setOnCheckedChangeListener { _, b -> AppPreferences.showOldAnnouncements = b }
                    }

                    checkBox {
                        textResource = R.string.settings_use_days_of_the_week
                        isChecked = AppPreferences.shortenDates
                        setOnCheckedChangeListener { _, b -> AppPreferences.shortenDates = b }
                    }

                    checkBox {
                        textResource = R.string.settings_switch_short_and_long_press_for_events
                        isChecked = AppPreferences.dialogOnEventPress
                        setOnCheckedChangeListener { _, b -> AppPreferences.dialogOnEventPress = b }
                    }

                    checkBox {
                        textResource = R.string.settings_immediately_close_app_on_back
                        isChecked = BackgroundPreferences.closeAppImmediately
                        setOnCheckedChangeListener { _, b -> BackgroundPreferences.closeAppImmediately = b }
                    }

                    linearLayout {
                        weightSum = 10F

                        editText {
                            hintResource = R.string.settings_get_notified_minutes_before
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

                        textView {
                            textResource = R.string.settings_amount_of_minutes_to_get_an_alert
                            textColor = ContextCompat.getColor(ctx, R.color.textBlack)
                        }.lparams(dip(0), wrapContent) {
                            weight = 8F
                        }
                    }



                    textView {
                        textResource = R.string.settings_analytics_settings
                        padding = dip(15)
                    }

                    checkBox {
                        textResource = R.string.settings_analytics_track_usage
                        isChecked = AnalyticsPreferences.enabled
                        setOnCheckedChangeListener { _, value -> AnalyticsPreferences.enabled = value }
                    }

                    checkBox {
                        textResource = R.string.settings_analytics_track_performance
                        isChecked = AnalyticsPreferences.performanceTracking
                        setOnCheckedChangeListener { _, value -> AnalyticsPreferences.performanceTracking = value }
                    }
                }

                verticalLayout {
                    padding = dip(10)
                    visibility = if (BuildConfig.DEBUG) View.VISIBLE else View.GONE

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
                        textResource = R.string.settings_tweak_event_days_so_it_seems_like_its_today
                        isChecked = DebugPreferences.debugDates
                        setOnClickListener { DebugPreferences.debugDates = !DebugPreferences.debugDates }
                    }

                    linearLayout {
                        weightSum = 10F

                        editText {
                            hintResource = R.string.settings_amount_of_days_to_offset_by
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
                            textResource = R.string.settings_amount_of_days_to_offset_event_schedule_by
                            textColor = ContextCompat.getColor(ctx, R.color.textBlack)
                        }.lparams(dip(0), wrapContent) {
                            weight = 8F
                        }
                    }

                    checkBox {
                        textResource = R.string.settings_schedule_events_in_5_minutes
                        isChecked = DebugPreferences.scheduleNotificationsForTest
                        setOnClickListener { DebugPreferences.scheduleNotificationsForTest = !DebugPreferences.scheduleNotificationsForTest }
                    }
                }

                lparams(matchParent, matchParent)
            }
        }
    }
}