package org.eurofurence.connavigator.ui

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.ui.activities.StartActivity
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test

@LargeTest
class StartActivityUITests {

    @get:Rule
    var activityRule = ActivityTestRule(StartActivity::class.java)

    fun initialize() {
        activityRule.activity.db.clear()
    }

    @Test
    @Ignore
    fun acceptConditions() {
        onView(withText(R.string.misc_yes))
                .perform(click())
                .check(matches(withEffectiveVisibility(Visibility.GONE)))
    }
}