package com.FileDownlaodWithProgressUpdate.MyWork.bll;

import android.content.Context;
import android.database.Cursor;

import com.FileDownlaodWithProgressUpdate.MyWork.bean.TrackByteBean;
import com.FileDownlaodWithProgressUpdate.MyWork.util.Log;
import com.FileDownlaodWithProgressUpdate.MyWork.util.DBHelper;

import java.util.ArrayList;

public class TrackByteBLL {

    public Context context = null;

    public TrackByteBLL(Context context) {
        this.context = context;
    }

    public int Insert(TrackByteBean trackByteBean) {
        int ResultID = 0;
        DBHelper objDBHelper = new DBHelper(this.context);
        StringBuilder Query = new StringBuilder();
        Cursor objCursor = null;
        try {

            Query.append("INSERT INTO ")
                    .append("FileToByte (trackId, trackByte) ")
                    .append("Values (")
                    .append(trackByteBean.id)
                    .append(", '")
                    .append(trackByteBean.trackByte)
                    .append("')");

            Log.print(this.getClass() + " :: insert() :: SQL :: "
                    + Query.toString());

            objDBHelper.execute(Query.toString());

        } catch (Exception e) {
            Log.error(this.getClass() + " :: Insert() ", e);
            e.printStackTrace();
            ResultID = 0;
        } finally {
            objDBHelper = null;
            Query = null;
            if (objCursor != null && !objCursor.isClosed())
                objCursor.close();
            objCursor = null;
        }

        return ResultID;
    }


    public ArrayList<TrackByteBean> SelectAll() {
        DBHelper dbHelper = new DBHelper(this.context);
        StringBuilder Query = new StringBuilder();
        ArrayList<TrackByteBean> objList = new ArrayList<>();
        Cursor objCursor = null;
        TrackByteBean trackByteBean = null;
        try {

            Query.append("SELECT ")
                    .append("trackId, trackByte ")
                    .append("FROM FileToByte Order by trackId desc ");

            Log.print("selectDownloadDescription :: " + Query.toString());
            objCursor = dbHelper.query(Query.toString());

            if (objCursor != null && objCursor.getCount() > 0) {
                objCursor.moveToPosition(-1);

                while (objCursor.moveToNext()) {

                    trackByteBean = new TrackByteBean();

                    trackByteBean.id = objCursor.getInt(0);
                    trackByteBean.trackByte = objCursor.getBlob(1);
                    Log.print(" *** SelectAll trackByte : " + trackByteBean.trackByte);
                    objList.add(trackByteBean);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.error(TrackByteBLL.class + "selectDownloadDescription : ", e);
        } finally {

            dbHelper = null;
            Query = null;
            if (objCursor != null && !objCursor.isClosed()) {
                System.out.println("*********** selectDownloadDescription CLOSE CURSOURE*************");
                objCursor.close();
            }
            objCursor = null;
            trackByteBean = null;
        }

        return objList;
    }

    public void UpdateDownload(int PurchaseId, int isDownload, int doupdate, int isDownloading) {
        DBHelper objDataAccess = new DBHelper(context);
        StringBuilder Query = new StringBuilder();

        try {

            Query.append("UPDATE Image ")
                    .append("SET ")
                    .append("is_downloaded = ")
                    .append(isDownload)
                    .append(", do_update = ")
                    .append(doupdate)
                    .append(", is_downloading = ")
                    .append(isDownloading)
                    .append(" WHERE ").append("imageId = ")
                    .append(PurchaseId);

            objDataAccess.execute(Query.toString());
            System.out
                    .println("++++++++++++++++++ Update Download +++++++++++++++++++++++++");
            System.out.println(Query.toString());
            System.out.println("+++++++++++++++++++++++++++++++++++++++++++");
        } catch (Exception e) {
            Log.error(TrackByteBLL.class + "Update : ", e.getMessage());
        } finally {
            objDataAccess = null;
            Query = null;
        }
    }

}