package cn.k.mybatis_learn.interceptor;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;


public class AESUtil {

	public static final String AES_ALGORITHM = "AES/ECB/PKCS5Padding";

	public static final String CHAR_ENCODING = "UTF-8";
	
	public static final String AES_KEY="73af3a2fe4eb4737";

    public static byte[] encrypt(byte[] data, byte[] key) {
		if(key.length!=16){
			throw new RuntimeException("Invalid AES key length (must be 16 bytes)");
		}
		try {
			SecretKeySpec secretKey = new SecretKeySpec(key, "AES"); 
			byte[] enCodeFormat = secretKey.getEncoded();
			SecretKeySpec seckey = new SecretKeySpec(enCodeFormat,"AES");
			Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
			cipher.init(Cipher.ENCRYPT_MODE, seckey);
			byte[] result = cipher.doFinal(data);
			return result;
		} catch (Exception e){
			throw new RuntimeException("encrypt fail!", e);
		}
	}

	public static byte[] decrypt(byte[] data, byte[] key) {
		if(key.length!=16){
			throw new RuntimeException("Invalid AES key length (must be 16 bytes)");
		}
		try {
			SecretKeySpec secretKey = new SecretKeySpec(key, "AES"); 
			byte[] enCodeFormat = secretKey.getEncoded();
			SecretKeySpec seckey = new SecretKeySpec(enCodeFormat, "AES");
			Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
			cipher.init(Cipher.DECRYPT_MODE, seckey);
			byte[] result = cipher.doFinal(data);
			return result;
		} catch (Exception e){
			throw new RuntimeException("decrypt fail!", e);
		}
	}

	public static String encryptToBase64(String data, String key){
		try {
			byte[] valueByte = encrypt(data.getBytes(CHAR_ENCODING), key.getBytes(CHAR_ENCODING));
			return new String(Base64Util.encode(valueByte));
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("encrypt fail!", e);
		}

	}
	
	public static String decryptFromBase64(String data, String key){
		try {
			byte[] originalData = Base64Util.decode(data.getBytes());
			byte[] valueByte = decrypt(originalData, key.getBytes(CHAR_ENCODING));
			return new String(valueByte, CHAR_ENCODING);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("decrypt fail!", e);
		}
	}
	
	public static String encryptWithKeyBase64(String data, String key){
		try {
			byte[] valueByte = encrypt(data.getBytes(CHAR_ENCODING), Base64Util.decode(key.getBytes()));
			return new String(Base64Util.encode(valueByte));
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("encrypt fail!", e);
		}
	}
	
	public static String decryptWithKeyBase64(String data, String key){
		try {
			byte[] originalData = Base64Util.decode(data.getBytes());
			byte[] valueByte = decrypt(originalData, Base64Util.decode(key.getBytes()));
			return new String(valueByte, CHAR_ENCODING);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("decrypt fail!", e);
		}
	}
	
	public static byte[] genarateRandomKey(){
		KeyGenerator keygen = null;
		try {
			keygen = KeyGenerator.getInstance(AES_ALGORITHM);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(" genarateRandomKey fail!", e);
		}
		SecureRandom random = new SecureRandom();
		keygen.init(random);
		Key key = keygen.generateKey();
		return key.getEncoded();
	}
	
	public static String genarateRandomKeyWithBase64(){
		return new String(Base64Util.encode(genarateRandomKey()));
	}

	public static void main(String args[]){

		String s = AESUtil.encryptToBase64("111111","3619025136190251");
		System.out.println(s);
		s = AESUtil.decryptFromBase64(s,"3619025136190251");
		System.out.println(s);
	}
	
}

