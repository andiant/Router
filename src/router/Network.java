package router;

public class Network {
	
	private String networkAdress;		// Hex String LONGEST
	private int bits;

	public Network(String networkAdress, int bits) {
		super();
		this.networkAdress = networkAdress;
		this.bits = bits;
	}

	public String getNetworkAdress() {
		return networkAdress;
	}

	public int getBits() {
		return bits;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + bits;
		result = prime * result + ((networkAdress == null) ? 0 : networkAdress.hashCode());
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
		Network other = (Network) obj;
		if (bits != other.bits)
			return false;
		if (networkAdress == null) {
			if (other.networkAdress != null)
				return false;
		} else if (!networkAdress.equals(other.networkAdress))
			return false;
		return true;
	}
	
	
}
