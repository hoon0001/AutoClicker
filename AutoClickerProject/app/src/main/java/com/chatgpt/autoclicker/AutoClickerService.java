
package com.chatgpt.autoclicker;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.graphics.Path;
import android.os.Handler;
import android.os.Looper;
import android.view.accessibility.AccessibilityEvent;

public class AutoClickerService extends AccessibilityService {

    private Handler handler = new Handler(Looper.getMainLooper());
    private boolean isRunning = false;
    private int intervalMs = 1000;
    private int step = 0;

    private final int CENTER_X = 540;
    private final int CENTER_Y = 960;
    private final int OFFSET = 300;

    private final Runnable clickRunnable = new Runnable() {
        @Override
        public void run() {
            if (!isRunning) return;

            int x = CENTER_X, y = CENTER_Y;
            switch (step % 4) {
                case 0: y -= OFFSET; break;
                case 1: x += OFFSET; break;
                case 2: y += OFFSET; break;
                case 3: x -= OFFSET; break;
            }
            step++;
            click(x, y);
            handler.postDelayed(this, intervalMs);
        }
    };

    private void click(int x, int y) {
        Path clickPath = new Path();
        clickPath.moveTo(x, y);
        GestureDescription.StrokeDescription clickStroke = new GestureDescription.StrokeDescription(clickPath, 0, 50);
        GestureDescription.Builder builder = new GestureDescription.Builder();
        builder.addStroke(clickStroke);
        dispatchGesture(builder.build(), null, null);
    }

    public void startClicking(int interval) {
        this.intervalMs = interval;
        this.isRunning = true;
        handler.post(clickRunnable);
    }

    public void stopClicking() {
        this.isRunning = false;
        handler.removeCallbacks(clickRunnable);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {}

    @Override
    public void onInterrupt() {
        stopClicking();
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
    }
}
