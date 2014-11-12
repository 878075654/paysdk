package com.xy.xypay.utils;

/**
 * 字符串工具类
 * @author ruanwei
 *
 */

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;

public class StringUtils {

	private StringUtils() {
	}
//
	
	/**  
	 * TODO 判断字符串是否全部是数字。
	 *  @param cardno 传入的字符串
	 *  @return
	 * @throw 
	 * @return boolean 返回是否是数字
	 */
	public static boolean isLegalCardNo(String cardno) {
		Pattern patter = Pattern.compile("\\d+");
		Matcher matcher = patter.matcher(cardno);
		return matcher.matches();
	}
//
	
	/**  
	 * TODO 把byte数组转化为16进制字符串
	 *  @param b
	 *  @return
	 * @throw 
	 * @return String 
	 */
	private static String toHexString(byte b[]) {
		StringBuilder sb = new StringBuilder(b.length * 2);
		for (int i = 0; i < b.length; i++) {
			sb.append(HEX_DIGITS[(b[i] & 0xf0) >>> 4]);
			sb.append(HEX_DIGITS[b[i] & 0xf]);
		}

		return sb.toString();
	}
	//
	/**  
	 * TODO 获得手机的imei号码
	 *  @param context
	 *  @return
	 * @throw 
	 * @return String 
	 */
	public static String getPhoneIMEI(Context context) {
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return tm.getDeviceId();
	}
//
	
	/**  
	 * TODO md5加密
	 *  @param s
	 *  @return
	 * @throw 
	 * @return String 
	 */
	public static String md5(String s) {
		try {
			MessageDigest digest = MessageDigest.getInstance("MD5");
			try {
				digest.update(s.getBytes("UTF8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			byte messageDigest[] = digest.digest();
			String temp = toHexString(messageDigest);
			return temp;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return "";
	}

	public static boolean isEmpty(String input) {
		if (input == null || "".equals(input))
			return true;
		for (int i = 0; i < input.length(); i++) {
			char c = input.charAt(i);
			if (c != ' ' && c != '\t' && c != '\r' && c != '\n')
				return false;
		}

		return true;
	}

	public static String decode(String in) {
		try {
			return decode(in.toCharArray());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return in;
	}

	private static String decode(char in[]) throws Exception {
		int off = 0;
		char out[] = new char[in.length];
		int outLen = 0;
		while (off < in.length) {
			char c = in[off++];
			if (c == '\\') {
				if (in.length > off) {
					c = in[off++];
				} else {
					out[outLen++] = '\\';
					break;
				}
				if (c == 'u') {
					int value = 0;
					if (in.length > off + 4) {
						boolean isUnicode = true;
						for (int i = 0; i < 4; i++) {
							c = in[off++];
							switch (c) {
							case 48: // '0'
							case 49: // '1'
							case 50: // '2'
							case 51: // '3'
							case 52: // '4'
							case 53: // '5'
							case 54: // '6'
							case 55: // '7'
							case 56: // '8'
							case 57: // '9'
								value = ((value << 4) + c) - 48;
								break;

							case 97: // 'a'
							case 98: // 'b'
							case 99: // 'c'
							case 100: // 'd'
							case 101: // 'e'
							case 102: // 'f'
								value = ((value << 4) + 10 + c) - 97;
								break;

							case 65: // 'A'
							case 66: // 'B'
							case 67: // 'C'
							case 68: // 'D'
							case 69: // 'E'
							case 70: // 'F'
								value = ((value << 4) + 10 + c) - 65;
								break;

							case 58: // ':'
							case 59: // ';'
							case 60: // '<'
							case 61: // '='
							case 62: // '>'
							case 63: // '?'
							case 64: // '@'
							case 71: // 'G'
							case 72: // 'H'
							case 73: // 'I'
							case 74: // 'J'
							case 75: // 'K'
							case 76: // 'L'
							case 77: // 'M'
							case 78: // 'N'
							case 79: // 'O'
							case 80: // 'P'
							case 81: // 'Q'
							case 82: // 'R'
							case 83: // 'S'
							case 84: // 'T'
							case 85: // 'U'
							case 86: // 'V'
							case 87: // 'W'
							case 88: // 'X'
							case 89: // 'Y'
							case 90: // 'Z'
							case 91: // '['
							case 92: // '\\'
							case 93: // ']'
							case 94: // '^'
							case 95: // '_'
							case 96: // '`'
							default:
								isUnicode = false;
								break;
							}
						}

						if (isUnicode) {
							out[outLen++] = (char) value;
						} else {
							off -= 4;
							out[outLen++] = '\\';
							out[outLen++] = 'u';
							out[outLen++] = in[off++];
						}
					} else {
						out[outLen++] = '\\';
						out[outLen++] = 'u';
					}
				} else {
					switch (c) {
					case 116: // 't'
						c = '\t';
						out[outLen++] = c;
						break;

					case 114: // 'r'
						c = '\r';
						out[outLen++] = c;
						break;

					case 110: // 'n'
						c = '\n';
						out[outLen++] = c;
						break;

					case 102: // 'f'
						c = '\f';
						out[outLen++] = c;
						break;

					default:
						out[outLen++] = '\\';
						out[outLen++] = c;
						break;
					}
				}
			} else {
				out[outLen++] = c;
			}
		}
		return new String(out, 0, outLen);
	}

	private static final char HEX_DIGITS[] = { '0', '1', '2', '3', '4', '5',
			'6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	public static void printLog(boolean isDebug, String tag, String message) {
		if (isDebug) {
			Log.e(tag + "", "" + message);
		}
	}

}
