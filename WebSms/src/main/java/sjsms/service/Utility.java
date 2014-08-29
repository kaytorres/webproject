package sjsms.service;

import java.io.UnsupportedEncodingException;

import com.owtelse.codec.Base64;

public class Utility {
	public Utility() {
		// TODO Auto-generated constructor stub
	}
	
	public static String getMD5(String jidpassword) {   
		 byte[] source=jidpassword.getBytes();
      String s = null;   
      char hexDigits[] = {       //    
         '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',  'e', 'f'};    
       try  
       {   
        java.security.MessageDigest md = java.security.MessageDigest.getInstance( "MD5" );   
        md.update( source );   
        byte tmp[] = md.digest();          //      
        char str[] = new char[16 * 2];   //  
        int k = 0;                                //  
        for (int i = 0; i < 16; i++) {          // 
                                                     //
         byte byte0 = tmp[i];                 // 
         str[k++] = hexDigits[byte0 >>> 4 & 0xf];  //     
                                                                 // 
         str[k++] = hexDigits[byte0 & 0xf];            // 
        }    
        s = new String(str);                                 // 

       }catch( Exception e )   
       {   
        e.printStackTrace();   
       }   
       return s;   
     }
	
	public static byte[] fromBASE64(String str) {
		if (str == null) {
			return null;
		}
		try
		{
			return Base64._decode(str.getBytes("UTF-8"));
			
			
		}
		catch (UnsupportedEncodingException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (RuntimeException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}
	

	public static String toBASE64(byte[]str) {
		if (str == null) {
			return null;
		}
		String encoded = "";
		try {
			encoded = new String(Base64.encode(str));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return encoded;
	}
}
