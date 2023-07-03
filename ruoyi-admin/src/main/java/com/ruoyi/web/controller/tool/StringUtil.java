package com.ruoyi.web.controller.tool;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author CAI
 * @desc
 * @create 2020-09-07 17:00
 **/
public class StringUtil {
	/**
	 * 生成订单编号
	 * @return
	 */
	public static String createOrderNum(){
		int hashCodeV = UUID.randomUUID().toString().hashCode();
		if(hashCodeV < 0) {//有可能是负数
			hashCodeV = - hashCodeV;
		}
		// 0 代表前面补充0
		// 10 代表长度为10
		// d 代表参数为正数型
		String endStr = String.format("%010d", hashCodeV);
		String startStr = String.valueOf((int)((Math.random()*9+1)*10000));
		return   startStr+endStr;
	}

	private static final String TAG = "StringUtil";

	/**
	 * byte 数组  转 16进制字符串
	 *
	 * @param src
	 * @return
	 */
	public static String bytesToHexString(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();
	}

	/**
	 * 16进制字符串 转byte 数组
	 * @param hexStrs FA000830202009211257527
	 *                FA000830202009211259045F
	 * @return
	 */
	public static byte[] hexStrTobytes(String hexStrs) {
		byte[] bytes = new byte[hexStrs.length()/2];
		if (hexStrs.length() % 2 != 0){
			//Log.d(TAG, "hexStrTobytes() returned: " + "命令帧错误");
			return bytes;
		}

		for (int i = 0; i < hexStrs.length()/2; i++) {
			bytes[i] = (byte) Integer.parseInt(hexStrs.substring(i*2,i*2+2),16);
		}
		return bytes;
	}

	/**
	 * crc 校验
	 *
	 * @param hexs
	 * @return
	 */
	public static int crcStr(byte[] hexs) {
		int len = hexs.length;
		int crc = 0x0000;
		short c, i;
		int mindex = 0;
		while (len != 0) {
			c = hexs[mindex++];
			for (i = 0; i < 8; i++) {
				if (((crc ^ c) & 1) > 0) {
					crc = (crc >> 1) ^ 0xa001;
				}else {
					crc >>= 1;
				}
				c >>= 1;
			}
			len--;

		}
		return crc;
	}

	/*
	 * @param bs        要转换的字节数组
	 * @param isReverse 是否遵循高字节在前，低字节在后；为false则按正常顺序输出
	 * @return
	 */
	public static String getStringByByteArray(byte[] bs, boolean isReverse) {
		if (bs == null) {
			return null;
		}
		String s = "";
		if (isReverse) {
			for (int i = bs.length - 1; i >= 0; i--) {
				s += String.format("%02X", bs[i] & 0xff);
			}
		} else {
			for (int i = 0; i < bs.length; i++) {
				s += String.format("%02X", bs[i] & 0xff);
			}
		}
		return s;
	}

	/**
	 * @param s         要转换的字节数组
	 * @param isReverse 是否遵循高字节在前，低字节在后；为false则按正常顺序输出
	 */
	public static byte[] getByteArrayByString(String s, boolean isReverse) {
		if (s == null || s.length() % 2 != 0) {
			return null;
		}

		byte[] bs = new byte[s.length() / 2];
		if (isReverse) {
			for (int i = 0; i < bs.length; i++) {
				bs[bs.length - 1 - i] = (byte) Integer.parseInt(s.substring(i * 2, i * 2 + 2), 16);
			}
		} else {
			for (int i = 0; i < bs.length; i++) {
				bs[i] = (byte) Integer.parseInt(s.substring(i * 2, i * 2 + 2), 16);
			}
		}
		return bs;
	}

	/**
	 * @return 将byte转化为String
	 */
	public static String getStringByByte(byte b) {
		return String.format("%02X", b & 0xff);
	}

