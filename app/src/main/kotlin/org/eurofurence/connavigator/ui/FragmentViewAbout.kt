package org.eurofurence.connavigator.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import org.eurofurence.connavigator.BuildConfig
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.net.imageService
import org.eurofurence.connavigator.tracking.Analytics
import org.eurofurence.connavigator.ui.communication.ContentAPI
import org.eurofurence.connavigator.util.delegators.view
import org.eurofurence.connavigator.util.extensions.applyOnRoot
import org.eurofurence.connavigator.util.extensions.markdownView
import org.jetbrains.anko.*
import us.feras.mdv.MarkdownView

/**
 * Created by David on 28-4-2016.
 */
class FragmentViewAbout : Fragment(), ContentAPI {
    val ui = AboutUi()
    private val attributations = "Google Map\n\nIcons8"

    val textVersion: TextView by view()
    val markdownAttributation: MarkdownView by view()
    val aboutRequinard: LinearLayout by view()
    val aboutPazuzu: LinearLayout by view()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            ui.createView(AnkoContext.Companion.create(context, container!!))

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        Analytics.screen(activity, "View About")

        applyOnRoot { changeTitle("About") }
    }
}

class AboutUi : AnkoComponent<ViewGroup> {
    val avatarSize = 128
    override fun createView(ui: AnkoContext<ViewGroup>) = with(ui) {
        scrollView {
            lparams(matchParent, matchParent)

            verticalLayout {
                themedTextView("Developed by", R.style.AppTheme_Header) {
                    setTextAppearance(ctx, android.R.style.TextAppearance_Large_Inverse)
                }

                linearLayout {
                    backgroundResource = R.color.cardview_light_background
                    setOnTouchListener { _, _ -> ctx.browse("https://furry.requinard.nl") }

                    imageView {
                        imageService.imageLoader.displayImage("https://en.gravatar.com/avatar/42d336e4b6f13d687c32eaaf9c8fb0ea?s=$avatarSize", this)
                    }.lparams(dip(avatarSize), dip(avatarSize))

                    textView {
                        gravity = Gravity.CENTER
                        text = "Requinard"
                        setTextAppearance(ctx, android.R.style.TextAppearance_Medium)
                    }.lparams(matchParent, dip(avatarSize))
                }

                linearLayout {
                    backgroundResource = R.color.cardview_light_background
                    setOnTouchListener { _, _ -> ctx.browse("https://twitter.com/Pazuzupizza") }

                    imageView {
                        imageService.imageLoader.displayImage("https://en.gravatar.com/avatar/a5db6ad5350a2ee91408120b94d9fa24?s=$avatarSize", this)
                    }.lparams(dip(avatarSize), dip(avatarSize))

                    textView {
                        gravity = Gravity.CENTER
                        text = "Pazuzu"
                        setTextAppearance(ctx, android.R.style.TextAppearance_Medium)
                    }.lparams(matchParent, dip(avatarSize))
                }

                themedTextView("Version: ${BuildConfig.VERSION_NAME} - Build: ${BuildConfig.VERSION_CODE}", R.style.AppTheme_Header_Sub)


                markdownView {
                    lparams(matchParent, wrapContent)

                    loadMarkdown("""## Also thanks to
                        - Luchs
                        - ZefiroDragon
                        - Fenrikur
                        - ShezHusky

                        ## Made with the following

                        - Kotlin & Anko
                        - FontAwesome
                    """)
                }
            }
        }
    }

}