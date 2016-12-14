package router;

public class RoutingNode {

	private String fromAdress;
	private String toAdress;
	private int toPort;

	public RoutingNode(String fromAdress, String toAdress, int toPort) {
		super();
		this.fromAdress = fromAdress;
		this.toAdress = toAdress;
		this.toPort = toPort;
	}

	public String getFromAdress() {
		return fromAdress;
	}

	public String getToAdress() {
		return toAdress;
	}

	public int getToPort() {
		return toPort;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fromAdress == null) ? 0 : fromAdress.hashCode());
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
		if (fromAdress == null) {
			if (other.fromAdress != null)
				return false;
		} else if (!fromAdress.equals(other.fromAdress))
			return false;
		return true;
	}

}
