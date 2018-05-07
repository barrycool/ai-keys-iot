package com.ai_keys.iot.net;

import android.text.TextUtils;

import com.ai_keys.iot.tools.XLogger;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class HttpNet {

	public static final String GET = "GET";
	public static final String POST = "POST";

	public static final String HOSTNAME = "";

	public String doHttpPost(String url, String entry) {

		HttpURLConnection connection = null;
		try {
			URL mUrl = new URL(url);
			XLogger.d(url);
			
			connection = (HttpURLConnection) mUrl.openConnection();
			connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
			connection.setRequestMethod(POST);
			connection.setReadTimeout(30 * 1000);
			connection.setConnectTimeout(10 * 1000);
			connection.setRequestProperty("Content-Type", "text/plain");
			connection.addRequestProperty("Connection", "close");
			connection.connect();
			
			if(!TextUtils.isEmpty(entry)){
	            DataOutputStream dos = new DataOutputStream(connection.getOutputStream());
	            dos.write(entry.getBytes());
	            dos.flush();
	            dos.close();
            }
			
            String outputStr = null;
            XLogger.e("getResponseCode:" + connection.getResponseCode());
			if (connection.getResponseCode() == HttpsURLConnection.HTTP_OK) {
				outputStr = streamToString(connection.getInputStream());
			}
			
			return outputStr;
		} catch (Exception e) {
			e.printStackTrace();
            XLogger.e("doHttpsPost:" + e.getMessage());
			return null;
		} finally {
			if (connection != null) {
				connection.disconnect();
				connection = null;
			}
		}
	}
	
	private String streamToString(InputStream is) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = is.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            baos.close();
            is.close();
            byte[] byteArray = baos.toByteArray();
            return new String(byteArray);
        } catch (Exception e) {
        	XLogger.e("streamToString:" + e.getMessage());
            return null;
        }
    }
}