package router;

import java.util.Arrays;

public class Hop {

	private byte[] adress;		//16 byte Ipv6 Adresse
	private int port;

	public Hop(byte[] adress, int port) {
		super();
		this.adress = adress;
		this.port = port;
	}

	public byte[] getAdress() {
		return adress;
	}

	public int getPort() {
		return port;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(adress);
		result = prime * result + port;
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
		Hop other = (Hop) obj;
		if (!Arrays.equals(adress, other.adress))
			return false;
		if (port != other.port)
			return false;
		return true;
	}
	
	

}
