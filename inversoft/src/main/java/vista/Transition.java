package vista;

import java.awt.Window;
import javax.swing.Timer;

/**
 * Simple per-window fade-in / fade-out transitions using Window.setOpacity.
 * Falls back to immediate show/dispose if the platform doesn't support translucency.
 */
public class Transition {

    public static void fadeIn(Window w) {
        try {
            // Start fully transparent and animate to opaque while visible
            try { w.setOpacity(0f); } catch (Throwable ignore) {}
            final float[] op = {0f};
            Timer t = new Timer(20, null);
            t.addActionListener(e -> {
                op[0] += 0.08f;
                if (op[0] >= 1f) {
                    try { w.setOpacity(1f); } catch (Throwable ignore) {}
                    t.stop();
                } else {
                    try { w.setOpacity(Math.max(0f, Math.min(1f, op[0]))); } catch (Throwable ignore) {}
                }
            });
            t.setRepeats(true);
            t.start();
            // Show modal dialog (blocks until disposed); timer runs on EDT to animate
            w.setVisible(true);
        } catch (Throwable ex) {
            // Platform doesn't support translucency — fall back to normal show
            try { w.setVisible(true); } catch (Throwable ignore) {}
        }
    }

    public static void fadeOutAndDispose(Window w) {
        try {
            final float[] op = {1f};
            Timer t = new Timer(20, null);
            t.addActionListener(e -> {
                op[0] -= 0.08f;
                if (op[0] <= 0f) {
                    t.stop();
                    try { w.dispose(); } catch (Throwable ignore) {}
                } else {
                    try { w.setOpacity(Math.max(0f, Math.min(1f, op[0]))); } catch (Throwable ignore) {}
                }
            });
            t.setRepeats(true);
            t.start();
        } catch (Throwable ex) {
            try { w.dispose(); } catch (Throwable ignore) {}
        }
    }
}
