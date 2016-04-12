package org.eurofurence.connavigator.ui

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.*
import io.swagger.client.model.EventEntry
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.database.Database
import org.eurofurence.connavigator.database.UpdateIntentService
import org.eurofurence.connavigator.net.imageService
import org.eurofurence.connavigator.util.delegators.viewInHolder
import org.eurofurence.connavigator.util.extensions.booleans
import org.eurofurence.connavigator.util.extensions.localReceiver
import org.eurofurence.connavigator.util.extensions.logd
import org.eurofurence.connavigator.util.extensions.objects
import org.joda.time.DateTime
import org.joda.time.Days
import java.util.*

class LaunchScreenActivity : BaseActivity() {
    /**
     * Listens to update responses, since the event recycler holds database related data
     */
    val updateReceiver = localReceiver(UpdateIntentService.UPDATE_COMPLETE) {
        // Get intent extras
        val success = it.booleans["success"]
        val time = it.objects["time", Date::class.java]

        // Update the views
        eventRecycler.adapter.notifyDataSetChanged()

        // Make a snackbar for the result
        Snackbar.make(findViewById(R.id.fab), "Database reload ${if (success) "successful" else "failed"}, version $time", Snackbar.LENGTH_LONG).show()
    }

    lateinit var swipeToRefresh: SwipeRefreshLayout
    lateinit var roomSelector: Spinner
    lateinit var daySelector: Spinner

    var events: List<EventEntry> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        logd { "Launch screen creating" }

        val database = Database(this)

        // Assign the main layout
        setContentView(R.layout.activity_launch_screen_base)

        // Inject menu navigation
        injectNavigation(savedInstanceState)

        // Inject current views
        injectViews()

        // Fill views
        fillViews()

        // Turn this off if there are views of different sizes in the recycler
        eventRecycler.setHasFixedSize(true)

        // Default setup for recycler layout and animation
        eventRecycler.layoutManager = LinearLayoutManager(this)
        eventRecycler.itemAnimator = DefaultItemAnimator()

        // Event view holder finds and memorizes the views in an event card
        class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

            val eventImage by viewInHolder(ImageView::class.java)
            val eventTitle by viewInHolder(TextView::class.java)
            val eventDate by viewInHolder(TextView::class.java)
            val eventHosts by viewInHolder(TextView::class.java)
            val eventDescription by viewInHolder(TextView::class.java)
            lateinit var eventEntry: EventEntry

            init {
                eventTitle.setOnClickListener { onClick(it) }
                eventImage.setOnClickListener { onClick(it) }

            }

            override fun onClick(v: View?) {
                onEventViewPress(eventEntry)
            }
        }
        events = database.eventEntryDb.elements

        // Assign a new adapter mapping to the previously defined view event holders
        eventRecycler.adapter = object : RecyclerView.Adapter<EventViewHolder>() {
            override fun onCreateViewHolder(parent: ViewGroup, p1: Int): EventViewHolder {
                // To create the view holder, inflate the main event. This can be replaced, but differently
                // sized fragments will require the fixed size property of the recycler to be lifted
                return EventViewHolder(LayoutInflater
                        .from(parent.context)
                        .inflate(R.layout.fragment_main_event, parent, false))
            }

            override fun getItemCount(): Int {
                // Fixed size, map to the events
                return events.size
            }

            override fun onBindViewHolder(holder: EventViewHolder, pos: Int) {
                // Get the event for the position
                val event = events[pos]

                // Assign the properties of the view
                holder.eventTitle.text = event.title
                holder.eventDate.text = event.startTime
                holder.eventHosts.text = event.panelHosts
                holder.eventDescription.text = event.description
                holder.eventEntry = event

                // Assign an image if present
                if (event.imageId != null) {
                    val img = database.imageDb.elements.firstOrNull { it.id == event.imageId }
                    if (img != null)
                        imageService.load(img, holder.eventImage)

                    holder.eventImage.visibility = View.VISIBLE
                } else {
                    holder.eventImage.visibility = View.GONE
                }
            }
        }

        // Manually set the first date, since the database is not updated with EF 22
        val firstDay = DateTime(2016, 8, 17, 0, 0)

        // Calculate the days between, using the current time. Todo: timezones
        val days = Days.daysBetween(DateTime.now(), DateTime(firstDay)).days

        // On con vs. before con. This should be updated on day changes
        if (days <= 0)
            navDays.text = "Day ${1 - days}"
        else
            navDays.text = "Only $days days left"


        // Find and setup the floating button
        val fab = findViewById(R.id.fab) as FloatingActionButton
        fab.setOnClickListener { view ->
            // Notify the update to the user
            Snackbar.make(findViewById(R.id.fab), "Updating the database", Snackbar.LENGTH_LONG).show()

            // Start the update service
            UpdateIntentService.dispatchUpdate(this@LaunchScreenActivity)
        }

        logd { "Launch screen created" }
    }

    override fun onResume() {
        super.onResume()
        updateReceiver.register()
    }

    override fun onPause() {
        updateReceiver.unregister()
        super.onPause()
    }

    private fun fillViews() {

        val database = Database(this)

        // Create a click listener
        val listener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                filterEvents(view)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                filterEvents(null)
            }
        }

        // Set up the rooms in the spinner
        val rooms = ArrayList<String>()
        rooms.add("All")
        rooms.addAll(database.eventConferenceRoomDb.elements.map { it.name }.toTypedArray())
        val roomAdapter = ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, rooms)

        roomSelector.adapter = roomAdapter
        roomSelector.onItemSelectedListener = listener

        // Set up con days in the spinner
        val days = ArrayList<String>()
        days.add("All")
        days.addAll(database.eventConferenceDayDb.elements.map { it.date }.toTypedArray())

        val dayAdapter = ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, days)

        daySelector.adapter = dayAdapter

        daySelector.onItemSelectedListener = listener
    }

    private fun filterEvents(view: View?) {
        val database = Database(this)
        if (view == null) {
            events = database.eventEntryDb.elements
        } else {
            val roomSelected = roomSelector.selectedItem
            val daySelected = daySelector.selectedItem

            var event_set = database.eventEntryDb.elements

            if (roomSelected != "All") {
                val room = database.eventConferenceRoomDb.elements.filter { it.name == roomSelected }.first()
                event_set = event_set.filter { it.conferenceRoomId == room.id }
            }

            if (daySelected != "All") {
                val day = database.eventConferenceDayDb.elements.filter { it.date == daySelected }.first()
                event_set = event_set.filter { it.conferenceDayId == day.id }
            }

            events = event_set
        }

        swipeToRefresh.setOnRefreshListener {
            UpdateIntentService.dispatchUpdate(this@LaunchScreenActivity)
            events = database.eventEntryDb.elements;
            eventRecycler.adapter.notifyDataSetChanged();
            fillViews();
            swipeToRefresh.isRefreshing = false;
        }

        eventRecycler.adapter.notifyDataSetChanged()
    }

    private fun injectViews() {
        val database = Database(this)
        roomSelector = findViewById(R.id.roomSelector) as Spinner
        daySelector = findViewById(R.id.daySelector) as Spinner

        swipeToRefresh = findViewById(R.id.eventRefresh) as SwipeRefreshLayout
    }

    fun onEventViewPress(eventEntry: EventEntry) {
        val intent = Intent(this, EventActivity::class.java)
        intent.putExtra("uid", eventEntry.id.toString())

        startActivity(intent)
    }


    override fun onBackPressed() {
        // Sample method, maps a press on the back button to either 'close the drawer' or to the default behavior
        val drawer = findViewById(R.id.drawer_layout) as DrawerLayout
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.launch_screen, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }
}
