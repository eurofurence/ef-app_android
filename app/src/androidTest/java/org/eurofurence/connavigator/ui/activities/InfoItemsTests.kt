package org.eurofurence.connavigator.ui.activities


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import org.eurofurence.connavigator.R
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.IsInstanceOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class InfoItemsTests {
    @Test
    fun infoItemsTests() {
        val appCompatImageButton = onView(
                allOf(withContentDescription("Open navigation drawer"),
                        childAtPosition(
                                allOf(withId(R.id.toolbar),
                                        childAtPosition(
                                                withClassName(`is`("org.jetbrains.anko._LinearLayout")),
                                                0)),
                                1),
                        isDisplayed()))
        appCompatImageButton.perform(click())

        val navigationMenuItemView = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.design_navigation_view),
                                childAtPosition(
                                        withClassName(`is`("com.google.android.material.navigation.NavigationView")),
                                        0)),
                        2),
                        isDisplayed()))
        navigationMenuItemView.perform(click())

        val linearLayout = onView(
                allOf(childAtPosition(
                        childAtPosition(
                                IsInstanceOf.instanceOf(android.widget.LinearLayout::class.java),
                                0),
                        1),
                        isDisplayed()))
        linearLayout.check(matches(isDisplayed()))

        val _LinearLayout = onView(
                allOf(childAtPosition(
                        childAtPosition(
                                withId(R.id.info_group_container),
                                1),
                        0),
                        isDisplayed()))
        _LinearLayout.perform(click())

        val _LinearLayout2 = onView(
                allOf(withId(R.id.layout),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(`is`("org.jetbrains.anko._LinearLayout")),
                                        0),
                                0),
                        isDisplayed()))
        _LinearLayout2.perform(click())

        val textView = onView(
                allOf(withText("\uF132"),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.instanceOf(android.widget.LinearLayout::class.java),
                                        0),
                                0),
                        isDisplayed()))
        textView.check(matches(isDisplayed()))

        val webView = onView(
                allOf(childAtPosition(
                        childAtPosition(
                                IsInstanceOf.instanceOf(android.widget.LinearLayout::class.java),
                                1),
                        0),
                        isDisplayed()))
        webView.check(matches(isDisplayed()))
    }

    private fun childAtPosition(
            parentMatcher: Matcher<View>, position: Int): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
