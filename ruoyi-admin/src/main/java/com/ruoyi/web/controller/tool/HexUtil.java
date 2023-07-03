package com.ruoyi.web.controller.tool;

import java.math.BigInteger;

/**
 * @author CAI
 * @desc
 * @create 2020-09-07 10:54
 **/
public class HexUtil {
	/**
	 * 对double 类型 保留point位小数
	 *
	 * @param d
	 * @param point
	 * @return
	 */
	public static String formatDouble(double d, int point) {
		String df = String.format("%." + point + "f", d);
		return df;
	}

	/**
	 * 字符串长度补齐length位, 如果不够 前位补0
	 *
	 * @param str
	 * @param length
	 * @return
	 */
	public static String coverStrFromFirst(String str, int length) {
		int count = length - str.length();
		for (int i = 0; i < count; i++) {
			str = "0" + str;
		}
		return str;
	}

	/**
	 * 303132 ----> 023
	 *
	 * @param s
	 * @return
	 */
	public static String ASCII2Str(String s) {
		byte[] baKeyword = new byte[s.length() / 2];
		for (int i = 0; i < baKeyword.length; i++) {
			try {
				baKeyword[i] = (byte) (0xff & Integer.parseInt(s.substring(i * 2, i * 2 + 2), 16));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		try {
			s = new String(baKeyword, "ASCII");
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return s;
	}

	/**
	 * (123,4) --> 30 313233(长度不足 前位补30)
	 * (2,5) -->  3030303032
	 *
	 * @param str
	 * @param length
	 * @return
	 */
	public static String str2ASCII(String str, int length) {
		int res = length - str.length();
		if (res > 0) {
			for (int i = 0; i < res; i++) {
				str = "0" + str;
			}
		}
		char[] chars = str.toCharArray();

		StringBuffer hex = new StringBuffer();
		for (int i = 0; i < length; i++) {
			hex.append(Integer.toHexString((int) chars[i]));
		}
		return hex.toString();
	}

	/**
	 * 字符串转换成为16进制(无需Unicode编码)  可以将汉字转码成16进制
	 * @param str
	 * @return
	 */
	public static String str2HexStr(String str) {
		char[] chars = "0123456789ABCDEF".toCharArray();
		StringBuilder sb = new StringBuilder("");
		byte[] bs = str.getBytes();
		int bit;
		for (int i = 0; i < bs.length; i++) {
			bit = (bs[i] & 0x0f0) >> 4;
			sb.append(chars[bit]);
			bit = bs[i] & 0x0f;
			sb.append(chars[bit]);
			// sb.append(' ');
		}
		return sb.toString().trim();
	}

	/**
	 * 16进制直接转换成为字符串(无需Unicode解码)  可以将16进制转码成汉字
	 * @param hexStr
	 * @return
	 */
	public static String hexStr2Str(String hexStr) {
		String str = "0123456789ABCDEF";
		char[] hexs = hexStr.toCharArray();
		byte[] bytes = new byte[hexStr.length() / 2];
		int n;
		for (int i = 0; i < bytes.length; i++) {
			n = str.indexOf(hexs[2 * i]) * 16;
			n += str.indexOf(hexs[2 * i + 1]);
			bytes[i] = (byte) (n & 0xff);
		}
		return new String(bytes);
	}


	/**
	 * double 转16进制
	 *
	 * @param dou
	 * @param hexSize 字节长度
	 * @return
	 */
	public static String double2Hex(double dou, int hexSize) {
		String hex = Long.toHexString(Double.doubleToRawLongBits(dou));
		if (hex.length() % 2 != 0) {
			hex = "0" + hex;
		}
		int lastLength = hexSize - hex.length() / 2;

		for (int i = 0; i < lastLength; i++) {
			hex = "" + hex;
		}
		return hex;
	}

	/**
	 * int 转16进制
	 *
	 * @param in
	 * @param hexSize 字节长度
	 * @return
	 */
	public static String int2Hex(int in, int hexSize) {
		String hex = Integer.toHexString(in);
		if (hex.length() % 2 != 0) {
			hex = "0" + hex;
		}
		int lastLength = hexSize - hex.length() / 2;
		for (int i = 0; i < lastLength; i++) {
			hex = "00" + hex;
		}
		return hex;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println(rsData("test1"));
		System.out.println(str2HexStr("test1").length());
		System.out.println(0x66>>2);
		System.out.println(hexStr2Str("0003"));
		System.out.println(Integer.toBinaryString(Integer.valueOf("01", 16)));
		System.out.println(int2Hex(3,2).toUpperCase());
		System.out.println(getValidData("0003","540101"));
		System.out.println(validData("FA0008302020092112575207"));
		System.out.println(int2Hex(101,1));
		System.out.println(int2Hex(111222,6));
		System.out.println(Integer.parseInt("01"));
		System.out.println(hexToBiary("00000000"));
		System.out.println(int2Hex(-3 *10000,4));
	}

	/**
	 * 16进制字符串转int
	 *
	 * @param hex
	 * @return
	 */
	public static int hex2Int(String hex) {
		if (hex.length() > 8) {
			System.out.println("HexUtils hex2Int: 报错了-----------");
			return 0;
		}
		return Integer.valueOf(hex, 16);
	}
	public static int hexToDecimal(String hex) {
		BigInteger bi = new BigInteger(hex, 16);
		return bi.intValue();
	}


	public void test() {
	}

	public static final String bytesToHexString(byte[] bArray) {
		StringBuffer sb = new StringBuffer(bArray.length);
		String sTemp;
		for (int i = 0; i < bArray.length; i++) {
			sTemp = Integer.toHexString(0xFF & bArray[i]);
			if (sTemp.length() < 2) {
				sb.append(0);
			}
			sb.append(sTemp.toUpperCase());
		}
		return sb.toString();
	}
	public static final String byteToHexString(byte b) {
		StringBuffer sb = new StringBuffer();
		String sTemp = Integer.toHexString(0xFF & b);
		if (sTemp.length() < 2) {
			sb.append(0);
		}
		sb.append(sTemp.toUpperCase());
		return sb.toString();
	}
	public static final boolean validData(String hexStr){
		String length = hexStr.substring(2,6);
		String validData = hexStr.substring(hexStr.length()-2);
		try {
			String calData = getValidData(length, hexStr.substring(6, hexStr.length() - 2));
			if (validData.equals(calData)) {
				return true;
			}
			return false;
		}catch (Exception e){
			return false;
		}
	}
	public static final String getValidData(String hexLength,String hexStr){
		String code = "";
		int length = hex2Int(hexLength);
		int index = 0;
		for(int i=0;i<=length;i++){
			if(i == 0) {
				code = xor(hexLength.substring(0,2),hexLength.substring(2,4));
			}else {
				code = xor(code,hexStr.substring(index,index+2));
				index += 2;
			}
		}
		return code.toUpperCase();
	}
	private static String xor(String strHex_X,String strHex_Y) {
		//将x、y转成二进制形式
		String anotherBinary = Integer.toBinaryString(Integer.valueOf(strHex_X, 16));
		String thisBinary = Integer.toBinaryString(Integer.valueOf(strHex_Y, 16));
		String result = "";
		//判断是否为8位二进制，否则左补零
		if (anotherBinary.length() != 8) {
			for (int i = anotherBinary.length(); i < 8; i++) {
				anotherBinary = "0" + anotherBinary;
			}
		}
		if (thisBinary.length() != 8) {
			for (int i = thisBinary.length(); i < 8; i++) {
				thisBinary = "0" + thisBinary;
			}
		}
		//异或运算
		for (int i = 0; i < anotherBinary.length(); i++) {
			//如果相同位置数相同，则补0，否则补1
			if (thisBinary.charAt(i) == anotherBinary.charAt(i)) {
				result += "0";
			}else {
				result += "1";
			}
		}
		return int2Hex(Integer.parseInt(result, 2),1);
	}
	public static String hexToBiary(String hex){
		StringBuilder stringBuilder = new StringBuilder();
		int snum = 0;
		int endnum = 2;
		for(int i=0;i<4;i++) {
			String b = hex.substring(snum,endnum);
			String binary = Integer.toBinaryString(Integer.valueOf(b, 16));
			if (binary.length() != 8) {
				for (int j = binary.length(); j < 8; j++) {
					binary = "0" + binary;
				}
			}
			stringBuilder.append(binary);
			snum+=2;
			endnum+=2;
		}

		return stringBuilder.toString();
	}

	public static String rsData(String data){
		int i =0;
		StringBuilder rsData = new StringBuilder();
		if(null != data){
			i = data.length();
			rsData.append(data);
		}
		for(int j = i;j<80;j++){
			rsData.append("0");
		}
		return rsData.toString();
	}
}
