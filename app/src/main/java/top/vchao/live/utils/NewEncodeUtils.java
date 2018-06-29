package top.vchao.live.utils;

import java.io.UnsupportedEncodingException;

/**
 * @ Create_time: 2018/6/29 on 14:01.
 * @ description：
 * @ author: vchao  blog: http://blog.csdn.net/zheng_weichao
 */
public class NewEncodeUtils {

    public String bytes2HexString(byte b) {
        return bytes2HexString(new byte[]{b});
    }

    // 汉字转换成区位码
    public String bytes2HexString(byte[] b) {
        String ret = "";
        for (int i = 0; i < b.length; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            ret += hex.toUpperCase();
        }
        return ret;
    }

    // 汉字转换成区位码
    public String getString(String chinese) {
        byte[] bs;
        String s = "";
        try {
            bs = chinese.getBytes("GB2312");

            for (int i = 0; i < bs.length; i++) {
                int a = Integer.parseInt(bytes2HexString(bs[i]), 16);
                s += (a - 0x80 - 0x20) + "";
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return s;
    }

    // 区位码转换成汉字
    public static String CodeToChinese(String code) {
        String Chinese = "";
        for (int i = 0; i < code.length(); i += 4) {
            byte[] bytes = new byte[2];
            String lowCode = code.substring(i, i + 2);
            int tempLow = Integer.parseInt(lowCode);
            tempLow += 160;
            bytes[0] = (byte) tempLow;
            String highCode = code.substring(i + 2, i + 4);
            int tempHigh = Integer.parseInt(highCode);
            tempHigh += 160;
            bytes[1] = (byte) tempHigh;
            String chara = new String(bytes);
            Chinese += chara;
        }
        return Chinese;
    }


}

