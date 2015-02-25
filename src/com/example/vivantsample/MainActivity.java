package com.example.vivantsample;


import com.example.vivantsample.data.Galleries;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.Toast;

public class MainActivity extends Activity {
	ProgressDialog progressDialog = null;
	private static Galleries galleries = null ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		if (android.os.Build.VERSION.SDK_INT > 9) {
		    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		    StrictMode.setThreadPolicy(policy);
		}
		
		showLoadingSpinner();
		galleries = (new JsonLoader()).loadJsonData();
		
		if (galleries == null) {
			showToastMessage("Error reading data");
			finish();
		}
		else {
			Global.galleries = this.galleries;
			dismissLoadingSpinner();
			
			Intent i = new Intent();
			i.setClass(this, VivantMap.class);
			startActivity(i);
		}
	}
	
	private void showLoadingSpinner() {
		progressDialog = new ProgressDialog(this);
		progressDialog.show();
	}
	
	private void dismissLoadingSpinner() {
		progressDialog.dismiss();
	}
	
	private void showToastMessage(String str) {
		Toast.makeText(this, str, Toast.LENGTH_LONG ).show();
	}
}
