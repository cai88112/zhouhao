package com.ruoyi.web.controller.tool;


import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author CAI
 * @desc
 * @create 2020-09-07 16:58
 **/
public class ConvertUtil {
		private static final String TAG = "ConvertUtil";

		/**
		 * 十六进制转BCD
		 *
		 * @param data
		 * @return
		 */
		public static int HexToBCD(int data) {
			int temp;
			temp = data / 10 * 16 + data % 10;
			return temp;
		}

		/**
		 * byte数组转16进制字符串
		 *
		 * @param bytes
		 * @param hexSize 字节长度
		 * @return
		 */
		public static String byte2HexStr(byte[] bytes, int hexSize) {
			String hexStr = "";
			if (bytes.length <= hexSize) {
				for (int i = 0; i < hexSize; i++) {
					if (i < bytes.length) {
						hexStr += String.format("%02x", bytes[i]);
					} else {
						hexStr = "00" + hexStr;
					}
				}
			} else {
				for (int i = 0; i < bytes.length; i++) {
					hexStr += String.format("%02x", bytes[i]);
				}
			}
			return hexStr;
		}

		/**
		 * 16进制字符串转byte[]
		 *
		 * @param hexStr
		 * @return
		 */
		public static byte[] hexStr2Bytes(String hexStr) {
			if (hexStr.length() % 2 != 0) {
				hexStr = "0" + hexStr;
			}
			byte[] bytes = new byte[hexStr.length() / 2];
			for (int i = 0; i < hexStr.length() / 2; i++) {
				bytes[i] = (byte) Integer.parseInt(hexStr.substring(2 * i, 2 * i + 2), 16);
			}
			return bytes;
		}

		/**
		 * double  转byte[] 数组
		 *
		 * @param d
		 * @return
		 */
		public static byte[] double2Bytes(double d) {
			long value = Double.doubleToRawLongBits(d);
			byte[] byteRet = new byte[8];
			for (int i = 0; i < 8; i++) {
				byteRet[i] = (byte) ((value >> 8 * i) & 0xff);
			}
			return byteRet;
		}

		/**
		 * byte数组 转double
		 *
		 * @param arr
		 * @return
		 */
		public static double bytes2Double(byte[] arr) {
			long value = 0;

			for (int i = 0; i < 8; i++) {
				value |= ((long) (arr[i] & 0xff)) << (8 * i);
			}
			return Double.longBitsToDouble(value);
		}

		/**
		 * 获取时间BCD码
		 *
		 * @param str
		 * @return
		 */
		public static byte[] getBCD(String str) {
			int len = str.length();
			byte[] dest = new byte[len / 2];
			if (len % 2 != 0) {
				return dest;
			}
			for (int i = 0; i < dest.length; i++) {
				dest[i] = (byte) HexToBCD(Integer.parseInt(str.substring(i * 2, i * 2 + 2)));
			}
			return dest;
		}

		public static String crc16(String adata) {
			String ad = adata.substring(2, 4);
			int[] w = new int[adata.length() / 2];
			for (int i = 0; i < adata.length(); i = i + 2) {
				ad = adata.substring(i, i + 2);
				w[i / 2] = Integer.parseInt(ad, 16);
			}
			int[] da = w;
			int[] stem = new int[da.length + 2];
			int a, b, c;
			a = 0xFFFF;
			b = 0xA001;
			for (int i = 0; i < da.length; i++) {
				a ^= da[i];
				for (int j = 0; j < 8; j++) {
					c = (int) (a & 0x01);
					a >>= 1;
					if (c == 1) {
						a ^= b;
					}
					System.arraycopy(da, 0, stem, 0, da.length);
					stem[stem.length - 2] = (int) (a & 0xFF);
					stem[stem.length - 1] = (int) (a >> 8);
				}
			}
			int[] z = stem;
			StringBuffer s = new StringBuffer();
			// for (int j = 0; j < z.length; j++) {
			s.append(String.format("%02X", z[z.length - 2]));
			s.append(String.format("%02X", z[z.length - 1]));
			// }
			return s.toString();
		}

