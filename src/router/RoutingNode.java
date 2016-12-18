package router;

import java.net.Inet6Address;

public class RoutingNode {

	private byte[] targetNetwork;
	private byte[] targetAdress;
	private int targetPort;

	public RoutingNode(byte[] targetNetwork, byte[] targetAdress, int targetPort) {
		super();
		this.targetNetwork = targetNetwork;
		this.targetAdress = targetAdress;
		this.targetPort = targetPort;
	}

	public byte[] getTargetNetwork() {
		return targetNetwork;
	}

	public byte[] getTargetAdress() {
		return targetAdress;
	}

	public int getTargetPort() {
		return targetPort;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((targetNetwork == null) ? 0 : targetNetwork.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		RoutingNode other = (RoutingNode) obj;
		if (targetNetwork == null) {
			if (other.targetNetwork != null)
				return false;
		} else if (!targetNetwork.equals(other.targetNetwork))
			return false;
		return true;
	}

}
