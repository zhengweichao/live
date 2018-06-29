package top.vchao.live.utils;

import java.io.UnsupportedEncodingException;

/**
 * @ Create_time: 2018/6/29 on 11:16.
 * @ description： 汉字区位码 与 汉字  互转
 * @ author: vchao  blog: http://blog.csdn.net/zheng_weichao
 */
public class WordUtil {

    public static String encode(String str) {
        try {
            byte[] bs = str.getBytes("GB2312");
            String s = "";
            for (int i = 0; i < bs.length; i++) {
                LogUtils.e(bs[i] + "    " + bytes2HexString(bs[i]));
                int a = Integer.parseInt(bytes2HexString(bs[i]), 16);
                if ((a - 0x80 - 0x20) <= 10) {
                    s += "0" + (a - 0x80 - 0x20);
                } else {
                    s += (a - 0x80 - 0x20) + "";
                }
                LogUtils.e("s ==   " + s);
            }
            LogUtils.e(s);
            return s;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "null";
    }

    public static String codeToChinese(String code) {          // 将区位码转换为汉字的方法
        String Chinese = "";
        for (int i = 0; i < code.length() - 1; i += 4) {
            LogUtils.e("从 " + i + " 至 " + (i + 4) + "位的转换");
            byte[] bytes = new byte[2];
            // 存储区位码的字节数组
            String highCode = code.substring(i, i + 2);     // 获得高位
            int tempHigh = Integer.parseInt(highCode);          // 将高位转换为整数
            tempHigh += 160;
            // 计算出区号
            bytes[0] = (byte) tempHigh;

            // 将区号存储到字节数组
            String lowCode = code.substring(i + 2, i + 4);  // 获得低位
            int tempLow = Integer.parseInt(lowCode);                    // 将低位转换为整数
            tempLow += 160;
            // 计算出位号
            bytes[1] = (byte) tempLow;

            // 将位号存储到字节数组
            String singleChar = "";
            // 存储转换的单个字符
            try {
                singleChar = new String(bytes, "GB2312");        // 通过  GB2312  编码进行转换
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            Chinese += singleChar;
            // 存储转换后的结果
        }
        return Chinese;
    }

    public static String bytes2HexString(byte b) {
        return bytes2HexString(new byte[]{b});
    }

    public static String bytes2HexString(byte[] b) {
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
}