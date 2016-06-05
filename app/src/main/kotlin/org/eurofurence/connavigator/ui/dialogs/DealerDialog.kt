package org.eurofurence.connavigator.ui.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.DialogFragment
import io.swagger.client.model.Dealer
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.util.Formatter
import org.eurofurence.connavigator.util.extensions.logd

/**
 * Created by David on 6/5/2016.
 */
class DealerDialog(val dealer: Dealer) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)

        builder.setTitle(dealer.displayName)

        builder.setItems(R.array.dealer_options, DialogInterface.OnClickListener { dialogInterface, i -> update(dialogInterface, i) })

        return builder.create()
    }

    private fun update(dialogInterface: DialogInterface?, i: Int) =
            when (i) {
                0 -> logd { "send to notes" }
                else -> {
                    val shareIntent = Intent()
                    shareIntent.setAction(Intent.ACTION_SEND)
                    shareIntent.putExtra(Intent.EXTRA_TEXT, Formatter.shareDealer(dealer))
                    shareIntent.setType("text/plain")
                    startActivity(shareIntent)
                }
            }
}