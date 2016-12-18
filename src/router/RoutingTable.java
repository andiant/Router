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
 * Die Adressen müssen in Hex dargestellt werden und es dürfen keine Verkürzungsformen benutzt werden
 * z.B. 2001:db8::/48 nicht erlaubt wegen "::" und "db8", richtig wäre die Nullen zu füllen und "db08" zu schreiben!
 * @author Assiel
 *
 */
public class RoutingTable {
	
	private final int ADRESS_LEN_BITS = 128;
	private HashMap<Network, Hop> forwarding;
	
	public RoutingTable(String configFilePath) throws Exception{
		this.forwarding = new HashMap<>();
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
				
				// arr[1], arr[2] Adresse an die gesendet werden soll und zugehöriger port
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
		//Convert adress to binary
		String srcBinary = Converter.convertByteArrayToBinaryString(packet.getDestinationAddress().getAddress());
		int longestMatchingBits = 0;
		Network longestMatchingNetwork = null;		// dummy Network so muss eine Schicht drüber geprüft werden ob die IP ungleich null ist
		
		for(Network network : forwarding.keySet()){
			try {
				// prüfe ob das Netz in Frage kommt
				String netBinary = Converter.convertHexAdressToBinaryString(network.getNetworkAdress());
				if(match(srcBinary, netBinary, network.getBits())){
					
					// Prüfe wieviel Bits übereinstimmen
					int i;
					for(i = 0; i < ADRESS_LEN_BITS; i++){
						if(srcBinary.charAt(i) != netBinary.charAt(i)){
							break;
						}
					}
					
					if(i > longestMatchingBits){
						longestMatchingBits = i;
						longestMatchingNetwork = network;
					}
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if(longestMatchingNetwork != null){
			Hop nextHop = forwarding.get(longestMatchingNetwork);
			byte[] nextHopAdress = nextHop.getAdress();
			Inet6Address newHopIp = (Inet6Address) Inet6Address.getByAddress(nextHopAdress); 
			packet.setNextHopIp(newHopIp);
			packet.setNextPort(nextHop.getPort());
		}else{
			packet.setNextHopIp(null);
		}
	}
	
	private boolean match(String srcBinary, String netBinary, int bits) {
		if(srcBinary.startsWith(netBinary.substring(0, bits))){
			return true;
		}
		return false;
	}

	public static void main(String[] args) throws Exception {
		RoutingTable t = new RoutingTable("configtest.txt");
	}
	
	

}
