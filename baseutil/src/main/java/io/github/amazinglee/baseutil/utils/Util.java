package io.github.amazinglee.baseutil.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Base64;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 项目名称：Base
 * 类描述：
 * 创建人：renhao
 * 创建时间：2016/9/1 1:18
 * 修改备注：
 */
public class Util {
    /**
     * MD5加密
     *
     * @param password
     * @return
     */
    public static String makeMD5(String password) {
        try {
            // 生成一个MD5加密计算摘要
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 计算md5函数
            md.update(password.getBytes());
            // digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
            // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
            return new BigInteger(1, md.digest()).toString(16);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return password;
    }


    /**
     * 二进制流转换为图片
     *
     * @param img
     * @return
     */
    public static Bitmap getBitmapFromByte(byte[] img) {
        if (img != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(img, 0, img.length);
            return bitmap;
        } else {
            return null;
        }
    }


    /**
     * 二进制流转换为图片
     *
     * @param img
     * @return
     */
    public static Bitmap getBitmapFromByte(String img) {
        byte[] temp = Base64.decode(img.getBytes(), Base64.DEFAULT);
        if (temp != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(temp, 0, temp.length);
            return bitmap;
        } else {
            return null;
        }
    }

    /**
     * 获取手机的MAC地址
     *
     * @return
     */
    public static String getMac() {
        String macSerial = null;
        String str = "";
        try {
            Process pp = Runtime.getRuntime().exec("cat /sys/class/net/wlan0/address ");
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);

            for (; null != str; ) {
                str = input.readLine();
                if (str != null) {
                    macSerial = str.trim();// 去空格
                    break;
                }
            }
        } catch (IOException ex) {
            // 赋予默认值
            ex.printStackTrace();
        }
        return macSerial;
    }

    /**
     * 获取当前的时间
     *
     * @return
     */
    public static String getNowDate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        return format.format(new Date());
    }

    /**
     * 获取当前的时间
     *
     * @param dateFormat
     * @return
     */
    public static String getNowDate(String dateFormat) {
        SimpleDateFormat format = new SimpleDateFormat(dateFormat, Locale.CHINA);
        return format.format(new Date());
    }

    /**
     * 获取当前的时间
     *
     * @param dateFormat
     * @param local
     * @return
     */
    public static String getNowDate(String dateFormat, Locale local) {
        SimpleDateFormat format = new SimpleDateFormat(dateFormat, local);
        return format.format(new Date());
    }

    /**
     * 检查设备是否存在SDCard的工具方法
     */
    public static boolean hasSDcard() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            // 有存储的SDCard
            return true;
        } else {
            return false;
        }
    }

    /**
     * 是否是电话号码
     *
     * @param number
     * @return
     */
    public static boolean isPhoneNumber(String number) {
        String pattern = "0?(13|14|15|18)[0-9]{9}";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(number);
        if (m.matches())
            return true;
        else
            return false;
    }

    /**
     * 是否是邮箱
     *
     * @param email
     * @return
     */
    public static boolean isEmail(String email) {
        String pattern = "\\w[-\\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\\.)+[A-Za-z]{2,14}";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(email);
        if (m.matches())
            return true;
        else
            return false;
    }

    /**
     * 是否是身份证号码
     *
     * @param id
     * @return
     */
    public static boolean isID(String id) {
        String pattern = "\\d{17}[\\d|x]|\\d{15}";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(id);
        if (m.matches())
            return true;
        else
            return false;
    }
}
