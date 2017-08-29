package com.FileDownlaodWithProgressUpdate.MyWork.adapter;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.FileDownlaodWithProgressUpdate.MyWork.CallBack.UpdateDownloadCallBack;
import com.FileDownlaodWithProgressUpdate.MyWork.CallBack.UpdateProgressCallBack;
import com.FileDownlaodWithProgressUpdate.MyWork.DownloadFIleAsync.DownloadingAsynTask;
import com.FileDownlaodWithProgressUpdate.MyWork.MainRecyclerViewActivity;
import com.FileDownlaodWithProgressUpdate.MyWork.Services.DownloadResultReceiver;
import com.FileDownlaodWithProgressUpdate.MyWork.Services.DownloadService;
import com.FileDownlaodWithProgressUpdate.MyWork.bean.DownloadFileBean;
import com.FileDownlaodWithProgressUpdate.MyWork.bll.ImageBLL;
import com.FileDownlaodWithProgressUpdate.MyWork.uc.SimpleCircularProgressbar;
import com.myfiledownlaodencypt.R;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hexagon on 1/6/17.
 */

public class MyWorkDownloadAdapter extends RecyclerView.Adapter<MyWorkDownloadAdapter.msgItemViewHolder>
        implements UpdateDownloadCallBack, UpdateProgressCallBack {
    static String TAG = "Shreyans";
    Activity context;
    public static ArrayList<DownloadFileBean> arrayDataList;
    DownloadFileBean beanCategory = new DownloadFileBean();
    DownloadingAsynTask downloadTask;
    public static UpdateDownloadCallBack downloadCallBack;
    public static UpdateProgressCallBack ProgressCallBack;

    static RecyclerView recyclerView_list;
    public static final String MESSAGE_PROGRESS = "message_progress";
    ImageBLL imageBLL;

    //    public static ArrayList<SimpleCircularProgressbar> arrayProgressBar = new ArrayList<>();
    //    public static ArrayList<TextView> arrayTextview = new ArrayList<>();
    public static DownloadResultReceiver mReceiver;
    static LinearLayoutManager layoutManager;

    public MyWorkDownloadAdapter(RecyclerView recyclerView_list, Activity activity,
                                 ArrayList<DownloadFileBean> arrayDataList,
                                 LinearLayoutManager layoutManager) {
        this.recyclerView_list = recyclerView_list;
        this.context = activity;
        this.arrayDataList = arrayDataList;
        downloadCallBack = this;
        ProgressCallBack = this;
        this.layoutManager = layoutManager;
    }

    @Override
    public void onDownloadComplete(int pos, int status) {
        imageBLL = new ImageBLL(context);
        imageBLL.UpdateDownloading(Integer.parseInt(arrayDataList.get(pos).ImageID), status);
        DownloadFileBean beanDownload = arrayDataList.get(pos);
        beanDownload.is_downloading = status;
        arrayDataList.set(pos, beanDownload);
        notifyItemChanged(pos);
    }


    public void updateProgressbar(final int progressCount, final int position) {
        notifyItemChanged(position);
    }

    @Override
    public void onUpdateProgress() {
//        notifyDataSetChanged();
    }

    @Override
    public msgItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.mywork_recyclerview_row, null);
        msgItemViewHolder rcv = new msgItemViewHolder(layoutView);

        return rcv;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public void onBindViewHolder(final MyWorkDownloadAdapter.msgItemViewHolder holder,
                                 final int position) {

        DownloadFileBean beanDownload = this.arrayDataList.get(position);
        Picasso.with(context).load(beanDownload.ImagePathFull)
                .memoryPolicy(MemoryPolicy.NO_CACHE).into(holder.imageView);

        if (beanDownload.is_downloading == 2) {
            holder.progress.setVisibility(View.GONE);
            holder.btn_download.setVisibility(View.VISIBLE);
            holder.btn_download.setBackgroundResource(R.drawable.icon_music);
        } else if (beanDownload.is_downloading == 1) {
            holder.progress.setVisibility(View.VISIBLE);
            holder.btn_download.setVisibility(View.GONE);
            holder.progress.setProgress(beanDownload.progress);
        } else {
            holder.progress.setVisibility(View.GONE);
            holder.btn_download.setVisibility(View.VISIBLE);
            holder.btn_download.setBackgroundResource(R.drawable.icon_download);
        }

        // ColImgID
        holder.txtImgID.setText("ID : " + beanDownload.ImageID.toString() + " position :: " + position);
        // ColImgName
        holder.txtPicName.setText("Name : " + beanDownload.ImageName.toString());
        holder.btn_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (arrayDataList.get(position).is_downloading == 0) {
                    Log.e("Shreyans ", "=-=-=-=-=-=-= ImageId :: " + arrayDataList.get(position).ImageID);
                    holder.progress.setVisibility(View.VISIBLE);
                    imageBLL = new ImageBLL(context);
                    imageBLL.UpdateDownloading(Integer.parseInt(arrayDataList.get(position).ImageID), 1);
                    DownloadFileBean beanDownload = arrayDataList.get(position);
                    beanDownload.is_downloading = 1;
                    arrayDataList.set(position, beanDownload);
                    notifyItemChanged(position);
                    beanDownload.progress = 0;

                    /* Starting Download Service */
                    if (context instanceof MainRecyclerViewActivity) {
                        mReceiver = new DownloadResultReceiver(new Handler());
                        mReceiver.setReceiver(mDownloadResultReceiver);
                        Intent intent = new Intent(Intent.ACTION_SYNC, null, context, DownloadService.class);
                        intent.putExtra("url", arrayDataList.get(position).Audio);
                        intent.putExtra("receiver", mReceiver);
                        intent.putExtra("position", position + "");
                        ((MainRecyclerViewActivity) context).startService(intent);
                    }

//                    downloadTask = new DownloadingAsynTask(context, downloadCallBack, ProgressCallBack, position);
////                downloadTask.execute(arrayDataList.get(position).Audio);
//                    downloadTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, arrayDataList.get(position).Audio);
                }
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick: position  " + position);
                String sdcard_path = context.getCacheDir().getPath();
                List<File> files = getListFiles(new File(sdcard_path + "/MyNEW/"));
                Log.e(TAG, "onClick: FILE SIZE:  " + files.size());
                if (files.size() > 0) {
                    for (File f : files)
                        Log.e(TAG, "onClick: FILE getPath:  " + f.getPath().toString());
                }
//                MainDetailsActivity.beanDownload = new DownloadFileBean();
//                MainDetailsActivity.beanDownload = arrayDataList.get(position);
//                Intent intent = new Intent(context, MainDetailsActivity.class);
//                intent.putExtra("position", position + "");
//                context.startActivity(intent);
            }
        });
    }

    private List<File> getListFiles(File parentDir) {
        ArrayList<File> inFiles = new ArrayList<File>();
        File[] files = parentDir.listFiles();
        if (files.length > 0) {
            for (File file : files) {
                if (file.isDirectory()) {
                    inFiles.addAll(getListFiles(file));
                } else {
                    if (file.getName().endsWith(".mp3")) {
                        inFiles.add(file);
                    }
                }
            }
        }
        return inFiles;
    }

    public static DownloadResultReceiver.Receiver mDownloadResultReceiver = new DownloadResultReceiver.Receiver() {
        @Override
        public void onReceiveResult(int resultCode, Bundle resultData) {
            int progress = resultData.getInt("progress");
            int position = resultData.getInt("position");
            switch (resultCode) {
                case DownloadService.STATUS_RUNNING_PROGRESS:
                    Log.e(TAG, "onReceiveResult: Progress : " + resultData.getInt("progress"));
                    Log.e(TAG, "onReceiveResult: position : " + resultData.getInt("position"));
                    DownloadFileBean beanDownload = arrayDataList.get(position);
                    beanDownload.progress = progress;
                    arrayDataList.set(position, beanDownload);

                    int first = layoutManager.findFirstVisibleItemPosition();
                    int last = layoutManager.findLastVisibleItemPosition();
                    if (position < first || position > last) {
                        //just update your DataSet
                        //the next time getView is called
                        //the ui is updated automatically
                    } else {
                        View convertView = recyclerView_list.getChildAt(position - first);
                        //this is the convertView that you previously returned in getView
                        //just fix it (for example:)
                        SimpleCircularProgressbar bar = (SimpleCircularProgressbar) convertView.findViewById(R.id.progressBar);
                        bar.setProgress(progress);
                    }


//                    notifyDataSetChanged();
                    break;
                case DownloadService.STATUS_RUNNING:
                    Log.e(TAG, "*&*&*&  onReceiveResult: RUNNING:: ");
                    break;
                case DownloadService.STATUS_FINISHED:
                    String result = resultData.getString("result");
                    Log.e(TAG, "onReceiveResult: result :: " + result);
                    break;
            }
        }
    };

    class FileExtensionFilter implements FilenameFilter {
        public boolean accept(File dir, String name) {
            return (name.endsWith(".mp3") || name.endsWith(".MP3"));
        }
    }

    @Override
    public int getItemCount() {
        return arrayDataList.size();
    }

    public class msgItemViewHolder extends RecyclerView.ViewHolder {
        public TextView txtImgID;
        public TextView txtPicName;
        public TextView txtTempProgressCount;
        public ImageView btn_download;
        //        public ProgressBar progress;
        public SimpleCircularProgressbar progress;
        //        public CircularProgressBar progress;
        public ImageView imageView;

        public msgItemViewHolder(final View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.ColImgPath);
            imageView.getLayoutParams().height = 80;
            imageView.getLayoutParams().width = 80;
            imageView.setPadding(10, 10, 10, 10);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

            txtImgID = (TextView) itemView.findViewById(R.id.ColImgID);
            txtImgID.setPadding(10, 0, 0, 0);

            txtPicName = (TextView) itemView.findViewById(R.id.ColImgName);
            txtPicName.setPadding(10, 0, 0, 0);

            txtTempProgressCount = (TextView) itemView.findViewById(R.id.txt_tempProgressCount);
//            txtTempProgressCount.setPadding(10, 0, 0, 0);

            btn_download = (ImageView) itemView.findViewById(R.id.btn_download);
//            btn_download.setTag(getPosition() + "");

            progress = (SimpleCircularProgressbar) itemView.findViewById(R.id.progressBar);
            progress.setPadding(10, 0, 10, 0);
            progress.setVisibility(View.GONE);
            progress.setProgress(0);
            progress.setCircleColor(Color.parseColor("#303F9F"),
                    Color.parseColor("#FF4081"));
        }
    }

    public void updateProgress() {
        notifyDataSetChanged();
    }

    public void update(ArrayList<DownloadFileBean> modelList) {
        arrayDataList.clear();
        for (DownloadFileBean model : modelList) {
            arrayDataList.add(model);
        }
        notifyDataSetChanged();
    }

    public void remove(int position) {
        arrayDataList.remove(position);
        //notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    public void set(int position, DownloadFileBean beanCategory) {
        arrayDataList.set(position, beanCategory);
        notifyDataSetChanged();
    }

    public void getListofDownload() {

        try {
//            String dirPath=Environment.getExternalStorageDirectory().getAbsolutePath()+"/Download/";
//            String selection =MediaStore.Audio.Media.DATA +" like ?";
//            String[] projection = {MediaStore.Audio.Media.DATA};
//            String[] selectionArgs={dirPath+"%"};
//            Cursor cur = ((Activity) context).managedQuery(
//                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
//                    projection,
//                    selection,
//                    selectionArgs,
//                    null);

            String[] STAR = {"*"};
            Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            String selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0";
            String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";
//
//            Cursor cur = ((Activity) context).getContentResolver().query(uri, STAR, selection, null, sortOrder);
            Cursor cur = ((Activity) context).getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, MediaStore.Audio.Media.DATA + " like ? ",
                    new String[]{"%Download%"}, sortOrder);

            int count = 0;

            if (cur != null) {
                count = cur.getCount();

                Log.e("shreyans", "getListofDownload:count " + count + "");
                if (count > 0) {
                    while (cur.moveToNext()) {
                        String data = cur.getString(cur.getColumnIndex(MediaStore.Audio.Media.DATA));
                        String id = cur.getString(cur.getColumnIndex(MediaStore.Audio.Media._ID));
                        String displayName = cur.getString(cur.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
                        String title = cur.getString(cur.getColumnIndex(MediaStore.Audio.Media.TITLE));
                        String artist = cur.getString(cur.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                        String duration = cur.getString(cur.getColumnIndex(MediaStore.Audio.Media.DURATION));

                        Log.e("shreyans", "*******************************");
                        Log.e("shreyans", "data : " + data);
                        Log.e("shreyans", "id : " + id);
                        Log.e("shreyans", "displayName : " + displayName);
                        Log.e("shreyans", "title : " + title);
                        Log.e("shreyans", "artist : " + artist);
                        Log.e("shreyans", "duration : " + duration);
                        Log.e("shreyans", "*******************************");
                        // Add code to get more column here

                        // Save to your list here
                    }

                }
            }

            cur.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}