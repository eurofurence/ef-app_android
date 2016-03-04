package org.eurofurence.connavigator

import android.support.v4.app.Fragment
import android.app.FragmentTransaction
import android.net.Uri
import android.os.Bundle
import android.os.StrictMode
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.*
import android.view.*
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import com.google.inject.Inject
import com.google.inject.Provider
import io.swagger.annotations.Api
import io.swagger.client.JsonUtil
import io.swagger.client.api.DefaultApi
import io.swagger.client.model.EventEntry
import org.joda.time.DateTime
import org.joda.time.Days
import roboguice.RoboGuice
import roboguice.activity.RoboActionBarActivity
import roboguice.inject.ContentView
import roboguice.inject.InjectView
import roboguice.util.RoboContext
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ForkJoinPool

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

        setContentView(R.layout.activity_launch_screen)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        val fab = findViewById(R.id.fab) as FloatingActionButton
        fab.setOnClickListener { view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show() }

        val drawer = findViewById(R.id.drawer_layout) as DrawerLayout
        val toggle = ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.setDrawerListener(toggle)
        toggle.syncState()

        val navigationView = findViewById(R.id.nav_view) as NavigationView
        navigationView.setNavigationItemSelectedListener(this)

        val header = navigationView.getHeaderView(0)
        navDays = header.findViewById(R.id.navDays)as TextView
        navTitle = header.findViewById(R.id.navTitle)as TextView
        navSubtitle = header.findViewById(R.id.navSubtitle)as TextView

        eventsView.setHasFixedSize(true)
        eventsView.layoutManager = LinearLayoutManager(this)
        eventsView.itemAnimator = DefaultItemAnimator()


        class EventViewHolder(viewItem: View,
                              val event: CardView = viewItem.findViewById(R.id.eventCard) as CardView,
                              val title: TextView = viewItem.findViewById(R.id.eventTitle) as TextView,
                              val date: TextView = viewItem.findViewById(R.id.eventDate) as TextView,
                              val hosts: TextView = viewItem.findViewById(R.id.eventHosts) as TextView,
                              val description: TextView = viewItem.findViewById(R.id.eventDescription) as TextView)
        : RecyclerView.ViewHolder(viewItem) {
            // No body
        }

        StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder().permitAll().build())
        Thread {
            val api: DefaultApi = DefaultApi();
            val df = SimpleDateFormat("yyyy-MM-dd")
//            val firstDay = api.eventConferenceDayGet(null)
//                    .map { df.parse(it.date) }
//                    .min()

            // Imma dummy, this is now the real date
            val firstDay = DateTime(2016, 8, 17, 0, 0)

            val days = Days.daysBetween(DateTime.now(), DateTime(firstDay)).days

            // api.apiClient.dateFormat = ISO8601DateFormat();
            val events = api.eventEntryGet(null)

            runOnUiThread {
                if (days <= 0)
                    navDays.text = "Day ${1 - days}"
                else
                    navDays.text = "Only $days days left"

                eventsView.adapter = object : RecyclerView.Adapter<EventViewHolder>() {

                    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): EventViewHolder {
                        return EventViewHolder(LayoutInflater
                                .from(parent.context)
                                .inflate(R.layout.fragment_main_event, parent, false))
                    }

                    override fun getItemCount(): Int {
                        return events.size
                    }

                    override fun onBindViewHolder(holder: EventViewHolder, pos: Int) {
                        val event = events[pos]
                        holder.title.text = event.title
                        holder.date.text = event.startTime
                        holder.hosts.text = event.panelHosts
                        holder.description.text = event.description
                    }
                }
            }

        }.start()
    }


    override fun onBackPressed() {
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
