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
import com.joanzapata.iconify.widget.IconTextView
import io.swagger.client.model.DealerRecord
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.database.Db
import org.eurofurence.connavigator.database.HasDb
import org.eurofurence.connavigator.net.imageService
import org.eurofurence.connavigator.ui.dialogs.DealerDialog
import org.eurofurence.connavigator.util.delegators.view
import org.eurofurence.connavigator.util.extensions.allDaysAvailable
import org.eurofurence.connavigator.util.extensions.applyOnRoot
import org.eurofurence.connavigator.util.extensions.fontAwesomeView
import org.eurofurence.connavigator.util.extensions.getName
import org.eurofurence.connavigator.util.v2.compatAppearance
import org.eurofurence.connavigator.util.v2.get
import org.jetbrains.anko.AnkoComponent
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.backgroundResource
import org.jetbrains.anko.dip
import org.jetbrains.anko.imageView
import org.jetbrains.anko.linearLayout
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.padding
import org.jetbrains.anko.textView
import org.jetbrains.anko.verticalLayout
import org.jetbrains.anko.wrapContent

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

class DealerRecyclerAdapter(val effective_events: List<DealerRecord>, override val db: Db, val fragment: Fragment) :
        RecyclerView.Adapter<DealerDataHolder>(), HasDb {
    override fun getItemCount(): Int {
        return effective_events.count()
    }

    override fun onBindViewHolder(holder: DealerDataHolder, position: Int) {
        val dealer = effective_events[position]

        holder.dealerName.text = dealer.getName()
        holder.dealerSubText.text = dealer.displayName

        holder.dealerSubText.visibility = if (dealer.displayName.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }

        // If no dealer preview was provided, load the YCH icon
        if (dealer[toThumbnail] != null) {
            imageService.load(dealer[toThumbnail], holder.dealerPreviewImage, false)
        } else {
            holder.dealerPreviewImage.setImageDrawable(
                    ContextCompat.getDrawable(fragment.context, R.drawable.dealer_black))
        }

        holder.moon.visibility = if (dealer.isAfterDark) {
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
            fragment.applyOnRoot { navigateToDealer(dealer) }
        }

        holder.layout.setOnLongClickListener {
            DealerDialog(dealer).show(fragment.childFragmentManager, "Dealer menu")
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

            imageView {
                padding = dip(5)
                scaleType = ImageView.ScaleType.FIT_CENTER
                id = R.id.dealerPreviewImage
            }.lparams(dip(0), wrapContent, 20F)

            verticalLayout {
                textView {
                    compatAppearance = android.R.style.TextAppearance_Large
                    id = R.id.dealerName
                }
                textView {
                    compatAppearance = android.R.style.TextAppearance_DeviceDefault_Small
                    id = R.id.dealerSubText
                }
                gravity = Gravity.CENTER_VERTICAL
            }.lparams(dip(0), matchParent, 70F)

            verticalLayout {
                fontAwesomeView {
                    text = "{fa-exclamation-triangle 25sp}"
                    id = R.id.danger
                }
                fontAwesomeView {
                    text = "{fa-moon-o 20sp}"
                    id = R.id.moon
                }
                gravity = Gravity.CENTER
            }.lparams(dip(0), matchParent, 10F)
        }
    }
}