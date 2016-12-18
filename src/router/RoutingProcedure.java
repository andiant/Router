package router;

import java.io.ByteArrayOutputStream;
import java.net.DatagramSocket;
import java.net.Inet6Address;
import java.util.HashSet;

import givenClasses.ControlPacket;
import givenClasses.IpPacket;
import givenClasses.NetworkLayer;

public class RoutingProcedure implements Runnable {
	
	private IpPacket ipPacket;
	private RoutingTable routingTable;
	private NetworkLayer networkLayer;
	
	public RoutingProcedure(IpPacket ipPacket,RoutingTable routingTable,NetworkLayer networkLayer) {
		this.ipPacket = ipPacket;
		this.routingTable = routingTable;
		this.networkLayer = networkLayer;
	}

	@Override
	public void run() {
		/* Routing-Algorithmus */
		if (ipPacket.getType() == IpPacket.Header.Control) {
			/* Falls emfpangenes Packet ein ControlPacket -> NextHop bestimmen und einfach weiterleiten*/
			assignNextHop(ipPacket);
			networkLayer.sendPacket(ipPacket);
		} else {
			/* Falls empfangenes Packet ein DataPacket */

			// Hop-Limit verringern und pr√ºfen
			ipPacket.setHopLimit(ipPacket.getHopLimit() - 1);
			if (ipPacket.getHopLimit() <= 0) {
				// ControllPacket senden mit Router IP und Packet verwerfen
				// TODO Time Exceeded Nicht sicher ob es richtig ist
				ControlPacket cPacket = new ControlPacket(ControlPacket.Type.TimeExceeded, extractIPHeaderFromIPPacket(ipPacket));
				IpPacket newPacket = new IpPacket((Inet6Address) Inet6Address.getLocalHost(), ipPacket.getSourceAddress(), hopLimit, nextHopIp, nextHopPort);
				
			} else {
				// n√§chstes Ziel gem√§√ü Longest-Prefix-Match bestimmen
				assignNextHop(ipPacket);
				if (ipPacket.getNextHopIp() == null) {
					// ControllPacket senden und Packet verwerfen
					// TODO Destination Unreachable
					ControlPacket cPacket = new ControlPacket(ControlPacket.Type.DestinationUnreachable, payload);
				} else {
					// Packet an n√§chsten Hop weiterleiten
					networkLayer.sendPacket(ipPacket);
				}
			}
		}
	}
	
	/**
	 * extrahiert den Header und gibt ihn als byte array zur¸ck.
	 * Nutzbar f¸r die Controll nachrichten welche nur den Header benˆtigen
	 * @param packet
	 * @return
	 */
	private byte[] extractIPHeaderFromIPPacket(IpPacket packet){
		ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] data = packet.getSourceAddress().getAddress();
        out.write(data, 0, data.length);
        data = packet.getDestinationAddress().getAddress();
        out.write(data, 0, data.length);
        out.write(packet.getHopLimit());
        out.write(packet.getType().ordinal());
        return out.toByteArray();
	}
	
	/**
	 * Longest-Prefix-Match mit routingTable
	 * 
	 * setzt IpPacket.NextHopIP und IpPacket.NextHopPort.
	 * Jeweils null, wenn keins gefunden!
	 */
	private void assignNextHop(IpPacket ipPacket) {
		try {
			routingTable.assignNextHop(ipPacket);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
