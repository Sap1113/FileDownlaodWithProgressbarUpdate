package com.FileDownlaodWithProgressUpdate.MyWork;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import com.FileDownlaodWithProgressUpdate.MyWork.bean.DownloadFileBean;
import com.FileDownlaodWithProgressUpdate.MyWork.bll.ImageBLL;
import com.FileDownlaodWithProgressUpdate.MyWork.Services.DownloadResultReceiver;
import com.FileDownlaodWithProgressUpdate.MyWork.Services.DownloadService;
import com.myfiledownlaodencypt.R;

import static com.FileDownlaodWithProgressUpdate.MyWork.adapter.MyWorkDownloadAdapter.arrayDataList;
import static com.FileDownlaodWithProgressUpdate.MyWork.adapter.MyWorkDownloadAdapter.mDownloadResultReceiver;
import static com.FileDownlaodWithProgressUpdate.MyWork.adapter.MyWorkDownloadAdapter.mReceiver;

/**
 * Created by Shreyans.
 */

public class MainDetailsActivity extends Activity {

    Button clickMe;
    ImageBLL imageBLL;
    public static DownloadFileBean beanDownload;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mywork_details);

        position = Integer.parseInt(getIntent().getStringExtra("position"));

        clickMe = (Button) findViewById(R.id.button);

        clickMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                imageBLL = new ImageBLL(MainDetailsActivity.this);
                imageBLL.UpdateDownloading(Integer.parseInt(beanDownload.ImageID), 1);

                DownloadFileBean beanDownloadFile = beanDownload;
                beanDownloadFile.is_downloading = 1;
                arrayDataList.set(position, beanDownloadFile);

                    /* Starting Download Service */
                mReceiver = new DownloadResultReceiver(new Handler());
                mReceiver.setReceiver(mDownloadResultReceiver);
                Intent intent = new Intent(Intent.ACTION_SYNC, null,
                        MainDetailsActivity.this, DownloadService.class);
                intent.putExtra("url", beanDownload.Audio);
                intent.putExtra("receiver", mReceiver);
                intent.putExtra("position", position + "");
                startService(intent);

            }
        });

    }
}
