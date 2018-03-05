package com.example.rssgrabber

import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.IdlingRegistry
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.contrib.DrawerActions
import android.support.test.espresso.contrib.DrawerMatchers.isOpen
import android.support.test.espresso.contrib.NavigationViewActions
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.espresso.matcher.ViewMatchers.withText
import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.view.Gravity
import org.hamcrest.CoreMatchers.not
import org.junit.*
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class MainActivityTest {

    @get:Rule
    public val mActivityRule: ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java)

    private var idlingResource: IntentServiceIdlingResource? = null

    @Before
    fun registerIntentServiceIdlingResource() {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        idlingResource = IntentServiceIdlingResource(instrumentation.targetContext)
        IdlingRegistry.getInstance().register(idlingResource)
    }

    @After
    fun unregisterIntentServiceIdlingResource() {
        IdlingRegistry.getInstance().unregister(idlingResource)
    }

    @Test
    fun useAppContext() {
        val appContext = InstrumentationRegistry.getTargetContext()
        Assert.assertEquals("com.example.rssgrabber", appContext.packageName)
    }

    @Test
    fun mainList() {
        onView(withId(R.id.drawer_layout))
                .perform(DrawerActions.open())

        Thread.sleep(20000)

        onView(withId(R.id.drawer_layout))
                .check(matches(isOpen(Gravity.LEFT)))
                .perform(DrawerActions.close())

        Thread.sleep(1000)

        onView(withId(R.id.drawer_layout))
                .perform(DrawerActions.open())

        Thread.sleep(1000)

        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(5))

        Thread.sleep(1000)

        onView(withId(R.id.toolbar_title))
                .check(matches(not(withText(""))))
        
        onView(withId(R.id.list))
                .perform(ViewActions.swipeUp())
                .perform(ViewActions.swipeDown())
                .perform(ViewActions.swipeDown())

        Thread.sleep(3000)

        idlingResource?.isIntentServiceRunning = false
    }
}