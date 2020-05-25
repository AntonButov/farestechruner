package pro.butovanton.farestechruner;

import android.app.Application;
import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runner.manipulation.Ordering;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TestMock {

    @Mock
    FViewModel viewModel;

    private Boolean run;

    @Test
    public void VieModelStartStop() {
        run = false;
        when(viewModel.getServiceStatus()).thenReturn(run);
        String testNumber = "+79281247777";
        assertFalse( viewModel.getServiceStatus());
        //     assertTrue(viewModel.getServiceStatus());
        viewModel.starStopService(testNumber);
        run = true;
        when(viewModel.getServiceStatus()).thenReturn(run);
        assertTrue(viewModel.getServiceStatus());
    }

}
