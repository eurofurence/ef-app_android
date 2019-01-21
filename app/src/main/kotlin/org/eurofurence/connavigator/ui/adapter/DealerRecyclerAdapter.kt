package org.eurofurence.connavigator.ui.adapter

import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.joanzapata.iconify.widget.IconTextView
import io.swagger.client.model.DealerRecord
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.database.Db
import org.eurofurence.connavigator.database.HasDb
import org.eurofurence.connavigator.net.imageService
import org.eurofurence.connavigator.ui.dialogs.DealerDialog
import org.eurofurence.connavigator.ui.fragments.DealerListFragmentDirections
import org.eurofurence.connavigator.util.delegators.view
import org.eurofurence.connavigator.util.extensions.*
import org.eurofurence.connavigator.util.v2.compatAppearance
import org.eurofurence.connavigator.util.v2.get
import org.jetbrains.anko.*

/**
 * Created by David on 15-5-2016.
 */
class DealerDataHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
    val dealerName: TextView by view()
    val dealerSubText: TextView by view()
    val dealerPreviewImage: ImageView by view()
    val danger: IconTextView by view()
    val moon: IconTextView by view()
    val layout: LinearLayout by view()
}

class DealerRecyclerAdapter(private val effective_events: List<DealerRecord>, override val db: Db, val fragment: Fragment) :
        RecyclerView.Adapter<DealerDataHolder>(), HasDb {
    override fun getItemCount(): Int {
        return effective_events.count()
    }

    override fun onBindViewHolder(holder: DealerDataHolder, position: Int) {
        val dealer = effective_events[position]

        holder.dealerName.text = dealer.getName()
        holder.dealerSubText.text = dealer.attendeeNickname

        holder.dealerSubText.visibility = if (dealer.hasUniqueDisplayName()) {
            View.VISIBLE
        } else {
            View.GONE
        }

        // If no dealer preview was provided, load the YCH icon
        if (dealer[toThumbnail] != null) {
            imageService.load(dealer[toThumbnail], holder.dealerPreviewImage, false)
        } else {
            fragment.context?.let {
                holder.dealerPreviewImage.setImageDrawable(
                        ContextCompat.getDrawable(it, R.drawable.dealer_black))
            }
        }

        holder.moon.visibility = if (dealer.isAfterDark == true) {
            View.VISIBLE
        } else {
            View.GONE
        }

        holder.danger.visibility = if (dealer.allDaysAvailable()) {
            View.GONE
        } else {
            View.VISIBLE
        }

        holder.layout.setOnClickListener {
            val action = DealerListFragmentDirections
                    .actionFragmentViewDealersToFragmentViewDealer(dealer.id.toString())
            fragment.findNavController().navigate(action)
        }

        holder.layout.setOnLongClickListener {
            DealerDialog().withArguments(dealer).show(fragment.childFragmentManager, "Dealer menu")
            true
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DealerDataHolder =
            DealerDataHolder(DealerListItemUI().createView(
                    AnkoContext.create(parent.context.applicationContext, parent)))
}

class DealerListItemUI : AnkoComponent<ViewGroup> {
    override fun createView(ui: AnkoContext<ViewGroup>) = with(ui) {

        linearLayout {
            lparams(matchParent, wrapContent)
            backgroundResource = R.color.cardview_light_background
            id = R.id.layout
            weightSum = 100F
            padding = dip(10)

            verticalLayout {
                imageView {
                    padding = dip(5)
                    scaleType = ImageView.ScaleType.FIT_XY
                    id = R.id.dealerPreviewImage
                }.lparams(dip(75), dip(75)) { gravity = Gravity.LEFT }
            }.lparams(dip(0), wrapContent, 20F) { gravity = Gravity.CENTER_VERTICAL }

            verticalLayout {
                textView {
                    compatAppearance = android.R.style.TextAppearance_Large
                    id = R.id.dealerName
                }
                textView {
                    compatAppearance = android.R.style.TextAppearance_DeviceDefault_Small
                    id = R.id.dealerSubText
                }
            }.lparams(dip(0), wrapContent, 70F) { gravity = Gravity.CENTER_VERTICAL }

            verticalLayout {
                fontAwesomeView {
                    text = "{fa-exclamation-triangle 24sp}"
                    id = R.id.danger
                    gravity = Gravity.CENTER
                    setPadding(0, 0, 0, dip(5))
                }
                fontAwesomeView {
                    text = "{fa-moon-o 24sp}"
                    id = R.id.moon
                    gravity = Gravity.CENTER
                }

            }.lparams(dip(0), wrapContent, 10F) { gravity = Gravity.CENTER_VERTICAL }
        }
    }
}