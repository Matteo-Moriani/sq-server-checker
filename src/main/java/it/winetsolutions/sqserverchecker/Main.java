package it.winetsolutions.sqserverchecker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.apache.commons.exec.ExecuteException;
import org.apache.commons.io.IOUtils;

public class Main {

    final static String PROTOCOL = "http";
    final static String HOST = "localhost";
    final static int PORT = 9000;
    private static Logger logger;

    static {
        System.setProperty("java.util.logging.SimpleFormatter.format", "[%4$-7s] %5$s %n");
        logger = Logger.getLogger(Main.class.getName());
    }

    public static boolean serverListening() {

        HttpURLConnection conn = null;

        try {

            URL url = new URL(PROTOCOL + "://" + HOST + ":" + PORT + "/api/server");
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
            logger.info("Server is already listening");
            return;
        }

        try {
            String[] script = {"cmd.exe", "/c", "sc", "start", "SonarQube"};

            Process process = Runtime.getRuntime().exec(script);

            StringWriter writer = new StringWriter();
            IOUtils.copy(process.getInputStream(), writer, "UTF-8");

            logger.info(writer.toString());

            while (!serverListening()) {
                Thread.sleep(1000);
            }

        } catch (ExecuteException e) {
            logger.log(Level.SEVERE, "Execute Exception", e);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "IO Exception", e);
        } catch (InterruptedException e) {
            logger.log(Level.SEVERE, "Interrupted Exception", e);
        }
    }
}