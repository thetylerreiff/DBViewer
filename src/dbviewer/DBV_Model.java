// DB Viewer, DBV_Model
// (c) Tyler Reiff 2016


package dbviewer;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.util.Hashtable;
import java.util.Properties;

public class DBV_Model {
	static long lastMoveModel;
	static boolean dashboardRunning = false;
    static Hashtable<String, String> loginInformation;
	private static PrintWriter fileOut;
	static String credXML = "credentials.xml";

	public static void scriptExeLogic() {
		boolean hostIsReachable = isHostReachable();
		 	
		System.out.print("\nDashboard Running: " + dashboardRunning + " \n");	
	    System.out.println("Host is Reachable: " + hostIsReachable);
	    
	    // TR:  compare lastMove time to current time
	 	// TR:  if greater then 30 seconds run login batch file
		if (lastMoveModel == 0 ) {
			return;
		}
		else if (hostIsReachable == false) {
			long b = 30 + (lastMoveModel - System.currentTimeMillis())/1000;
			if(b > 0) {
				System.out.print("Time: " + b + "\n");
			}
			return;
			
		} 
		else if (lastMoveModel > 0 && (lastMoveModel+30000) > System.currentTimeMillis() 
				&& dashboardRunning == true) {
			return;
		} 
		else if (lastMoveModel+30000 <= System.currentTimeMillis() 
				&& dashboardRunning != true && hostIsReachable == true) {
			DBV_Controller.updateOnTop();
			exeLoginScript();
			return;
		}
		else {	
			long a = 30 + (lastMoveModel - System.currentTimeMillis())/1000;
			if(a > 0) {
				System.out.print("Time: " + a + "\n");
			}
			return;
		}
	}
	
	public static boolean isHostReachable() {
		boolean hostIsReachable = false;
		InetAddress byName;
		
		// TR: Try to reach host, 1 sec timeout 
		try {
			byName = InetAddress.getByName("192.168.1.149");
		    hostIsReachable = byName.isReachable(1000);
		} 
		catch (IOException e) {
			hostIsReachable = false;
		}
		return hostIsReachable;
	}
	
	public static void exeLoginScript() {
		dashboardRunning = true;
		Properties p = new Properties();
		InputStream input = null;
		String loginExe = null;
		
		System.out.println("Execute Autologin");
		
		DBV_Controller.updateOnTop();
		try {
			input = new FileInputStream("config.properties");
			p.load(input);
			loginExe = buildLoginVBScript(p.getProperty("username"), p.getProperty("password"));

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			DBV_View.displayAlert("ERROR!", e1.toString());
			e1.printStackTrace();
		}
		
		// write login script
		try {
			fileOut = new PrintWriter("login.vbs");
			fileOut.println(loginExe);
			fileOut.flush();
			fileOut.close();
			
			// TODO TR: run vbscript
			Runtime.getRuntime().exec("wscript login.vbs");
		} catch (IOException e) {
			DBV_View.displayAlert("ERROR!", e.toString());
			e.printStackTrace();
		}
	}

	public static String buildLoginVBScript(String user, String pass) {	
		// TR: build a batch file to execute an autologin on the defult system browser
		String batchFileText =
				"Call Main"  
				+ "\nFunction Main"
				+ "\nSet IE = WScript.CreateObject(\"InternetExplorer.Application\", \"IE_\")"
				+ "\nIE.Visible = True"
				+ "\nIE.Navigate \"http://192.168.1.149/login\""
				+ "\nWait IE"
				+ "\nWith IE.Document"
				+ "\n.getElementByID(\"username\").value = \"" + user + "\"" 
				+ "\n.getElementByID(\"password\").value = \""+ pass +"\""
				+ "\n.getElementByID(\"submitButton\").Click"
				+ "\nEnd With"
				+ "\nEnd Function"
				+ "\n"
				+ "\nSub Wait(IE)"
				+ "\nDo"
				+ "\nWScript.Sleep 500"
				+ "\nLoop While IE.ReadyState < 4 And IE.Busy"
				+ "\nEnd Sub";
		
		return batchFileText;
	}
	
	public static void startInfoSpeech() {
		System.out.print("Info Speech");
	}
	
	public static void appWillHide() {
		dashboardRunning = false;
		String closeBrowserScript = 
				"Dim WshShell, oExec"
				+ "\nSet WshShell = CreateObject(\"WScript.Shell\")"
				+ "\n"
				+ "\nSet oExec = WshShell.Exec(\"taskkill /fi \"\"imagename eq iexplore.exe\"\"\")"
				+ "\n"
				+ "\nDo While oExec.Status = 0"
				+ "\nWScript.Sleep 100"
				+ "\nLoop";
		
		try {
			fileOut = new PrintWriter("closeBrowser.vbs");
			fileOut.println(closeBrowserScript);
			fileOut.flush();
			fileOut.close();
			
			Runtime.getRuntime().exec("wscript closeBrowser.vbs");
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			DBV_View.displayAlert("ERROR!", e.toString());
			e.printStackTrace();
		}
		DBV_Controller.updateOffTop();
	}

	public static void appWillClose() {
		appWillHide();
		System.exit(0);
	}
}

