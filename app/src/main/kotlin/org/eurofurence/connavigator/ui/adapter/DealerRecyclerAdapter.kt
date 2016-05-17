package org.eurofurence.connavigator.ui.adapter

import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import io.swagger.client.model.Dealer
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.database.Database
import org.eurofurence.connavigator.net.imageService
import org.eurofurence.connavigator.util.Formatter
import org.eurofurence.connavigator.util.delegators.view
import org.eurofurence.connavigator.util.extensions.applyOnRoot
import org.eurofurence.connavigator.util.extensions.get

/**
 * Created by David on 15-5-2016.
 */
class DealerDataHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
    val dealerName by view(TextView::class.java)
    val dealerPreviewImage by view(ImageView::class.java)
    val layout by view(RelativeLayout::class.java)
}

class DealerRecyclerAdapter(val effective_events: List<Dealer>, val database: Database, val fragment: Fragment) : RecyclerView.Adapter<DealerDataHolder>() {
    override fun getItemCount(): Int {
        return effective_events.count()
    }

    override fun onBindViewHolder(holder: DealerDataHolder, position: Int) {
        val dealer = effective_events[position]

        holder.dealerName.text = Formatter.dealerName(dealer)
        imageService.load(database.imageDb[dealer.artistThumbnailImageId], holder.dealerPreviewImage, true)

        holder.layout.setOnClickListener {
            fragment.applyOnRoot { navigateToDealer(dealer) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DealerDataHolder =
            DealerDataHolder(LayoutInflater
                    .from(parent.context)
                    .inflate(R.layout.fragment_dealer, parent, false)
            )

}