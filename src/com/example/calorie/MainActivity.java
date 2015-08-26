package com.example.calorie;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {

	public static final String IMAGE_DIRECTORY_NAME = "calorieUpload";

	public static final int MEDIA_TYPE_IMAGE = 1;

	private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
	
	private static String fileName;

	private Uri fileUri;

	private Button btn_capture;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		btn_capture = (Button) findViewById(R.id.btn_capture);
		btn_capture.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				captureImage();
			}
		});
	}
	


	@Override
	protected void onResume() {
		super.onResume();
		
	}



	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putParcelable("file_uri", fileUri);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		fileUri = savedInstanceState.getParcelable("file_uri");
	}

	private void captureImage() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

		intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

		startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
	}

	private void launchResultActivity(boolean isImage) {
		Intent i = new Intent(MainActivity.this, ResultActivity.class);
		i.putExtra("filePath", fileUri.getPath());
		i.putExtra("isImage", isImage);
		startActivity(i);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				
				//imageUpload();
				launchResultActivity(true);
			} else if (resultCode == RESULT_CANCELED) {
				Toast.makeText(getApplicationContext(), "취소되었습니다.", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(getApplicationContext(), "이미지를 찾을수 없습니다.", Toast.LENGTH_SHORT).show();
			}
		}
	}

	public Uri getOutputMediaFileUri(int type) {
		return Uri.fromFile(getOutputMediaFile(type));
	}

	private static File getOutputMediaFile(int type) {

		File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
				IMAGE_DIRECTORY_NAME);
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				return null;
			}
		}

		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
		File mediaFile;
		if (type == MEDIA_TYPE_IMAGE) {
			mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
			fileName = "IMG_" + timeStamp + ".jpg";
		} else {
			return null;
		}
		return mediaFile;
	}
	
	

	public static void createThumbnail(Bitmap bitmap, String strFilePath, String filename) {

		File file = new File(strFilePath);

		if (!file.exists()) { 
			file.mkdirs();
		}
		File fileCacheItem = new File(strFilePath + filename);
		OutputStream out = null;

		try {

			int height = bitmap.getHeight();
			int width = bitmap.getWidth();

			fileCacheItem.createNewFile();
			out = new FileOutputStream(fileCacheItem);
			// 160 부분을 자신이 원하는 크기로 변경할 수 있습니다.
			bitmap = Bitmap.createScaledBitmap(bitmap, 500, height / (width / 500), true);
			bitmap.compress(CompressFormat.JPEG, 100, out);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