	/**
	 * @param s 字符串
	 * @return 将两个字符的String转化为byte
	 */
	public static byte getByteByString(String s) {
		if (s.length() != 2) {
			return 0x00;
		} else {
			return (byte) Integer.parseInt(s, 16);
		}
	}

	/**
	 * @return 模仿string的subString方法，对字节数组进行截取
	 */
	public static byte[] subByteArray(byte[] array, int start, int end) {
		if (start > end || start > array.length - 1 || start < 0) {
			throw new RuntimeException("subByteArray方法出错");
		}
		if (end > array.length) {
			end = array.length;
		}
		int count = end - start;
		if (count == 0) {
			return new byte[0];
		} else {
			byte[] result = new byte[count];
			System.arraycopy(array, start, result, 0, result.length);
			return result;
		}
	}

	/**
	 * @return 计算crc16值, 返回两个字节，低字节在前
	 */
	public static byte[] getCRC16(byte[] array) {
		if (array == null) throw new RuntimeException("array不能为null");
		int len = array.length;
		int crc = 0xFFFF;
		int offset = 0;
		while (len-- > 0) {
			crc ^= (array[offset++] & 0xFF);
			for (int i = 0; i < 8; i++) {
				if ((crc & 0x0001) != 0) {
					crc >>= 1;
					crc ^= 0xA001;
				} else {
					crc >>= 1;
				}
			}
		}
		return getByteArrayByString(String.format("%04X", crc), true);
	}

	/**
	 * crc校验和计算：将所有字节累加和计算，再模256
	 *
	 * @param data 需要计算和的数据
	 * @return 十六进制数
	 */
	public static byte getCrcSum(byte[] data) {
		int sum = 0;
		if (data == null || data.length == 0) {
			throw new NullPointerException("输入字节数组不能为NULL且长度不能为0");
		}
		try {
			for (byte b : data) {
				sum += b;
			}
			return (byte) (sum & 0xFF);
		} catch (Exception e) {
			e.printStackTrace();
			throw new NullPointerException("未知异常");
		}
	}

	/**
	 * 以（yyMMddHHmmss）格式获取当前时间字符串形式
	 */
	public static String getCurrentTimeString() {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		return format.format(new Date());
	}

	/**
	 * 以（yyMMddHHmmss）格式获取当前时间字符串形式
	 */
	public static byte[] getCurrentTimeByteArray() {
		SimpleDateFormat format = new SimpleDateFormat("yyMMddHHmmss");
		String date = format.format(new Date());
		return getByteArrayByString(date, false);
	}

	/************
	 * 把字符串转换为BCD码
	 *
	 * @param start ：起始位
	 * @param end   ：结束为
	 * @param str   ：字符串
	 * @return
	 */
	public static byte GetStrtoBCD(int start, int end, String str) {
		if (str.length() == 1) {
			str = '0' + str;
		}
		String tempstr = str.substring(start, end);
		String one = tempstr.substring(0, 1);
		String two = tempstr.substring(1, 2);
		return (byte) (Integer.parseInt(one, 16) * 16 + Integer.parseInt(two, 16));
	}

	/**
	 * 判断是否为null或空值
	 *
	 * @param str String
	 * @return true or false
	 */
	public static boolean isNullOrEmpty(String str) {
		return str == null || str.trim().length() == 0
				|| str.toLowerCase().contains("null");
	}

	/**
	 * 判断str1和str2是否相同
	 *
	 * @param str1 str1
	 * @param str2 str2
	 * @return true or false
	 */
	public static boolean equals(String str1, String str2) {
		return str1 == str2 || str1 != null && str1.equals(str2);
	}

	/**
	 * 判断str1和str2是否相同(不区分大小写)
	 *
	 * @param str1 str1
	 * @param str2 str2
	 * @return true or false
	 */
	public static boolean equalsIgnoreCase(String str1, String str2) {
		return str1 != null && str1.equalsIgnoreCase(str2);
	}

