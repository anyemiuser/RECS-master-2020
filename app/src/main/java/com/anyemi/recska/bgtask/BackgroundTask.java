/*
 * BackgroundTask.java 
 */

package com.anyemi.recska.bgtask;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;

import com.anyemi.recska.LoginActivity;
import com.anyemi.recska.R;
import com.anyemi.recska.exceptions.BadRequestException;
import com.anyemi.recska.exceptions.InternalServerError;
import com.anyemi.recska.exceptions.JavaLangException;
import com.anyemi.recska.model.StatusModel;
import com.anyemi.recska.utils.Globals;
import com.anyemi.recska.utils.Network;
import com.anyemi.recska.utils.PrintLog;
import com.google.gson.Gson;


/**
 * The Class BackgroundTask.
 * 
 * @author rjonnalagadda
 */
public final class BackgroundTask extends AsyncTask<Void, Void, Object> {
	private AlertDialog progressDialog;

	private static final String TAG = "BackgroundTask";

	/** The Background Thread Object. */
	private BackgroundThread bgtask;

	/** The context. */
	private Context context;

	/** is task cancelled. */
	boolean isCancelled;

	private String loadingMessage;

	/**
	 * Instantiates a new background task.
	 * 
	 * @param context
	 *            the context
	 * @param bgtask
	 *            the Background Thread Object
	 */
	public BackgroundTask(Context context, BackgroundThread bgtask) {
		this.context = context;
		this.bgtask = bgtask;
		this.loadingMessage = "";
	}

	public BackgroundTask(Context context, BackgroundThread bgtask, String loadingMessage) {
		this.context = context;
		this.bgtask = bgtask;
		this.loadingMessage = loadingMessage;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#onPreExecute()
	 */
	@Override
	protected void onPreExecute() {
		if (!loadingMessage.equals("")){
			ProgressDialogBox.show(context, new ProcessCancelListener() {
				@Override
				public void processCanceled() {
					isCancelled = true;
					cancel(true);
				}
			}, loadingMessage);
	}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#doInBackground(Params[])
	 */
	@Override
	protected Object doInBackground(Void... params) {
		PrintLog.print(TAG, "doInBackground...");
		if(Network.isAvailable(context)){
			return bgtask.runTask();
		}
		return new BadRequestException("No Internet Connection please connect an try again later");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
	 */
	@Override
	protected void onPostExecute(Object result) {
		PrintLog.print(TAG, "onPostExecute...");
		if (!isCancelled) {
			ProgressDialogBox.close();
			if (result instanceof BadRequestException) {
				Globals.showToast(context, ((BadRequestException) result).getMessage());
			} else if (result instanceof InternalServerError) {
				Globals.showToast(context, ((InternalServerError) result).getMessage());


				Dialog dialog = new Dialog(context);
				dialog.setTitle("Server Under maintance");
				dialog.setContentView(R.layout.serverdown);

				dialog.show();
			}else if (result instanceof JavaLangException) {
				Globals.showToast(context, ((JavaLangException) result).getMessage());
			}  else {

				bgtask.taskCompleted(result);

//				StatusModel statusModel =new StatusModel();
//				Gson gson = new Gson();
//				statusModel= gson.fromJson(result.toString(),StatusModel.class);
//				if(statusModel.getStatus()!=null) {
//					if (statusModel.getStatus().equalsIgnoreCase("Success")) {
//						bgtask.taskCompleted(result);
//					} else {
//						Globals.showToast(context, statusModel.getMsg());
//					}
//				}else {
//					bgtask.taskCompleted(result);
//				}

			}
		}
	}

	@Override
	protected void onCancelled() {
		if (!isCancelled) {
			PrintLog.print(TAG, "onCancelled...");
			ProgressDialogBox.close();
			super.onCancelled();
		}
	}
}
