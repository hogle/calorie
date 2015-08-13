package com.example.calorie;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

public class ResultActivity extends Activity{
	
	private String filePath = null;
	private ImageView img_view;
	private Button btn_write;
	
	private Spinner spinner;
	private TextView txt_value;
	
	private KeyValueArrayAdapter item_adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.result);
		
		item_adapter = new KeyValueArrayAdapter(this,android.R.layout.simple_spinner_item);
		item_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		item_adapter.setEntryValues(ConfigData.CD_KEY);
		item_adapter.setEntries(ConfigData.CD_VALUES);
		
		btn_write = (Button) findViewById(R.id.btn_write);
		img_view = (ImageView) findViewById(R.id.img_view);
		txt_value = (TextView) findViewById(R.id.txt_value);
		spinner = (Spinner) findViewById(R.id.spinner);
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
		} 

		btn_write.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// 종료
				finish();
			}
		});
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
		public void onNothingSelected(AdapterView<?> parent) {}
	};

}
