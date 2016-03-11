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
 * Unit Test One
 *
 * Description: This unit test tests whether a marker will appear after selecting an item from the
 * search bar.
 */
public class UnitTest3 {

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
    public void showMarker() throws UiObjectNotFoundException {
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
            }
        }
    }
}

