// DB Viewer, DBV_Controller
// (c) Tyler Reiff 2016

package dbviewer;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.*;

public class DBV_Controller implements ActionListener {
	
	public static void appDidStart() {
		DBV_View.createGUI();
		startMouseObserver();
		executionTimer();
		refreshTimer();
		IsKeyPressed.startKeyObserver();
	}
	// TR: This time running the execution logic 
	public static void executionTimer() {
		ActionListener taskPerformer = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				DBV_Model.scriptExeLogic();
			}
		};
		Timer timer = new Timer(1000 ,taskPerformer);
		timer.setRepeats(true);
		timer.start();
	}
	
	// TR: This timer refreshes the app ever one hour 
	public static void refreshTimer() {
		ActionListener taskPerformer = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				DBV_Model.appWillHide();
				try { wait(500); } 
				catch (InterruptedException e) {e.printStackTrace();}
				DBV_Model.exeLoginScript();
			}
		};
		Timer timer = new Timer(1000*60*60 ,taskPerformer);
		timer.setRepeats(true);
		timer.start();
	}
		
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == DBV_View.setCredentials) {
			System.out.println("Change Username Selected");
				DBV_View.changeCredentialsWindow();
		} 
		else if (e.getSource() == DBV_View.startDashboard) {
			System.out.println("Start Dashboard Selected");
			DBV_Model.exeLoginScript();
		}
		else if (e.getSource() == DBV_View.hideDashboard) {
			DBV_Model.appWillHide();
		} 
		else if (e.getSource() == DBV_View.quitDashboard) {
			DBV_Model.appWillClose();
		} 
		else if (e.getSource() == DBV_View.infoButton) {
			System.out.println("Info Button Selected");
		}
	}
	
	public static void updateOnTop() {
		DBV_View.f.setAlwaysOnTop(true);
		DBV_View.f.setVisible(true);
	}
	
	public static void updateOffTop() {
		DBV_View.f.setAlwaysOnTop(false);
		DBV_View.f.setVisible(false);
	}

	private static void startMouseObserver() {
        MouseObserver mo = new MouseObserver(DBV_View.f);        
        mo.addMouseMotionListener(new MouseMotionListener() {
        	public void mouseMoved(MouseEvent e) {        	
        		DBV_Model.lastMoveModel = e.getWhen();
        	}
        
        	public void mouseDragged(MouseEvent e) {
        	}
        });

        mo.start();
	}
}
