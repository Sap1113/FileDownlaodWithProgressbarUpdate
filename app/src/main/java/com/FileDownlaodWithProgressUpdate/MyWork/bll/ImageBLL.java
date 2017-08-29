package com.FileDownlaodWithProgressUpdate.MyWork.bll;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;

import com.FileDownlaodWithProgressUpdate.MyWork.util.Log;
import com.FileDownlaodWithProgressUpdate.MyWork.bean.DownloadFileBean;
import com.FileDownlaodWithProgressUpdate.MyWork.util.DBHelper;

public class ImageBLL {

    public Context context = null;

    public ImageBLL(Context context) {
        this.context = context;
    }

    public int Insert(DownloadFileBean purchaseBean) {
        int ResultID = 0;
        DBHelper objDBHelper = new DBHelper(this.context);
        StringBuilder Query = new StringBuilder();
        Cursor objCursor = null;
        try {

            Query.append("INSERT INTO ")
                    .append("Image (imageId, ImageName, ImagePathFull, ImagePathThum, Audio , is_downloaded, is_downloading ) ")
                    .append("Values (")
                    .append(purchaseBean.ImageID)
                    .append(", '")
                    .append(purchaseBean.ImageName)
                    .append("', '")
                    .append(purchaseBean.ImagePathFull)
                    .append("', '")
                    .append(purchaseBean.ImagePathThum)
                    .append("', '")
                    .append(purchaseBean.Audio)
                    .append("', ")
                    .append(purchaseBean.isDownload)
                    .append(", ")
                    .append(purchaseBean.is_downloading)
                    .append(")");


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


    public ArrayList<DownloadFileBean> SelectAll() {
        DBHelper dbHelper = new DBHelper(this.context);
        StringBuilder Query = new StringBuilder();
        ArrayList<DownloadFileBean> objList = new ArrayList<>();
        Cursor objCursor = null;
        DownloadFileBean imageBean = null;
        try {

            Query.append("SELECT ")
                    .append("imageId, ImageName, ImagePathFull, ImagePathThum, Audio , is_downloaded, is_downloading ")
                    .append("FROM Image");

            Log.print("selectDownloadDescription :: " + Query.toString());
            objCursor = dbHelper.query(Query.toString());

            if (objCursor != null && objCursor.getCount() > 0) {
                objCursor.moveToPosition(-1);

                while (objCursor.moveToNext()) {

                    imageBean = new DownloadFileBean();

                    imageBean.ImageID = objCursor.getString(0);
                    imageBean.ImageName = objCursor.getString(1);
                    imageBean.ImagePathFull = objCursor.getString(2);
                    imageBean.ImagePathThum = objCursor.getString(3);
                    imageBean.Audio = objCursor.getString(4);
                    imageBean.isDownload = objCursor.getInt(5);
                    imageBean.is_downloading = objCursor.getInt(6);
                    objList.add(imageBean);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.error(ImageBLL.class + "selectDownloadDescription : ", e);
        } finally {

            dbHelper = null;
            Query = null;
            if (objCursor != null && !objCursor.isClosed()) {
                objCursor.close();
            }
            objCursor = null;
            imageBean = null;
        }

        return objList;
    }

    public DownloadFileBean SelectMultipleDownload() {
        DBHelper dbHelper = new DBHelper(this.context);
        StringBuilder Query = new StringBuilder();
//        ArrayList<DownloadFileBean> objList = new ArrayList<>();
        Cursor objCursor = null;
        DownloadFileBean imageBean = null;
        try {

            Query.append("SELECT ")
                    .append("imageId, ImageName, ImagePathFull, ImagePathThum, Audio , is_downloaded, is_downloading ")
                    .append("FROM Image WHERE is_downloaded = 1 Limit 1");

            objCursor = dbHelper.query(Query.toString());

            if (objCursor != null && objCursor.getCount() > 0) {
                objCursor.moveToPosition(-1);

                while (objCursor.moveToNext()) {

                    imageBean = new DownloadFileBean();

                    imageBean.ImageID = objCursor.getString(0);
                    imageBean.ImageName = objCursor.getString(1);
                    imageBean.ImagePathFull = objCursor.getString(2);
                    imageBean.ImagePathThum = objCursor.getString(3);
                    imageBean.Audio = objCursor.getString(4);
                    imageBean.isDownload = objCursor.getInt(5);
                    imageBean.is_downloading = objCursor.getInt(6);
//                    objList.add(imageBean);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.error(ImageBLL.class + "selectDownloadDescription : ", e);
        } finally {

            dbHelper = null;
            Query = null;
            if (objCursor != null && !objCursor.isClosed()) {
                objCursor.close();
            }
            objCursor = null;
//            imageBean = null;
        }

        return imageBean;
    }

    public int countIsDownloading() {
        DBHelper objDataAccess = new DBHelper(context);
        StringBuilder Query = new StringBuilder();
        int no_isDownloading = 0;
        Cursor objCursor = null;
        try {

            Query.append("select count(is_downloading) from Image WHERE is_downloading = 1");

            objCursor = objDataAccess.query(Query.toString());

            if (objCursor != null && objCursor.getCount() > 0) {
                objCursor.moveToPosition(-1);

                while (objCursor.moveToNext()) {

                    no_isDownloading = objCursor.getInt(0);
                }

            }
        } catch (Exception e) {
            Log.error(ImageBLL.class + "countIsDownloading : ", e.getMessage());
        } finally {

            objDataAccess = null;
            Query = null;
            if (objCursor != null && !objCursor.isClosed()) {
                System.out.println("***********CLOSE CURSOURE*************");
                objCursor.close();
            }
            objCursor = null;
        }
        return no_isDownloading;
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

        } catch (Exception e) {
            Log.error(ImageBLL.class + "Update : ", e.getMessage());
        } finally {
            objDataAccess = null;
            Query = null;
        }
    }

    public void UpdateIsDelete(String PurchaseID) {
        StringBuilder Query = new StringBuilder();
        DBHelper dbhelper = new DBHelper(context);
        try {

            Query.append("UPDATE Image ")
                    .append("SET ")
                    .append("is_downloaded = ")
                    .append(0)
                    .append(", is_deleted = ")
                    .append(1)
                    .append(" WHERE ").append("imageId = ")
                    .append(PurchaseID);

            dbhelper.execute(Query.toString());
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            Log.error(this.getClass() + " ::Delete() :: ", e);
        }

        Query = null;
        dbhelper = null;

    }

    public void UpdateDownloading(int PurchaseId, int isDownloading) {

        DBHelper objDataAccess = new DBHelper(context);
        StringBuilder Query = new StringBuilder();

        try {
            Query.append("UPDATE Image ")
                    .append("SET ")
                    .append("is_downloading = ")
                    .append(isDownloading)
                    .append(" WHERE ").append("imageId = ")
                    .append(PurchaseId);

            objDataAccess.execute(Query.toString());

        } catch (Exception e) {
            Log.error(ImageBLL.class + "Update : ", e.getMessage());
            e.printStackTrace();
        } finally {
            objDataAccess = null;
            Query = null;
        }
    }

    public String SelectDownloading(int PurchaseId) {
        DBHelper objDataAccess = new DBHelper(context);
        StringBuilder Query = new StringBuilder();
        String Is_Downloading = "";
        Cursor objCursor = null;
        try {

            Query.append("select is_downloading  from Image where imageId = " + PurchaseId);
            objCursor = objDataAccess.query(Query.toString());

            if (objCursor != null && objCursor.getCount() > 0) {
                objCursor.moveToPosition(-1);

                while (objCursor.moveToNext()) {

                    Is_Downloading = objCursor.getString(0);
                }

            }
        } catch (Exception e) {
            Log.error(ImageBLL.class + "Is_Downloading : ", e.getMessage());
        } finally {

            objDataAccess = null;
            Query = null;
            if (objCursor != null && !objCursor.isClosed()) {
                System.out.println("***********CLOSE CURSOURE*************");
                objCursor.close();
            }
            objCursor = null;
        }
        return Is_Downloading;
    }
}