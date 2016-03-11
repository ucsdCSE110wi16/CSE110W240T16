package cse110w240t16.parket;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.SdkSuppress;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.support.test.uiautomator.Until;
import android.util.Log;
import android.view.KeyEvent;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
@SdkSuppress(minSdkVersion = 18)

/**
 * Scenario Test One: Place Picker
 *
 * Description: This scenario tests the Google Place Picker implementation of our app.
 * When the application is launched, UIAutomator will select the place picker button right below
 * the search bar. When place picker is launched, UIAutomator will use the keyboard and enter
 * "PAN". Then, UIAutomator will select the first item from the drop down result. If there is no
 * error, than it passes this test
 */
public class UIAutomatorTest1 {

    private static final String BASIC_SAMPLE_PACKAGE
            = "cse110w240t16.parket";
    private static final int LAUNCH_TIMEOUT = 10000;
    private UiDevice mDevice;

    @Before
    public void startMainActivityFromHomeScreen() {
        // Initialize UiDevice instance
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

        // Start from the home screen
        mDevice.pressHome();

        // Wait for launcher
        final String launcherPackage = mDevice.getLauncherPackageName();
        assertThat(launcherPackage, notNullValue());
        mDevice.wait(Until.hasObject(By.pkg(launcherPackage).depth(0)),
                LAUNCH_TIMEOUT);

        // Launch the app
        Context context = InstrumentationRegistry.getContext();
        final Intent intent = context.getPackageManager()
                .getLaunchIntentForPackage(BASIC_SAMPLE_PACKAGE);
        // Clear out any previous instances
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);

        // Wait for the app to appear
        mDevice.wait(Until.hasObject(By.pkg(BASIC_SAMPLE_PACKAGE).depth(0)),
                LAUNCH_TIMEOUT);
    }

    @Test
    public void findParkingStructure() throws UiObjectNotFoundException {
        UiObject button = mDevice.findObject(new UiSelector()
                .text("Or Explore Nearby Parking Lots")
                .className("android.widget.Button"));

        if (button.exists()){
            button.click();

            UiObject picker = mDevice.findObject(new UiSelector().textContains("place"));
            assertEquals("Pick a place", picker.getText());

            UiObject search = mDevice.findObject(new UiSelector().description("Search"));
            if (search.exists()) {
                search.click();

                UiObject type = mDevice.findObject(new UiSelector().text("Search"));
                if (type.exists()){
                    type.click();
                    mDevice.pressKeyCode(KeyEvent.KEYCODE_P);
                    mDevice.pressKeyCode(KeyEvent.KEYCODE_A);
                    mDevice.pressKeyCode(KeyEvent.KEYCODE_N);

                    UiObject parking = mDevice.findObject(new UiSelector()
                            .index(1)
                            .className("android.widget.RelativeLayout"));

                    UiObject name = mDevice.findObject(new UiSelector().textContains("Pangea"));
                    assertEquals("Pangea Parking Structure", name.getText());

                    if (parking.exists()){
                        parking.click();

                        UiObject location = mDevice.findObject(new UiSelector().index(0)
                                .className("android.widget.ImageView")
                                .resourceId("com.google.android.gms:id/icon"));

                        if (location.exists()){
                            location.click();

                            UiObject marker = mDevice.findObject(new UiSelector().index(0).className("android.view.View").clickable(false));
                            assertEquals("Pangea Parking Structure. Click here for More Details.", marker.getContentDescription());
                        }
                    }
                }
            }
        }


    }

}
