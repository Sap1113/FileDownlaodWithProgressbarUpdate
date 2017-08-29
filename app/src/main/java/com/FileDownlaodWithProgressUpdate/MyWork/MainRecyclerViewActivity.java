package com.FileDownlaodWithProgressUpdate.MyWork;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Window;

import com.FileDownlaodWithProgressUpdate.MyWork.bean.DownloadFileBean;
import com.FileDownlaodWithProgressUpdate.MyWork.bll.ImageBLL;
import com.FileDownlaodWithProgressUpdate.MyWork.uc.LoadImageBitmap;
import com.FileDownlaodWithProgressUpdate.MyWork.util.Utils;
import com.FileDownlaodWithProgressUpdate.MyWork.adapter.MyWorkDownloadAdapter;
import com.myfiledownlaodencypt.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainRecyclerViewActivity extends AppCompatActivity {

    private RecyclerView recyclerView_list;
    private static MyWorkDownloadAdapter imageAdapter;
    private Handler handler = new Handler();

    static ArrayList<DownloadFileBean> MyArrList = new ArrayList<>();
    DownloadFileBean bean = new DownloadFileBean();
    ImageBLL bll = new ImageBLL(MainRecyclerViewActivity.this);
    LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // ProgressBar
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        setContentView(R.layout.mywork_recyclerview);

        Utils.systemUpgrade(this);

        recyclerView_list = (RecyclerView) findViewById(R.id.recyclerView_list);

        layoutManager = (LinearLayoutManager) recyclerView_list.getLayoutManager();

        bll = new ImageBLL(this);
        MyArrList = bll.SelectAll();
        if (MyArrList.size() == 0) {
            new LoadContentFromServer().execute();
        } else {
            imageAdapter = new MyWorkDownloadAdapter(recyclerView_list, MainRecyclerViewActivity.this, MyArrList, layoutManager);
            recyclerView_list.setAdapter(imageAdapter);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 11);
            } else {

            }
        } else {

        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 11: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    new Handler().post(new Runnable() {
                        public void run() {

                        }
                    });
                }
            }
        }
    }

    class LoadContentFromServer extends AsyncTask<Object, Integer, Object> {

        protected void onPreExecute() {
            super.onPreExecute();
            setProgressBarIndeterminateVisibility(true);
        }

        @Override
        protected Object doInBackground(Object... params) {

            String jsonStr = "[{\"ImageID\":\"1\",\"ImageName\":\"Image 1\",\"ImagePath_Thumbnail\":\"https://s-media-cache-ak0.pinimg.com/originals/b1/bb/ec/b1bbec499a0d66e5403480e8cda1bcbe.png\",\"ImagePath_FullPhoto\":\"https://s-media-cache-ak0.pinimg.com/originals/b1/bb/ec/b1bbec499a0d66e5403480e8cda1bcbe.png\",\"Audio\":\"http://podcast-files.cnet.com/podcast/cnetbuzz_042307.mp3\"},{\"ImageID\":\"2\",\"ImageName\":\"Image 2\",\"ImagePath_Thumbnail\":\"https://image.flaticon.com/icons/png/128/145/145867.png\",\"ImagePath_FullPhoto\":\"https://image.flaticon.com/icons/png/128/145/145867.png\",\"Audio\":\"http://www.kozco.com/tech/piano2-CoolEdit.mp3\"},{\"ImageID\":\"3\",\"ImageName\":\"Image 3\",\"ImagePath_Thumbnail\":\"http://icons.iconarchive.com/icons/designbolts/free-male-avatars/128/Male-Avatar-icon.png\",\"ImagePath_FullPhoto\":\"http://icons.iconarchive.com/icons/designbolts/free-male-avatars/128/Male-Avatar-icon.png\",\"Audio\":\"http://www.kozco.com/tech/organfinale.mp3\"},{\"ImageID\":\"4\",\"ImageName\":\"Image 4\",\"ImagePath_Thumbnail\":\"https://s-media-cache-ak0.pinimg.com/originals/6a/83/f0/6a83f0ec5f43944c47df1b6cff801578.png\",\"ImagePath_FullPhoto\":\"https://s-media-cache-ak0.pinimg.com/originals/6a/83/f0/6a83f0ec5f43944c47df1b6cff801578.png\",\"Audio\":\"http://www.kozco.com/tech/32.mp3\"},{\"ImageID\":\"5\",\"ImageName\":\"Image 5\",\"ImagePath_Thumbnail\":\"http://kessler-aqua.de/wp-content/uploads/2015/10/flat-faces-icons-circle-man-2.png\",\"ImagePath_FullPhoto\":\"http://kessler-aqua.de/wp-content/uploads/2015/10/flat-faces-icons-circle-man-2.png\",\"Audio\":\"http://www.kozco.com/tech/c304-2.wav\"},{\"ImageID\":\"6\",\"ImageName\":\"Image 6\",\"ImagePath_Thumbnail\":\"http://www.iconninja.com/files/912/138/492/female-young-woman-user-avatar-person-icon.png\",\"ImagePath_FullPhoto\":\"http://www.iconninja.com/files/912/138/492/female-young-woman-user-avatar-person-icon.png\",\"Audio\":\"http://users.skynet.be/fa046054/home/P22/track80.mp3\"},{\"ImageID\":\"7\",\"ImageName\":\"Image 7\",\"ImagePath_Thumbnail\":\"https://s-media-cache-ak0.pinimg.com/originals/b1/bb/ec/b1bbec499a0d66e5403480e8cda1bcbe.png\",\"ImagePath_FullPhoto\":\"https://s-media-cache-ak0.pinimg.com/originals/b1/bb/ec/b1bbec499a0d66e5403480e8cda1bcbe.png\",\"Audio\":\"http://podcast-files.cnet.com/podcast/cnetbuzz_042307.mp3\"},{\"ImageID\":\"8\",\"ImageName\":\"Image 8\",\"ImagePath_Thumbnail\":\"https://image.flaticon.com/icons/png/128/145/145867.png\",\"ImagePath_FullPhoto\":\"https://image.flaticon.com/icons/png/128/145/145867.png\",\"Audio\":\"http://www.kozco.com/tech/piano2-CoolEdit.mp3\"},{\"ImageID\":\"9\",\"ImageName\":\"Image 9\",\"ImagePath_Thumbnail\":\"http://icons.iconarchive.com/icons/designbolts/free-male-avatars/128/Male-Avatar-icon.png\",\"ImagePath_FullPhoto\":\"http://icons.iconarchive.com/icons/designbolts/free-male-avatars/128/Male-Avatar-icon.png\",\"Audio\":\"http://www.kozco.com/tech/organfinale.mp3\"},{\"ImageID\":\"10\",\"ImageName\":\"Image 10\",\"ImagePath_Thumbnail\":\"https://s-media-cache-ak0.pinimg.com/originals/6a/83/f0/6a83f0ec5f43944c47df1b6cff801578.png\",\"ImagePath_FullPhoto\":\"https://s-media-cache-ak0.pinimg.com/originals/6a/83/f0/6a83f0ec5f43944c47df1b6cff801578.png\",\"Audio\":\"http://www.kozco.com/tech/32.mp3\"},{\"ImageID\":\"11\",\"ImageName\":\"Image 11\",\"ImagePath_Thumbnail\":\"http://kessler-aqua.de/wp-content/uploads/2015/10/flat-faces-icons-circle-man-2.png\",\"ImagePath_FullPhoto\":\"http://kessler-aqua.de/wp-content/uploads/2015/10/flat-faces-icons-circle-man-2.png\",\"Audio\":\"http://www.kozco.com/tech/c304-2.wav\"},{\"ImageID\":\"12\",\"ImageName\":\"Image 12\",\"ImagePath_Thumbnail\":\"http://www.iconninja.com/files/912/138/492/female-young-woman-user-avatar-person-icon.png\",\"ImagePath_FullPhoto\":\"http://www.iconninja.com/files/912/138/492/female-young-woman-user-avatar-person-icon.png\",\"Audio\":\"http://users.skynet.be/fa046054/home/P22/track80.mp3\"},{\"ImageID\":\"13\",\"ImageName\":\"Image 13\",\"ImagePath_Thumbnail\":\"https://s-media-cache-ak0.pinimg.com/originals/b1/bb/ec/b1bbec499a0d66e5403480e8cda1bcbe.png\",\"ImagePath_FullPhoto\":\"https://s-media-cache-ak0.pinimg.com/originals/b1/bb/ec/b1bbec499a0d66e5403480e8cda1bcbe.png\",\"Audio\":\"http://podcast-files.cnet.com/podcast/cnetbuzz_042307.mp3\"},{\"ImageID\":\"14\",\"ImageName\":\"Image 14\",\"ImagePath_Thumbnail\":\"https://image.flaticon.com/icons/png/128/145/145867.png\",\"ImagePath_FullPhoto\":\"https://image.flaticon.com/icons/png/128/145/145867.png\",\"Audio\":\"http://www.kozco.com/tech/piano2-CoolEdit.mp3\"},{\"ImageID\":\"15\",\"ImageName\":\"Image 15\",\"ImagePath_Thumbnail\":\"http://icons.iconarchive.com/icons/designbolts/free-male-avatars/128/Male-Avatar-icon.png\",\"ImagePath_FullPhoto\":\"http://icons.iconarchive.com/icons/designbolts/free-male-avatars/128/Male-Avatar-icon.png\",\"Audio\":\"http://www.kozco.com/tech/organfinale.mp3\"},{\"ImageID\":\"16\",\"ImageName\":\"Image 16\",\"ImagePath_Thumbnail\":\"https://s-media-cache-ak0.pinimg.com/originals/6a/83/f0/6a83f0ec5f43944c47df1b6cff801578.png\",\"ImagePath_FullPhoto\":\"https://s-media-cache-ak0.pinimg.com/originals/6a/83/f0/6a83f0ec5f43944c47df1b6cff801578.png\",\"Audio\":\"http://www.kozco.com/tech/32.mp3\"},{\"ImageID\":\"17\",\"ImageName\":\"Image 17\",\"ImagePath_Thumbnail\":\"http://kessler-aqua.de/wp-content/uploads/2015/10/flat-faces-icons-circle-man-2.png\",\"ImagePath_FullPhoto\":\"http://kessler-aqua.de/wp-content/uploads/2015/10/flat-faces-icons-circle-man-2.png\",\"Audio\":\"http://www.kozco.com/tech/c304-2.wav\"},{\"ImageID\":\"18\",\"ImageName\":\"Image 18\",\"ImagePath_Thumbnail\":\"http://www.iconninja.com/files/912/138/492/female-young-woman-user-avatar-person-icon.png\",\"ImagePath_FullPhoto\":\"http://www.iconninja.com/files/912/138/492/female-young-woman-user-avatar-person-icon.png\",\"Audio\":\"http://users.skynet.be/fa046054/home/P22/track80.mp3\"},{\"ImageID\":\"19\",\"ImageName\":\"Image 19\",\"ImagePath_Thumbnail\":\"https://s-media-cache-ak0.pinimg.com/originals/b1/bb/ec/b1bbec499a0d66e5403480e8cda1bcbe.png\",\"ImagePath_FullPhoto\":\"https://s-media-cache-ak0.pinimg.com/originals/b1/bb/ec/b1bbec499a0d66e5403480e8cda1bcbe.png\",\"Audio\":\"http://podcast-files.cnet.com/podcast/cnetbuzz_042307.mp3\"},{\"ImageID\":\"20\",\"ImageName\":\"Image 20\",\"ImagePath_Thumbnail\":\"https://image.flaticon.com/icons/png/128/145/145867.png\",\"ImagePath_FullPhoto\":\"https://image.flaticon.com/icons/png/128/145/145867.png\",\"Audio\":\"http://www.kozco.com/tech/piano2-CoolEdit.mp3\"},{\"ImageID\":\"21\",\"ImageName\":\"Image 21\",\"ImagePath_Thumbnail\":\"http://icons.iconarchive.com/icons/designbolts/free-male-avatars/128/Male-Avatar-icon.png\",\"ImagePath_FullPhoto\":\"http://icons.iconarchive.com/icons/designbolts/free-male-avatars/128/Male-Avatar-icon.png\",\"Audio\":\"http://www.kozco.com/tech/organfinale.mp3\"},{\"ImageID\":\"22\",\"ImageName\":\"Image 22\",\"ImagePath_Thumbnail\":\"https://s-media-cache-ak0.pinimg.com/originals/6a/83/f0/6a83f0ec5f43944c47df1b6cff801578.png\",\"ImagePath_FullPhoto\":\"https://s-media-cache-ak0.pinimg.com/originals/6a/83/f0/6a83f0ec5f43944c47df1b6cff801578.png\",\"Audio\":\"http://www.kozco.com/tech/32.mp3\"},{\"ImageID\":\"23\",\"ImageName\":\"Image 23\",\"ImagePath_Thumbnail\":\"http://kessler-aqua.de/wp-content/uploads/2015/10/flat-faces-icons-circle-man-2.png\",\"ImagePath_FullPhoto\":\"http://kessler-aqua.de/wp-content/uploads/2015/10/flat-faces-icons-circle-man-2.png\",\"Audio\":\"http://www.kozco.com/tech/c304-2.wav\"},{\"ImageID\":\"24\",\"ImageName\":\"Image 24\",\"ImagePath_Thumbnail\":\"http://www.iconninja.com/files/912/138/492/female-young-woman-user-avatar-person-icon.png\",\"ImagePath_FullPhoto\":\"http://www.iconninja.com/files/912/138/492/female-young-woman-user-avatar-person-icon.png\",\"Audio\":\"http://users.skynet.be/fa046054/home/P22/track80.mp3\"}]";
            //http://podcast-files.cnet.com/podcast/cnetbuzz_042307.mp3
            //http://www.kozco.com/tech/organfinale.mp3
            //http://www.kozco.com/tech/32.mp3
            //http://www.kozco.com/tech/c304-2.wav
            //http://users.skynet.be/fa046054/home/P22/track80.mp3

            JSONArray data;
            try {
                data = new JSONArray(jsonStr.toString());

                MyArrList = new ArrayList<>();

                for (int i = 0; i < data.length(); i++) {
                    JSONObject c = data.getJSONObject(i);
                    bean = new DownloadFileBean();
                    bean.ImageID = (String) c.getString("ImageID");
                    bean.ImageName = (String) c.getString("ImageName");
                    // Thumbnail Get ImageBitmap To Object
                    bean.ImagePathThum = (String) c.getString("ImagePath_Thumbnail");
                    bean.ImageThumBitmap = (Bitmap) LoadImageBitmap.loadBitmap(c.getString("ImagePath_Thumbnail"));
                    // Full (for View Popup)
                    bean.ImagePathFull = (String) c.getString("ImagePath_FullPhoto");

                    bean.Audio = (String) c.getString("Audio");
                    bean.progress = 0;
//                    MyArrList.add(bean);

                    bll = new ImageBLL(MainRecyclerViewActivity.this);
                    bll.Insert(bean);

                    publishProgress(i);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            bll = new ImageBLL(MainRecyclerViewActivity.this);
            MyArrList = bll.SelectAll();
            Log.e("Shreyans ", "Array Size:: " + MyArrList.size());
            imageAdapter = new MyWorkDownloadAdapter(recyclerView_list, MainRecyclerViewActivity.this, MyArrList, layoutManager);
            recyclerView_list.setAdapter(imageAdapter);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (MyArrList.size() > 0) {
            imageAdapter = new MyWorkDownloadAdapter(recyclerView_list, MainRecyclerViewActivity.this, MyArrList, layoutManager);
            recyclerView_list.setAdapter(imageAdapter);
        }
    }
}
