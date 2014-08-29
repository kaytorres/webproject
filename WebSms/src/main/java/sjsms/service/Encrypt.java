package sjsms.service;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;




public class Encrypt {
	 public static final String KEY_ALGORITHM = "DES";
	  public static final String CIPHER_ALGORITHM = "DES/ECB/PKCS5Padding";
	  public static final String CHART_SET = "UTF-8";
	  public static String key;

	  private static Key toKey(String key) throws Exception
	  {
	    DESKeySpec des = new DESKeySpec(key.getBytes("UTF-8"));
	    SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
	    return keyFactory.generateSecret(des);
	  }

	  public static String encrypt(String content) throws Exception
	  {
	    Key k = toKey(key);
	    Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");//CBC,ECB
	    cipher.init(1, k);
	    byte[] pasByte = cipher.doFinal(content.getBytes("UTF-8"));
	    return toHexString(pasByte).toLowerCase();
	  }
	  public static String toHexString(byte[] b) {
		    StringBuffer hexString = new StringBuffer();
		    for (int i = 0; i < b.length; i++) {
		      String plainText = Integer.toHexString(0xFF & b[i]);
		      if (plainText.length() < 2)
		        plainText = "0" + plainText;
		      hexString.append(plainText);
		    }

		    return hexString.toString();
		  }
	  
   public static String decrypt(String content) throws Exception
	  {
	    Key k = toKey(key);
	    Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
	    cipher.init(2, k);
	    byte[] pasByte = cipher.doFinal(convertHexString(content));

	    return new String(pasByte, "UTF-8");
	  }

  public static byte[] convertHexString(String ss) {
	    byte[] digest = new byte[ss.length() / 2];
	    for (int i = 0; i < digest.length; i++) {
	      String byteString = ss.substring(2 * i, 2 * i + 2);
	      int byteValue = Integer.parseInt(byteString, 16);
	      digest[i] = ((byte)byteValue);
	    }

	    return digest;
	  }
	  
  public static void main(String[] args)
			    throws Exception
			  {
			   key = "C9eLew123456";
			  //  String security1="udYPoA1PCIs=";
			   // String content = "C9eLew123456";
			    String security = encrypt("12001");
			    System.out.println("en-------:"+security);
/*			    String deString=decrypt("85125f8084806495");
			    System.err.println("de-------:"+deString);*/
			  }

}
