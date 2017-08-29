package com.FileDownlaodWithProgressUpdate.MyWork.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Storage {

	public static void veirfyLogPath() throws IOException {
		File dir = new File(Const.LOG_DIR);

		if (!dir.exists()) {
			dir.mkdirs();
		}

		dir = null;
	}
	public static void veirfyUserDataPath() throws IOException {
		File dir = new File(Const.USER_DATA);

		if (!dir.exists()) {
			dir.mkdirs();
		}

		dir = null;
	}
	public static File verifyLogFile() throws IOException {
		File logFile = new File(Const.LOG_DIR + "/Log_"
				+ new SimpleDateFormat("yyyy_MM_dd").format(new Date())
				+ ".html");
		FileOutputStream fos = null;

		Storage.veirfyLogPath();

		if (!logFile.exists()) {
			logFile.createNewFile();

			fos = new FileOutputStream(logFile);

			String str = "<TABLE style=\"width:100%;border=1px\" cellpadding=2 cellspacing=2 border=1><TR>"
					+ "<TD style=\"width:30%\"><B>Date n Time</B></TD>"
					+ "<TD style=\"width:20%\"><B>Title</B></TD>"
					+ "<TD style=\"width:50%\"><B>Description</B></TD></TR>";

			fos.write(str.getBytes());
		}

		if (fos != null) {
			fos.close();
		}

		fos = null;

		return logFile;
	}

	public static void createLogZip() {
		ZipOutputStream zout = null;
		FileInputStream fis = null;
		String files[] = null;
		int ch;
		try {
			files = new File(Const.LOG_DIR).list();

			zout = new ZipOutputStream(new FileOutputStream(Const.LOG_ZIP));

			zout.setLevel(Deflater.DEFAULT_COMPRESSION);

			for (int ele = 0; ele < files.length; ele++) {
				fis = new FileInputStream(Const.LOG_DIR + "/" + files[ele]);

				/*
				 * To begin writing ZipEntry in the zip file, use
				 * 
				 * void putNextEntry(ZipEntry entry) method of ZipOutputStream
				 * class.
				 * 
				 * This method begins writing a new Zip entry to the zip file
				 * and positions the stream to the start of the entry data.
				 */

				zout.putNextEntry(new ZipEntry(Const.LOG_DIR + "/" + files[ele]));

				/*
				 * After creating entry in the zip file, actually write the
				 * file.
				 */

				while ((ch = fis.read()) > 0) {
					zout.write(ch);
				}

				/*
				 * After writing the file to ZipOutputStream, use
				 * 
				 * void closeEntry() method of ZipOutputStream class to close
				 * the current entry and position the stream to write the next
				 * entry.
				 */

				zout.closeEntry();

				// close the InputStream
				fis.close();
			}

			// close the ZipOutputStream
			zout.close();

		} catch (Exception e) {
			Log.error(Storage.class + " :: create log zip :: ", e);
		}

		zout = null;
		fis = null;
		files = null;
	}

	public static void clearLog() {
		String files[] = null;
		File file = null;
		try {
			files = new File(Const.LOG_DIR).list();

			for (int ele = 0; ele < files.length; ele++) {
				file = new File(Const.LOG_DIR, files[ele]);

				file.delete();
			}

		} catch (Exception e) {
			Log.error(Storage.class + " :: clearLog :: ", e);
		}

		files = null;
		files = null;
	}
}
