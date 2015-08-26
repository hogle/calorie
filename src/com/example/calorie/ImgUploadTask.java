package com.example.calorie;

import java.util.ArrayList;
import java.util.List;

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

import android.util.Log;

public class ImgUploadTask extends EnhancedAsyncTask<String, Void, ResponseEntity<String>, ResultActivity>{
	
	public ImgUploadTask(ResultActivity target) {
		super(target);
	}

	@Override
	protected ResponseEntity<String> doInBackground(ResultActivity target, String... param) {
		
		String filePath = param[0];
		
		String url = "http://api.cloudsightapi.com/image_requests";
		
		MultiValueMap<String, Object> params = new LinkedMultiValueMap<String, Object>();
		params.add("image_request[locale]", "ko-KR");
		params.add("image_request[language]", "ko-KR");
		
		FileSystemResource file = new FileSystemResource(filePath);
		params.add("image_request[image]", file);
		
		HttpHeaders requestHeaders = new HttpHeaders();
		List<MediaType> acceptableMediaTypes = new ArrayList<MediaType>();
        acceptableMediaTypes.add(MediaType.APPLICATION_JSON);
        requestHeaders.setAccept(acceptableMediaTypes);
		requestHeaders.add("Authorization", ConfigData.IMG_API);
		requestHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
		
		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<MultiValueMap<String, Object>>(
				params, requestHeaders);
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
		restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
		restTemplate.getMessageConverters().add(new FormHttpMessageConverter());
		ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

		String resVal = responseEntity.getBody();
		
		Log.d(resVal, resVal);
		
		return responseEntity;
	}

}