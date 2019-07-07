package org.eurofurence.connavigator.ui.activities


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
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
class LoginFragmentTest {
    @Test
    fun loginActivityTest() {
        val textView = onView(
                allOf(withText("You're not logged in!"),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.instanceOf(android.widget.LinearLayout::class.java),
                                        1),
                                0),
                        isDisplayed()))
        textView.check(matches(withText("You're not logged in!")))

        val _LinearLayout = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.home_user_status),
                                childAtPosition(
                                        withClassName(`is`("org.jetbrains.anko._LinearLayout")),
                                        1)),
                        0),
                        isDisplayed()))
        _LinearLayout.perform(click())

        val _LinearLayout2 = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.home_user_status),
                                childAtPosition(
                                        withClassName(`is`("org.jetbrains.anko._LinearLayout")),
                                        1)),
                        0),
                        isDisplayed()))
        _LinearLayout2.perform(click())

        val button = onView(
                allOf(childAtPosition(
                        childAtPosition(
                                IsInstanceOf.instanceOf(android.widget.LinearLayout::class.java),
                                1),
                        4),
                        isDisplayed()))
        button.check(matches(isDisplayed()))

        pressBack()
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
