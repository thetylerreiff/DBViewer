// DB Viewer, MouseObserver
// (c) Tyler Reiff 2016

package dbviewer;

import java.awt.Component;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.HashSet;
import java.util.Set;

import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class MouseObserver {
	/* the resolution of the mouse motion */
	 private static final int DELAY = 10;

	 private Component component;
	 private Timer timer;
	 private Set<MouseMotionListener> mouseMotionListeners;

	 protected MouseObserver(Component scene) {
	     if (scene == null) {
	         throw new IllegalArgumentException("Null component not allowed.");
	     }

	     this.component = scene;

	     /* poll mouse coordinates at the given rate */
	     timer = new Timer(DELAY, new ActionListener() {
	             private Point lastPoint = MouseInfo.getPointerInfo().getLocation();

	             /* called every DELAY milliseconds to fetch the
	              * current mouse coordinates */
	             public synchronized void actionPerformed(ActionEvent e) {
	                 Point point = MouseInfo.getPointerInfo().getLocation();

	                 if (!point.equals(lastPoint)) {
	                     fireMouseMotionEvent(point);
	                 }

	                 lastPoint = point;
	             }
	         });
	     mouseMotionListeners = new HashSet<MouseMotionListener>();
	 }

	 public Component getComponent() {
	     return component;
	 }

	 public void start() {
	     timer.start();
	 }

	 public void stop() {
	     timer.stop();
	 }

	 public void addMouseMotionListener(MouseMotionListener listener) {
	     synchronized (mouseMotionListeners) {
	         mouseMotionListeners.add(listener);
	     }
	 }

	 public void removeMouseMotionListener(MouseMotionListener listener) {
	     synchronized (mouseMotionListeners) {
	         mouseMotionListeners.remove(listener);
	     }
	 }

	 protected void fireMouseMotionEvent(Point point) {
	     synchronized (mouseMotionListeners) {
	         for (final MouseMotionListener listener : mouseMotionListeners) {
	             final MouseEvent event =
	                 new MouseEvent(component, MouseEvent.MOUSE_MOVED, System.currentTimeMillis(),
	                                0, point.x, point.y, 0, false);

	             SwingUtilities.invokeLater(new Runnable() {
	             	public void run() {
	             		listener.mouseMoved(event);
	                 }
	             });
	         }
	     }
	 }
}
