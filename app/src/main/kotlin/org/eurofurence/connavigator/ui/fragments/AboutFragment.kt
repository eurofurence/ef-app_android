package org.eurofurence.connavigator.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import org.eurofurence.connavigator.BuildConfig
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.services.ImageService
import org.eurofurence.connavigator.preferences.RemotePreferences
import org.eurofurence.connavigator.util.extensions.markdownView
import org.eurofurence.connavigator.util.extensions.now
import org.eurofurence.connavigator.util.v2.compatAppearance
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.UI
import org.jetbrains.anko.support.v4.browse

/**
 * Created by David on 28-4-2016.
 */
class FragmentViewAbout : Fragment(){
    val ui = AboutUi()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            UI { ui.createView(this) }.view

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        ui.requinardLayout.setOnClickListener { browse("https://furry.requinard.nl") }
        ui.pazuzuLayout.setOnClickListener { browse("https://twitter.com/Pazuzupizza") }

        ui.helpButton.setOnClickListener { browse(RemotePreferences.supportChatUrl) }
        ui.bugButton.setOnClickListener { browse("https://github.com/eurofurence/ef-app_android/issues") }
    }
}

class AboutUi : AnkoComponent<Fragment> {
    companion object {
        private const val avatarSize = 128
    }

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
                    backgroundResource = R.color.lightBackground
                    padding = dip(20)
                    weightSum = 10f

                    verticalLayout {
                        textView {
                            textResource = R.string.misc_version
                            compatAppearance = android.R.style.TextAppearance_Small
                        }
                        textView(BuildConfig.VERSION_NAME) {
                            compatAppearance = android.R.style.TextAppearance_Medium
                        }
                    }.lparams(dip(0), wrapContent) {
                        weight = 3F
                    }

                    verticalLayout {
                        textView {
                            text = resources.getString(R.string.misc_version_code, BuildConfig.VERSION_CODE)
                            compatAppearance = android.R.style.TextAppearance_Small
                        }
                        textView {
                            text = resources.getString(R.string.misc_remote_config_age, (now().millis - RemotePreferences.lastUpdatedDatetime.millis) / 1000 / 60 / 60)
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

                    textView {
                        textResource = R.string.misc_android_developers
                        compatAppearance = android.R.style.TextAppearance_Small_Inverse
                    }

                    requinardLayout = linearLayout {
                        setPadding(0, dip(10), 0, 0)
                        imageView {
                            ImageService.imageLoader.displayImage("https://en.gravatar.com/avatar/42d336e4b6f13d687c32eaaf9c8fb0ea?s=$avatarSize", this)
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
                            ImageService.imageLoader.displayImage("https://en.gravatar.com/avatar/a5db6ad5350a2ee91408120b94d9fa24?s=$avatarSize", this)
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
                    backgroundResource = R.color.lightBackground

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
- StreifiGreif
- Xil
- IceTiger

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


                    helpButton = button{
                        textResource = R.string.action_get_help
                    }.lparams(dip(0), wrapContent) {
                        weight = 1F
                    }
                    bugButton = button{
                        textResource = R.string.action_report_bug
                    }.lparams(dip(0), wrapContent) {
                        weight = 2F
                    }
                }
            }
        }
    }

}