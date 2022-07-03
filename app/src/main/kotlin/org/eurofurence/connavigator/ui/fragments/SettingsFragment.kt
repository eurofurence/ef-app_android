package org.eurofurence.connavigator.ui.fragments

import android.os.Bundle
import android.text.InputType
import android.text.method.TransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.pawegio.kandroid.activityManager
import com.pawegio.kandroid.textWatcher
import org.eurofurence.connavigator.BuildConfig
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.dropins.AnkoLogger
import org.eurofurence.connavigator.dropins.createView
import org.eurofurence.connavigator.dropins.*
import org.eurofurence.connavigator.preferences.*
import org.eurofurence.connavigator.ui.activities.NavActivity
import org.eurofurence.connavigator.util.DatetimeProxy


class SettingsFragment : Fragment(), AnkoLogger {

    lateinit var timeText: TextView

    private fun updateTime() {
        timeText.text = DatetimeProxy.now().toString()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = createView {
        scrollView {
            verticalLayout {
                layoutParams = viewGroupLayoutParams(matchParent, matchParent)

                verticalLayout {
                    padding = dip(10)
                    textView {
                        textResource = R.string.settings_ui_settings
                        padding = dip(15)
                    }

                    checkBox {
                        textResource = R.string.settings_show_irrelevant_announcements
                        isChecked = AppPreferences.showOldAnnouncements
                        setOnCheckedChangeListener { _, b ->
                            AppPreferences.showOldAnnouncements = b
                        }
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

                    linearLayout {
                        weightSum = 10F

                        editText {
                            layoutParams = linearLayoutParams(dip(0), wrapContent, 2f)

                            hintResource = R.string.settings_get_notified_minutes_before
                            setText(
                                AppPreferences.notificationMinutesBefore.toString(),
                                TextView.BufferType.EDITABLE
                            )
                            textWatcher {
                                afterTextChanged { text ->
                                    if (text!!.isNotEmpty()) AppPreferences.notificationMinutesBefore =
                                        text.toString().toInt()
                                }
                            }
                            inputType = InputType.TYPE_CLASS_NUMBER
                        }

                        textView {
                            layoutParams = linearLayoutParams(dip(0), wrapContent, 8f)
                            layoutParams = linearLayoutParams(dip(0), wrapContent) {
                                weight = 2F
                            }

                            textResource = R.string.settings_amount_of_minutes_to_get_an_alert
                            textColorResource = R.color.textBlack
                        }
                    }

                    textView {
                        textResource = R.string.settings_analytics_settings
                        padding = dip(15)
                    }

                    checkBox {
                        textResource = R.string.settings_analytics_track_usage
                        isChecked = AnalyticsPreferences.enabled
                        setOnCheckedChangeListener { _, value ->
                            AnalyticsPreferences.enabled = value
                        }
                    }

                    checkBox {
                        textResource = R.string.settings_analytics_track_performance
                        isChecked = AnalyticsPreferences.performanceTracking
                        setOnCheckedChangeListener { _, value ->
                            AnalyticsPreferences.performanceTracking = value
                        }
                    }
                }

                verticalLayout {
                    padding = dip(10)
                    visibility = if (BuildConfig.DEBUG) View.VISIBLE else View.GONE

                    view {
                        layoutParams = linearLayoutParams(matchParent, dip(1)) {
                            setMargins(0, dip(5), 0, dip(5))
                        }
                        backgroundResource = R.color.primary
                    }

                    textView("Debug settings") {
                        padding = dip(15)
                    }

                    timeText = textView(DatetimeProxy.now().toString()) {

                    }

                    linearLayout {
                        button("-1M") {
                            setOnClickListener {
                                DatetimeProxy.addMinutes(-1)
                                updateTime()
                            }
                        }
                        button("-1H") {
                            setOnClickListener {
                                DatetimeProxy.addHours(-1)
                                updateTime()
                            }
                        }
                        button("-1D") {
                            setOnClickListener {
                                DatetimeProxy.addDays(-1)
                                updateTime()
                            }
                        }
                    }
                    linearLayout {
                        button("RESET") {
                            setOnClickListener {
                                DatetimeProxy.reset()
                                updateTime()
                            }
                        }
                    }

                    linearLayout {
                        button("+1M") {
                            setOnClickListener {
                                DatetimeProxy.addMinutes(1)
                                updateTime()
                            }
                        }
                        button("+1H") {
                            setOnClickListener {
                                DatetimeProxy.addHours(1)
                                updateTime()
                            }
                        }
                        button("+1D") {
                            setOnClickListener {
                                DatetimeProxy.addDays(1)
                                updateTime()
                            }
                        }
                    }

                    // Set loading states
                    textView("Loading states") {

                    }

                    linearLayout {
                        button("UNIT") {
                            setOnClickListener {
                                BackgroundPreferences.loadingState = LoadingState.UNINITIALIZED
                            }
                        }
                        button("DATA") {
                            setOnClickListener {
                                BackgroundPreferences.loadingState = LoadingState.LOADING_DATA
                            }
                        }
                        button("IMG") {
                            setOnClickListener {
                                BackgroundPreferences.loadingState = LoadingState.LOADING_IMAGES
                            }
                        }
                        button("OK") {
                            setOnClickListener {
                                BackgroundPreferences.loadingState = LoadingState.SUCCEEDED
                            }
                        }
                        button("FAIL") {
                            setOnClickListener {
                                BackgroundPreferences.loadingState = LoadingState.FAILED
                            }
                        }
                    }



                    checkBox {
                        textResource = R.string.settings_schedule_events_in_5_minutes
                        isChecked = DebugPreferences.scheduleNotificationsForTest
                        setOnClickListener {
                            DebugPreferences.scheduleNotificationsForTest =
                                !DebugPreferences.scheduleNotificationsForTest
                        }
                    }
                }
            }
        }
    }
}
