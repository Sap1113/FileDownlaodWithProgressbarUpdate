package com.FileDownlaodWithProgressUpdate.MyWork.util;

import android.content.Context;
import android.os.Environment;

public class Const {

//    public static Context CONTEXT;

    public static final String DB_NAME = "file_download.db";

    public static final String PREF_FILE = "file_download_PREF";

//	public static final String HOST = "http://192.168.1.11:8080/pdfreader";
    //public static final String HOST = "http://203.124.96.20:8080/bsb";

    //public static final String HOST = "http://android-experts.co.in/pdfreader";

//	public static final String API_BASE_URL = HOST + "/api";
//	public static final String USER_DATA_URL = HOST + "/userdata";

    public static String APP_HOME = Environment.getExternalStorageDirectory()
            .getPath() + "/file_download";

    public static String LOG_DIR = APP_HOME + "/Log";
    public static String LOG_ZIP = APP_HOME + "/file_downloadLog.zip";
    public static String USER_DATA = APP_HOME + "/UserData";


    // Pref Variables
    public static String START_DOWNLOADING = "START_DOWNLOADING";
}