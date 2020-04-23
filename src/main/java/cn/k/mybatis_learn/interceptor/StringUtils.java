package cn.k.mybatis_learn.interceptor;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.Vector;
import java.util.regex.Pattern;

/**
 * StringUtil.java
 */
public class StringUtils {
	
	/**
	 * 获取唯一编码
	 * 
	 * @return
	 */
	public static String getUniqueCode(){
		return UUID.randomUUID().toString().replace("-", "");
	}
    
    /**
     * 生成指定位数的数字验证码
     * 
     * @param size
     *            要生成的验证码的长度
     * @return
     */
    public static String generateMac(int size) {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    public static boolean isEmpty(Object o) throws IllegalArgumentException {
        if (o == null)
            return true;
        if (o instanceof String) {
            return (o == null) || (o.toString().trim().length() == 0) || "null".equalsIgnoreCase(o.toString());
        } else if (o instanceof Collection) {
            if (((Collection) o).isEmpty()) {
                return true;
            }
        } else if (o.getClass().isArray()) {
            if (Array.getLength(o) == 0) {
                return true;
            }
        } else if (o instanceof Map) {
            if (((Map) o).isEmpty()) {
                return true;
            }
        } else {
            return false;
        }
        return false;
    }

    public static boolean isNotEmpty(Object o) {
        return !isEmpty(o);
    }

    /**
     * 判断字符串是否是数字
     *
     * @param s
     * @return
     * @author hankai
     */
    public final static boolean isNumeric(String s) {
        if (s != null && !"".equals(s.trim()))
            return s.matches("^[0-9]*$");
        else
            return false;
    }


    public static boolean isPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || "".equals(phoneNumber))
            return false;
        return Pattern.matches("1[3,5,7,8][0,1,2,3,4,5,6,7,8,9]\\d{8}", phoneNumber);
    }

    public static boolean isEmail(String email) {
    	if (email == null || "".equals(email))
            return false;
//        return Pattern.matches("[\\p{Alnum},_,.]+@[\\w+\\.]+\\p{Alpha}{2,3}", email.toLowerCase());

        return Pattern.matches("^^[a-z0-9]+([._\\\\-]*[a-z0-9])*@([a-z0-9]+[-a-z0-9]*[a-z0-9]+.){1,63}[a-z0-9]+$",email.toLowerCase());
    }


    public static String trim(String str) {
        if (isEmpty(str)) {
            return "";
        } else {
            return str.trim();
        }
    }

    /**
     * 模糊字符串  例如：银行卡号部分用*代替
     *
     * @param s
     * @param front
     * @param back
     * @return
     * @throws Exception
     */
    public static String fuzzyString(String s, int front, int back) throws Exception {

        if (s.length() < front + back) {
            throw new Exception("参数不正确");
        }

        String r = s.substring(0, front) + "****" + s.substring(s.length() - back, s.length());

        return r;

//
//        String r = s.substring(front);
//        for (int i = 0; i < front; i++) {
//            r = "*" + r;
//        }
//        r = r.substring(0, r.length() - back);
//        for (int j = 0; j < back; j++) {
//            r = r + "*";
//        }


//        return r;
    }

    public static String reSetAsc(String str){
        char[] plainChar = str.toCharArray();
        Arrays.sort(plainChar);
        return  new String(plainChar);
    }
    
    public static String maskPhoneNumber(String phoneNumber){
        if(isEmpty(phoneNumber)||!isPhoneNumber(phoneNumber)){
            return phoneNumber;
        }
        phoneNumber = phoneNumber.substring(0,phoneNumber.length()-(phoneNumber.substring(3)).length())+"****"+phoneNumber.substring(7);
        return phoneNumber;
    }
    
    public static String maskEmail(String email){
        if(isEmpty(email)||!isEmail(email)){
            return email;
        }
        String regex = "(\\w{3})(\\w+)(\\w{3})(@\\w+)";
        return email.replaceAll(regex, "$1****$4");

    }

    
    public static String maskIdNo(String idNo){
        if(isEmpty(idNo)){
            return idNo;
        }
        StringBuffer sb = new StringBuffer();
        sb.append(idNo.substring(0,3));
        for(int i = 0;i<(idNo.substring(3,idNo.length()-4)).length();i++){
            sb.append("*");
        }
        sb.append(idNo.substring(idNo.length()-4,idNo.length()));
        idNo = sb.toString();
        return idNo;
    }
    
    public static String maskBankCardNo(String bankCardNo){
        if(isEmpty(bankCardNo) || bankCardNo.length()<8){
            return bankCardNo;
        }
        StringBuffer sb = new StringBuffer();
//        sb.append(bankCardNo.substring(0,4));
        for(int i = 0;i<(bankCardNo.substring(0,bankCardNo.length()-4)).length();i++){
            sb.append("*");
        }
        sb.append(bankCardNo.substring(bankCardNo.length()-4,bankCardNo.length()));
        bankCardNo = sb.toString();
        return bankCardNo;
    }

    public static String maskName(String name){
        if(isEmpty(name)){
            return name;
        }
        StringBuffer sb = new StringBuffer();
        for(int i=0;i<(name.substring(0,name.length()-1)).length();i++){
            sb.append("*");
        }
        sb.append(name.substring(name.length()-1,name.length()));
        return sb.toString();
    }
    
    public static String[] splitWords(String words, String regex) {
        String[] terms = words.split(regex);
        Vector<String> result = new Vector<String>();
        for (int i = 0; i < terms.length; ++i)
            if (terms[i].length() > 0)
                result.add(terms[i]);
        String[] data = new String[result.size()];
        return (String[]) result.toArray(data);
    }

    public static String[] splitWords(String words) {
        return splitWords(words, "[\\p{Blank}\\p{Punct}]");
    }

    public static void main(String[] args) {
		System.out.println(maskBankCardNo("88888888232929283"));
	}
}