		public static byte[] crc16(byte[] mybyte) {
			StringBuilder ss = new StringBuilder();
			for (int i = 0; i < mybyte.length; i++) {
				ss.append(String.format("%02X", mybyte[i] & 0xFF));
			}
			String adata = ss.toString();
			String ad = adata.substring(2, 4);
			int[] w = new int[adata.length() / 2];
			for (int i = 0; i < adata.length(); i = i + 2) {
				ad = adata.substring(i, i + 2);
				w[i / 2] = Integer.parseInt(ad, 16);
			}
			int[] da = w;
			int[] stem = new int[da.length + 2];
			int a, b, c;
			a = 0xFFFF;
			b = 0xA001;
			for (int i = 0; i < da.length; i++) {
				a ^= da[i];
				for (int j = 0; j < 8; j++) {
					c = (int) (a & 0x01);
					a >>= 1;
					if (c == 1) {
						a ^= b;
					}
					System.arraycopy(da, 0, stem, 0, da.length);
					stem[stem.length - 2] = (int) (a & 0xFF);
					stem[stem.length - 1] = (int) (a >> 8);
				}
			}
			int[] z = stem;
			StringBuffer s = new StringBuffer();
			s.append(String.format("%02X", z[z.length - 2]));
			s.append(String.format("%02X", z[z.length - 1]));
			byte[] crcbyte = new byte[]{(byte) (z[z.length - 2] & 0xFF), (byte) (z[z.length - 1] & 0xFF)};
			return crcbyte;
		}

		/**
		 * 将十六进制转换为byte
		 *
		 * @param hexStr 16进制字符
		 * @return byte数组
		 */
		public static byte[] parseHexStr2Byte(String hexStr) {
			if (hexStr.length() < 1) {
				return null;
			} else {
				int mode = hexStr.length();
				hexStr = mode % 2 == 0 ? hexStr : "0" + hexStr;
			}
			byte[] result = new byte[hexStr.length() / 2];
			for (int i = 0; i < hexStr.length() / 2; i++) {
				int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
				int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
				result[i] = (byte) (high * 16 + low);
			}
			return result;
		}

		/**
		 * 将byte数组转换成十六进制字符串
		 *
		 * @param buf byte数组
		 * @return 16进制字符串
		 */
		public static String parseByte2HexStr(byte[] buf) {
			StringBuilder sb = new StringBuilder();
			for (byte b : buf) {
				String hexString = Integer.toHexString(b & 0xff);
				if (hexString.length() == 1) {
					hexString = "0" + hexString;
				}
				sb.append(hexString);
			}
			return sb.toString();
		}

		/**
		 * @param me
		 * @param mode
		 * @return 描述：向前补齐十六进制字符串通过提供的mode
		 * @author CBB
		 */
		public static byte[] fromHexStringToByteByMode(String me, int mode) {
			String newsms = me;
			for (int i = 0; i < mode - me.length(); i++) {
				newsms = "0" + newsms;
			}
			return parseHexStr2Byte(newsms);
		}

		/**
		 * @param me
		 * @param mode
		 * @return 描述：向后补齐字符串通过提供的mode
		 * @author CBB
		 */
		public static String fromHexStringBackByMode(String me, int mode) {
			String newsms = me;
			for (int i = 0; i < mode - me.length(); i++) {
				newsms += "0";
			}
			return newsms;
		}

		/**
		 * //获得最少（mode/2）个字节的十六进制字符串，如果不够mode/2个字节（mode位）则前位补0
		 *
		 * @param me
		 * @param mode
		 * @return 描述：向前补齐字符串通过提供的mode
		 * @author CBB
		 */
		public static String fromHexStringByMode(String me, int mode) {
			String newsms = me;
			for (int i = 0; i < mode - me.length(); i++) {
				newsms = "0" + newsms;
			}
			return newsms;
		}

