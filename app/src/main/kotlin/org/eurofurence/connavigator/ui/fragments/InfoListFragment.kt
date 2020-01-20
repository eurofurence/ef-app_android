package org.eurofurence.connavigator.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.reactivex.disposables.Disposables
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.database.HasDb
import org.eurofurence.connavigator.database.lazyLocateDb
import org.eurofurence.connavigator.util.v2.plus
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.UI
import org.jetbrains.anko.support.v4.withArguments

// TODO req: Replace null assertions in fragments
// TODO req: port to nav graph
// TODO req: fix build
// TODO req: add state saving

class InfoListFragment : DisposingFragment(), HasDb{
    override val db by lazyLocateDb()
    val ui = ViewInfoGroupsUi()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            UI { ui.createView(this) }.view

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fillUi()
        db.subscribe {
            fillUi()
        }
        .collectOnDestroyView()
    }

    private fun fillUi() {
        val transaction = childFragmentManager.beginTransaction()

        // Remove existing instances
        db.knowledgeGroups.items
                .map { it.id.toString() }
                .map { childFragmentManager.findFragmentByTag(it) }
                .filter { it != null }
                .forEach { transaction.remove(it!!) }

        // create new instances
        db.knowledgeGroups.items
                .sortedBy { it.order }
                .map { Pair(InfoGroupFragment().withArguments("id" to it.id.toString()), it.id.toString()) }
                .forEach { transaction.add(R.id.info_group_container, it.first, it.second) }

        transaction.commit()
    }
}

class ViewInfoGroupsUi : AnkoComponent<Fragment> {
    override fun createView(ui: AnkoContext<Fragment>) = with(ui) {
        scrollView {
            lparams(matchParent, matchParent)
            relativeLayout {
                verticalLayout {
                    topPadding = dip(10)
                    id = R.id.info_group_container
                }.lparams(matchParent, wrapContent)
            }.lparams(matchParent, wrapContent)
        }
    }

}