// DB Viewer, DBV_View
// (c) Tyler Reiff 2016

package dbviewer;

import java.util.Properties;
import java.awt.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.swing.*;

public class DBV_View {	
	static JFrame f;
	static JButton infoButton;
	static JMenuBar menuBar;
	static JMenu menu;
	static JMenuItem setCredentials, startDashboard, 
					 quitDashboard, hideDashboard;

	public static void main(String args[]) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() { 
				new DBV_Controller();
				DBV_Controller.appDidStart();
			}
		});
	}
	
	public static void createGUI() {
		f = new JFrame("DB Viewer");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(200, 120);
		f.setAlwaysOnTop(false);
		f.setLocationByPlatform(true);
		f.setUndecorated(true);
		f.setBackground(new Color(0, 255, 0, 5));
		f.setExtendedState(JFrame.MAXIMIZED_BOTH);
		
		menuBar = new JMenuBar();
		menuBar.setVisible(false);
		
		menu = new JMenu("Start");
		
		menuBar.add(menu);
		
		setCredentials = new JMenuItem("Change Credentials");
		setCredentials.addActionListener(new DBV_Controller());
		menu.add(setCredentials);
		
		startDashboard = new JMenuItem("Start Dashbaord");
		startDashboard.addActionListener(new DBV_Controller());
		menu.add(startDashboard);
		
		hideDashboard = new JMenuItem("Hide Dahsboard");
		hideDashboard.addActionListener(new DBV_Controller());
		menu.add(hideDashboard);
		
		quitDashboard = new JMenuItem("Quit Dashbaord");
		quitDashboard.addActionListener(new DBV_Controller());
		menu.add(quitDashboard);
			
		infoButton = new JButton("Learn More");
		infoButton.setFont(new Font("Arial", Font.BOLD, 32));
		infoButton.setPreferredSize(new Dimension(60, 40));
		infoButton.addActionListener(new DBV_Controller());

		f.setJMenuBar(menuBar);
		//f.add(infoButton);
		f.setVisible(true); 
	}
	
	public static void toggleMenuBar() {
		if (menuBar.isVisible()){
			menuBar.setVisible(false);
			return;
		} 
		else {
			menuBar.setVisible(true);
		}
	}
	
	public static String appCloseWindow() {
		char[] pass = null;
		JPanel p = new JPanel(new BorderLayout(10, 5));
		JPanel label = new JPanel(new GridLayout(0, 1, 2, 2));
		
		label.add(new JLabel("Enter Password", SwingConstants.RIGHT));
		
		JPanel controls = new JPanel(new GridLayout(0, 1, 2, 2));
	    JPasswordField password = new JPasswordField();
	    controls.add(password);
	    p.add(controls, BorderLayout.CENTER);
	    
	    JOptionPane.showMessageDialog(f, p, "Enter Password", JOptionPane.OK_CANCEL_OPTION);
	    
	    pass = password.getPassword();
	    
	    return pass.toString();
		}
	
	public static void changeCredentialsWindow() {
		Properties p = new Properties();
		 OutputStream output = null;
		
		JPanel panel = new JPanel(new BorderLayout(10, 5));
		
	    JPanel label = new JPanel(new GridLayout(0, 1, 2, 2));
	    label.add(new JLabel("Username", SwingConstants.RIGHT));
	    label.add(new JLabel("Password", SwingConstants.RIGHT));
	    panel.add(label, BorderLayout.WEST);

	    JPanel controls = new JPanel(new GridLayout(0, 1, 2, 2));
	    controls.setPreferredSize(new Dimension(200, 50));
	    JTextField username = new JTextField();
	    controls.add(username);
	    JTextField password = new JTextField();
	    controls.add(password);
	    panel.add(controls, BorderLayout.CENTER);
	    
	    try {
	    	output = new FileOutputStream("config.properties");
	    	
	    	JOptionPane.showMessageDialog(f, panel, "Set Credentials", JOptionPane.OK_CANCEL_OPTION);
	    	p.setProperty("username", username.getText());
	    	p.setProperty("password", password.getText());
	
			p.store(output, null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void displayAlert(String err, String desc) {
		JLabel[] labelArr = {new JLabel(err), new JLabel(desc)};
		JOptionPane.showMessageDialog(f, labelArr);
	}
}

