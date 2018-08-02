package org.eurofurence.connavigator.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import org.eurofurence.connavigator.database.HasDb
import org.eurofurence.connavigator.database.lazyLocateDb
import org.eurofurence.connavigator.ui.communication.ContentAPI
import org.eurofurence.connavigator.ui.fragments.InfoGroupFragment
import org.jetbrains.anko.support.v4.UI
import org.jetbrains.anko.support.v4.withArguments
import org.eurofurence.connavigator.util.delegators.view
import org.eurofurence.connavigator.util.extensions.applyOnRoot
import org.jetbrains.anko.*


class FragmentViewInfoGroups : Fragment(), ContentAPI, HasDb {
    override val db by lazyLocateDb()
    val ui = ViewInfoGroupsUi()

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?) =
            UI { ui.createView(this) }.view

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        applyOnRoot { changeTitle("Convention Information") }

        db.subscribe {
            val transaction = childFragmentManager.beginTransaction()

            // Remove existing instances
            db.knowledgeGroups.items
                    .map { it.id.toString() }
                    .map { childFragmentManager.findFragmentByTag(it) }
                    .filter { it != null }
                    .forEach { transaction.remove(it) }

            // create new instances
            db.knowledgeGroups.items
                    .map { Pair(InfoGroupFragment().withArguments("id" to it.id.toString()), it.id.toString()) }
                    .forEach { transaction.add(1, it.first, it.second) }

            transaction.commit()
        }
    }
}

class ViewInfoGroupsUi : AnkoComponent<Fragment> {
    override fun createView(ui: AnkoContext<Fragment>) = with(ui) {
        scrollView {
            topPadding = dip(10)
            verticalLayout {
                id = 1
            }
        }
    }

}