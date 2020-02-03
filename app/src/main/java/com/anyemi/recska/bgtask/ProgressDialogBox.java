/*
 * ProgressDialogBox.java
 */

package com.anyemi.recska.bgtask;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.util.Log;

import com.anyemi.recska.R;


/**
 * The Class ProgressDialogBox is used to show Progress Dialog.
 */
public final class ProgressDialogBox {


	private static final String TAG = "ProgressDialogBox";

	/** The progress Dialog */
	private static ProgressDialog progress;

//	private static AlertDialog progressDialog;

	/**
	 * Instantiates a new progress dialog box.
	 */
	private ProgressDialogBox() {
		// Singleton class
	}

	public static void show(final Context context) {
		show(context, null);
	}

	public static void show(final Context context, final ProcessCancelListener processCancelListener) {
		show(context, processCancelListener, context.getString(R.string.loading_txt));
	}

	/**
	 * Show the Progress Dialog.
	 * 
	 * @param context
	 *            the context
	 * @param processCancelListener
	 *            the process cancel listener
	 */
	public static void show(final Context context, final ProcessCancelListener processCancelListener, String message) {
		close();
		progress = new ProgressDialog(context);
		progress.setMessage(message);
		progress.setIndeterminate(true);
		progress.setCancelable(false);

		if (processCancelListener != null) {
			progress.setOnCancelListener(new OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					processCancelListener.processCanceled();
				}
			});
		}

		if (progress == null)
			return;
		synchronized (progress) {
			try {
				progress.show();
			} catch (Exception e) {
				Log.e(TAG, e.toString());
				// e.printStackTrace();
			}
		}
	}

	public static void close() {
		try {
			if (progress != null) {
				synchronized (progress) {
					progress.dismiss();
					progress = null;
				}
			}
		}

		catch (Exception e) // To handle device orientation change crash
		{
			Log.e(TAG, e.toString(), e);
		}
	}
}
