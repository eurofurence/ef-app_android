package org.eurofurence.connavigator.ui

import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import io.swagger.client.model.Info
import io.swagger.client.model.InfoGroup
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.database.Database
import org.eurofurence.connavigator.net.imageService
import org.eurofurence.connavigator.util.Choice
import org.eurofurence.connavigator.util.delegators.view
import org.eurofurence.connavigator.util.delegators.viewInHolder

class InfoListActivity : BaseActivity() {

    val infoRecycler  by view(RecyclerView::class.java)

    fun <T : Any, U : Any> weave(parents: List<T>, children: Map<T, List<U>>): List<Choice<T, U>> =
            parents.flatMap {
                val head = listOf(Choice.pri<T, U>(it))
                val tail = (children[it] ?: emptyList()).map { Choice.snd<T, U>(it) }
                head + tail
            }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_list_base)
        injectNavigation(savedInstanceState)

        val database = Database(this)

        // Default setup for recycler layout and animation
        infoRecycler.layoutManager = LinearLayoutManager(this)
        infoRecycler.itemAnimator = DefaultItemAnimator()

        // Event view holder finds and memorizes the views in an event card
        class InfoFragmentHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

            val infoFragmentTitle by viewInHolder(TextView::class.java)

            lateinit var info: Info

            init {
                itemView.setOnClickListener(this)
                infoFragmentTitle.setOnClickListener(this)
            }

            override fun onClick(v: View?) {
                startActivity(InfoActivity.makeIntent(this@InfoListActivity, info))
            }
        }

        // Event view holder finds and memorizes the views in an event card
        class InfoGroupFragmentHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val infoGroupFragmentImage by viewInHolder(ImageView::class.java)
            val infoGroupFragmentTitle by viewInHolder(TextView::class.java)
            val infoGroupFragmentDescription by viewInHolder(TextView::class.java)
            lateinit var infoGroup: InfoGroup

            //            init {
            //                itemView.setOnClickListener { onClick(it) }
            //                infoGroupFragmentTitle.setOnClickListener { onClick(it) }
            //            }
            //
            //            override fun onClick(v: View?) {
            //                startActivity(InfoActivity.makeIntent(this@InfoListActivity, info))
            //            }
        }

        val groups = database.infoGroupDb.elements
                .sortedBy { it.position }
        val elements = database.infoDb.elements
                .groupBy { database.infoGroupDb.keyValues[it.infoGroupId]!! }
                .mapValues { it.value.sortedBy { it.position } }

        val weaved = weave(groups, elements)

        // Assign a new adapter mapping to the previously defined view event holders
        infoRecycler.adapter = object : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            val ITEM = 0
            val HEADER = 1
            override fun getItemViewType(pos: Int) =
                    if (weaved[pos].isPrimary)
                        HEADER
                    else
                        ITEM

            override fun onCreateViewHolder(parent: ViewGroup, type: Int): RecyclerView.ViewHolder =
                    when (type) {
                        ITEM -> InfoFragmentHolder(LayoutInflater
                                .from(parent.context)
                                .inflate(R.layout.fragment_info_item, parent, false))
                        HEADER -> InfoGroupFragmentHolder(LayoutInflater.
                                from(parent.context)
                                .inflate(R.layout.fragment_info_group, parent, false))
                        else -> throw IllegalStateException()

                    }


            override fun getItemCount(): Int {
                // Fixed size, map to the events
                return weaved.size
            }

            override fun onBindViewHolder(holder: RecyclerView.ViewHolder, pos: Int) {
                weaved[pos].apply(
                        {
                            val holder = holder as InfoGroupFragmentHolder

                            // Display the image
                            val img = database.imageDb.keyValues[imageId]
                            if (img != null) {
                                imageService.load(img, holder.infoGroupFragmentImage)
                                holder.infoGroupFragmentImage.visibility = View.VISIBLE
                            } else
                                holder.infoGroupFragmentImage.visibility = View.GONE

                            holder.infoGroupFragmentTitle.text = name
                            holder.infoGroupFragmentDescription.text = description
                            holder.infoGroup = this
                        },
                        {
                            val holder = holder as InfoFragmentHolder
                            holder.infoFragmentTitle.text = title
                            holder.info = this
                        }
                )
            }
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
}
