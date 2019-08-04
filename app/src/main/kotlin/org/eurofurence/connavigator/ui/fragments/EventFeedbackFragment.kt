package org.eurofurence.connavigator.ui.fragments

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import io.swagger.client.model.EventFeedbackRecord
import io.swagger.client.model.EventRecord
import nl.komponents.kovenant.then
import nl.komponents.kovenant.ui.failUi
import nl.komponents.kovenant.ui.promiseOnUi
import nl.komponents.kovenant.ui.successUi
import org.eurofurence.connavigator.BuildConfig
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.database.HasDb
import org.eurofurence.connavigator.database.lazyLocateDb
import org.eurofurence.connavigator.ui.LayoutConstants
import org.eurofurence.connavigator.ui.filters.start
import org.eurofurence.connavigator.util.extensions.*
import org.eurofurence.connavigator.services.apiService
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.UI
import org.jetbrains.anko.support.v4.longToast
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
        val rating = ui.ratingBar.rating.toInt()
        val comments = ui.textInput.text.toString()

        if(rating == 0) {
            throw Exception()
        }

        val eventFeedback = EventFeedbackRecord().apply {
            this.eventId = this@EventFeedbackFragment.eventId
            this.message = comments
            this.rating = rating
        }

        apiService.feedbacks.apiEventFeedbackPost(eventFeedback)
    } successUi {
        ui.done()
    } failUi {
        if(ui.ratingBar.rating == 0f ) {
                longToast("You have to select a rating!")
        } else {
            longToast("Failed to send feedback! Try again.")
        }
        ui.dataInput()
    }
}

class EventFeedbackUi : AnkoComponent<Fragment> {
    lateinit var titleView: TextView
    lateinit var dateView: TextView
    lateinit var locationView: TextView
    lateinit var hostView: TextView

    lateinit var submitButton: Button
    lateinit var textInput: EditText
    lateinit var ratingBar: RatingBar
    lateinit var dataInputLayout: LinearLayout
    lateinit var loadingLayout: LinearLayout
    lateinit var doneLayout: LinearLayout

    fun dataInput() {
        dataInputLayout.visibility = View.VISIBLE
        loadingLayout.visibility = View.GONE
        doneLayout.visibility = View.GONE
    }

    fun loading() {
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
                // Event Info
                linearLayout {
                    weightSum = 10F
                    padding = dip(LayoutConstants.MARGIN_LARGE)

                    verticalLayout {
                        fontAwesomeView {
                            verticalPadding = dip(LayoutConstants.MARGIN_LARGE)
                            text = "{fa-comments 30sp}"
                            textSize = LayoutConstants.TEXT_ICON_SIZE
                            textColorResource = R.color.primaryDark
                            gravity = Gravity.CENTER
                        }
                    }.weight(2F)

                    verticalLayout {
                        titleView = textView() {
                            verticalPadding = dip(LayoutConstants.MARGIN_SMALL)
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
                    backgroundResource = R.color.lightBackground

                    dataInputLayout = verticalLayout {
                        textView(R.string.event_feedback_rate_title) {
                            textColorResource = R.color.textBlack
                            textAppearance = android.R.style.TextAppearance_DeviceDefault_Medium
                        }

                        ratingBar = ratingBar {
                            verticalPadding = dip(LayoutConstants.MARGIN_LARGE)
                            numStars = 5
                            stepSize = 1F
                        }.lparams(wrapContent, wrapContent)

                        textView(R.string.event_feedback_comment_header) {
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

