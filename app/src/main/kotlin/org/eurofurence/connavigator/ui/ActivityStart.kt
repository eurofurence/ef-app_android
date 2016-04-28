package org.eurofurence.connavigator.ui

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.database.Database
import org.eurofurence.connavigator.util.delegators.view

/**
 * Created by David on 28-4-2016.
 */
class ActivityStart : AppCompatActivity() {
    val buttonStart by view(Button::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_start)

        val db = Database(this)

        if ( db.eventConferenceDayDb.items.isNotEmpty())
            startRootActivity()

        buttonStart.setOnClickListener {
            startRootActivity()
        }

        buttonStart.visibility = View.VISIBLE
    }

    private fun startRootActivity() {
        val intent = Intent(this, ActivityRoot::class.java)
        startActivity(intent)
    }
}