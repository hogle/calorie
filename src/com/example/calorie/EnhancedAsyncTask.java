package com.example.calorie;

import java.lang.ref.WeakReference;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public abstract class EnhancedAsyncTask<Params, Progress, Result, WeakTarget extends Activity>
		extends AsyncTask<Params, Progress, Result> {

	protected WeakReference<WeakTarget> mTarget;
	protected Throwable mException = null;

	protected String mTaskCancelledMessage = "작업이 취소되었습니다.";

	protected String cancelledMsg = null;

	protected boolean mLoadingDialogEnabled = true;

	public EnhancedAsyncTask(WeakTarget target) {
		mTarget = new WeakReference<WeakTarget>(target);
	}

	@Override
	protected final void onPreExecute() {
		final WeakTarget target = mTarget.get();
		if (target != null) {
			showLoadingDialog(target);
			this.onPreExecute(target);
		}
	}

	@Override
	protected final Result doInBackground(Params... params) {
		final WeakTarget target = mTarget.get();
		if (target != null) {
			try {
				return this.doInBackground(target, params);
			} catch (Throwable t) {
				this.mException = t;
				return null;
			}
		} else {
			return null;
		}
	}

	@Override
	protected final void onPostExecute(Result result) {
		final WeakTarget target = mTarget.get();

		if (target != null) {
			if (mException != null) {
				showError(target, mException);
				return;
			}

			dismissLoadingDialog();
			
			this.onPostExecute(target, result);

		}
	}

	@Override
	protected void onCancelled() {
		final WeakTarget target = mTarget.get();
		if (target != null) {
			dismissLoadingDialog();
			this.onCancelled(target);
		}
	}

	protected void dismissLoadingDialog() {
		if (mLoadingDialogEnabled == false)
			return;
	}

	protected void showCancelMessage(Context context) {
		dismissLoadingDialog();
		Toast.makeText(context, mTaskCancelledMessage, Toast.LENGTH_SHORT).show();
	}

	protected void showError(Context context, Throwable t) {

		dismissLoadingDialog();
		if (t.getMessage() != null) {
				Toast.makeText(context, "메세지 : "+ t.getMessage(), Toast.LENGTH_LONG).show();
		}
	}

	protected void showLoadingDialog(Context context) {
		if (mLoadingDialogEnabled == false)
			return;
	}

	protected void onPreExecute(WeakTarget target) {
	}

	protected abstract Result doInBackground(WeakTarget target, Params... params);
	
	protected void onPostExecute(WeakTarget target, Result result){
	}

	protected void onCancelled(WeakTarget target) {
	}

	protected void onProgressUpdate(Integer[] progress) {
	}
	
}