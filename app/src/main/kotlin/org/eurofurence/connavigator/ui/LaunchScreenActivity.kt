package org.eurofurence.connavigator.ui

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.widget.CardView
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.TextView
import io.swagger.client.model.EventEntry
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.db.DBCallback
import org.joda.time.DateTime
import org.joda.time.Days

class LaunchScreenActivity : BaseActivity() {


    private var events: List<EventEntry> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Assign the main layout
        setContentView(R.layout.activity_launch_screen)

        // Inject menu navigation
        injectNavigation(savedInstanceState)

        // Turn this off if there are views of different sizes in the recycler
        eventRecycler.setHasFixedSize(true)

        // Default setup for recycler layout and animation
        eventRecycler.layoutManager = LinearLayoutManager(this)
        eventRecycler.itemAnimator = DefaultItemAnimator()


        // Event view holder finds and memorizes the views in an event card
        class EventViewHolder(viewItem: View,
                              val event: CardView = viewItem.findViewById(R.id.eventCard) as CardView,
                              val title: TextView = viewItem.findViewById(R.id.eventTitle) as TextView,
                              val date: TextView = viewItem.findViewById(R.id.eventDate) as TextView,
                              val hosts: TextView = viewItem.findViewById(R.id.eventHosts) as TextView,
                              val description: TextView = viewItem.findViewById(R.id.eventDescription) as TextView)
        : RecyclerView.ViewHolder(viewItem) {
            // No body
        }

        // Initialize the database service to listen in this context
        dbService.initialize()

        // Query and memorize the events
        events = dbService.eventEntryDb.elements.toList()

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
                holder.title.text = event.title
                holder.date.text = event.startTime
                holder.hosts.text = event.panelHosts
                holder.description.text = event.description
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
            Snackbar.make(view, "Reloading database", Snackbar.LENGTH_SHORT).show()

            // Update the database
            dbService.update (object : DBCallback {
                override fun gotEvents(delta: List<EventEntry>) {
                    // Notify the recycler that its content has changed
                    events = dbService.eventEntryDb.elements
                    eventRecycler.adapter.notifyDataSetChanged()
                }

                override fun done(success: Boolean) {
                    val cts = dbService.dateDb.elements.firstOrNull()
                    Snackbar.make(findViewById(R.id.fab), "Database reload ${if (success) "successful" else "failed"}, version $cts", Snackbar.LENGTH_SHORT).show()
                }
            } + DBCallback.OUTPUT)
        }
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

    @SuppressWarnings("StatementWithEmptyBody")
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        val id = item.itemId

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {
            for (x in dbService.eventConferenceDayDb.elements)
                println(x)
            for (x in dbService.eventConferenceRoomDb.elements)
                println(x)
            for (x in dbService.eventConferenceTrackDb.elements)
                println(x)
            for (x in dbService.eventEntryDb.elements)
                println(x)
            for (x in dbService.imageDb.elements)
                println(x)
            for (x in dbService.infoDb.elements)
                println(x)
            for (x in dbService.infoGroupDb.elements)
                println(x)

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        val drawer = findViewById(R.id.drawer_layout) as DrawerLayout
        drawer.closeDrawer(GravityCompat.START)
        return true
    }
}
