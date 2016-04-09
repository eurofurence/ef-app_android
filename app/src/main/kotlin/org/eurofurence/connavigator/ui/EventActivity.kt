package org.eurofurence.connavigator.ui

import android.os.Bundle
import org.eurofurence.connavigator.R

/**
 * Created by David on 4/9/2016.
 */
class EventActivity : BaseActivity() {
    lateinit var id: String

    override fun onCreate(savedBundleInstance: Bundle?) {
        super.onCreate(savedBundleInstance);

        id = intent.getStringExtra("uid")

        print(id)

        driver.initialize()

        setContentView(R.layout.activity_event);

        injectNavigation(savedBundleInstance);
    }
}