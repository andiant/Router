package router;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.Inet6Address;
import java.net.UnknownHostException;
import java.util.HashSet;

import givenClasses.ControlPacket;
import givenClasses.IpPacket;
import givenClasses.NetworkLayer;

public class RoutingProcedure implements Runnable {

	private final int HOP_LIMIT = 6;
	private IpPacket ipPacket;
	private RoutingTable routingTable;
	private NetworkLayer networkLayer;

	public RoutingProcedure(IpPacket ipPacket, RoutingTable routingTable, NetworkLayer networkLayer) {
		this.ipPacket = ipPacket;
		this.routingTable = routingTable;
		this.networkLayer = networkLayer;
	}

	@Override
	public void run() {
		/* Routing-Algorithmus */
		Inet6Address tmpNextHop = ipPacket.getNextHopIp();
		int tmpPort = ipPacket.getNextHopPort();
		/* Falls empfangenes Packet ein DataPacket */

		// Hop-Limit verringern und prüfen
		ipPacket.setHopLimit(ipPacket.getHopLimit() - 1);
		if (ipPacket.getHopLimit() <= 0) {
			// ControllPacket senden mit Router IP und Packet verwerfen
			// TODO Time Exceeded Nicht sicher ob es richtig ist
			ControlPacket cPacket = new ControlPacket(ControlPacket.Type.TimeExceeded,
					extractIPHeaderFromIPPacket(ipPacket));
			Inet6Address routerAdress;
			try {
				routerAdress = (Inet6Address) Inet6Address.getByName("::2");
				IpPacket newPacket = new IpPacket(routerAdress, ipPacket.getSourceAddress(), HOP_LIMIT, tmpNextHop,
						tmpPort);
				newPacket.setControlPayload(cPacket.getBytes());
				networkLayer.sendPacket(newPacket);
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			// nächstes Ziel gemäß Longest-Prefix-Match bestimmen
			assignNextHop(ipPacket);
			if (ipPacket.getNextHopIp() == null) {
				// ControllPacket senden und Packet verwerfen
				try {
					ControlPacket cPacket = new ControlPacket(ControlPacket.Type.DestinationUnreachable,
							extractIPHeaderFromIPPacket(ipPacket));
					Inet6Address routerAdress = (Inet6Address) Inet6Address.getByName("::2");
					IpPacket newPacket = new IpPacket(routerAdress, ipPacket.getSourceAddress(), HOP_LIMIT,
							tmpNextHop, tmpPort);
					newPacket.setControlPayload(cPacket.getBytes());
					networkLayer.sendPacket(newPacket);
				} catch (UnknownHostException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				// Packet an nächsten Hop weiterleiten
				try {
					System.out.println("Sending to IP: " + ipPacket.getNextHopIp() + ":" + ipPacket.getNextHopPort());
					networkLayer.sendPacket(ipPacket);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * extrahiert den Header und gibt ihn als byte array zur�ck. Nutzbar f�r die
	 * Controll nachrichten welche nur den Header ben�tigen
	 * 
	 * @param packet
	 * @return
	 */
	private byte[] extractIPHeaderFromIPPacket(IpPacket packet) {
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
	 * setzt IpPacket.NextHopIP und IpPacket.NextHopPort. Jeweils null, wenn
	 * keins gefunden!
	 */
	private void assignNextHop(IpPacket ipPacket) {
		try {
			routingTable.assignNextHop(ipPacket);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
