package org.eurofurence.connavigator.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import org.eurofurence.connavigator.BuildConfig
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.net.imageService
import org.eurofurence.connavigator.pref.RemotePreferences
import org.eurofurence.connavigator.ui.communication.ContentAPI
import org.eurofurence.connavigator.util.extensions.applyOnRoot
import org.eurofurence.connavigator.util.extensions.markdownView
import org.eurofurence.connavigator.util.extensions.now
import org.eurofurence.connavigator.util.v2.compatAppearance
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.UI
import org.jetbrains.anko.support.v4.browse

/**
 * Created by David on 28-4-2016.
 */
class FragmentViewAbout : Fragment(), ContentAPI, NavRepresented {
    override val drawerItemId: Int
        get() = R.id.navAbout


    val ui = AboutUi()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            UI { ui.createView(this) }.view

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        applyOnRoot { changeTitle("About") }

        ui.requinardLayout.setOnClickListener { browse("https://furry.requinard.nl") }
        ui.pazuzuLayout.setOnClickListener { browse("https://twitter.com/Pazuzupizza") }

        ui.helpButton.setOnClickListener { browse(RemotePreferences.supportChatUrl) }
        ui.bugButton.setOnClickListener { browse("https://github.com/eurofurence/ef-app_android/issues") }
    }
}

class AboutUi : AnkoComponent<Fragment> {
    private val avatarSize = 128

    lateinit var requinardLayout: LinearLayout
    lateinit var pazuzuLayout: LinearLayout

    lateinit var helpButton: Button
    lateinit var bugButton: Button

    override fun createView(ui: AnkoContext<Fragment>) = with(ui) {
        scrollView {
            lparams(matchParent, matchParent)
            backgroundResource = R.color.backgroundGrey

            verticalLayout {

                linearLayout {
                    backgroundResource = R.color.cardview_light_background
                    padding = dip(20)
                    weightSum = 10f

                    verticalLayout {
                        textView("Version") {
                            compatAppearance = android.R.style.TextAppearance_Small
                        }
                        textView(BuildConfig.VERSION_NAME) {
                            compatAppearance = android.R.style.TextAppearance_Medium
                        }
                    }.lparams(dip(0), wrapContent) {
                        weight = 3F
                    }

                    verticalLayout {
                        textView("Version Code: ${BuildConfig.VERSION_CODE}") {
                            compatAppearance = android.R.style.TextAppearance_Small
                        }
                        textView("Remote Configuration: ${(now().millis - RemotePreferences.lastUpdatedDatetime.millis) / 1000 / 60 / 60} hours     ago") {
                            compatAppearance = android.R.style.TextAppearance_Small
                        }
                    }.lparams(dip(0), wrapContent) {
                        weight = 7F
                    }
                }.lparams(matchParent, wrapContent) {
                    topMargin = dip(20)
                }

                verticalLayout {
                    backgroundResource = R.color.primaryDark
                    padding = dip(20)

                    textView("Android Developers") {
                        compatAppearance = android.R.style.TextAppearance_Small_Inverse
                    }

                    requinardLayout = linearLayout {
                        setPadding(0, dip(10), 0, 0)
                        imageView {
                            imageService.imageLoader.displayImage("https://en.gravatar.com/avatar/42d336e4b6f13d687c32eaaf9c8fb0ea?s=$avatarSize", this)
                        }.lparams(dip(75), dip(75))

                        textView {
                            gravity = Gravity.CENTER_VERTICAL
                            text = "Requinard"
                            compatAppearance = android.R.style.TextAppearance_Large_Inverse
                            padding = dip(10)
                        }.lparams(matchParent, dip(75))
                    }.lparams(matchParent, wrapContent)

                    pazuzuLayout = linearLayout {
                        setPadding(0, dip(10), 0, 0)
                        imageView {
                            imageService.imageLoader.displayImage("https://en.gravatar.com/avatar/a5db6ad5350a2ee91408120b94d9fa24?s=$avatarSize", this)
                        }.lparams(dip(75), dip(75))

                        textView {
                            gravity = Gravity.CENTER_VERTICAL
                            text = "Pazuzu"
                            compatAppearance = android.R.style.TextAppearance_Large_Inverse
                            padding = dip(10)
                        }.lparams(matchParent, dip(75))
                    }
                }.lparams(matchParent, wrapContent) {
                    topMargin = dip(20)
                }

                verticalLayout {
                    padding = dip(20)
                    backgroundResource = R.color.cardview_light_background

                    markdownView {
                        lparams(matchParent, wrapContent)
                        isFocusable = false
                        loadMarkdown("""
**iOS:**

- Fenrikur
- Shez

**Windows Mobile:**

- Luchs

**Program Management:**

- Luchs
- Zefiro

**Special Thanks:**

- Akulatraxas
- Aragon Tigerseye
- Atkelar
- Cairyn
- Carenath Stormwind
- Jul
- Liam
- NordicFuzzCon (Catch'em all)
- Pattarchus
- Snow-wolf
- StrefiGreif
- Xil

**Made with**

- Icons8
- FontAwesome
- Kotlin
- Anko


**Disclaimer**

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
                    """)
                    }
                }.lparams(matchParent, wrapContent) {
                    topMargin = dip(20)
                }

                linearLayout {
                    padding = dip(20)
                    weightSum = 3F


                    helpButton = button("Get help").lparams(dip(0), wrapContent) {
                        weight = 1F
                    }
                    bugButton = button("Report a bug").lparams(dip(0), wrapContent) {
                        weight = 2F
                    }
                }
            }
        }
    }

}