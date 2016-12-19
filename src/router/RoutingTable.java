package router;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.Inet6Address;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;

import javax.xml.bind.DatatypeConverter;

import givenClasses.IpPacket;
import utils.Converter;

/**
 * Die Adressen m�ssen in Hex dargestellt werden und es d�rfen keine Verk�rzungsformen benutzt werden
 * z.B. 2001:db8::/48 nicht erlaubt wegen "::" und "db8", richtig w�re die Nullen zu f�llen und "db08" zu schreiben!
 * @author Assiel
 *
 */
public class RoutingTable {
	
	private final int ADRESS_LEN_BITS = 128;
	private Forwarding forwarding;
	
	public RoutingTable(String configFilePath) throws Exception{
		this.forwarding = new Forwarding();
		try {
			BufferedReader in = new BufferedReader(new FileReader(configFilePath));
			String line;
			while((line = in.readLine()) != null){
				String[] arr = line.split(";");
				
				if(arr.length != 3){
					throw new Exception("ConfigFile Syntax Error!");
				}
				
				// arr[0] das Netzwerk
				String[] netAdress= arr[0].split("/");
				if(arr.length != 3){
					throw new Exception("ConfigFile Syntax Error!");
				}
				int bits = Integer.parseInt(netAdress[1]);
				Network network  = new Network(netAdress[0], bits);
				
				// arr[1], arr[2] Adresse an die gesendet werden soll und zugeh�riger port
				byte[] byteAdressDest = Converter.stringHexToByteArray(arr[1]);
				int port = Integer.parseInt(arr[2]);
				Hop hop = new Hop(byteAdressDest, port);
				
				forwarding.put(network, hop);
			}
		} catch (FileNotFoundException e) {
			System.out.println("Could not find '"+configFilePath+"'");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * port und nextHop null setzen wenn keine Zuordnung gefunden!
	 * @param packet
	 * @throws Exception 
	 * @throws UnknownHostException 
	 */
	public void assignNextHop(IpPacket packet) throws UnknownHostException, Exception{
		Hop nextHop = forwarding.getNextHop(packet);
		
		if(nextHop != null){
			byte[] nextHopAdress = nextHop.getAdress();
			Inet6Address newHopIp = (Inet6Address) Inet6Address.getByAddress(nextHopAdress); 
			packet.setNextHopIp(newHopIp);
			packet.setNextPort(nextHop.getPort());
		}else{
			packet.setNextHopIp(null);
		}
	}
}
