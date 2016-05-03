// DB Viewer, isKeyPressed
// (c) Tyler Reiff 2016

package dbviewer;

import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;

public class IsKeyPressed {
    private static boolean hPressed = true;
    public static boolean isTabPressed() {
        synchronized (IsKeyPressed.class) {
            return hPressed;
        }
    }

    public static void startKeyObserver() {
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {        	
           
        	@Override
            public boolean dispatchKeyEvent(KeyEvent ke) {
                synchronized (IsKeyPressed.class) {
                    switch (ke.getID()) {
                    case KeyEvent.KEY_PRESSED:
                        if (ke.getKeyCode() == KeyEvent.VK_H) {
                        	DBV_View.toggleMenuBar();
                        }
                        break;

                    case KeyEvent.KEY_RELEASED:
                        if (ke.getKeyCode() == KeyEvent.VK_W) {
                            hPressed = false;
                        }
                        break;
                    }
                    return false;
                }
            }
        });
    }
}