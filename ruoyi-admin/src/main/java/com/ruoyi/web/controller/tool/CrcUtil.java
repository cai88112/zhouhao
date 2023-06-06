package com.ruoyi.web.controller.tool;

/**
 * @author CAI
 * @desc
 * @create 2020-09-08 09:03
 **/
public class CrcUtil {

	public static int crc16Byte(byte[] buf) {
		int crc = 0x0000;
		short c, i;
		int mindex = 0;
		int len = buf.length;
		while (len != 0) {
			c = buf[mindex++];
			for (i = 0; i < 8; i++) {
				if (((crc ^ c) & 1) > 0)
					crc = (crc >> 1) ^ 0xa001;
				else
					crc >>= 1;
				c >>= 1;
			}
			len--;
		}
		return crc;
	}

	/**
	 * crc校验  结果高低位反转
	 * @param frameHexStr
	 * @return
	 */
	public static String crc16StringHex(String frameHexStr) {
		String crcHex;
		try {
			if (frameHexStr.length() % 2 != 0) {
				System.out.println("crc16StringHex() returned: " + "CrcUtil帧数据有误");
				return null;
			}
			byte[] bytes = new byte[frameHexStr.length()/2];
			for (int i = 0; i < frameHexStr.length()/2; i++) {
				bytes[i] = (byte) Integer.parseInt(frameHexStr.substring(i*2,i*2+2),16);
			}
			int crc = crc16Byte(bytes);
			String hexString = String.format("%04x",crc);
			crcHex = hexString.substring(2,4) + hexString.substring(0,2);
		}catch (Exception e) {
			crcHex = "0000";
		}

		return crcHex;
	}
}
