package org.eurofurence.connavigator.ui

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import io.swagger.client.model.EventEntry
import org.eurofurence.connavigator.R

/**
 * Created by David on 4/9/2016.
 */
class EventActivity : BaseActivity() {
    lateinit var id: String
    lateinit var eventTitleView: TextView
    lateinit var eventDescriptionView: TextView
    lateinit var eventImageView: ImageView
    lateinit var event: EventEntry

    override fun onCreate(savedBundleInstance: Bundle?) {
        super.onCreate(savedBundleInstance);

        id = intent.getStringExtra("uid")

        print(id)

        setContentView(R.layout.activity_event_base);

        retrieveEntry()

        injectNavigation(savedBundleInstance);

        injectViews()

        fillActivity()
    }

    private fun retrieveEntry() {

    }

    private fun fillActivity() {
        eventTitleView.text = id.toString()
        eventDescriptionView.text = id.toString()
    }

    private fun injectViews() {
        eventTitleView = findViewById(R.id.eventTitle) as TextView
        eventDescriptionView = findViewById(R.id.eventDescription) as TextView
        eventImageView = findViewById(R.id.eventImage) as ImageView
    }
}