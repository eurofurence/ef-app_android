package org.eurofurence.connavigator.ui.adapter

import androidx.fragment.app.Fragment
import androidx.core.content.ContextCompat
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import io.swagger.client.model.DealerRecord
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.database.Db
import org.eurofurence.connavigator.database.HasDb
import org.eurofurence.connavigator.dropins.*
import org.eurofurence.connavigator.dropins.fa.Fa
import org.eurofurence.connavigator.services.ImageService
import org.eurofurence.connavigator.ui.dialogs.DealerDialog
import org.eurofurence.connavigator.ui.fragments.DealerListFragmentDirections
import org.eurofurence.connavigator.util.delegators.view
import org.eurofurence.connavigator.util.extensions.*
import org.eurofurence.connavigator.util.v2.get


/**
 * Created by David on 15-5-2016.
 */
class DealerDataHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val dealerName: TextView by view()
    val dealerSubText: TextView by view()
    val dealerPreviewImage: ImageView by view()
    val danger: TextView by view() // TODO: Icon text view.
    val moon: TextView by view() // TODO: Icon text view.
    val layout: LinearLayout by view()
}

class DealerRecyclerAdapter(
    private val effective_events: List<DealerRecord>,
    override val db: Db,
    val fragment: Fragment
) :
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
            ImageService.load(dealer[toThumbnail], holder.dealerPreviewImage, false)
        } else {
            fragment.context?.let {
                holder.dealerPreviewImage.setImageDrawable(
                    ContextCompat.getDrawable(it, R.drawable.dealer_frame)
                )

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
                .actionFragmentViewDealersToFragmentViewDealer(dealer.id.toString(), null)
            fragment.findNavController().navigate(action)
        }

        holder.layout.setOnLongClickListener {
            DealerDialog().withArguments(dealer).show(fragment.childFragmentManager, "Dealer menu")
            true
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DealerDataHolder =
        DealerDataHolder(parent.context.createView {
            linearLayout {
                layoutParams = viewGroupLayoutParams(matchParent, wrapContent)

                backgroundResource = R.color.lightBackground
                id = R.id.layout
                weightSum = 100F
                setPadding(dip(10), 0, dip(10), 0)

                verticalLayout {
                    layoutParams = linearLayoutParams(0, wrapContent, 20f) {
                        gravity = Gravity.CENTER_VERTICAL
                    }
                    imageView {
                        layoutParams = linearLayoutParams(dip(75), dip(75)) {
                            gravity = Gravity.LEFT
                        }
                        padding = dip(5)
                        scaleType = ImageView.ScaleType.FIT_XY
                        id = R.id.dealerPreviewImage
                    }
                }

                verticalLayout {
                    layoutParams = linearLayoutParams(0, wrapContent, 70f) {
                        gravity = Gravity.CENTER_VERTICAL
                    }
                    textView {
                        compatAppearance = android.R.style.TextAppearance_Large
                        id = R.id.dealerName
                    }
                    textView {
                        compatAppearance = android.R.style.TextAppearance_DeviceDefault_Small
                        id = R.id.dealerSubText
                    }
                }

                verticalLayout {
                    layoutParams = linearLayoutParams(0, wrapContent, 10f) {
                        gravity = Gravity.CENTER_VERTICAL
                    }
                    fontAwesomeView {
                        text = Fa.fa_exclamation_triangle
                        textSize = 24f
                        id = R.id.danger
                        gravity = Gravity.CENTER
                        setPadding(0, 0, 0, dip(5))
                    }
                    fontAwesomeView {
                        text = Fa.fa_moon_o
                        textSize = 24f
                        id = R.id.moon
                        gravity = Gravity.CENTER
                    }

                }
            }
        })
}
