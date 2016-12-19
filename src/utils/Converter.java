package utils;

import java.io.IOException;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import givenClasses.IpPacket;

public class Converter {

	private final static int IPv6_HEX_BYTES = 8;
	private final static int BYTE_COUNT_ADRESS = 16;
	private final static int BITS = 128;

	private static int hex2decimal(String s) {
		String digits = "0123456789ABCDEF";
		s = s.toUpperCase();
		int val = 0;
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			int d = digits.indexOf(c);
			val = 16 * val + d;
		}
		return val;
	}

	/**
	 * hex Adress MUSS in der LONGEST Form sein!!! hex ist in der Form
	 * '2001:db08:0000:0000:0000:0000:0000:0000'
	 * 
	 * @param hex
	 * @return
	 * @throws Exception
	 */
	public static byte[] stringHexToByteArray(String hex) throws Exception {
		String[] hexAdress = hex.split(":");

		if (hexAdress.length != IPv6_HEX_BYTES) {
			throw new Exception("Adress hast to be 16 byte HEX!");
		}
		byte[] byteAdress = new byte[BYTE_COUNT_ADRESS];
		// Convert the HEX in byteS (decimal)
		byte index = 0;
		for (String s : hexAdress) {
			byteAdress[index++] = (byte) Converter.hex2decimal(s.substring(0, 2));
			byteAdress[index++] = (byte) Converter.hex2decimal(s.substring(2, 4));
		}

		return byteAdress;
	}

	
	/**
	 * Convertiert eine 16 byte IPv6 Adresse in seine Binäre Form als String
	 * @param adress
	 * @return
	 */
	public static String convertByteArrayToBinaryString(byte[] adress) {
		String binary = "";
		for (byte b : adress) {
			int x =  b & 0xFF;
			String bString = Integer.toBinaryString(x);
			int countZerosToFillBits = (8 - bString.length());
			for (int i = 0; i < countZerosToFillBits; i++) {
				bString = "0" + bString;
			}

			binary += bString;
		}
		return binary;
	}

	
	/**
	 * Convertiert eine in Hex dargestellte IPv6 Adreese in seine binäre Form als String 
	 * @param hex
	 * @return
	 * @throws Exception
	 */
	public static String convertHexAdressToBinaryString(String hex) throws Exception {
		String binary = "";
		String[] hexArr = hex.split(":");
		if (hexArr.length != IPv6_HEX_BYTES) {
			throw new Exception("WRONG HEX ADRESS!!");
		}

		for (String s : hexArr) {
			String firstByte = s.substring(0, 2);
			String secondByte = s.substring(2, 4);

			String FirstBinAddr = Integer.toBinaryString(Integer.parseInt(firstByte, 16));
			String SecondBinAddr = Integer.toBinaryString(Integer.parseInt(secondByte, 16));

			int countZerosToFillBits = (8 - FirstBinAddr.length());
			for (int i = 0; i < countZerosToFillBits; i++) {
				FirstBinAddr = "0" + FirstBinAddr;
			}

			countZerosToFillBits = (8 - SecondBinAddr.length());
			for (int i = 0; i < countZerosToFillBits; i++) {
				SecondBinAddr = "0" + SecondBinAddr;
			}

			binary += FirstBinAddr + SecondBinAddr;
		}
		return binary;
	}

}
