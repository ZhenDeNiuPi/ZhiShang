package com.fire.util;

import java.security.MessageDigest;

public class MD5Util {

	public static void main(String[] args) throws Exception{
		System.out.println(getMD5Code("1"));
	}
	
	//=====================BEGIN MD5========================
	
	//MD5Util2次混淆加密
	public final static String getMD5Code(Object pwd)throws Exception
	{
		String m1=MD5Util.MD5Encode(pwd);
		String pwd2=m1+"以无限为有限,以無法為有法"+m1;
		String m2=MD5Util.MD5Encode(pwd2);
		return m2;
	}
	
	 private final static String[] hexDigits = {
	     "0", "1", "2", "3", "4", "5", "6", "7",
	     "8", "9", "a", "b", "c", "d", "e", "f"};

	  /**
	   * 转换字节数组为16进制字串
	   * @param b 字节数组
	   * @return 16进制字串
	   */
	  private static String byteArrayToHexString(byte[] b)
	  {
	      StringBuffer resultSb = new StringBuffer();
	      for (int i = 0; i < b.length; i++)
	      {
	         resultSb.append(byteToHexString(b[i]));
	      }
	      return resultSb.toString();
	  }
	  /**
	   * 转换字节为16进制字符串
	   * @param b byte
	   * @return String
	   */
	  private static String byteToHexString(byte b)
	  {
	      int n = b;
	      if (n < 0)
	         n = 256 + n;
	      int d1 = n / 16;
	      int d2 = n % 16;
	      return hexDigits[d1] + hexDigits[d2];
	  }
	  /**
	   * 得到MD5Util的秘文密码
	   * @param origin String
	   * @throws Exception
	   * @return String
	   */
	  private static String MD5Encode(Object origin) throws Exception
	  {
	       String resultString = null;
	       try
	       {
	           resultString=new String(origin.toString());
	           MessageDigest md = MessageDigest.getInstance("MD5");
	           resultString=byteArrayToHexString(md.digest(resultString.getBytes("GBK")));
	           return resultString;
	       }
	       catch (Exception ex)
	       {
	          throw ex;
	       }
	  }	
	//******************END  MD5******************
}