	/**
	 * 判断字符串str1是否包含字符串str2
	 *
	 * @param str1 源字符串
	 * @param str2 指定字符串
	 * @return true源字符串包含指定字符串，false源字符串不包含指定字符串
	 */
	public static boolean contains(String str1, String str2) {
		return str1 != null && str1.contains(str2);
	}

	/**
	 * 判断字符串是否为空，为空则返回一个空值，不为空则返回原字符串
	 *
	 * @param str 待判断字符串
	 * @return 判断后的字符串
	 */
	public static String getString(String str) {
		return str == null ? "" : str;
	}

	/**
	 * 过滤HTML标签，取出文本内容
	 *
	 * @param inputString HTML字符串
	 * @return 过滤了HTML标签的字符串
	 */
	public static String filterHtmlTag(String inputString) {
		String htmlStr = inputString;
		String textStr = "";
		Pattern pScript;
		Matcher mScript;
		Pattern pStyle;
		Matcher mStyle;
		Pattern pHtml;
		Matcher mHtml;

		try {
			// 定义script的正则表达式
			String regExScript = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?/[\\s]*?script[\\s]*?>";
			// 定义style的正则表达式
			String regExStyle = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?/[\\s]*?style[\\s]*?>";
			// 定义HTML标签的正则表达式
			String regExHtml = "<[^>\"]+>";

			pScript = Pattern.compile(regExScript, Pattern.CASE_INSENSITIVE);
			mScript = pScript.matcher(htmlStr);
			// 过滤script标签
			htmlStr = mScript.replaceAll("");

			pStyle = Pattern.compile(regExStyle, Pattern.CASE_INSENSITIVE);
			mStyle = pStyle.matcher(htmlStr);
			// 过滤style标签
			htmlStr = mStyle.replaceAll("");

			pHtml = Pattern.compile(regExHtml, Pattern.CASE_INSENSITIVE);
			mHtml = pHtml.matcher(htmlStr);
			// 过滤html标签
			htmlStr = mHtml.replaceAll("");

			textStr = htmlStr;

		} catch (Exception e) {
			System.err.println("Html2Text: " + e.getMessage());
		}

		return textStr;
	}

	/**
	 * 将字符串数组转化为字符串，默认以","分隔
	 *
	 * @param values 字符串数组
	 * @param split  分隔符，默认为","
	 * @return 有字符串数组转化后的字符串
	 */
	public static String arrayToString(String[] values, String split) {
		StringBuffer buffer = new StringBuffer();
		if (values != null) {
			if (split == null) {
				split = ",";
			}
			int len = values.length;
			for (int i = 0; i < len; i++) {
				buffer.append(values[i]);
				if (i != len - 1) {
					buffer.append(split);
				}
			}
		}
		return buffer.toString();
	}

	/**
	 * 将字符串list转化为字符串，默认以","分隔<BR>
	 *
	 * @param strList 字符串list
	 * @param split   分隔符，默认为","
	 * @return 组装后的字符串对象
	 */
	public static String listToString(Collection<String> strList, String split) {
		String[] values = null;
		if (strList != null) {
			values = new String[strList.size()];
			strList.toArray(values);
		}
		return arrayToString(values, split);
	}

	/**
	 * 验证字符串是否符合email格式
	 *
	 * @param email 需要验证的字符串
	 * @return 验证其是否符合email格式，符合则返回true,不符合则返回false
	 */
	public static boolean isEmail(String email) {

		// 通过正则表达式验证email是否合法
		return email != null
				&& email.matches("(\\w[\\w\\.\\-]*)@\\w[\\w\\-]*[\\.(com|cn|org|edu|hk)]+[a-z]$");
	}

	/**
	 * 验证字符串是否为数字
	 *
	 * @param str 需要验证的字符串
	 * @return 不是数字返回false，是数字就返回true
	 */
	public static boolean isNumeric(String str) {
		return str != null && str.matches("[0-9]*");
	}

