package org.eurofurence.connavigator.ui.fragments

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.pawegio.kandroid.textWatcher
import org.eurofurence.connavigator.BuildConfig
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.preferences.*
import org.eurofurence.connavigator.util.DatetimeProxy
import org.jetbrains.anko.*
import org.jetbrains.anko.appcompat.v7.titleResource
import org.jetbrains.anko.appcompat.v7.toolbar
import org.jetbrains.anko.support.v4.UI

class SettingsFragment : Fragment(), AnkoLogger {
    val ui by lazy { SettingsUi() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
            UI { ui.createView(this) }.view
}

class SettingsUi : AnkoComponent<Fragment> {
    private fun updateTime() {
        timeText.text = DatetimeProxy.now().toString()
    }

    lateinit var timeText: TextView

    override fun createView(ui: AnkoContext<Fragment>) = with(ui) {
        scrollView {
            verticalLayout {
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
                        backgroundResource = R.color.primary
                    }.lparams(matchParent, dip(1)) {
                        verticalMargin = dip(5)
                    }

                    textView("Debug settings") {
                        padding = dip(15)
                    }

                    timeText = textView(DatetimeProxy.now().toString())
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
                    textView("Loading states")
                    linearLayout {
                        button("UNIT") {
                            setOnClickListener { BackgroundPreferences.loadingState = LoadingState.UNINITIALIZED }
                        }
                        button("DATA") {
                            setOnClickListener { BackgroundPreferences.loadingState = LoadingState.LOADING_DATA }
                        }
                        button("IMG") {
                            setOnClickListener { BackgroundPreferences.loadingState = LoadingState.LOADING_IMAGES }
                        }
                        button("OK") {
                            setOnClickListener { BackgroundPreferences.loadingState = LoadingState.SUCCEEDED }
                        }
                        button("FAIL") {
                            setOnClickListener { BackgroundPreferences.loadingState = LoadingState.FAILED }
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