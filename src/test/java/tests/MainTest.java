package tests;

import static org.junit.Assert.*;
import org.junit.Test;
import it.winetsolutions.sqserverchecker.Main;


public class MainTest {

	@Test
	public void serverListeningTest() {
		assertEquals(true,Main.serverListening("localhost", 9000));
		assertEquals(false,Main.serverListening("localhost", 9999));
	}

}
