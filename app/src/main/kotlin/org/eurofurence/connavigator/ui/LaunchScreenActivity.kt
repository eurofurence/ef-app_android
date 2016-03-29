package org.eurofurence.connavigator

import android.os.Bundle
import android.view.*
import android.widget.TextView
import io.swagger.client.model.EventEntry
import org.eurofurence.connavigator.db.DBCallback
import org.joda.time.DateTime
import org.joda.time.Days

class LaunchScreenActivity : BaseActity() {


    @InjectView(R.id.eventRecycler)
    private lateinit var eventsView: RecyclerView

    private var events: List<EventEntry> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Assign the main layout
        setContentView(R.layout.activity_launch_screen)

        // Inject menu navigation
        injectNavigation()

        // Turn this off if there are views of different sizes in the recycler
        eventsView.setHasFixedSize(true)

        // Default setup for recycler layout and animation
        eventsView.layoutManager = LinearLayoutManager(this)
        eventsView.itemAnimator = DefaultItemAnimator()


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
        dbservice.initialize()

        // Query and memorize the events
        events = dbservice.eventEntryDb.elements.toList()

        // Assign a new adapter mapping to the previously defined view event holders
        eventsView.adapter = object : RecyclerView.Adapter<EventViewHolder>() {
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
            dbservice.update (object : DBCallback {
                override fun gotEvents(values: List<EventEntry>) {
                    // Notify the recycler that its content has changed
                    events = values
                    eventsView.adapter.notifyDataSetChanged()
                }

                override fun done() {
                    val cts = dbservice.dateDb.elements.firstOrNull()
                    Snackbar.make(findViewById(R.id.fab), "Database reload complete, version $cts", Snackbar.LENGTH_SHORT).show()
                }
            })
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
            for (x in dbservice.eventConferenceDayDb.elements)
                println(x)
            for (x in dbservice.eventConferenceRoomDb.elements)
                println(x)
            for (x in dbservice.eventConferenceTrackDb.elements)
                println(x)
            for (x in dbservice.eventEntryDb.elements)
                println(x)
            for (x in dbservice.imageDb.elements)
                println(x)
            for (x in dbservice.infoDb.elements)
                println(x)
            for (x in dbservice.infoGroupDb.elements)
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