		public static String convertStringToHex(String str) {

			char[] chars = str.toCharArray();

			StringBuffer hex = new StringBuffer();
			for (int i = 0; i < chars.length; i++) {
				hex.append(Integer.toHexString((int) chars[i]));
			}

			return hex.toString();
		}

		public static String convertHexToString(String hex) {

			StringBuilder sb = new StringBuilder();
			StringBuilder temp = new StringBuilder();

			// 49204c6f7665204a617661 split into two characters 49, 20, 4c...
			for (int i = 0; i < hex.length() - 1; i += 2) {

				// grab the hex in pairs
				String output = hex.substring(i, (i + 2));
				// convert hex to decimal
				int decimal = Integer.parseInt(output, 16);
				// convert the decimal to character
				sb.append((char) decimal);

				temp.append(decimal);
			}

			return sb.toString();
		}

		public static String ECBMode(String data) {
			String KEY = "";
			// String KEY = AppApplication.preferenceProvider.getSecretKey();
			int len = data.length();
			String mydata = data;
			int mod = len % 16;
			if (mod != 0) {
				for (int i = 0; i < 16 - mod; i++) {
					if (i == 0) {
						mydata = mydata + "8";
					} else {
						mydata += "0";
					}
				}
			}
			String result = "";
			int flag = 0;
			for (int i = 0; i < mydata.length() / 16; i++) {
				flag = KEY.length() * i;
				for (int j = 0; j < KEY.length() / 2; j++) {
					int need = Integer.parseInt(mydata.substring(flag + j * 2, flag + j * 2 + 2), 16);
					int key = Integer.parseInt(KEY.substring(2 * j, 2 * j + 2), 16);
					int r = (need ^ key);
					String me = Integer.toHexString(r);
					if (r < 16 && r >= 0) {
						me = "0" + me;
					}
					result = result + me;
				}
			}
			return result;
		}

		/**
		 * @param src * @return 描述：组装字节数组
		 * @author CBB
		 */
		public static byte[] sysArrayCopy(byte[]... src) {
			byte[] destArray = null;
			if (src.length > 0) {
				int desLen = 0;
				for (int j = 0; j < src.length; j++) {
					desLen += src[j].length;
				}
				destArray = new byte[desLen];
				int from = 0;
				for (int t = 0; t < src.length; t++) {
					System.arraycopy(src[t], 0, destArray, from, src[t].length);
					from = from + src[t].length;
				}
			}
			return destArray;
		}

		/**
		 * @return 描述：首位数组对调
		 * @author CBB
		 */
		public static byte[] changeOrder(byte[] arr) {
			for (int i = 0; i < arr.length / 2; i++) {
				byte temp = arr[i];
				arr[i] = arr[arr.length - i - 1];
				arr[arr.length - i - 1] = temp;
			}
			return arr;
		}

		/**
		 * @return 数组 描述：首位数组对调 字符串
		 * @author CBB
		 */
		public static String[] changeOrder(String[] arr) {
			for (int i = 0; i < arr.length / 2; i++) {
				String temp = arr[i];
				arr[i] = arr[arr.length - i - 1];
				arr[arr.length - i - 1] = temp;
			}
			return arr;
		}

		/**
		 * @return 字符创 描述：首位数组对调 字符串
		 * @author CBB
		 */
		public static String changeOrderToString(String[] arr) {
			String ss = "";
			for (int i = 0; i < arr.length / 2; i++) {
				String temp = arr[i];
				arr[i] = arr[arr.length - i - 1];
				arr[arr.length - i - 1] = temp;
			}
			for (String string : arr) {
				ss += string;
			}
			return ss;
		}

		/**
		 * @param data
		 * @param mod  精确几位数
		 * @return 描述：描述：给数据加小数点精确
		 * @author fan 保留小数位，不够的补零
		 * @author CBB
		 */
		public static String plusDotCoverZero(long data, int mod) {
			String daString = String.valueOf(data);
			// fan
			if (daString.length() - mod > 0) {
				String begin = daString.substring(0, daString.length() - mod);
				String end = daString.substring(daString.length() - mod, daString.length());
				return begin + "." + end;
			} else {

				int zeroNum = mod - daString.length();
				StringBuffer stringBuffer = new StringBuffer();
				stringBuffer.append("0.");
				for (int i = 0; i < zeroNum; i++) {
					stringBuffer.append("0");
				}
				stringBuffer.append(daString);
				return stringBuffer.toString();
			}
		}

