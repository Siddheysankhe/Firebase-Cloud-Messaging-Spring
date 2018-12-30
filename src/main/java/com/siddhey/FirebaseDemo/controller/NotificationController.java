package com.siddhey.FirebaseDemo.controller;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.siddhey.FirebaseDemo.service.AndroidPushNotificationsService;

@RestController()
@RequestMapping("/notifications")
public class NotificationController {
	
	private AndroidPushNotificationsService androidService;
	
	public NotificationController(AndroidPushNotificationsService androidService) {
		this.androidService = androidService;
	}
	
	@GetMapping("/send")
	public ResponseEntity<String> send() throws JSONException{
		
		JSONObject body = new JSONObject();
		body.put("to", "c5FvvULa4Wc:APA91bEbexaoUZ7J4q0Bn8MFMWyHWzS5B9T0CWcdnhrYGYvsamGm6QgJmWjXshCATJqCfOb-wAXtmwDRByikgOAYzKf6-jB5mUhRRSDjTwqw1cE_xlMtXPSse4Udk67rrYBjtMMmCXZG");
		body.put("priority", "high");
		
		JSONObject notification = new JSONObject();
		notification.put("title", "New Notification");
		notification.put("body", "Happy New Year");
		
		JSONObject data = new JSONObject();
		data.put("Key-1", "JSA Data 1");
		data.put("Key-2", "JSA Data 2");
 
		body.put("notification", notification);
		body.put("data", data);
		
		HttpEntity<String> request = new HttpEntity<>(body.toString());
		 
		CompletableFuture<String> pushNotification = androidService.send(request);
		CompletableFuture.allOf(pushNotification).join();
 
		try {
			String firebaseResponse = pushNotification.get();
			
			return new ResponseEntity<>(firebaseResponse, HttpStatus.OK);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
 
		return new ResponseEntity<>("Push Notification ERROR!", HttpStatus.BAD_REQUEST);
	}
		
}
