package it.winetsolutions.sqserverchecker;

import java.io.IOException;
import java.net.Socket;
import org.apache.commons.exec.ExecuteException;


public class Main {
	final static String HOST = "localhost";
    final static int PORT = 9000;

    
	public static boolean serverListening(String host, int port){
	    try {
	        Socket s = new Socket(host, port);
	        s.close();
	        return true;
	    } catch (Exception e){
	        return false;
	    }
	}	
	
	public static void main(String[] args) {
		
		if (!serverListening(HOST,PORT)) {
			String path = "C:/sonarqube-6.7.2/bin/windows-x86-64/StartSonar.bat";
//			CommandLine commandLine = CommandLine.parse(path);
//			DefaultExecutor executor = new DefaultExecutor();
//			executor.setExitValue(1);
			try {
				Runtime.getRuntime().exec("cmd /c start "+path);
//				int exitValue = executor.execute(commandLine);
//				System.out.println("Executed. Exit value: "+exitValue);
			} catch (ExecuteException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Server is listening");
		}
		
	}
}
