package com.ai_keys.iot.net;

import com.ai_keys.iot.tools.XLogger;

import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class AsyncRequest {

	private volatile static AsyncRequest instance;
	
	private ExecutorService mExecutorService;

	private HttpNet mHttpNet;

	public AsyncRequest() {
		mExecutorService = Executors.newFixedThreadPool(5);
		mHttpNet = new HttpNet();
	}
	
	public static AsyncRequest getInstance(){
		if(instance == null){
			synchronized (AsyncRequest.class) {
				if(instance == null){
					instance = new AsyncRequest();
				}
			}
		}
		return instance;
	}
	
	public void requestMessage(final String url, final String message, final RequestListener listener) {	
		mExecutorService.submit(new Runnable() {
			
			@Override
			public void run() {
				try {
					String is = mHttpNet.doHttpPost(url, message);
					listener.onComplete(is);
				} catch (Exception e) {
					XLogger.e(e.getMessage());
				}
			}
		});
	}

	public static interface RequestListener {
		public void onComplete(String response);
	}
	
	public static interface RequestListenerStream{
		public void onComplete(InputStream response);
	}
}
