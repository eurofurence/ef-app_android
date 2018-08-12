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
import org.eurofurence.connavigator.util.extensions.contains
import org.eurofurence.connavigator.util.extensions.jsonObjects
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.debug
import kotlin.properties.Delegates.notNull

/**
 * Created by David on 6/5/2016.
 */
class DealerDialog : DialogFragment(), AnkoLogger {
    var dealerRecord by notNull<DealerRecord>()

    fun withArguments(dealerRecord: DealerRecord) = apply {
        arguments = Bundle().apply {
            jsonObjects["dealerRecord"] = dealerRecord
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)

        if ("dealerRecord" in arguments) {
            dealerRecord = arguments.jsonObjects["dealerRecord"]

            builder.setTitle("Dealer options for ${dealerRecord.displayName}")

            builder.setItems(R.array.dealer_options, DialogInterface.OnClickListener { dialogInterface, i -> update(dialogInterface, i) })

            return builder.create()
        } else {
            return super.onCreateDialog(savedInstanceState)
        }
    }

    private fun update(dialogInterface: DialogInterface?, i: Int) =
            when (i) {
                0 -> debug { "send to notes" }
                else -> {
                    Analytics.event(Analytics.Category.DEALER, Analytics.Action.SHARED, dealerRecord.displayName
                            ?: dealerRecord.attendeeNickname)

                    startActivity(Intent.createChooser(SharingUtility.share(Formatter.shareDealer(dealerRecord)), "Share Dealer"))
                }
            }
}