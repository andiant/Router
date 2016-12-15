package router;

import java.io.IOException;
import java.net.SocketException;
import java.util.HashSet;

import givenClasses.IpPacket;
import givenClasses.NetworkLayer;

public class Router extends Thread {

	private int port;
	private HashSet<RoutingNode> rountingTable;

	
	/**
	 * @param port
	 * @param rountingTable
	 */
	public Router(int port, HashSet<RoutingNode> rountingTable) {
		this.port = port;
		this.rountingTable = rountingTable;
	}
	
	@Override
	public void run() {
		try {
			// TODO hier der Routingalgorythmus
			NetworkLayer nl;
			nl = new NetworkLayer(port);
			
			IpPacket ipPacket;
			while (!this.isInterrupted()) {
				ipPacket = nl.getPacket();
				
				// Hop-Limit verringern und prüfen
				ipPacket.setHopLimit(ipPacket.getHopLimit() - 1);
				
				if (ipPacket.getHopLimit() <= 0) {
					// ControllPacket senden und Packet verwerfen
					// TODO Time Exceeded = 11
					
				} else {
					// nächstes Ziel gemäß Longest-Prefix-Match bestimmen
					String nextTarget = getNextTarget();
					if (nextTarget == null) {
						// ControllPacket senden und Packet verwerfen
						// TODO Destination Unreachable = 3
						
					} else {
						// Packet an nächsten Hop weiterleiten
						
					}
				}
				
			}
		} catch (SocketException e) {
			System.out.println("Fehler bei initialisierung von networkLayer");
			System.out.println(e.getMessage());
		} catch (IOException e) {
			
		}
		
		
	}
	
	private String getNextTarget() {
		// TODO
		return null;
	}


	/************ STATIC ****************************/
	/**
	 * Liest die Konfigurations-Datei und füllt die 
	 * Routing-Tabelle mit entsprechenden RoutingNode's 
	 *
	 * @return HashSet containing RoutingNode's
	 */
	private static HashSet<RoutingNode> readConfigFile(String configFilePath) {
		HashSet<RoutingNode> routingTable;
		routingTable = new HashSet<RoutingNode>();
		
		// TODO Routing-Tabelle aus Config-Datei füllen
		
		return routingTable;
	}

	/**
	 * @param [UDP-listening-port] [configfile-path]
	 */
	public static void main(String... args) {
		try {

			int port = Integer.parseInt(args[0]);
			String configFilePath = args[1];

			Router router = new Router(port, readConfigFile(configFilePath));
			router.start();
			router.join();
			
		} catch (NumberFormatException e) {
			System.out.println("The port has to be numeric");
			errorMessage(e);
		} catch (IndexOutOfBoundsException e) {
			System.out.println("wrong count of arguments!");
			errorMessage(e);
		} catch (Exception e) {
			System.out.println("wrong arguments");
			errorMessage(e);
		}
		
	}
	
	private static void errorMessage(Exception e) {
		System.out.println("\nrouter [PORT] [PATH_TO_CONFIG_FILE]\n");
		System.out.println(e.getMessage());
	}
}
