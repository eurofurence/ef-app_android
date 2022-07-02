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
import org.eurofurence.connavigator.dropins.*
import org.eurofurence.connavigator.util.extensions.withArguments
import org.eurofurence.connavigator.util.v2.plus

// TODO req: Replace null assertions in fragments
// TODO req: port to nav graph
// TODO req: fix build
// TODO req: add state saving

class InfoListFragment : DisposingFragment(), HasDb {
    override val db by lazyLocateDb()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = createView {
        scrollView {
            layoutParams = viewGroupLayoutParams(matchParent, matchParent)
            relativeLayout {
                layoutParams = viewGroupLayoutParams(matchParent, wrapContent)
                verticalLayout {
                    layoutParams = relativeLayoutParams(matchParent, wrapContent)
                    setPadding(0, dip(10), 0, 0)
                    id = R.id.info_group_container
                }
            }
        }
    }

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
            .map {
                Pair(
                    InfoGroupFragment().withArguments {
                        putString("id", it.id.toString())
                    },
                    it.id.toString()
                )
            }
            .forEach { transaction.add(R.id.info_group_container, it.first, it.second) }

        transaction.commit()
    }
}
