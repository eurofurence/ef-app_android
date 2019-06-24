package org.eurofurence.connavigator.ui.fragments

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import io.swagger.client.model.EventFeedbackRecord
import io.swagger.client.model.EventRecord
import nl.komponents.kovenant.then
import nl.komponents.kovenant.ui.promiseOnUi
import nl.komponents.kovenant.ui.successUi
import org.eurofurence.connavigator.BuildConfig
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.database.HasDb
import org.eurofurence.connavigator.database.lazyLocateDb
import org.eurofurence.connavigator.ui.LayoutConstants
import org.eurofurence.connavigator.ui.filters.start
import org.eurofurence.connavigator.util.extensions.*
import org.eurofurence.connavigator.webapi.apiService
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.UI
import java.util.*

class EventFeedbackFragment : Fragment(), HasDb {
    val ui = EventFeedbackUi()
    private val args: EventFeedbackFragmentArgs by navArgs()
    val eventId by lazy { UUID.fromString(args.eventId) }
    val event: EventRecord? by lazy { db.events[eventId] }
    val conferenceRoom by lazy { db.rooms[event?.conferenceRoomId] }

    override val db by lazyLocateDb()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            UI { ui.createView(this) }.view

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        event?.let {
            if (!it.isAcceptingFeedback && !BuildConfig.DEBUG)
                findNavController().popBackStack()

            ui.apply {
                titleView.text = it.fullTitle()
                dateView.text = getString(R.string.event_weekday_from_to, it.start.dayOfWeek().asText, it.startTimeString(), it.endTimeString())
                hostView.text = it.ownerString()
                locationView.text = getString(R.string.misc_room_number, conferenceRoom?.name
                        ?: getString(R.string.event_unable_to_locate_room))

                submitButton.setOnClickListener { submit() }
            }
        }
    }

    private fun submit() = promiseOnUi {
        ui.loading()
    } then {
        val rating = 1
        val comments = ui.textInput.text.toString()

        val eventFeedback = EventFeedbackRecord().apply {
            this.eventId = this@EventFeedbackFragment.eventId
            this.message = comments
            this.rating = rating
        }

        apiService.feedbacks.apiEventFeedbackPost(eventFeedback) // TODO: this will 404!
    } successUi {
        ui.done()
    }
}

class EventFeedbackUi : AnkoComponent<Fragment> {
    lateinit var titleView: TextView
    lateinit var dateView: TextView
    lateinit var locationView: TextView
    lateinit var hostView: TextView

    lateinit var submitButton: Button
    lateinit var textInput: EditText

    lateinit var dataInputLayout: LinearLayout
    lateinit var loadingLayout: LinearLayout
    lateinit var doneLayout: LinearLayout

    fun loading() {
        dataInputLayout.visibility = View.GONE
        loadingLayout.visibility = View.VISIBLE
        doneLayout.visibility = View.GONE
    }

    fun done() {
        dataInputLayout.visibility = View.GONE
        loadingLayout.visibility = View.GONE
        doneLayout.visibility = View.VISIBLE
    }

    override fun createView(ui: AnkoContext<Fragment>) = with(ui) {
        scrollView {
            backgroundResource = R.color.backgroundGrey

            verticalLayout {
                textView("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa").lparams(matchParent, dip(0))
                // Event Info
                linearLayout {
                    weightSum = 10F
                    padding = dip(LayoutConstants.MARGIN_LARGE)

                    verticalLayout {
                        fontAwesomeView {
                            padding = dip(LayoutConstants.MARGIN_LARGE)
                            text = "f075".toUnicode()
                            textSize = LayoutConstants.TEXT_ICON_SIZE
                            textColorResource = R.color.primaryDark
                            gravity = Gravity.CENTER
                        }
                    }.weight(2F)

                    verticalLayout {
                        titleView = textView() {
                            padding = dip(LayoutConstants.MARGIN_SMALL)
                            textAppearance = android.R.style.TextAppearance_DeviceDefault_Large
                        }

                        dateView = textView()
                        locationView = textView()
                        hostView = textView()
                    }.weight(8F)
                }.lparams(matchParent, wrapContent)
                // End Event info

                // Event Feedback
                verticalLayout {
                    padding = dip(LayoutConstants.MARGIN_LARGE)
                    backgroundResource = R.color.background_material_light

                    dataInputLayout = verticalLayout {
                        textView(R.string.event_feedback_rate_title) {
                            textColorResource = R.color.textBlack
                            textAppearance = android.R.style.TextAppearance_DeviceDefault_Medium
                        }

                        imageView(R.drawable.placeholder_event) {
                            verticalPadding = dip(LayoutConstants.MARGIN_LARGE)
                        }.lparams(matchParent, dip(100)) // placeholder for rating

                        textView(R.string.event_feedback_comment_header) {
                            verticalPadding = dip(LayoutConstants.MARGIN_LARGE)
                            textColorResource = R.color.textBlack
                            textAppearance = android.R.style.TextAppearance_DeviceDefault_Medium
                        }

                        textInput = editText {
                            hint = context.getString(R.string.event_feedback_comment_hint)
                        }

                        textView(R.string.event_feedback_anonymous_help) {
                            verticalPadding = dip(LayoutConstants.MARGIN_LARGE)
                            textAppearance = android.R.style.TextAppearance_DeviceDefault_Small
                        }

                        linearLayout {
                            weightSum = 10F

                            view().lparams(dip(0), wrapContent, 6F)

                            submitButton = button(R.string.event_feedback_submit) {
                                backgroundResource = R.color.primaryLight
                                textColorResource = R.color.textWhite
                            }.lparams(dip(0), wrapContent, 4F)
                        }.lparams(matchParent, wrapContent) {
                            topMargin = dip(LayoutConstants.MARGIN_LARGE)
                        }
                    }

                    loadingLayout = verticalLayout {
                        visibility = View.GONE
                        progressBar()
                    }

                    doneLayout = verticalLayout {
                        visibility = View.GONE
                        textView(R.string.event_feedback_thank) {
                            textColorResource = R.color.textBlack
                            textAppearance = android.R.style.TextAppearance_DeviceDefault_Medium
                        }
                    }
                }
            }
        }
    }

}

