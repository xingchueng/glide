package com.bumptech.glide;

import static com.bumptech.glide.tests.BackgroundUtil.testInBackground;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.content.Context;

import com.bumptech.glide.manager.ConnectivityMonitor;
import com.bumptech.glide.manager.ConnectivityMonitor.ConnectivityListener;
import com.bumptech.glide.manager.ConnectivityMonitorFactory;
import com.bumptech.glide.manager.Lifecycle;
import com.bumptech.glide.manager.RequestTracker;
import com.bumptech.glide.tests.BackgroundUtil;
import com.bumptech.glide.tests.GlideShadowLooper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE, emulateSdk = 18, shadows = GlideShadowLooper.class)
public class RequestManagerTest {
    private RequestManager manager;
    private ConnectivityMonitor connectivityMonitor;
    private RequestTracker requestTracker;
    private ConnectivityListener connectivityListener;
    private Lifecycle lifecycle = mock(Lifecycle.class);

    @Before
    public void setUp() {
        connectivityMonitor = mock(ConnectivityMonitor.class);
        ConnectivityMonitorFactory factory = mock(ConnectivityMonitorFactory.class);
        when(factory.build(any(Context.class), any(ConnectivityMonitor.ConnectivityListener.class)))
                .thenAnswer(new Answer<ConnectivityMonitor>() {
                    @Override
                    public ConnectivityMonitor answer(InvocationOnMock invocation) throws Throwable {
                        connectivityListener = (ConnectivityListener) invocation.getArguments()[1];
                        return connectivityMonitor;
                    }
                });
        requestTracker = mock(RequestTracker.class);
        manager = new RequestManager(Robolectric.application, lifecycle, requestTracker, factory);
    }

    @Test
    public void testPauseRequestsPausesRequests() {
        manager.pauseRequests();

        verify(requestTracker).pauseRequests();
    }

    @Test
    public void testResumeRequestsResumesRequests() {
        manager.resumeRequests();

        verify(requestTracker).resumeRequests();
    }

    @Test
    public void testPausesRequestsOnStop() {
        manager.onStart();
        manager.onStop();

        verify(requestTracker).pauseRequests();
    }

    @Test
    public void testResumesRequestsOnStart() {
        manager.onStart();

        verify(requestTracker).resumeRequests();
    }

    @Test
    public void testClearsRequestsOnDestroy() {
        manager.onDestroy();

        verify(requestTracker).clearRequests();
    }

    @Test
    public void testAddsConnectivityMonitorToLifecycleWhenConstructed() {
        verify(lifecycle).addListener(eq(connectivityMonitor));
    }

    @Test
    public void testAddsSelfToLifecycleWhenConstructed() {
        verify(lifecycle).addListener(eq(manager));
    }

    @Test
    public void testRestartsRequestOnConnected() {
        connectivityListener.onConnectivityChanged(true);

        verify(requestTracker).restartRequests();
    }

    @Test
    public void testDoesNotRestartRequestsOnDisconnected() {
        connectivityListener.onConnectivityChanged(false);

        verify(requestTracker, never()).restartRequests();
    }

    @Test(expected = RuntimeException.class)
    public void testThrowsIfResumeCalledOnBackgroundThread() throws InterruptedException {
        testInBackground(new BackgroundUtil.BackgroundTester() {
            @Override
            public void runTest() throws Exception {
                manager.resumeRequests();
            }
        });
    }

    @Test(expected = RuntimeException.class)
    public void testThrowsIfPauseCalledOnBackgroundThread() throws InterruptedException {
        testInBackground(new BackgroundUtil.BackgroundTester() {
            @Override
            public void runTest() throws Exception {
                manager.pauseRequests();
            }
        });
    }

    @Test
    public void testDelegatesIsPausedToRequestTracker() {
        when(requestTracker.isPaused()).thenReturn(true);
        assertTrue(manager.isPaused());
        when(requestTracker.isPaused()).thenReturn(false);
        assertFalse(manager.isPaused());
    }
}