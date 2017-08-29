package com.FileDownlaodWithProgressUpdate.MyWork.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Pattern;

import org.w3c.dom.CharacterData;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.telephony.TelephonyManager;

public class Utils {

    public static Pattern EMAIL_ADDRESS_PATTERN = Pattern
            .compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                    + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                    + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                    + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                    + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                    + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$");

    public static Pattern NAME = Pattern.compile("^[a-z_A-Z0-9 ]*$");

    public static void systemUpgrade(Context context) {
        DBHelper dbHelper = new DBHelper(context);
        int level = Integer.parseInt(Pref.getValue(context, "LEVEL", "0"));

        if (level == 0) {
            dbHelper.upgrade(level);

            // Create not confirmed order
            level++;

        }
        Pref.setValue(context, "LEVEL", level + "");
    }

    public static void upgradeLevel1() {
        // Set default value
//        new DBHelper().upgrade(1);
    }

    public static void upgradeLevel2() {

    }

    public static String getDeviceID(Context context) {
        return ((TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
    }

    public static int indexOfArray(String[] strArray, String strFind) {
        int index;

        for (index = 0; index < strArray.length; index++)
            if (strArray[index].equals(strFind))
                break;

        return index;
    }

    public static String[] arrListToArray(ArrayList<Object> arrList,
                                          String strMName) {
        String[] objStr = null;
        Method m = null;
        try {
            if (arrList != null) {
                objStr = new String[arrList.size()];
                for (int index = 0; index < arrList.size(); index++) {
                    Object obj = arrList.get(index);
                    m = obj.getClass().getMethod(strMName);
                    objStr[index] = (String) m.invoke(obj, new Object[0]);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return objStr;

    }

    public static Bitmap download(String bitmapURL) throws IOException {

        Bitmap bitmap = null;
        URL url = null;
        HttpURLConnection conn = null;
        InputStream is = null;

        url = new URL(bitmapURL);
        Log.print("Bitmap URL ::: ", url + "");
        conn = (HttpURLConnection) url.openConnection();

        if ((conn).getResponseCode() == HttpURLConnection.HTTP_OK) {
            is = conn.getInputStream();
            if (is != null) {
                Log.print("Utils :::", "" + HttpURLConnection.HTTP_OK);
                bitmap = BitmapFactory.decodeStream(is);
                is.close();

            }
        }

        conn.disconnect();

        url = null;
        conn = null;
        is = null;

        return bitmap;
    }

    public static String getCharacterDataFromElement(Element e) {
        Node child = e.getFirstChild();
        if (child instanceof CharacterData) {
            CharacterData cd = (CharacterData) child;
            return cd.getData();
        }
        return "";
    }

    public static long dateToMilisec(String parseFormat) {
        return Utils.convertStringToDate(
                convertDateToString(new Date(), parseFormat), parseFormat)
                .getTime();
    }

    public static long dateToMilisec(Date date, String parseFormat) {
        return Utils.convertStringToDate(
                convertDateToString(date, parseFormat), parseFormat).getTime();
    }

    public static long stringToMilisec(String date, String parseFormat) {
        return Utils.convertStringToDate(date, parseFormat).getTime();
    }

    public static void showAlert(Context context, String title, String message,
                                 String btnText) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setButton(btnText, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alertDialog.show();
    }

    public static String convertDateToString(Date objDate, String parseFormat) {
        try {
            return new SimpleDateFormat(parseFormat).format(objDate);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static Date convertStringToDate(String strDate, String parseFormat) {
        try {
            return new SimpleDateFormat(parseFormat).parse(strDate);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getStringToDate(String dateformet, Date date) {
        String newString = null;
        try {
            SimpleDateFormat newFormat = new SimpleDateFormat(dateformet);
            newString = newFormat.format(date);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return newString;
    }

    public static String convertDateStringToString(String strDate,
                                                   String currentFormat, String parseFormat)

    {
        try {
            return convertDateToString(
                    convertStringToDate(strDate, currentFormat), parseFormat);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static double getdistInMeter(double lat1, double lng1, double lat2,
                                        double lng2) {
        double earthRadius = 3958.75;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLng / 2)
                * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double dist = earthRadius * c;

        int meterConversion = 1609;

        return new Double(dist * meterConversion).doubleValue();
    }

    public static int distance(double lat1, double lon1, double lat2,
                               double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        // dist = dist * 1.609344; // In Km
        dist = dist * 1609.344; // In Mter
        System.out.println("&&&&&& Distance m :: " + dist);
        return (int) dist;
    }

    /* ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: */
    /* :: This function converts decimal degrees to radians : */
	/* ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: */
    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    /* ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: */
	/* :: This function converts radians to decimal degrees : */
	/* ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: */
    private static double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

	/*private static void setDisabledDateTextViews(ViewGroup dp) {

		for (int x = 0, n = dp.getChildCount(); x < n; x++) {
			View v = dp.getChildAt(x);
			if (v instanceof TextView) {
				v.setEnabled(false);
			} else if (v instanceof ViewGroup) {
				setDisabledDateTextViews((ViewGroup) v);
			}
		}

	}

	private static void setDisabledTimeTextViews(ViewGroup dp) {

		for (int x = 0, n = ((ViewGroup) dp.getChildAt(0)).getChildCount(); x < n - 1; x++)
			(((ViewGroup) ((ViewGroup) dp.getChildAt(0)).getChildAt(x))
					.getChildAt(1)).setEnabled(false);

	}

	private static String pad(int c) {
		return c >= 10 ? String.valueOf(c) : "0" + String.valueOf(c);
	}*/

    public static long getMillis(long date, String time) {
        String hhmm[] = time.split(":");

        return date + (Integer.parseInt(hhmm[0]) * 60 * 60 * 1000)
                + (Integer.parseInt(hhmm[1]) * 60 * 1000);
    }

    public static long getMinutes(long date, String time) {
        String hhmm[] = time.split(":");

        return (date / 1000 / 60) + (Integer.parseInt(hhmm[0]) * 60)
                + Integer.parseInt(hhmm[1]);
    }

    public static int getTime(String strTime) {
        int time = 0;

        time = Integer.parseInt(strTime.replaceAll(":", ""));

        return time;
    }

    public static boolean checkEmail(String email) {
        return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
    }

    public static boolean checkName(String name) {
        return NAME.matcher(name).matches();
    }

    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }


    public static int ConvertAverageDuration(double number) {
        NumberFormat formatter1 = new DecimalFormat("#0.00");
        double Test = Double.parseDouble((formatter1.format(number)));
        System.out.println((int) Test * 60);
        return (int) Test * 60;

    }

    public static String millisToDate(long millis, String format) {

        return new SimpleDateFormat(format).format(new Date(millis));
    }

    public static String UnicodetoUTF8(String str) {
        String stringUTF = "";
        try {
            // Convert from Unicode to UTF-8
            stringUTF = str;
            byte[] utf8 = stringUTF.getBytes("UTF-8");

            // Convert from UTF-8 to Unicode
            stringUTF = new String(utf8, "UTF-8");
            System.out.println(stringUTF);
        } catch (UnsupportedEncodingException e) {
        }
        return stringUTF;
    }
}