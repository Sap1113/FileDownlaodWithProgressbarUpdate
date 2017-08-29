package com.FileDownlaodWithProgressUpdate.MyWork.Services;

import android.app.IntentService;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.util.Log;

import com.FileDownlaodWithProgressUpdate.MyWork.DownloadFIleAsync.DownloadingAsynTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.FileDownlaodWithProgressUpdate.MyWork.adapter.MyWorkDownloadAdapter.ProgressCallBack;
import static com.FileDownlaodWithProgressUpdate.MyWork.adapter.MyWorkDownloadAdapter.downloadCallBack;

/**
 * Created by Shreyans.
 */

public class DownloadService extends IntentService {

    public static final int STATUS_RUNNING = 0;
    public static final int STATUS_RUNNING_PROGRESS = 3;
    public static final int STATUS_FINISHED = 1;
    public static final int STATUS_ERROR = 2;
    int i = 0;
    private static final String TAG = "DownloadService";
    DownloadingAsynTask downloadTask;

    public DownloadService() {
        super(DownloadService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Log.d(TAG, "Service Started!");

        final ResultReceiver receiver = intent.getParcelableExtra("receiver");
        final int position = Integer.parseInt(intent.getStringExtra("position"));
        String url = intent.getStringExtra("url");
//        String url = "https://dl.pagal.link/upload_file/367/382/7491/PagalWorld%20-%20Bollywood%20Mp4%20Video%20Songs%202016/Dangal%20%282016%29%20Mp4%20Video%20Songs/Dhaakad%20%28Aamir%20Khan%20Version%29%20-%20Dangal%20MP4.mp4";

        final Bundle bundle = new Bundle();

        if (!TextUtils.isEmpty(url)) {
            /* Update UI: Download Service is Running */
            receiver.send(STATUS_RUNNING, Bundle.EMPTY);
            downloadTask = new DownloadingAsynTask(this, downloadCallBack, ProgressCallBack, position, receiver);
            downloadTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url);

//            int i = 0;
//            while (i <= 100) {
//                try {
//                    Thread.sleep(100);
//                    i++;
//                    Log.e(TAG, "i: " + i);
//                    bundle.putInt("progress", i);
//                    receiver.send(STATUS_RUNNING_PROGRESS, bundle);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }

//            try {
//                String[] results = downloadData(url);
//
//                /* Sending result back to activity */
//                if (null != results && results.length > 0) {
//                    bundle.putStringArray("result", results);
//                    receiver.send(STATUS_FINISHED, bundle);
//                }
//            } catch (Exception e) {
//
//                /* Sending error message back to activity */
//                bundle.putString(Intent.EXTRA_TEXT, e.toString());
//                receiver.send(STATUS_ERROR, bundle);
//            }
        }
        Log.d(TAG, "Service Stopping!");
        this.stopSelf();
    }

    private String[] downloadData(String requestUrl) throws IOException, DownloadException {
        InputStream inputStream = null;

        HttpURLConnection urlConnection = null;

        /* forming th java.net.URL object */
        URL url = new URL(requestUrl);

        urlConnection = (HttpURLConnection) url.openConnection();

        /* optional request header */
        urlConnection.setRequestProperty("Content-Type", "application/json");

        /* optional request header */
        urlConnection.setRequestProperty("Accept", "application/json");

        /* for Get request */
        urlConnection.setRequestMethod("GET");

        int statusCode = urlConnection.getResponseCode();

        /* 200 represents HTTP OK */
        if (statusCode == 200) {
            inputStream = new BufferedInputStream(urlConnection.getInputStream());

            String response = convertInputStreamToString(inputStream);

            String[] results = parseResult(response);

            return results;
        } else {
            throw new DownloadException("Failed to fetch data!!");
        }
    }

    private String convertInputStreamToString(InputStream inputStream) throws IOException {

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";

        while ((line = bufferedReader.readLine()) != null) {
            result += line;
        }

            /* Close Stream */
        if (null != inputStream) {
            inputStream.close();
        }

        return result;
    }

    private String[] parseResult(String result) {

        String[] blogTitles = null;
        try {
            JSONObject response = new JSONObject(result);

            JSONArray posts = response.optJSONArray("posts");

            blogTitles = new String[posts.length()];

            for (int i = 0; i < posts.length(); i++) {
                JSONObject post = posts.optJSONObject(i);
                String title = post.optString("title");

                blogTitles[i] = title;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return blogTitles;
    }

    public class DownloadException extends Exception {

        public DownloadException(String message) {
            super(message);
        }

        public DownloadException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
