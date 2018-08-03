package org.eurofurence.connavigator.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.reactivex.disposables.Disposables
import org.eurofurence.connavigator.database.HasDb
import org.eurofurence.connavigator.database.lazyLocateDb
import org.eurofurence.connavigator.ui.communication.ContentAPI
import org.eurofurence.connavigator.ui.fragments.InfoGroupFragment
import org.eurofurence.connavigator.util.extensions.applyOnRoot
import org.eurofurence.connavigator.util.v2.plus
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.UI
import org.jetbrains.anko.support.v4.withArguments


class FragmentViewInfoGroups : Fragment(), ContentAPI, HasDb {
    override val db by lazyLocateDb()
    val ui = ViewInfoGroupsUi()

    var subscriptions = Disposables.empty()

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?) =
            UI { ui.createView(this) }.view

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        applyOnRoot { changeTitle("Convention Information") }

        fillUi()
        subscriptions += db.subscribe {
            fillUi()
        }
    }

    private fun fillUi() {
        val transaction = childFragmentManager.beginTransaction()

        // Remove existing instances
        db.knowledgeGroups.items
                .map { it.id.toString() }
                .map { childFragmentManager.findFragmentByTag(it) }
                .filter { it != null }
                .forEach { transaction.remove(it) }

        // create new instances
        db.knowledgeGroups.items
                .sortedBy { it.order }
                .map { Pair(InfoGroupFragment().withArguments("id" to it.id.toString()), it.id.toString()) }
                .forEach { transaction.add(1, it.first, it.second) }

        transaction.commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        subscriptions.dispose()
        subscriptions = Disposables.empty()
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