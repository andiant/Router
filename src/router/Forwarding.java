package router;

import java.net.Inet6Address;
import java.util.ArrayList;
import java.util.List;

import givenClasses.IpPacket;
import utils.Converter;

/**
 * intern eine liste welche sortiert ist nach höheren Präfix!!
 * 
 * @author Assiel
 *
 */
public class Forwarding {

	private List<RoutingNode> data;

	public Forwarding() {
		data = new ArrayList<>();
	}

	/**
	 * fügt das Paar der Liste hinzu und sortiert absteigender nach der Länge des Prefix.
	 * @param network
	 * @param hop
	 */
	public void put(Network network, Hop hop) {
		RoutingNode newNode = new RoutingNode(network, hop);
		if (data.isEmpty()) {
			data.add(newNode);
			return;
		}

		for (int i = 0; i < data.size(); i++) {
			int actualPrefix = data.get(i).getTargetNetwork().getBits();
			if (network.getBits() > actualPrefix) {
				data.add(i, newNode);
				return;
			}
			data.add(newNode);
		}
	}

	public Hop getNextHop(IpPacket packet) {
		// Convert adress to binary
		String srcBinary = Converter.convertByteArrayToBinaryString(packet.getDestinationAddress().getAddress());
		try{
			for(RoutingNode node : data){
				String netBinary = Converter.convertHexAdressToBinaryString(node.getTargetNetwork().getNetworkAdress());
				if(match(srcBinary, netBinary, node.getTargetNetwork().getBits())){
					return node.getTargetHop();
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}

		return null;
	}

	private boolean match(String srcBinary, String netBinary, int bits) {
		if (srcBinary.startsWith(netBinary.substring(0, bits))) {
			return true;
		}
		return false;
	}

}
