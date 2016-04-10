package org.eurofurence.connavigator.ui

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import io.swagger.client.model.EventEntry
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.database.Database

/**
 * Created by David on 4/9/2016.
 */
class EventActivity : BaseActivity() {
    lateinit var id: String

    lateinit var eventTitleView: TextView
    lateinit var eventDescriptionView: TextView
    lateinit var eventImageView: ImageView
    lateinit var eventOrganizers: TextView
    lateinit var eventTime: TextView
    lateinit var eventRoom: TextView
    lateinit var eventDay: TextView

    lateinit var event: EventEntry

    override fun onCreate(savedBundleInstance: Bundle?) {
        super.onCreate(savedBundleInstance);

        id = intent.getStringExtra("uid")

        setContentView(R.layout.activity_event_base);

        injectNavigation(savedBundleInstance);

        retrieveEntry()

        if (event == null) {
            // If there's no event, we finish the activity and go back
            finish()
        }
        injectViews()

        fillActivity()
    }

    private fun retrieveEntry() {
        event = Database(this).eventEntryDb.elements.filter { it.id.toString().contentEquals(id) }.first()
    }

    private fun fillActivity() {
        eventTitleView.text = event.title
        eventDescriptionView.text = event.description
        eventTime.text = eventTime.text.toString().format(event.startTime, event.endTime)
        eventOrganizers.text = eventOrganizers.text.toString().format(event.panelHosts)
        eventDay.text = eventDay.text.toString().format(event.conferenceDayId)
        eventRoom.text = eventRoom.text.toString().format(event.conferenceRoomId)
    }

    private fun injectViews() {
        eventTitleView = findViewById(R.id.eventTitle) as TextView
        eventDescriptionView = findViewById(R.id.eventDescription) as TextView
        eventImageView = findViewById(R.id.eventImage) as ImageView
        eventOrganizers = findViewById(R.id.eventOrganizers) as TextView
        eventTime = findViewById(R.id.eventTime) as TextView
        eventRoom = findViewById(R.id.eventRoom) as TextView
        eventDay = findViewById(R.id.eventDay) as TextView
    }
}