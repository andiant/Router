package router;

import java.util.HashSet;

public class Router extends Thread {

	private String _configFilePath;
	private int _port;
	private HashSet _rountingTable;
	
	/**
	 * Liest die Konfigurations-Datei und füllt die 
	 * Routing-Tabelle mit entsprechdenen RoutingNode's 
	 */
	
	public Router(HashSet rountingTable) {
		this._rountingTable = rountingTable;
	}
	
	
	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
	}


	/************ STATIC **** METHODS ******************/
	
	
	/**
	 * @return HashSet containing RoutingNode's
	 */
	private static /* TODO */ HashSet readConfigFile() {
		HashSet<RoutingNode> routingTable;
		routingTable = new HashSet<RoutingNode>();
		
		// do some stuff
		
		return routingTable;
	}

	/**
	 * @param [UDP-listening-port] [configfile-path]
	 */
	public static void main(String... args) {
		try {

			int port = Integer.parseInt(args[0]);
			String configFilePath = args[1];

			Router router = new Router();
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