		public static String bcdToStr(byte[] bytes) {
			StringBuffer temp = new StringBuffer(bytes.length * 2);
			for (int i = 0; i < bytes.length; i++) {
				temp.append((byte) ((bytes[i] & 0xf0) >>> 4));
				temp.append((byte) (bytes[i] & 0x0f));
			}

			return temp.toString().substring(0, 1).equalsIgnoreCase("0") ? temp.toString().substring(1) : temp.toString();
		}

		/**
		 * 小数点精确
		 *
		 * @param val   对象
		 * @param scale 精确位数
		 * @param mode  精确模式
		 * @return
		 */
		public static double round(double val, int scale, int mode) {
			BigDecimal bigDecimal = new BigDecimal(val);
			bigDecimal = bigDecimal.setScale(scale, mode);
			double as = bigDecimal.doubleValue();
			bigDecimal = null;
			return as;
		}

		/**
		 * （两字节）补码转化
		 *
		 * @param code
		 * @return
		 */
		public static Integer complementTransfer(int code) {
			String binary = Integer.toBinaryString(code);
			binary = binary.length() < 16 ? ConvertUtil.fromHexStringByMode(binary, 16) : binary;
			String[] binArr = StringUtil.toStringByRadius(binary, 1);
			// 最高位判断 若为1则为负数 若为0则为正数 正数的补码是自身
			String maxBit = binArr[0];
			if (maxBit.equals("0")) {
				return Integer.parseInt(binary, 2);
			}

			// 为负数
			StringBuilder builder = new StringBuilder();
			for (int i = 0; i < binArr.length; i++) {
				// 取反
				if (binArr[i].equals("1")) {
					builder.append("0");
				} else {
					builder.append("1");
				}
			}
			// 反码
			String fanMa = builder.toString();
			// 反码+1
			return -(Integer.parseInt(fanMa, 2) + 1);
		}

		/**
		 * @param src
		 * @return
		 * @author fan 低位在前，高位在后
		 */
		public static String reversal(String src) {
			byte[] bytes = parseHexStr2Byte(src);
			StringBuilder builder = new StringBuilder();
			for (int i = bytes.length - 1; i >= 0; i--) {
				builder.append(String.format("%02X", bytes[i]));
			}
			return builder.toString();
		}

		/**
		 * 、
		 *
		 * @param
		 * @return
		 * @author fan
		 * 服务器的IP地址，四字节HEX,IPV4格式，每个域用一个字节表示，例如125.130.135.140用7D82878C表示；
		 */
		public static String iP2HEX(String str) {
			String[] ipSplites = str.split("\\u002E");
			String ipStr = "";
			for (int i = 0; i < ipSplites.length; i++) {
//			ipStr += Integer.toHexString(Integer.parseInt(ipSplites[i]),1);
				ipStr += ConvertUtil.fromHexStringByMode(Integer.toHexString(Integer.parseInt(ipSplites[i])), 2);
			}
			return ipStr;
		}

		/**
		 * @param bcdDate （6字节BCD），秒分时日月年 转化为 2017-02-10 10:20:36
		 * @author fan
		 * @deprecated
		 */
		public static String dateBcdToString(String bcdDate) {
			StringBuilder reportStartingTimeBuilder = new StringBuilder();
			reportStartingTimeBuilder.append("20" + bcdDate.substring(10, 12)).append("-");
			reportStartingTimeBuilder.append(bcdDate.substring(8, 10)).append("-");
			reportStartingTimeBuilder.append(bcdDate.substring(6, 8)).append(" ");
			reportStartingTimeBuilder.append(bcdDate.substring(4, 6)).append(":");
			reportStartingTimeBuilder.append(bcdDate.substring(2, 4)).append(":");
			reportStartingTimeBuilder.append(bcdDate.substring(0, 2));
			return reportStartingTimeBuilder.toString();
		}

