package cse110w240t16.parket;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
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

import static android.os.SystemClock.sleep;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
@SdkSuppress(minSdkVersion = 18)

/**
 * Scenario Test Three
 *
 * Description: This scenario tests our implementation of user report system.
 * When the app is lauched, UIAutomator will select the search bar and enter "HOP" using the
 * keyboard. Then, it'll select the first item called "Hopkins Parking Structure". After a marker is
 * displayed on the map, UIAutomator will click the marker, which brings the user to another view.
 * UIAutomator will test the report system by opening the spinner, and selecting "Almost Full" option.
 * If the actions terminates without any error, then it'll pass the test.
 */
public class UIAutomatorTest3 {

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
    public void testDetailedView() throws UiObjectNotFoundException {
        UiObject autoCompleteFragment = mDevice.findObject(new UiSelector()
                .text("Search For A Parking Lot")
                .className("android.widget.EditText"));

        if (autoCompleteFragment.exists()) {
            autoCompleteFragment.click();

            mDevice.pressKeyCode(KeyEvent.KEYCODE_H);
            mDevice.pressKeyCode(KeyEvent.KEYCODE_O);
            mDevice.pressKeyCode(KeyEvent.KEYCODE_P);

            UiObject name = mDevice.findObject(new UiSelector()
                    .index(1)
                    .className("android.widget.RelativeLayout"));

            if (name.exists()) {
                name.click();

                UiObject marker = mDevice.findObject(new UiSelector().index(0).className("android.view.View").clickable(false));
                assertEquals("Hopkins Parking Structure. Click Here For More Details.", marker.getContentDescription());

                SystemClock.sleep(9500);

                marker.click();

                mDevice.click(353, 533);

                UiObject status = mDevice.findObject(new UiSelector().text("Please Select A Status"));
                status.click();
                UiObject select = mDevice.findObject(new UiSelector().className("android.widget.CheckedTextView").index(2));
                assertEquals("Almost Full", select.getText());
                select.click();

                UiObject scroll = mDevice.findObject(new UiSelector().index(1).className("android.widget.EditText"));
                scroll.swipeUp(10);
                scroll.swipeUp(10);
                scroll.swipeUp(10);

                SystemClock.sleep(5000);

                UiObject report = mDevice.findObject(new UiSelector().index(11).className("android.widget.Button"));
                assertEquals("Report Pricing", report.getText());
                report.click();

            }
        }
    }
}

