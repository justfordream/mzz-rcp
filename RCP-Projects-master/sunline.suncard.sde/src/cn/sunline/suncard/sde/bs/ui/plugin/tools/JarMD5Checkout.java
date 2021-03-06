/*
 * 文件名：     JarMD5Checkout.java
 * 版权：          Copyright 2011-2020 Sunline Tech. Co. Ltd. All Rights Reserved.
 * 描述：	 MD5校验类
 * 修改人：     易振强
 * 修改时间：2011-9-20
 * 修改内容：创建
 */

package cn.sunline.suncard.sde.bs.ui.plugin.tools;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import cn.sunline.suncard.sde.bs.ui.plugin.JarXMLAnalysis;

/**
 * MD5校验类
 * 通过文件或文件流等获得其对应的MD5码
 * @author  易振强
 * @version 1.0, 2011-9-20
 * @see     JarXMLAnalysis
 * @since   1.0
 */
public class JarMD5Checkout {
	/**
	 * 设置输入流字节的最大值
	 */
	private final static int BYTE_MAX = SDEPluginZip.BUFFER_SIZE;
	
	/**
	 * 设置校验方式，默认为"MD5"
	 */
	private String hashType = "MD5";
	
	/**
	 * MD5码字符串
	 */
	private String hashCode = "";
	
	/**
	 * 16进制的字符数组
	 */
	public static char[] hexChar = { '0', '1', '2', '3', '4', '5', '6', '7',
			'8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	/**
	 * 初始化MD5码
	 * 通过指定的插件压缩文件路径来得到其MD5码
	 * @param  	fileName String 文件路径值
	 * @throws IOException 
	 * @throws NoSuchAlgorithmException 
	 * @throws Exception 
	 * @see     JarMD5Checkout#getHash
	 */
	public void initHashCode(String fileName) throws NoSuchAlgorithmException, IOException {
		hashCode = getHash(fileName);
	}
	
	/**
	 * 初始化MD5码
	 * 通过指定的插件压缩文件字符流来得到其MD5码
	 * @param  	bytes byte[] 压缩文件字符流
	 * @throws NoSuchAlgorithmException 
	 * @see     JarMD5Checkout#getHash
	 */
	public void initHashCode(byte[] bytes) throws NoSuchAlgorithmException {
		hashCode = getHash(bytes);
	}
	
	/**
	 * 返回MD5码
	 */
	public String getHashCode() {
		return hashCode;
	}

	/**
	 * 获取校验码。
	 * 通过文件获取校验码
	 * @param  fileName String 插件压缩文件路径
	 * @return String MD5码
	 * @throws NoSuchAlgorithmException 
	 * @throws IOException 
	 */
	public String getHash(String fileName) throws NoSuchAlgorithmException, IOException
			{
		InputStream fis = new FileInputStream(fileName);;

		byte[] buffer = new byte[BYTE_MAX];
		
		MessageDigest md5 = MessageDigest.getInstance(hashType);
		
		int numRead = 0;
//		while ((numRead = fis.read(buffer)) > 0) {
//			md5.update(buffer, 0, numRead);
//		}
		
		numRead = fis.read(buffer);
		md5.update(buffer, 0, numRead);
		System.out.println(numRead);	
		
		
		fis.close();
		return toHexString(md5.digest());
	}

	/**
	 * 获取校验码。
	 * 通过byte流获取校验码
	 * @param  bytes byte[] 插件压缩文件字符流
	 * @return String MD5码
	 * @throws NoSuchAlgorithmException 
	 */
	public String getHash(byte[] bytes) throws NoSuchAlgorithmException {	
		MessageDigest md5 = MessageDigest.getInstance(hashType);
		
		if(SDEPluginZip.JAR_SIZE != 0) {
			md5.update(bytes, 0, SDEPluginZip.JAR_SIZE);
			
		} else {
			int count = BYTE_MAX - 1;
			while(bytes[count] == 0) {
				count--;
			}
			
			//文件结束符占4字节
			md5.update(bytes, 0, count + 5);
		}
		return toHexString(md5.digest());
	}
	
	/**
	 * 数组转字符串
	 * 把byte数组转换成16进制字符串
	 * @param  bytes byte[]
	 * @return String 转换后的字符串
	 */
	public String toHexString(byte[] bytes) {
		StringBuilder sb = new StringBuilder(bytes.length << 1);
		for (int i = 0; i < bytes.length; i++) {
			sb.append(hexChar[(bytes[i] & 0xf0) >>> 4]);
			sb.append(hexChar[bytes[i] & 0x0f]);
		}
		return sb.toString();
	}
	
	public static void main(String[] args) throws Exception {
//		BufferedInputStream i = new BufferedInputStream(new FileInputStream("d:/123.jar"));
//		byte[] b = new byte[BYTE_MAX];
//		int count = i.read(b, 0, BYTE_MAX);
//		i.close();
//		JarMD5Checkout jarMD5Checkout = new JarMD5Checkout();
//		jarMD5Checkout.initHashCode(b);
//		System.out.println(jarMD5Checkout.getHashCode());
		
		String fileName = "C:/33333/sunline.suncard.sde.dm_1.0.0.jar";
		JarMD5Checkout jarMD5Checkout = new JarMD5Checkout();
		jarMD5Checkout.initHashCode(fileName);
		System.out.println(jarMD5Checkout.getHashCode());
	}

}
