package router;

import java.io.IOException;
import java.net.SocketException;
import java.util.HashSet;

import givenClasses.IpPacket;
import givenClasses.NetworkLayer;

public class Router extends Thread {
	
	public HashSet<RoutingNode> routingTable;
	public NetworkLayer networkLayer;
	
	/**
	 * @param port
	 * @param rountingTable
	 */
	private Router(int port, HashSet<RoutingNode> rountingTable) {
		this.routingTable = rountingTable;
		try {
			networkLayer = new NetworkLayer(port);
		} catch (SocketException e) {
			System.out.println("Fehler bei initialisierung von networkLayer");
			System.out.println(e.getMessage());
		}
	}

	@Override
	public void run() {
		IpPacket ipPacket;
		while (!this.isInterrupted()) {
			try {
				// Auf eingehende Packet warten
				ipPacket = networkLayer.getPacket();
				if (ipPacket != null) {
					// Wenn Packet eingegangen, dann routingThread für dieses Packet starten
					Thread routingThread = new Thread(new RoutingProcedure(ipPacket,routingTable,networkLayer));
					routingThread.start();
				}
			} catch (IOException e) {
				e.printStackTrace();
				this.interrupt();
			}

		}
	}


	/************ STATIC ****************************/
	/**
	 * Liest die Konfigurations-Datei und füllt die Routing-Tabelle mit
	 * entsprechenden RoutingNode's
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
