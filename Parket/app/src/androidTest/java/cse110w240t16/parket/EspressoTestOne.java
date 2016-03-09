package cse110w240t16.parket;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.hasSibling;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.allOf;

import android.test.suitebuilder.annotation.LargeTest;


@RunWith(AndroidJUnit4.class)
@LargeTest

public class EspressoTestOne {

    private String mStringToBeTyped;

    @Rule
    public ActivityTestRule<MapsActivity> mActivityRule = new ActivityTestRule<>(
            MapsActivity.class);

    @Before
    public void initValidString() {
        mStringToBeTyped = "Pangea Parking Structure";
    }

    @Test
    public void changeText_sameActivity() {
        onView(withId(R.id.place_autocomplete_fragment))
                .perform(click());
        onView(withId(R.id.place_autocomplete_search_input))
                .perform(typeText(mStringToBeTyped), closeSoftKeyboard());
        onView(withId(R.id.place_autocomplete_prediction_primary_text))
                .perform(click());
        //onView(allOf(withId(R.id.map)), hasSibling());

    }
}
