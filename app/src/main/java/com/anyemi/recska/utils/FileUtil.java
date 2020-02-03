/*
 * FileUtil.java 
 */

package com.anyemi.recska.utils;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class FileUtil {
	private static final int BUFFER_SIZE = 1024; // 1kb

	public static void copyAssetToInternal(Context context, String srcFileName, String destFileName) throws IOException {
		copyStream(context.getAssets().open(srcFileName), context.openFileOutput(destFileName, Context.MODE_PRIVATE));
	}

	public static void saveStreamToInternal(Context context, InputStream is, String destFileName) throws IOException {
		copyStream(is, context.openFileOutput(destFileName, Context.MODE_PRIVATE));
	}

	@RequiresApi(api = Build.VERSION_CODES.KITKAT)
	public static String internalFileToString(Context context, String fileName) throws IOException {
		return new String(toByteArray(context.openFileInput(fileName)), StandardCharsets.UTF_8);
	}

	public static byte[] fileToByte(String fileName) throws IOException {
		FileInputStream fin = new FileInputStream(fileName);
		BufferedInputStream bis = new BufferedInputStream(fin);
		DataInputStream dis = new DataInputStream(bis);
		return toByteArray(dis);
	}

	public static byte[] toByteArray(InputStream in) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		copyStream(in, out);
		return out.toByteArray();
	}

	public static InputStream byteArrayToStream(byte[] bytes) {
		return new ByteArrayInputStream(bytes);
	}

	public static long copyStream(InputStream from, OutputStream to) throws IOException {
		byte[] buf = new byte[BUFFER_SIZE];
		long total = 0;
		while (true) {
			int r = from.read(buf);
			if (r == -1) {
				break;
			}
			to.write(buf, 0, r);
			total += r;
		}
		return total;
	}

	public static void copyFile(String fromFile, String toFile) throws IOException {
		InputStream in = new FileInputStream(fromFile);
		OutputStream out = new FileOutputStream(toFile);

		byte[] buffer = new byte[1024];
		int read;
		while ((read = in.read(buffer)) != -1) {
			out.write(buffer, 0, read);
		}
		in.close();
		in = null;
		out.flush();
		out.close();
		out = null;
	}

	public static String readFile(String file) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line = null;
		StringBuilder stringBuilder = new StringBuilder();
		String ls = System.getProperty("line.separator");

		while ((line = reader.readLine()) != null) {
			stringBuilder.append(line);
			stringBuilder.append(ls);
		}

		reader.close();
		return stringBuilder.toString();
	}
}
