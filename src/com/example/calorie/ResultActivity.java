package com.example.calorie;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

public class ResultActivity extends Activity {

	public static String resultData = null;

	public String filePath = null;
	public ImageView img_view;
	public Button btn_write;

	public Spinner spinner;
	public TextView txt_value;
	public TextView tv_result;
	public TextView TextView2;

	public int cnt = 0;

	private String token;

	private KeyValueArrayAdapter item_adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.result);

		item_adapter = new KeyValueArrayAdapter(this, android.R.layout.simple_spinner_item);
		item_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		item_adapter.setEntryValues(ConfigData.CD_KEY);
		item_adapter.setEntries(ConfigData.CD_VALUES);

		btn_write = (Button) findViewById(R.id.btn_write);
		img_view = (ImageView) findViewById(R.id.img_view);
		txt_value = (TextView) findViewById(R.id.txt_value);
		txt_value.setVisibility(View.GONE);
		tv_result = (TextView) findViewById(R.id.tv_result);
		btn_write.setVisibility(View.GONE);

		TextView2 = (TextView) findViewById(R.id.TextView2);
		TextView2.setVisibility(View.GONE);

		spinner = (Spinner) findViewById(R.id.spinner);
		spinner.setVisibility(View.GONE);
		spinner.setAdapter(item_adapter);
		spinner.setOnItemSelectedListener(onSpinnerEvent);

		// 랜덤으로 데이터 뿌려짐
		int index = (int) (Math.random() * ConfigData.CD_KEY.length);
		spinner.setSelection(index);

		Intent i = getIntent();

		filePath = i.getStringExtra("filePath");
		boolean isImage = i.getBooleanExtra("isImage", true);

		if (filePath != null) {
			previewMedia(isImage);
			imageUpload();
			//
		}

		btn_write.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	private void imageUpload() {

		ImgUploadTask imgUploadTask = new ImgUploadTask(this) {

			@Override
			protected void onPostExecute(ResultActivity target, ResponseEntity<String> result) {

				if (result != null) {

					JSONObject obj = null;
					try {
						obj = new JSONObject(result.getBody());
						token = obj.getString("token");
						imgSearchTask();
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}
		};
		imgUploadTask.execute(filePath);
	}

	public String imgSearchTask() {

		ImgSearchTask imgSearchTask = new ImgSearchTask(this) {

			@Override
			protected void onPostExecute(ResultActivity target, String result) {
				if (result != null) {
					try {
						JSONObject obj = new JSONObject(result);
						String status = obj.getString("status");
						if (status.equalsIgnoreCase("completed") || status.equalsIgnoreCase("skipped")) {
							System.out.println(result);
							String code = obj.getString("name");
							tv_result.setText(code);
							tv_result.setTextColor(Color.BLUE);

							boolean search = false;

							for (int i = 0; i < ConfigData.CD_VALUES.length; i++) {
								String codes[] = code.split(" ");
								for (String string : codes) {
									if (ConfigData.CD_VALUES[i].equals(string)) {
										spinner.setSelection(i);
										search = true;
										break;
									}
								}
							}

							if (search == true) {
								btn_write.setVisibility(View.VISIBLE);
								spinner.setVisibility(View.VISIBLE);
								txt_value.setVisibility(View.VISIBLE);
								TextView2.setVisibility(View.VISIBLE);
							} else {
//								txt_value.setVisibility(View.VISIBLE);
//								btn_write.setVisibility(View.VISIBLE);
//								txt_value.setText("일치하는 항목이 없습니다.");
								btn_write.setVisibility(View.VISIBLE);
								spinner.setVisibility(View.VISIBLE);
								txt_value.setVisibility(View.VISIBLE);
								TextView2.setVisibility(View.VISIBLE);
							}

						} else if (cnt >= 10) {

						} else {
							Thread.sleep(2 * 1000);
							imgSearchTask();
						}

					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			}
		};
		imgSearchTask.execute(token);
		return resultData;
	}

	private void previewMedia(boolean isImage) {

		if (isImage) {
			img_view.setVisibility(View.VISIBLE);
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 8;
			final Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
			img_view.setImageBitmap(bitmap);
		}
	}

	private OnItemSelectedListener onSpinnerEvent = new AdapterView.OnItemSelectedListener() {
		public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

			KeyValueArrayAdapter adapter = (KeyValueArrayAdapter) parent.getAdapter();
			txt_value.setText(adapter.getEntryValue(pos));

		}

		public void onNothingSelected(AdapterView<?> parent) {
		}
	};

}
