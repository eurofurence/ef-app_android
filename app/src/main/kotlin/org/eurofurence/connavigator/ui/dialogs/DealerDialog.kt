package org.eurofurence.connavigator.ui.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.DialogFragment
import io.swagger.client.model.DealerRecord
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.tracking.Analytics
import org.eurofurence.connavigator.util.Formatter
import org.eurofurence.connavigator.util.SharingUtility
import org.eurofurence.connavigator.util.extensions.logd

/**
 * Created by David on 6/5/2016.
 */
class DealerDialog(val dealer: DealerRecord) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)

        builder.setTitle("Dealer options for ${dealer.displayName}")

        builder.setItems(R.array.dealer_options, DialogInterface.OnClickListener { dialogInterface, i -> update(i) })

        return builder.create()
    }

    private fun update(i: Int) =
            when (i) {
                0 -> logd { "send to notes" }
                else -> {
                    Analytics.event(Analytics.Category.DEALER, Analytics.Action.SHARED, dealer.displayName ?: dealer.attendeeNickname)

                    startActivity(Intent.createChooser(SharingUtility.share(Formatter.shareDealer(dealer)), "Share Dealer"))
                }
            }
}