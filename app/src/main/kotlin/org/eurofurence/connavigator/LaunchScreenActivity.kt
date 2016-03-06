package org.eurofurence.connavigator

import android.net.Uri
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.*
import android.view.*
import android.widget.TextView
import io.swagger.client.model.EventEntry
import org.eurofurence.connavigator.util.assert
import org.joda.time.DateTime
import org.joda.time.Days
import roboguice.activity.RoboActionBarActivity
import roboguice.inject.InjectView
import java.io.File

class LaunchScreenActivity : RoboActionBarActivity(), NavigationView.OnNavigationItemSelectedListener, MainEventFragment.OnFragmentInteractionListener {
    override fun onFragmentInteraction(uri: Uri?) {
        println(uri)
    }

    private lateinit var navDays: TextView
    private lateinit var navTitle: TextView
    private lateinit var navSubtitle: TextView

    @InjectView(R.id.eventRecycler)
    private lateinit var eventsView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Assign the main layout
        setContentView(R.layout.activity_launch_screen)

        // Find and configure the toolbar
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)


        // Find and populate the main layout manager
        val drawer = findViewById(R.id.drawer_layout) as DrawerLayout
        val toggle = ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.setDrawerListener(toggle)
        toggle.syncState()

        // Find navigation drawer and add this activity as a listener
        val navigationView = findViewById(R.id.nav_view) as NavigationView
        navigationView.setNavigationItemSelectedListener(this)

        // Resolve the header in the navigation drawer, bind the views
        val header = navigationView.getHeaderView(0)
        navDays = header.findViewById(R.id.navDays)as TextView
        navTitle = header.findViewById(R.id.navTitle)as TextView
        navSubtitle = header.findViewById(R.id.navSubtitle)as TextView

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

        // Setup the database
        val eventsDbFile = File(cacheDir, "eventsdb.db")
        val eventsDb = JsonStreamDB(EventEntry::class.java, { it.id.toString() }, eventsDbFile)

        // Query and memorize the events
        val events = eventsDb.getAll().values.toMutableList()

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

            // Query events, feed to scoped receiver
            queryEventEntry("fromfab")
        }

        // Create a scoped receiver
        createEventEntryReceiver("fromfab") {
            // Reset the events
            events.clear()
            events.addAll(it)

            // Write all to the database
            eventsDb.replaceAll(events)

            // Notify the recycler that its content has changed
            eventsView.adapter.notifyDataSetChanged()
            Snackbar.make(findViewById(R.id.fab), "Database reload complete", Snackbar.LENGTH_SHORT).show()

        }.register() assert true
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
