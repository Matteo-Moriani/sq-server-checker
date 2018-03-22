package it.winetsolutions.sqserverchecker;

import java.io.IOException;
import java.io.StringWriter;
import java.net.Socket;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.io.IOUtils;


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
			
			try {    
			    // start
			    String[] script = {"cmd.exe", "/c", "sc", "start", "SonarQube"};
			    // stop
			    // String[] script = {"cmd.exe", "/c", "sc", "stop", "SonarQube"};
			    // check
			    //String[] script = {"cmd.exe", "/c", "sc", "query", "SonarQube", "|", "find", "/C", "\"RUNNING\""};
			    
			    Process process = Runtime.getRuntime().exec(script);
			    
			    StringWriter writer = new StringWriter();
			    IOUtils.copy(process.getInputStream(), writer, "UTF-8");
			    System.out.println(writer.toString());
			    System.out.println(process.exitValue());
			
//			String path = "C:/sonarqube-6.7.2/bin/windows-x86-64/StartSonar.bat";
////			CommandLine commandLine = CommandLine.parse(path);
////			DefaultExecutor executor = new DefaultExecutor();
////			executor.setExitValue(1);
//			try {
//				System.out.println("I'm trying to start "+path);
//				Runtime.getRuntime().exec("cmd /c start "+path);
////				int exitValue = executor.execute(commandLine);
////				System.out.println("Executed. Exit value: "+exitValue);
			} catch (ExecuteException e) {
				System.out.println("Execute Exception");
			} catch (IOException e) {
				System.out.println("IO Exception");
			}
		} else {
			System.out.println("Server is already listening");
		}
		
	}
}
