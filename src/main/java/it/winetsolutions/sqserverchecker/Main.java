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
			    String[] script = {"cmd.exe", "/c", "sc", "start", "SonarQube"};
			    
			    Process process = Runtime.getRuntime().exec(script);
			    
			    StringWriter writer = new StringWriter();
			    IOUtils.copy(process.getInputStream(), writer, "UTF-8");
			    System.out.println(writer.toString());
			    System.out.println(process.exitValue());
			    
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
