package pro.butovanton.farestechruner;

import android.app.Application;
import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.rule.ServiceTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

@RunWith(AndroidJUnit4.class)
public class InstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        assertEquals("pro.butovanton.farestech_runer", appContext.getPackageName());
    }

    @Rule
    public final ServiceTestRule serviceRule = new ServiceTestRule();

    @Rule public GrantPermissionRule permissionRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);

     @Test
    public void VieModelStartStop() {
        FViewModel viewModel = new FViewModel((Application) InstrumentationRegistry.getInstrumentation().getTargetContext().getApplicationContext());
        String testNumber = "+79281247777";
        assertFalse( viewModel.getServiceStatus());
        viewModel.starStopService(testNumber);
        assertTrue(viewModel.getServiceStatus());
        viewModel.starStopService(testNumber);
        assertFalse( viewModel.getServiceStatus());
     }


}
