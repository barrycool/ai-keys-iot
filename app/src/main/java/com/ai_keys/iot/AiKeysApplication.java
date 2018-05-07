package com.ai_keys.iot;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;

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
        initAsyn();
        initSyn();
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
            path = Environment.getExternalStorageDirectory().toString() + "/Espressif/";
        }
        return path;
    }

    public String getContextFilesDirPath()
    {
        return getFilesDir().toString();
    }

    private String __formatString(int value)
    {
        String strValue = "";
        byte[] ary = __intToByteArray(value);
        for (int i = ary.length - 1; i >= 0; i--)
        {
            strValue += (ary[i] & 0xFF);
            if (i > 0)
            {
                strValue += ".";
            }
        }
        return strValue;
    }

    private byte[] __intToByteArray(int value)
    {
        byte[] b = new byte[4];
        for (int i = 0; i < 4; i++)
        {
            int offset = (b.length - 1 - i) * 8;
            b[i] = (byte)((value >>> offset) & 0xFF);
        }
        return b;
    }

    public String getGateway()
    {
        WifiManager wm = (WifiManager)getSystemService(Context.WIFI_SERVICE);
        DhcpInfo d = wm.getDhcpInfo();
        return __formatString(d.gateway);
    }

    private void initSyn()
    {
//        // init db
//        DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, EspStrings.DB.DB_NAME, null);
//        SQLiteDatabase db = helper.getWritableDatabase();
//        DaoMaster daoMaster = new DaoMaster(db);
//        DaoSession daoSession = daoMaster.newSession();
//        IOTUserDBManager.init(daoSession);
//        IOTDeviceDBManager.init(daoSession);
//        EspGroupDBManager.init(daoSession);
//        IOTApDBManager.init(daoSession);
//        IOTDownloadIdValueDBManager.init(daoSession);
//        // data and data directory using seperate session for they maybe take long time
//        daoSession = daoMaster.newSession();
//        IOTGenericDataDBManager.init(daoSession);
//        IOTGenericDataDirectoryDBManager.init(daoSession);
    }

    private void initAsyn()
    {
        new Thread()
        {
            @Override
            public void run()
            {
//                InitLogger.init();
//                CachedThreadPool.getInstance();
//                WifiAdmin.getInstance();
//                EspTimeManager.getInstance().getUTCTimeLong();
            }
        }.start();
    }
}
