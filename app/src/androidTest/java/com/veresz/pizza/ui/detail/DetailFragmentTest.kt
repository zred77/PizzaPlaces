package com.veresz.pizza.ui.detail

import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.hasChildCount
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.veresz.pizza.R
import com.veresz.pizza.model.Place
import com.veresz.pizza.repository.friend.FriendRepository
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import org.hamcrest.Matchers.*
import org.junit.*
import org.junit.runner.*

@RunWith(AndroidJUnit4::class)
class DetailFragmentTest {

    @MockK var place: Place = mockk {
        every { images } returns listOf(
            mockk {
                every { url } returns ""
            }
        )
        every { distanceFrom } returns (0..10).random().toFloat()
        every { rating } returns (0..50).random().toFloat().div(10f)
        every { openingHours } returns listOf("monday", "tuesday")
        every { friendIds } returns List((0..10).random()) { it.toString() }
    }

    @MockK val repository: FriendRepository = mockk()

    private val fragmentArgs = Bundle().apply {
        putParcelable("key.place", place)
    }

    @Before
    fun before() {
        launchFragmentInContainer<TestDetailFragment>(factory = DetailFragmentFactory(place, repository, fragmentArgs), themeResId = R.style.AppTheme)
    }

    @Test
    fun friendsAvatarCount() {
        onView(withId(R.id.friendsLayout)).check(matches(hasChildCount(place.friendIds.size)))
        onView(withId(R.id.friendsLayout)).check(matches(isDisplayed()))
    }

    @Test
    fun click_readMore_openingHour_isVisible() {
        onView(withId(R.id.opening)).check(matches(not(isDisplayed())))
        onView(withId(R.id.readMore)).perform(ViewActions.click())
        onView(withId(R.id.opening)).check(matches(isDisplayed()))
    }
}
