package org.eurofurence.connavigator.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import io.swagger.client.model.Dealer
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.util.delegators.view

/**
 * Created by David on 15-5-2016.
 */
class DealerDataHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
    val dealerName by view(TextView::class.java)
}

class DealerRecyclerAdapter(public var effective_events: List<Dealer>) : RecyclerView.Adapter<DealerDataHolder>() {
    override fun getItemCount(): Int {
        return effective_events.count()
    }

    override fun onBindViewHolder(holder: DealerDataHolder, position: Int) {
        val dealer = effective_events[position]

        holder.dealerName.text = dealer.attendeeNickname

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DealerDataHolder =
            DealerDataHolder(LayoutInflater
                    .from(parent.context)
                    .inflate(R.layout.fragment_dealer, parent, false)
            )

}