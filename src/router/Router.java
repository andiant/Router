package router;

import java.util.HashSet;

public class Router extends Thread {

	private int port;
	private HashSet<RoutingNode> rountingTable;

	
	/**
	 * @param port
	 * @param rountingTable
	 */
	public Router(int port, HashSet rountingTable) {
		this.port = port;
		this.rountingTable = rountingTable;
	}
	
	@Override
	public void run() {
		// TODO hier der Routingalgorythmus
		
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
