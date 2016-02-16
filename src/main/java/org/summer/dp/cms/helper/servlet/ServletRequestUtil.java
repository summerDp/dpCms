package org.summer.dp.cms.helper.servlet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServletRequestUtil {

	private static Logger log = LoggerFactory.getLogger(ServletRequestUtil.class);

	private static final String CHARSET = "UTF-8";

	public static String IMAGE_UPLOAD_REALPATH = "E:/uploads";
	
	public static String IMAGE_DOMAIN = "http://192.168.1.91:8081/upload";
	
	
	/**
	 * 获取HttpServletRequest请求post数据流字符串
	 * 
	 * @param request
	 * @return
	 */
	public static String getPostData(HttpServletRequest request) {
		String postData = null;
		InputStream is = null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			is = request.getInputStream();
			int ch;
			while ((ch = is.read()) != -1) {
				baos.write(ch);
			}
			postData = new String(baos.toByteArray(), CHARSET);
		} catch (IOException e) {
			log.error(e.getMessage());
		} finally {
			if (baos != null) {
				try {
					baos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return postData;
	}

	/**
	 * 将字符串流的方式通过POST传输
	 * 
	 * @param url
	 * @param data
	 * @return
	 * @throws IOException
	 */
	public static String post(String url, String data) throws IOException {
		URL netUrl = new URL(url);
		HttpURLConnection conn = null;
		OutputStream os = null;
		InputStream is = null;
		ByteArrayOutputStream baos = null;
		byte[] ret = null;
		try {
			conn = (HttpURLConnection) netUrl.openConnection();
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/octet-stream");
			os = conn.getOutputStream();
			os.write(data.getBytes(CHARSET));
			os.flush();
			is = conn.getInputStream();
			int ch;
			baos = new ByteArrayOutputStream();
			while ((ch = is.read()) != -1) {
				baos.write(ch);
			}
			ret = baos.toByteArray();
		} catch (IOException e) {
			log.error(e.getMessage());
		} finally {
			if (baos != null) {
				try {
					baos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
				}
			}
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
				}
			}
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
				}
			}
		}
		return new String(ret, "UTF-8");
	}

}
