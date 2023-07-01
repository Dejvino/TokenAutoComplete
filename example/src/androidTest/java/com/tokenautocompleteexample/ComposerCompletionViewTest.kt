package com.tokenautocompleteexample

import android.view.View
import android.widget.TabHost
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.core.internal.deps.guava.base.Preconditions
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.tokenautocompleteexample.InputConnectionTest.forceCommitText
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.`is`
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ComposerCompletionViewTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(TestCleanTokenActivity::class.java)

    @Before
    fun switchToComposer() {
        onView(withId(R.id.tabHost)).perform(switchToTabByTag("Composer"))
    }

    @Test
    fun completesOnSpace() {
        val tagA = Tag.sampleTags()[0]
        val tagB = Tag.sampleTags()[5]
        val tagC = Tag.sampleTags()[10]
        val text = "A sample $tagA tweet with $tagB $tagC"
        val firstTagIndex = text.indexOf(tagA.toString())

        onView(withId(R.id.composeView))
                .perform(typeText(text.substring(0, firstTagIndex)))
                .check(matches(tokenCount(`is`(0))))
                .perform(typeText(text.substring(firstTagIndex)))
                .check(matches(tokenCount(`is`(2))))
                .check(matches(withText(text)))
    }

    @Test
    fun doesNotCompleteWithoutSpace() {
        onView(withId(R.id.composeView))
                .perform(typeText("Unfinished #token #autocomplete"))
                .check(matches(tokenCount(`is`(1))))
    }

    @Test
    fun completesMultipleWhenPasting() {
        onView(withId(R.id.composeView))
                .perform(typeText("Pasting "))
                .perform(forceCommitText("multiple #token #autocomplete #hashtags."))
                .check(matches(tokenCount(`is`(3))))
    }

    private fun switchToTabByTag(tag: String) = object : ViewAction {
        override fun getDescription(): String = "switch to a tab with tag '$tag'"

        override fun getConstraints(): Matcher<View> = allOf(isDisplayed(), isAssignableFrom(TabHost::class.java))

        override fun perform(uiController: UiController?, view: View?) {
            uiController.run {
                (view as TabHost).setCurrentTabByTag(tag)
            }
        }
    }

    private fun tokenCount(intMatcher: Matcher<Int?>): Matcher<View?>? {
        Preconditions.checkNotNull(intMatcher)
        return object : BoundedMatcher<View?, TagCompletionView>(TagCompletionView::class.java) {
            override fun describeTo(description: Description) {
                description.appendText("token count: ")
                intMatcher.describeTo(description)
            }

            override fun matchesSafely(view: TagCompletionView): Boolean {
                return intMatcher.matches(view.objects.size)
            }
        }
    }
}

