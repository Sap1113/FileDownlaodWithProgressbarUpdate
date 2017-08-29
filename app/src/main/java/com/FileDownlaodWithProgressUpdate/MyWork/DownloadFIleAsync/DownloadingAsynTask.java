package com.FileDownlaodWithProgressUpdate.MyWork.DownloadFIleAsync;

import android.content.Context;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.ResultReceiver;
import android.util.Log;
import android.widget.Toast;

import com.FileDownlaodWithProgressUpdate.MyWork.CallBack.UpdateDownloadCallBack;
import com.FileDownlaodWithProgressUpdate.MyWork.CallBack.UpdateProgressCallBack;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.FileDownlaodWithProgressUpdate.MyWork.Services.DownloadService.STATUS_FINISHED;
import static com.FileDownlaodWithProgressUpdate.MyWork.Services.DownloadService.STATUS_RUNNING_PROGRESS;

/**
 * Created by Shreyans.
 */
public class DownloadingAsynTask extends AsyncTask<String, Integer, String> {

    Context context;
    private PowerManager.WakeLock mWakeLock;
    //    ProgressDialog mProgressDialog;
    private static final int MEGABYTE = 1024 * 1024;
    DownloadingAsynTask downloadTask;
    String Name;
    UpdateDownloadCallBack downloadCallBack;
    UpdateProgressCallBack ProgressCallBack;
    int position;

    String TAG = "Shreyans";
    IntentFilter intentFilter;
    ResultReceiver receiver;

    public DownloadingAsynTask(Context context, UpdateDownloadCallBack downloadCallBack,
                               UpdateProgressCallBack ProgressCallBack, int postion, ResultReceiver receiver) {
        this.context = context;
        this.downloadCallBack = downloadCallBack;
        this.ProgressCallBack = ProgressCallBack;
        this.position = postion;
        this.receiver = receiver;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                getClass().getName());
        mWakeLock.acquire();

    }


    @Override
    protected String doInBackground(String... str) {

        String URL = str[0];
        // Get File Name from URL
        Name = URL.substring(URL.lastIndexOf('/') + 1, URL.length());
        InputStream input = null;
        OutputStream output = null;
        HttpURLConnection connection = null;
        try {
            java.net.URL url = new URL(URL);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            // expect HTTP 200 OK, so we don't mistakenly save error report
            // instead of the file
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return "Server returned HTTP " + connection.getResponseCode()
                        + " " + connection.getResponseMessage();
            }

            // this will be useful to display download percentage
            // might be -1: server did not report the length
            int fileLength = connection.getContentLength();

            // download the file
            input = connection.getInputStream();

//            String sdcard_path = Environment.getExternalStorageDirectory().getPath();
            String sdcard_path = context.getCacheDir().getPath();
            // create a File object for the parent directory
            File PapersDiractory = new File(sdcard_path + "/MyNEW");
            // have the object build the directory structure, if needed.
            PapersDiractory.mkdirs();
            // create a File object for the output file
            File outputFile = new File(PapersDiractory, "/" + Name);
            // now attach the OutputStream to the file object, instead of a String representation
            output = new FileOutputStream(outputFile);

            byte data[] = new byte[MEGABYTE];
            long total = 0;
            int count;
            while ((count = input.read(data)) != -1) {
                // allow canceling with back button
                if (isCancelled()) {
                    input.close();
                    return null;
                }
                total += count;
                // publishing the progress....
                if (fileLength > 0) // only if total length is known
                    publishProgress((int) (total * 100 / fileLength));
                int progress = (int) (total * 100 / fileLength);
//                Log.d("doInBackground ", " Progress : " + progress);
                Bundle bundle = new Bundle();
                bundle.putInt("progress", progress);
                bundle.putInt("position", position);
                receiver.send(STATUS_RUNNING_PROGRESS, bundle);

                output.write(data, 0, count);
            }
        } catch (Exception e) {
            return e.toString();
        } finally {
            try {
                if (output != null)
                    output.close();
                if (input != null)
                    input.close();
            } catch (IOException ignored) {
            }

            if (connection != null)
                connection.disconnect();
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        super.onProgressUpdate(progress);
//        Log.d("Progress = ", "" + progress[0]);


    }

    @Override
    protected void onPostExecute(String result) {
        mWakeLock.release();
//        mProgressDialog.dismiss();
        if (result != null) {
            Log.e("Shreyans ", "onPostExecute: Download error: " + result);
            Toast.makeText(context, "Download error: " + result, Toast.LENGTH_LONG).show();
            Bundle bundle = new Bundle();
            bundle.putString("result", "Download error: " + result);
            receiver.send(STATUS_FINISHED, bundle);
            downloadCallBack.onDownloadComplete(position, 0);
        } else {
            Toast.makeText(context, "File downloaded", Toast.LENGTH_SHORT).show();
            Bundle bundle = new Bundle();
            bundle.putString("result", "Sucessfully File Download");
            receiver.send(STATUS_FINISHED, bundle);
            downloadCallBack.onDownloadComplete(position, 2);
//            FileEncryptorAndDecryptor.decryptFile(Name);
        }
    }

}