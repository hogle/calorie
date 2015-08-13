package com.example.calorie;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class ResultActivity extends Activity{
	
	private String filePath = null;
	private ImageView img_view;
	private Button btn_write;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.result);
		
		btn_write = (Button) findViewById(R.id.btn_write);
		img_view = (ImageView) findViewById(R.id.img_view);
		
		Intent i = getIntent();
		
		filePath = i.getStringExtra("filePath");
		boolean isImage = i.getBooleanExtra("isImage", true);
		
		if (filePath != null) {
			previewMedia(isImage);
		} 

		btn_write.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(getApplicationContext(), "등록 되었습니다.", Toast.LENGTH_LONG);
				//Intent intent = new Intent(getApplicationContext(), MainActivity.class);
				//startActivity(intent);
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

}
