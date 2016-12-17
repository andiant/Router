package router;

import java.util.HashSet;

import givenClasses.ControlPacket;
import givenClasses.IpPacket;
import givenClasses.NetworkLayer;

public class RoutingProcedure implements Runnable {
	
	private IpPacket ipPacket;
	private HashSet<RoutingNode> routingTable;
	private NetworkLayer networkLayer;
	
	public RoutingProcedure(IpPacket ipPacket,HashSet<RoutingNode> routingTable,NetworkLayer networkLayer) {
		this.ipPacket = ipPacket;
		this.routingTable = routingTable;
		this.networkLayer = networkLayer;
	}

	@Override
	public void run() {
		/* Routing-Algorithmus */
		if (ipPacket.getType() == IpPacket.Header.Control) {
			/* Falls emfpangenes Packet ein ControlPacket */
			
		} else {
			/* Falls empfangenes Packet ein DataPacket */

			// Hop-Limit verringern und prüfen
			ipPacket.setHopLimit(ipPacket.getHopLimit() - 1);
			if (ipPacket.getHopLimit() <= 0) {
				// ControllPacket senden und Packet verwerfen
				// TODO Time Exceeded
				ControlPacket cPacket = new ControlPacket(ControlPacket.Type.TimeExceeded, payload);

			} else {
				// nächstes Ziel gemäß Longest-Prefix-Match bestimmen
				assignNextHop(ipPacket);
				if (ipPacket.getNextHopIp() == null) {
					// ControllPacket senden und Packet verwerfen
					// TODO Destination Unreachable
					ControlPacket cPacket = new ControlPacket(ControlPacket.Type.DestinationUnreachable, payload);
				} else {
					// Packet an nächsten Hop weiterleiten

				}
			}
		}
	}
	
	/**
	 * Longest-Prefix-Match mit routingTable
	 * 
	 * setzt IpPacket.NextHopIP und IpPacket.NextHopPort.
	 * Jeweils null, wenn keins gefunden!
	 */
	private void assignNextHop(IpPacket ipPacket) {
		// TODO Logest-Prefix-Match
	}

}
