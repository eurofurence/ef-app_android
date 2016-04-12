package org.eurofurence.connavigator.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import io.swagger.client.model.Info
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.database.Database
import org.eurofurence.connavigator.net.imageService
import org.eurofurence.connavigator.util.delegators.view
import org.eurofurence.connavigator.util.extensions.objects
import us.feras.mdv.MarkdownView
import java.util.*

/**
 * Views an info based on an ID passed to the intent
 */
class InfoActivity : BaseActivity() {
    companion object {
        fun makeIntent(context: Context, id: UUID): Intent {
            val result = Intent(context, InfoActivity::class.java)
            result.objects["id"] = id
            return result
        }

        fun makeIntent(context: Context, info: Info) = makeIntent(context, info.id)
    }

    val infoViewImage by view(ImageView::class.java)
    val infoViewContent by view(MarkdownView::class.java)

    override fun onCreate(savedBundleInstance: Bundle?) {
        super.onCreate(savedBundleInstance);
        setContentView(R.layout.activity_info_base);
        injectNavigation(savedBundleInstance);

        // Assert consistency of input
        val id = intent.objects["id"]
        if (id == null) {
            finish()
            return
        }

        // Get info by id
        val database = Database(this)
        val info = database.infoDb.keyValues[id]

        // Assert info is actually present
        if (info == null) {
            finish()
            return
        }

        // Display title in action bar
        title = info.title

        // Display processed text in markdown view
        infoViewContent.loadMarkdown(info.text)

        // Display the image
        val img = database.imageDb.keyValues[info.imageId]
        if (img != null) {
            imageService.load(img, infoViewImage)
            infoViewImage.visibility = View.VISIBLE
        } else
            infoViewImage.visibility = View.GONE
    }
}