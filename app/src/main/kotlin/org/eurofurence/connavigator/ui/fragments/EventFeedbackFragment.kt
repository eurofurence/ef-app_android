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
import com.pawegio.kandroid.longToast
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
import org.eurofurence.connavigator.dropins.*
import org.eurofurence.connavigator.ui.LayoutConstants
import org.eurofurence.connavigator.ui.filters.start
import org.eurofurence.connavigator.util.extensions.*
import org.eurofurence.connavigator.services.apiService

import java.util.*

class EventFeedbackFragment : Fragment(), HasDb {

    private val args: EventFeedbackFragmentArgs by navArgs()
    val eventId by lazy { UUID.fromString(args.eventId) }
    val event: EventRecord? by lazy { db.events[eventId] }
    val conferenceRoom by lazy { db.rooms[event?.conferenceRoomId] }

    override val db by lazyLocateDb()

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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = createView {
        scrollView {
            backgroundResource = R.color.backgroundGrey

            verticalLayout {
                // Event Info
                linearLayout {
                    layoutParams = linearLayoutParams(matchParent, wrapContent)
                    weightSum = 10F
                    padding = dip(LayoutConstants.MARGIN_LARGE)

                    verticalLayout {
                        layoutParams = linearLayoutParams(0, wrapContent, 2f)

                        fontAwesomeView {
                            setPadding(
                                0,
                                dip(LayoutConstants.MARGIN_LARGE),
                                0,
                                dip(LayoutConstants.MARGIN_LARGE)
                            )
                            text = getString(R.string.fa_comment)
                            textSize = LayoutConstants.TEXT_ICON_SIZE
                            textColorResource = R.color.primaryDark
                            gravity = Gravity.CENTER
                        }
                    }

                    verticalLayout {
                        layoutParams = linearLayoutParams(0, wrapContent, 8f)

                        titleView = textView() {
                            setPadding(
                                0,
                                dip(LayoutConstants.MARGIN_SMALL),
                                0,
                                dip(LayoutConstants.MARGIN_SMALL)
                            )
                            compatAppearance = android.R.style.TextAppearance_DeviceDefault_Large
                        }

                        dateView = textView {}
                        locationView = textView {}
                        hostView = textView {}
                    }
                }
                // End Event info

                // Event Feedback
                verticalLayout {
                    padding = dip(LayoutConstants.MARGIN_LARGE)
                    backgroundResource = R.color.lightBackground

                    dataInputLayout = verticalLayout {
                        textView(R.string.event_feedback_rate_title) {
                            textColorResource = R.color.textBlack
                            compatAppearance = android.R.style.TextAppearance_DeviceDefault_Medium
                        }

                        ratingBar = ratingBar {
                            layoutParams = linearLayoutParams(wrapContent, wrapContent)
                            setPadding(
                                0,
                                dip(LayoutConstants.MARGIN_LARGE),
                                0,
                                dip(LayoutConstants.MARGIN_LARGE)
                            )
                            numStars = 5
                            stepSize = 1F
                        }

                        textView(R.string.event_feedback_comment_header) {
                            textColorResource = R.color.textBlack
                            compatAppearance = android.R.style.TextAppearance_DeviceDefault_Medium
                        }

                        textInput = editText {
                            hint = context.getString(R.string.event_feedback_comment_hint)
                        }

                        textView(R.string.event_feedback_anonymous_help) {
                            setPadding(
                                0,
                                dip(LayoutConstants.MARGIN_LARGE),
                                0,
                                dip(LayoutConstants.MARGIN_LARGE)
                            )
                            compatAppearance = android.R.style.TextAppearance_DeviceDefault_Small
                        }

                        linearLayout {
                            layoutParams = linearLayoutParams(matchParent, wrapContent) {
                                topMargin = dip(LayoutConstants.MARGIN_LARGE)
                            }
                            weightSum = 10F

                            view {
                                layoutParams = linearLayoutParams(dip(0), wrapContent, 6F)
                            }

                            submitButton = button(R.string.event_feedback_submit) {
                                layoutParams = linearLayoutParams(dip(0), wrapContent, 4F)
                                backgroundResource = (R.color.primaryLight)
                                textColorResource = R.color.textWhite
                            }
                        }
                    }

                    loadingLayout = verticalLayout {
                        visibility = View.GONE
                        progressBar {}
                    }

                    doneLayout = verticalLayout {
                        visibility = View.GONE
                        textView(R.string.event_feedback_thank) {
                            textColorResource = R.color.textBlack
                            compatAppearance = android.R.style.TextAppearance_DeviceDefault_Medium
                        }
                    }
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        event?.let {
            if (!it.isAcceptingFeedback && !BuildConfig.DEBUG)
                findNavController().popBackStack()

            titleView.text = it.fullTitle()
            dateView.text = getString(
                R.string.event_weekday_from_to,
                it.start.dayOfWeek().asText,
                it.startTimeString(),
                it.endTimeString()
            )
            hostView.text = it.ownerString()
            locationView.text = getString(
                R.string.misc_room_number, conferenceRoom?.name
                    ?: getString(R.string.event_unable_to_locate_room)
            )

            submitButton.setOnClickListener { submit() }
        }
    }

    private fun submit() = promiseOnUi {
        loading()
    } then {
        val rating = ratingBar.rating.toInt()
        val comments = textInput.text.toString()

        if (rating == 0) {
            throw Exception()
        }

        val eventFeedback = EventFeedbackRecord().apply {
            this.eventId = this@EventFeedbackFragment.eventId
            this.message = comments
            this.rating = rating
        }

        apiService.feedbacks.apiEventFeedbackPost(eventFeedback)
    } successUi {
        done()
    } failUi {
        if (ratingBar.rating == 0f) {
            longToast("You have to select a rating!")
        } else {
            longToast("Failed to send feedback! Try again.")
        }
        dataInput()
    }
}
