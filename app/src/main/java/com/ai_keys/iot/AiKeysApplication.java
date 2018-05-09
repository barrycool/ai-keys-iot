package com.ai_keys.iot;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Environment;

import com.ai_keys.iot.ui.main.EspMainActivity;

public class AiKeysApplication extends Application{
    private static AiKeysApplication instance;

    public static final String version = "1.0.0";

    public static AiKeysApplication sharedInstance()
    {
        if (instance == null)
        {
            throw new NullPointerException(
                    "EspApplication instance is null, please register in AndroidManifest.xml first");
        }
        return instance;
    }

    public static boolean SUPPORT_APK_UPGRADE = true;

    @Override
    public void onCreate()
    {
        super.onCreate();
        instance = this;
    }

    public Context getContext()
    {
        return getBaseContext();
    }

    public Resources getResources()
    {
        return getBaseContext().getResources();
    }

    public String getVersionName()
    {
        String version = "";
        try
        {
            PackageInfo pi = getPackageManager().getPackageInfo(getPackageName(), 0);
            version = pi.versionName;
        }
        catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
            version = "Not found version";
        }
        return version;
    }

    public int getVersionCode()
    {
        int code = 0;
        try
        {
            PackageInfo pi = getPackageManager().getPackageInfo(getPackageName(), 0);
            code = pi.versionCode;
        }
        catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
        }
        return code;
    }

    public String getEspRootSDPath()
    {
        String path = null;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
        {
            path = Environment.getExternalStorageDirectory().toString() + "/Ai-keys/";
        }
        return path;
    }

    public String getContextFilesDirPath()
    {
        return getFilesDir().toString();
    }


    public static void onExitApp(final Activity activity) {

        new AlertDialog.Builder(activity).setMessage(R.string.esp_main_exit_message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new FinishTask(activity).execute();
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }

    private static class FinishTask extends AsyncTask<Void, Void, Void> {
        private ProgressDialog mDialog;
        private Activity mActivity;

        public FinishTask(Activity activity) {
            mActivity = activity;
        }

        @Override
        protected void onPreExecute() {
            mDialog = new ProgressDialog(mActivity);
            mDialog.setMessage(mActivity.getString(R.string.esp_main_exiting));
            mDialog.setCancelable(false);
            mDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (mDialog != null) {
                mDialog.dismiss();
                mDialog = null;
            }
            System.exit(0);
        }
    }
}
