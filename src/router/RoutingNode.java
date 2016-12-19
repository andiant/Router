package router;

import java.net.Inet6Address;

public class RoutingNode {

	private Network targetNetwork;
	private Hop targetHop;

	public RoutingNode(Network targetNetwork, Hop targetHop) {
		super();
		this.targetNetwork = targetNetwork;
		this.targetHop = targetHop;
	}

	public Network getTargetNetwork() {
		return targetNetwork;
	}

	public Hop getTargetHop() {
		return targetHop;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((targetHop == null) ? 0 : targetHop.hashCode());
		result = prime * result + ((targetNetwork == null) ? 0 : targetNetwork.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RoutingNode other = (RoutingNode) obj;
		if (targetHop == null) {
			if (other.targetHop != null)
				return false;
		} else if (!targetHop.equals(other.targetHop))
			return false;
		if (targetNetwork == null) {
			if (other.targetNetwork != null)
				return false;
		} else if (!targetNetwork.equals(other.targetNetwork))
			return false;
		return true;
	}

}
