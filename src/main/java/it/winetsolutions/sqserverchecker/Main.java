package it.winetsolutions.sqserverchecker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;

import org.apache.commons.exec.ExecuteException;
import org.apache.commons.io.IOUtils;


public class Main {

    final static String HOST = "localhost";
    final static int PORT = 9000;

    public static boolean serverListening() {

        HttpURLConnection conn = null;

        try {

            URL url = new URL("http://" + HOST + ":" + PORT + "/api/server");
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("HTTP error code : " + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

            String output = "";
            String line;
            while ((line = br.readLine()) != null) {
                if (line != null) {
                    output += line;
                }
            }

            return output.contains("UP");

        } catch (Exception e) {
            return false;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    public static void main(String[] args) {

        if (serverListening()) {
            System.out.println("Server is already listening");
            return;
        }

        try {
            String[] script = {"cmd.exe", "/c", "sc", "start", "SonarQube"};

            Process process = Runtime.getRuntime().exec(script);

            StringWriter writer = new StringWriter();
            IOUtils.copy(process.getInputStream(), writer, "UTF-8");

            System.out.println(writer.toString());
            System.out.println(process.exitValue());

            while (!serverListening()) {
                Thread.sleep(1000);
            }

        } catch (ExecuteException e) {
            System.out.println("Execute Exception");
        } catch (IOException e) {
            System.out.println("IO Exception");
        } catch (Exception e) {
            System.out.println("Exception");
        }
    }
}