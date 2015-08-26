package com.example.calorie;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
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

import android.graphics.Color;
import android.view.View;

public class ImgSearchTask extends EnhancedAsyncTask<String, Void, String, ResultActivity> {

	public ImgSearchTask(ResultActivity target) {
		super(target);
	}

	@Override
	protected String doInBackground(ResultActivity target, String... param) {

		String token = param[0];

		String url = "http://api.cloudsightapi.com/image_responses/" + token;

		MultiValueMap<String, Object> params = new LinkedMultiValueMap<String, Object>();

		HttpHeaders requestHeaders = new HttpHeaders();
		List<MediaType> acceptableMediaTypes = new ArrayList<MediaType>();
		acceptableMediaTypes.add(MediaType.APPLICATION_JSON);
		requestHeaders.setAccept(acceptableMediaTypes);
		requestHeaders.add("Authorization", "CloudSight amZd_zG32VK-AoSz05JLIA");

		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<MultiValueMap<String, Object>>(params,
				requestHeaders);
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
		restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
		restTemplate.getMessageConverters().add(new FormHttpMessageConverter());

		ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
		String resultData = responseEntity.getBody();
		
		return resultData;
	}

}
