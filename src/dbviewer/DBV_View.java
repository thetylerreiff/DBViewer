// DB Viewer, DBV_View
// (c) Tyler Reiff 2016

package dbviewer;

import java.util.Properties;
import java.awt.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.swing.*;

public class DBV_View {	
	static JFrame f;
	static JButton infoButton;
	static JMenuBar menuBar;
	static JMenu start, settings;
	static JMenuItem setCredentials, startDashboard, 
					 quitDashboard, hideDashboard, setTTSText;

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
		f = new JFrame("DashBoard Viewer");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(200, 120);
		f.setAlwaysOnTop(false);
		f.setLocationByPlatform(true);
		f.setUndecorated(true);
		f.setBackground(new Color(0, 255, 0, 2));
		f.setExtendedState(JFrame.MAXIMIZED_BOTH);
		
		menuBar = new JMenuBar();
		menuBar.setVisible(false);
		
		start = new JMenu("Start");
		settings = new JMenu("Settings");
		
		startDashboard = new JMenuItem("Start Dashbaord");
		startDashboard.addActionListener(new DBV_Controller());
		start.add(startDashboard);
		
		hideDashboard = new JMenuItem("Hide Dahsboard");
		hideDashboard.addActionListener(new DBV_Controller());
		start.add(hideDashboard);
		
		quitDashboard = new JMenuItem("Quit Dashbaord");
		quitDashboard.addActionListener(new DBV_Controller());
		start.add(quitDashboard);
		
		setCredentials = new JMenuItem("Change Credentials");
		setCredentials.addActionListener(new DBV_Controller());
		settings.add(setCredentials);
		
		setTTSText = new JMenuItem("Set \"Learn More\" Text");
		setTTSText.addActionListener(new DBV_Controller());
		settings.add(setTTSText);
		
		menuBar.add(start);
		menuBar.add(settings);
		
		f.setJMenuBar(menuBar);
		
		f.setLayout(new BorderLayout());
		JPanel infoPanel = new JPanel();
		infoPanel.setBackground(new Color(0, 255, 0, 0));
		infoPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		
		infoButton = new JButton("Learn More");
		infoButton.setFont(new Font("Arial", Font.BOLD, 32));
		infoButton.setPreferredSize(new Dimension(280, 70));
		infoButton.addActionListener(new DBV_Controller());
		infoPanel.add(infoButton);
		f.add(infoPanel, BorderLayout.SOUTH);
		
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
	
	public static char[] passwordCheckWindow() {		
		JPanel panel = new JPanel(new BorderLayout(10, 5));
		
		JPanel controls = new JPanel(new GridLayout(0, 1, 2, 2));
	    controls.setPreferredSize(new Dimension(200, 50));
	    JPasswordField password = new JPasswordField();
	    controls.add(password);
	    panel.add(controls, BorderLayout.CENTER);
	    
    	JOptionPane.showMessageDialog(f, panel, "Set Credentials", JOptionPane.OK_CANCEL_OPTION);

	    return password.getPassword();
	 }
	
	public static void changeCredentialsWindow() {
		Properties p = new Properties();
		OutputStream output = null;
		char[] pass = null;
		
		JPanel panel = new JPanel(new BorderLayout(10, 5));
		
	    JPanel label = new JPanel(new GridLayout(0, 1, 2, 2));
	    label.add(new JLabel("Username", SwingConstants.RIGHT));
	    label.add(new JLabel("Password", SwingConstants.RIGHT));
	    panel.add(label, BorderLayout.WEST);

	    JPanel controls = new JPanel(new GridLayout(0, 1, 2, 2));
	    controls.setPreferredSize(new Dimension(200, 50));
	    JTextField username = new JTextField();
	    controls.add(username);
	    JPasswordField password = new JPasswordField();
	    controls.add(password);
	    panel.add(controls, BorderLayout.CENTER);
	    
    	JOptionPane.showMessageDialog(f, panel, "Set Credentials", JOptionPane.OK_CANCEL_OPTION);

	    try {
	    	output = new FileOutputStream("config.properties");
	    	
	    	p.setProperty("username", username.getText());
	    	pass = password.getPassword();
	    	p.setProperty("password", pass.toString());
	
			p.store(output, null);
		} catch (IOException e) {
			displayAlert("Error", "Dashboard Viewer ran into a problem");
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

	public static void setLearnMoreText() {
		Properties p = new Properties();
		OutputStream output = null;
		InputStream input = null;
		
		JPanel panel = new JPanel(new BorderLayout(10, 5));
		
		JPanel label = new JPanel(new GridLayout(0, 1, 2, 2));
	    label.add(new JLabel("Username", SwingConstants.RIGHT));
	    panel.add(label, BorderLayout.CENTER);
		
		JPanel controls = new JPanel(new GridLayout(0, 1, 2, 2));
	    controls.setPreferredSize(new Dimension(400, 300));
	    JTextArea learnMoreTxt = new JTextArea();
	    learnMoreTxt.setLineWrap(true);
	    try {
			input = new FileInputStream("learnMore.properties");
			p.load(input);
		} catch (IOException e) {
			displayAlert("Error", "Dashboard Viewer ran into a problem");
			e.printStackTrace();
		}
	    learnMoreTxt.setText(p.getProperty("learnMoretxt"));
	    controls.add(learnMoreTxt);
	    
	    panel.add(controls, BorderLayout.CENTER);
	    
	    JOptionPane.showConfirmDialog(f, panel, "Set Text to Speak", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
	    
	    try {
	    	output = new FileOutputStream("learnMore.properties");
	    	p.setProperty("learnMoretxt", learnMoreTxt.getText());
			p.store(output, null);
		} catch (IOException e) {
			displayAlert("Error", "Dashboard Viewer ran into a problem");
			e.printStackTrace();
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					displayAlert("Error", "Dashboard Viewer ran into a problem");
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void displayLearnMoreWindow() {
		Properties p = new Properties();
		InputStream input = null;
		JTextArea txtArea = new JTextArea();
		
		try {
			input = new FileInputStream("learnMore.properties");
			p.load(input);
		} catch (IOException e) {
			displayAlert("Error", "Dashboard Viewer ran into a problem");
			e.printStackTrace();
		}
		// TR: Create the JPanel that displays the learn more window
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(700, 450));
		
		txtArea.setText(p.getProperty("learnMoretxt"));
		txtArea.setEditable(false);
		txtArea.setLineWrap(true);
		txtArea.setPreferredSize(new Dimension(700,450));
		panel.add(txtArea);
		
		JOptionPane.showConfirmDialog(f, panel, "Learn More", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		
	}
	
	public static void displayAlert(String err, String desc) {
		JLabel[] labelArr = {new JLabel(err), new JLabel(desc)};
		JOptionPane.showMessageDialog(f, labelArr);
	}
}