		/**
		 * 深圳水务专用的 日期格式
		 *
		 * @param bcdDate
		 * @return
		 */
		public static String formatShenShuiDate(String bcdDate) {

			StringBuilder reportStartingTimeBuilder = new StringBuilder();
			reportStartingTimeBuilder.append("20" + bcdDate.substring(10, 12));
			reportStartingTimeBuilder.append(bcdDate.substring(8, 10));
			reportStartingTimeBuilder.append(bcdDate.substring(6, 8)).append("T");
			reportStartingTimeBuilder.append(bcdDate.substring(4, 6));
			reportStartingTimeBuilder.append(bcdDate.substring(2, 4));
			reportStartingTimeBuilder.append(bcdDate.substring(0, 2)).append("Z");
			return reportStartingTimeBuilder.toString();

		}

		/**
		 * fan
		 * 将 2017-02-10 10:20:36 的字符串 转化为BCD 字节码 362010100217
		 *
		 * @param timeStr
		 * @return
		 */
		public static String timeStringToBCD(String timeStr) {
			String timeBCDStr = timeStr.replace("-", "").replace(" ", "").replace(":", "");//20{170210102036}
			if (timeBCDStr.length() == 14) {
				timeBCDStr = timeBCDStr.substring(2, timeBCDStr.length());
			}
			String[] tims = ConvertUtil.changeOrder(StringUtil.toStringByRadius(timeBCDStr, 2));
			String timeReverse = "";
			for (String string : tims) {
				timeReverse += string;
			}
			return timeReverse;

		}

		/**
		 * 深圳水务专用 :下发拼接data ，结合timeStringToBCD（）方法使用
		 * 将 20170210T102036Z 的字符串 转化为BCD 字节码 2017-02-10 10:20:36
		 *
		 * @param timeStr
		 * @return
		 */
		public static String shenshuiTimeStringToBCD(String timeStr) {
			timeStr = timeStr.replace("T", "").replace("Z", "");
			SimpleDateFormat sdfsz = new SimpleDateFormat("yyyyMMddHHmmss");
			Date date = null;
			try {
				date = sdfsz.parse(timeStr);
				SimpleDateFormat sdfxt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				return timeStringToBCD(sdfxt.format(date));
			} catch (ParseException e) {
				e.printStackTrace();
				return "EEEEEEEEEEEE";
			}
		}

		/**
		 * @param str
		 * @param length 字符串长度限制值
		 * @return
		 * @author fan
		 * 限制字符串的长度， 如果超过length 则返回 length个“E”的字符串
		 * @deprecated
		 */
		public static String checkStringLimitLength(String str, int length) {
			StringBuffer sBuffer = new StringBuffer();
			if (str.length() > length) {
				for (int i = 0; i < length; i++) {
					sBuffer.append("E");
				}
				return sBuffer.toString();
			} else {
				return str;
			}
		}

		/**
		 * @param in
		 * @param byteLength 字节长度 （1个字节：）
		 * @return
		 * @author fan  int类型 转 byteLength字节的字符串，如果int值超过了 byteLength字节的最大值，则返回 “EE”
		 */
		public static String checkIntegerToHexStr(int in, int byteLength) {
			String hexStr = "";
			if (in < 255 * byteLength) {
				hexStr = String.format("%0" + (2 * byteLength) + "x", in);
			} else {
				for (int i = 0; i < byteLength; i++) {
					hexStr += "EE";
				}
			}
			return hexStr;
		}

	public static void main(String[] args) {
			byte[] b = new byte[]{11,22,33,44,55,66};
		//System.out.println(byte2HexStr(b,2));
		System.out.println(StringUtil.getCurrentTimeString());
		System.out.println(timeStringToBCD(StringUtil.getCurrentTimeString()));
	}

	}
