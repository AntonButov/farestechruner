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

@RunWith(MockitoJUnitRunner.class)
public class TestMock {

    @Mock
    Application mockContext;

    @Test
    public void VieModelStartStop() {
        FViewModel viewModel = new FViewModel(mockContext);
        String testNumber = "+79281247777";
        assertFalse( viewModel.getServiceStatus());
        //     assertTrue(viewModel.getServiceStatus());
        viewModel.starStopService(testNumber);
        //     assertFalse(viewModel.getServiceStatus());
    }

}
