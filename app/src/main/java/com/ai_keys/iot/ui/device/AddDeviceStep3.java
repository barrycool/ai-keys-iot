package com.ai_keys.iot.ui.device;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ai_keys.iot.R;
import com.ai_keys.iot.account.AccountManager;
import com.ai_keys.iot.esptouch.EspWifiAdminSimple;
import com.ai_keys.iot.esptouch.EsptouchTask;
import com.ai_keys.iot.esptouch.IEsptouchListener;
import com.ai_keys.iot.esptouch.IEsptouchResult;
import com.ai_keys.iot.esptouch.IEsptouchTask;
import com.ai_keys.iot.esptouch.util.EspAES;
import com.ai_keys.iot.net.HttpManager;
import com.ai_keys.iot.net.HttpManagerInterface;
import com.ai_keys.iot.tools.Constant;
import com.ai_keys.iot.tools.XLogger;
import com.ai_keys.iot.ui.main.EspMainActivity;
import com.ai_keys.iot.util.ToastUtils;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.Socket;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class AddDeviceStep3 extends Activity{

	private EsptouchAsyncTask mTask;
	private EspWifiAdminSimple mWifiAdmin;
	private IEsptouchResult mIEsptouchResult;
	private Intent mIntent;

	private Timer progress_timer = new Timer();
	private TimerTask progress_task;
	private int max_wait_time = 60; //s

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.esp_wait_page);

		mIntent = getIntent();
		mWifiAdmin = new EspWifiAdminSimple(this);

		final ProgressBar progressBar = (ProgressBar) findViewById(R.id.pb_wait_paring_progress);
		progressBar.setProgress(0);
		progressBar.setMax(max_wait_time);

		progress_task = new TimerTask() {
			@Override
			public void run() {
				if (progressBar.getProgress() < max_wait_time) {
					progressBar.setProgress(progressBar.getProgress() + 1);
				}
				else {
					progress_timer.cancel();
				}
			}
		};
		progress_timer.schedule(progress_task, 0, 1000);

		ImageView IV_back = (ImageView) findViewById(R.id.add_device_back);
		IV_back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				finish();
			}
		});

		TextView textView = (TextView) findViewById(R.id.tv_wait_paring_cancel);
		textView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mTask != null) {
					mTask.cancelEsptouch();
				}
				startActivity(new Intent(AddDeviceStep3.this, EspMainActivity.class));
			}
		});

		doEspTouch();
	}

	private void doEspTouch()
	{
		if(mIntent == null){
			return;
		}

		String apSsid = mIntent.getStringExtra("wifiSSID");
		String apPassword = mIntent.getStringExtra("wifiPWD");
		String apBssid = mIntent.getStringExtra("bssid");
		String taskResultCountStr = mIntent.getStringExtra("taskCount");

		XLogger.d("mBtnConfirm is clicked, mEdtApSsid = " + apSsid
				+ ", " + " mEdtApPassword = " + apPassword);
		if(mTask != null) {
			mTask.cancelEsptouch();
		}
		mTask = new EsptouchAsyncTask();
		mTask.execute(apSsid, apBssid, apPassword, taskResultCountStr);
	}

	private void deviceRegister(String info){
		try {
			JSONObject infoObj = new JSONObject(info);

			JSONObject addDevice = new JSONObject();
			addDevice.put("deviceId", mIEsptouchResult.getBssid());
			addDevice.put("deviceType", infoObj.optString("deviceType"));
			addDevice.put("friendlyName", infoObj.optString("deviceType") + "-" + mIEsptouchResult.getBssid().substring(mIEsptouchResult.getBssid().length() - 4));
			addDevice.put("manufacturerName", infoObj.optString("manufacturerName"));
			addDevice.put("userId", AccountManager.getInstance().getUserInfo(this).getUserId());

			HttpManager.getInstances().requestDeviceRegister(this, addDevice.toString(), new HttpManagerInterface() {

				@Override
				public void onRequestResult(int flag, String msg) {
					if(flag == HttpManagerInterface.REQUEST_OK){
						try {
							JSONObject content = new JSONObject(msg);
							JSONObject result = new JSONObject(content.optString("result"));
							if("OK".equals(result.optString("code"))) {
								//ToastUtils.showToast(AddDeviceStep3.this, "设备注册成功");

								Intent intent = new Intent(AddDeviceStep3.this, AddDeviceStep4.class);
								intent.putExtra("deviceId", content.optString("deviceId", "unknown deviceId"));
								intent.putExtra("friendlyName", content.optString("friendlyName", "unknown friendlyName"));
								startActivity(intent);
							}
						} catch (Exception e) {
							startActivity(new Intent(AddDeviceStep3.this, PairFailActivity.class));
						}
					}else{
						startActivity(new Intent(AddDeviceStep3.this, PairFailActivity.class));
					}
				}
			});
		} catch (Exception e) {
			startActivity(new Intent(AddDeviceStep3.this, PairFailActivity.class));
		}
	}


	private void onEsptoucResultAddedPerform(final IEsptouchResult result) {
		XLogger.d("onEsptoucResultAddedPerform");

		try {
			mIEsptouchResult = result;

			getDeviceInfo();

		} catch (Exception e) {

		}
	}

	private Socket socket;

	private void getDeviceInfo(){

		XLogger.d("getDeviceInfo");

		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					XLogger.d("IP:" + mIEsptouchResult.getInetAddress().getHostAddress());

					socket = new Socket(mIEsptouchResult.getInetAddress().getHostAddress(), 55555);
					InputStream is = socket.getInputStream();
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					int i=-1;
					while(  (i=is.read()) !=-1  ){
						baos.write(i);
					}
					String result = baos.toString();

					XLogger.d("getUserInfo result=" + result);
					is.close();
					socket.close();

					deviceRegister(result);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	private IEsptouchListener myListener = new IEsptouchListener() {

		@Override
		public void onEsptouchResultAdded(final IEsptouchResult result) {
			onEsptoucResultAddedPerform(result);
		}
	};

	private class EsptouchAsyncTask extends AsyncTask<String, Void, List<IEsptouchResult>> {

		// without the lock, if the user tap confirm and cancel quickly enough,
		// the bug will arise. the reason is follows:
		// 0. task is starting created, but not finished
		// 1. the task is cancel for the task hasn't been created, it do nothing
		// 2. task is created
		// 3. Oops, the task should be cancelled, but it is running
		private final Object mLock = new Object();
		private ProgressDialog mProgressDialog;
		private IEsptouchTask mEsptouchTask;

		public void cancelEsptouch() {
			cancel(true);
			if (mProgressDialog != null) {
				mProgressDialog.dismiss();
			}
			if (mEsptouchTask != null) {
				mEsptouchTask.interrupt();
			}
		}

		@Override
		protected void onPreExecute() {
			/*mProgressDialog = new ProgressDialog(AddDeviceStep3.this);
			mProgressDialog
					.setMessage("Esptouch is configuring, please wait for a moment...");
			mProgressDialog.setCanceledOnTouchOutside(false);
			mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					synchronized (mLock) {
						XLogger.d("progress dialog is canceled");
						if (mEsptouchTask != null) {
							mEsptouchTask.interrupt();
						}
					}
				}
			});
			mProgressDialog.setButton(DialogInterface.BUTTON_POSITIVE,
					"Waiting...", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							XLogger.d("progress dialog is ok");
							if(mIEsptouchResult != null){
								Intent intent = new Intent(Constant.DEVICE_ADD_COMPLETE);
								intent.putExtra("bssid", mIEsptouchResult.getBssid());
								intent.putExtra("ip", mIEsptouchResult.getInetAddress().getHostAddress());
								LocalBroadcastManager.getInstance(AddDeviceStep3.this).sendBroadcast(intent);

								setResult(RESULT_OK, intent);
								finish();
							}
						}
					});
			mProgressDialog.show();
			mProgressDialog.getButton(DialogInterface.BUTTON_POSITIVE)
					.setEnabled(false);*/
		}

		@Override
		protected List<IEsptouchResult> doInBackground(String... params) {
			int taskResultCount = -1;
			synchronized (mLock) {
				// !!!NOTICE
				String apSsid = mWifiAdmin.getWifiConnectedSsidAscii(params[0]);
				String apBssid = params[1];
				String apPassword = params[2];
				String taskResultCountStr = params[3];
				taskResultCount = Integer.parseInt(taskResultCountStr);
				boolean useAes = false;
				if (useAes) {
					byte[] secretKey = "1234567890123456".getBytes(); // TODO modify your own key
					EspAES aes = new EspAES(secretKey);
					mEsptouchTask = new EsptouchTask(apSsid, apBssid, apPassword, aes, AddDeviceStep3.this);
				} else {
					mEsptouchTask = new EsptouchTask(apSsid, apBssid, apPassword, null, AddDeviceStep3.this);
				}
				mEsptouchTask.setEsptouchListener(myListener);
			}
			List<IEsptouchResult> resultList = mEsptouchTask.executeForResults(taskResultCount);
			return resultList;
		}

		@Override
		protected void onPostExecute(List<IEsptouchResult> result) {
			/*mProgressDialog.show();
			mProgressDialog.getButton(DialogInterface.BUTTON_POSITIVE)
					.setEnabled(true);
			mProgressDialog.getButton(DialogInterface.BUTTON_POSITIVE).setText(
					"OK");
			if (result == null) {
				mProgressDialog.setMessage("Create Esptouch task failed, the esptouch port could be used by other thread");
				return;
			}*/

			IEsptouchResult firstResult = result.get(0);
			// check whether the task is cancelled and no results received
			if (!firstResult.isCancelled()) {
				int count = 0;
				// max results to be displayed, if it is more than maxDisplayCount,
				// just show the count of redundant ones
				final int maxDisplayCount = 5;
				// the task received some results including cancelled while
				// executing before receiving enough results
				if (firstResult.isSuc()) {
					StringBuilder sb = new StringBuilder();
					for (IEsptouchResult resultInList : result) {
						sb.append("Esptouch success, bssid = "
								+ resultInList.getBssid()
								+ ",InetAddress = "
								+ resultInList.getInetAddress()
								.getHostAddress() + "\n");
						count++;
						if (count >= maxDisplayCount) {
							break;
						}
					}
					if (count < result.size()) {
						sb.append("\nthere's " + (result.size() - count)
								+ " more result(s) without showing\n");
					}
					//mProgressDialog.setMessage(sb.toString());
				} else {
					//mProgressDialog.setMessage("Esptouch fail");
					startActivity(new Intent(AddDeviceStep3.this, PairFailActivity.class));
					finish();
				}
			}
		}
	}
}