	/**
	 * 验证字符串是否符合手机号格式
	 *
	 * @param str 需要验证的字符串
	 * @return 不是手机号返回false，是手机号就返回true
	 */
	public static boolean isMobile(String str) {

		return str != null && str.matches("1[0-9]{10}");
	}

	/**
	 * 替换字符串中特殊字符
	 *
	 * @param strData 源字符串
	 * @return 替换了特殊字符后的字符串，如果strData为NULL，则返回空字符串
	 */
	public static String encodeString(String strData) {
		if (strData == null) {
			return "";
		}
		return strData.replaceAll("&", "&amp;").replaceAll("<", "&lt;")
				.replaceAll(">", "&gt;").replaceAll("'", "&apos;")
				.replaceAll("\"", "&quot;");
	}

	/**
	 * 还原字符串中特殊字符
	 *
	 * @param strData strData
	 * @return 还原后的字符串
	 */
	public static String decodeString(String strData) {
		if (strData == null) {
			return "";
		}
		return strData.replaceAll("&lt;", "<").replaceAll("&gt;", ">")
				.replaceAll("&apos;", "'").replaceAll("&quot;", "\"")
				.replaceAll("&amp;", "&");
	}

	/**
	 * 组装XML字符串<BR>
	 * [功能详细描述]
	 *
	 * @param map 键值Map
	 * @return XML字符串
	 */
	public static String generateXml(Map<String, Object> map) {

		StringBuffer xml = new StringBuffer();
		xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		xml.append("<root>");
		if (map != null) {
			Iterator<Map.Entry<String, Object>> it = map.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<String, Object> entry = it.next();
				String key = entry.getKey();
				xml.append("<");
				xml.append(key);
				xml.append(">");
				xml.append(entry.getValue());
				xml.append("</");
				xml.append(key);
				xml.append(">");
			}
		}
		xml.append("</root>");
		return xml.toString();
	}

	/**
	 * 组装XML字符串<BR>
	 * [功能详细描述]
	 *
	 * @param values key、value依次排列
	 * @return XML字符串
	 */
	public static String generateXml(String... values) {
		StringBuffer xml = new StringBuffer();
		xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		xml.append("<root>");
		if (values != null) {
			int size = values.length >> 1;
			for (int i = 0; i < size; i++) {
				xml.append("<");
				xml.append(values[i << 1]);
				xml.append(">");
				xml.append(values[(i << 1) + 1]);
				xml.append("</");
				xml.append(values[i << 1]);
				xml.append(">");
			}
		}
		xml.append("</root>");
		return xml.toString();
	}

	/**
	 * 将srcString按split拆分，并组装成List。默认以','拆分。<BR>
	 *
	 * @param srcString 源字符串
	 * @param split     分隔符
	 * @return 返回list
	 */
	public static List<String> parseStringToList(String srcString, String split) {
		List<String> list = null;
		if (!StringUtil.isNullOrEmpty(srcString)) {
			if (split == null) {
				split = ",";
			}
			String[] strArr = srcString.split(split);
			if (strArr != null && strArr.length > 0) {
				list = new ArrayList<String>(strArr.length);
				for (String str : strArr) {
					list.add(str);
				}
			}
		}
		return list;
	}

	/**
	 * 去掉url中多余的斜杠
	 *
	 * @param url 字符串
	 * @return 去掉多余斜杠的字符串
	 */
	public static String fixUrl(String url) {
		StringBuffer stringBuffer = new StringBuffer(url);
		for (int i = stringBuffer.indexOf("//", stringBuffer.indexOf("//") + 2); i != -1; i = stringBuffer
				.indexOf("//", i + 1)) {
			stringBuffer.deleteCharAt(i);
		}
		return stringBuffer.toString();
	}

	/**
	 * 按照一个汉字两个字节的方法计算字数
	 *
	 * @param string String
	 * @return 返回字符串's count
	 */
	public static int count2BytesChar(String string) {
		int count = 0;
		if (string != null) {
			for (char c : string.toCharArray()) {
				count++;
				if (isChinese(c)) {
					count++;
				}
			}
		}
		return count;
	}

	/**
	 * 判断字符串中是否包含中文 <BR>
	 * [功能详细描述] [added by 杨凡]
	 *
	 * @param str 检索的字符串
	 * @return 是否包含中文
	 */
	public static boolean hasChinese(String str) {
		boolean hasChinese = false;
		if (str != null) {
			for (char c : str.toCharArray()) {
				if (isChinese(c)) {
					hasChinese = true;
					break;
				}
			}
		}
		return hasChinese;
	}

	/**
	 * 截取字符串，一个汉字按两个字符来截取<BR>
	 * [功能详细描述] [added by 杨凡]
	 *
	 * @param src        源字符串
	 * @param charLength 字符长度
	 * @return 截取后符合长度的字符串
	 */
	public static String subString(String src, int charLength) {
		if (src != null) {
			int i = 0;
			for (char c : src.toCharArray()) {
				i++;
				charLength--;
				if (isChinese(c)) {
					charLength--;
				}
				if (charLength <= 0) {
					if (charLength < 0) {
						i--;
					}
					break;
				}
			}
			return src.substring(0, i);
		}
		return src;
	}

	/**
	 * 对字符串进行截取, 超过则以...结束
	 *
	 * @param originStr     原字符串
	 * @param maxCharLength 最大字符数
	 * @return 截取后的字符串
	 */
	public static String trim(String originStr, int maxCharLength) {
		int count = 0;
		int index = 0;
		int originLen = originStr.length();
		for (index = 0; index < originLen; index++) {
			char c = originStr.charAt(index);
			int len = 1;
			if (isChinese(c)) {
				len++;
			}
			if (count + len <= maxCharLength) {
				count += len;
			} else {
				break;
			}
		}

		if (index < originLen) {
			return originStr.substring(0, index) + "...";
		} else {
			return originStr;
		}
	}

	/**
	 * 判断参数c是否为中文<BR>
	 * [功能详细描述] [added by 杨凡]
	 *
	 * @param c char
	 * @return 是中文字符返回true，反之false
	 */
	public static boolean isChinese(char c) {
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
		return ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
				|| ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
				|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
				|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS;

	}

	/**
	 * 检测密码强度
	 *
	 * @param password 密码
	 * @return 密码强度（1：低 2：中 3：高）
	 */
	public static int checkStrong(String password) {
		boolean num = false;
		boolean lowerCase = false;
		boolean upperCase = false;
		boolean other = false;

		int threeMode = 0;
		int fourMode = 0;

		for (int i = 0; i < password.length(); i++) {
			// 单个字符是数字
			if (password.codePointAt(i) >= 48 && password.codePointAt(i) <= 57) {
				num = true;
			}
			// 单个字符是小写字母
			else if (password.codePointAt(i) >= 97
					&& password.codePointAt(i) <= 122) {
				lowerCase = true;
			}
			// 单个字符是大写字母
			else if (password.codePointAt(i) >= 65
					&& password.codePointAt(i) <= 90) {
				upperCase = true;
			}
			// 特殊字符
			else {
				other = true;
			}
		}

		if (num) {
			threeMode++;
			fourMode++;
		}

		if (lowerCase) {
			threeMode++;
			fourMode++;
		}

		if (upperCase) {
			threeMode++;
			fourMode++;
		}

		if (other) {
			fourMode++;
		}

		// 数字、大写字母、小写字母只有一个，密码强度低
		if (threeMode == 1 && !other) {
			return 1;
		}
		// 四种格式有其中两个，密码强度中
		else if (fourMode == 2) {
			return 2;
		}
		// 四种格式有三个或者四个，密码强度高
		else if (fourMode >= 3) {
			return 3;
		}
		// 正常情况下不会出现该判断
		else {
			return 0;
		}
	}

	/**
	 * 返回一个制定长度范围内的随机字符串
	 *
	 * @param min 范围下限
	 * @param max 范围上限
	 * @return 字符串
	 */
	public static String createRandomString(int min, int max) {
		StringBuffer strB = new StringBuffer();
		Random random = new Random();
		int lenght = min;
		if (max > min) {
			lenght += random.nextInt(max - min + 1);
		}
		for (int i = 0; i < lenght; i++) {
			strB.append((char) (97 + random.nextInt(26)));
		}
		return strB.toString();
	}

	/**
	 * [用于获取字符串中字符的个数]<BR>
	 * [功能详细描述]
	 *
	 * @param content 文本内容
	 * @return 返回字符的个数
	 */
	public static int getStringLeng(String content) {
		return (int) Math.ceil(count2BytesChar(content) / 2.0);
	}

	/**
	 * 根据参数tag（XML标签）解析该标签对应的值<BR>
	 * 本方法针对简单的XML文件，仅通过字符串截取的方式获取标签值
	 *
	 * @param xml XML文件字符串
	 * @param tag XML标签名，说明：标签名不需加“<>”，方法中已做处理
	 * @return 标签对应的值
	 */
	public static String getXmlValue(String xml, String tag) {
		if (xml == null || tag == null) {
			return null;
		}

		// 如果标签中包含了"<"或">"，先去掉
		tag = tag.replace("<", "").replace(">", "");

		// 截取值
		int index = xml.indexOf(tag);
		if (index != -1) {
			return xml.substring(index + tag.length() + 1,
					xml.indexOf('<', index));
		}

		return null;
	}

	/**
	 * 根据业务拼装电话号码<BR>
	 *
	 * @param number 电话号码
	 * @return 拼装后的电话号码
	 */
	public static String fixPortalPhoneNumber(String number) {
		// if (StringUtil.isNullOrEmpty(number))
		// {
		return number;
		// }

		// String retPhoneNumber = number.trim();

		// // 确定是否是手机号码，然后将前缀去除，只保留纯号码
		// if (isMobile(retPhoneNumber))
		// {
		// if (retPhoneNumber.startsWith("+86"))
		// {
		// retPhoneNumber = retPhoneNumber.substring(3);
		// }
		// else if (retPhoneNumber.startsWith("86"))
		// {
		// retPhoneNumber = retPhoneNumber.substring(2);
		// }
		// else if (retPhoneNumber.startsWith("0086"))
		// {
		// retPhoneNumber = retPhoneNumber.substring(4);
		// }
		// }
		//
		// return retPhoneNumber;
	}

	public static String replaceCountryCode(String number, String countryCode) {
		try {
			if (number.startsWith(countryCode)) {
				return number.replace(countryCode, "");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return number;
	}

	/**
	 * 生成唯一的字符串对象<BR>
	 *
	 * @return 唯一的字符串
	 */
	public static String generateUniqueID() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

	public static String substringBetween(String str, String tag) {
		return substringBetween(str, tag, tag);
	}

	public static String substringBetween(String str, String open, String close) {
		if ((str == null) || (open == null) || (close == null)) {
			return null;
		}
		int start = str.indexOf(open);
		if (start != -1) {
			int end = str.indexOf(close, start + open.length());
			if (end != -1) {
				return str.substring(start + open.length(), end);
			}
		}
		return null;
	}

	public static String getFormatSize(double size) {
		double kiloByte = size / 1024;
		if (kiloByte < 1) {
			return size + "Byte(s)";
		}

		double megaByte = kiloByte / 1024;
		if (megaByte < 1) {
			BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
			return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
					.toPlainString() + "KB";
		}

		double gigaByte = megaByte / 1024;
		if (gigaByte < 1) {
			BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
			return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
					.toPlainString() + "MB";
		}

		double teraBytes = gigaByte / 1024;
		if (teraBytes < 1) {
			BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
			return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
					.toPlainString() + "GB";
		}
		BigDecimal result4 = new BigDecimal(teraBytes);
		return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()
				+ "TB";
	}

	public static String generator() {
		StringBuffer now = new StringBuffer(new SimpleDateFormat(
				"yyyyMMddHHmmssSSS").format(new Date()));
		int a = (int) (Math.random() * 90000.0D + 10000.0D);
		int b = (int) (Math.random() * 90000.0D + 10000.0D);
		int c = (int) (Math.random() * 90000.0D + 10000.0D);
		return (now.append(a).append(b).append(c)).toString();
	}

	/**
	 * @param ss  要按模式切割的字符创
	 * @param mod 模式
	 * @return 描述：
	 * @author CBB
	 */
	public static String[] toStringByRadius(String ss, int mod) {
		String[] mStrings = null;
		if (!StringUtil.isNullOrEmpty(ss)) {
			char[] mchar = ss.toCharArray();
			int loop = 0;
			if (mchar.length % mod != 0) {
				loop = mchar.length / mod + 1;
			} else {
				loop = mchar.length / mod;
			}
			mStrings = new String[loop];
			int temp = 0;
			for (int m = 0; m < loop; m++) {
				for (int t = temp; t < mchar.length; t++) {
					if (mStrings[m] != null) {
						mStrings[m] += mchar[t];
					} else {
						mStrings[m] = String.valueOf(mchar[t]);
					}
					if ((t + 1) % mod == 0) {
						temp = t + 1;
						break;
					}
				}
			}
		}
		return mStrings;
	}

	/**
	 * @param str
	 * @return 描述：验证IP
	 * @author CBB
	 */
	public static boolean validateIP(String str) {
		Pattern pattern = Pattern
				.compile("^((\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5]"
						+ "|[*])\\.){3}(\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5]|[*])$");
		return pattern.matcher(str).matches();
	}

	/**
	 * @param str
	 * @param flag 标示   true 大  false 小
	 * @return 描述：验证是否为字母、大小写,默认为大写或小写都可以
	 * @author CBB
	 */
	public static boolean validateAZ(String str, Boolean flag) {
		String pat = "^[A-Za-z]+$";
		if (flag != null) {
			if (flag) {
				pat = "^[A-Z]+$";
			} else {
				pat = "^[a-z]+$";
			}
		}
		Pattern pattern = Pattern.compile(pat);
		return pattern.matcher(str).matches();
	}

	/**
	 * @param str
	 * @return 描述：验证数字
	 * @author CBB
	 */
	private static Pattern pattern = Pattern.compile("^\\d*[0-9]\\d*$");
	public static boolean validateInt(String str) {
		return pattern.matcher(str).matches();
	}

	/**
	 * @param mac
	 * @return 描述：验证MAC地址
	 * @author CBB
	 */
	public static boolean validateMAC(String mac) {
		String patternMac = "^[a-fA-F0-9]{2}+:[a-fA-F0-9]{2}+:[a-fA-F0-9]{2}+:[a-fA-F0-9]{2}+:[a-fA-F0-9]{2}+:[a-fA-F0-9]{2}$";
		Pattern pa = Pattern.compile(patternMac);
		return pa.matcher(mac).find();
	}
	public static String generateRandomPassword() {
		// 创建一个Random对象
		Random random = new Random();

		// 使用StringBuilder来构建密码
		StringBuilder passwordBuilder = new StringBuilder();

		// 生成6位随机数字
		for (int i = 0; i < 6; i++) {
			int digit = random.nextInt(10); // 生成一个0到9的随机整数
			passwordBuilder.append(digit); // 将数字添加到密码中
		}

		// 返回生成的随机密码
		return passwordBuilder.toString();
	}
	public static void main(String[] args) {
		System.out.println(System.currentTimeMillis());
	}
}
