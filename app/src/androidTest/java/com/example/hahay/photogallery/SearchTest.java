package com.example.hahay.photogallery;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class SearchTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.example.hahay.photogallery", appContext.getPackageName());
    }
    @Rule
    public ActivityTestRule<MainActivity> mainAct = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void searchTest() {
        onView(withId(R.id.captionInput)).perform(replaceText("test")).check(matches(withText("test")));
        onView(withId(R.id.saveButton)).perform(click());
        onView(withId(R.id.nextButton)).perform(click());
        onView(withId(R.id.searchButton)).perform(click());
        onView(withId(R.id.searchInput)).perform(replaceText("test"));
        onView(withId(R.id.searchButton2)).perform(click());
        onView(withId(R.id.captionInput)).check(matches(withText("test")));
    }
}