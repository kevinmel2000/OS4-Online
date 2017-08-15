package com.os4.ecb.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;

import static com.os4.ecb.R.xml.preferences;

public class SettingsActivity extends PreferenceActivity implements Preference.OnPreferenceClickListener,Preference.OnPreferenceChangeListener {

	private static final String TAG = SettingsActivity.class.getName();
	public static final int TAKE_PICTURE = 1;
	public static final int TAKE_GALERY = 2;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG,"onCreate");

		addPreferencesFromResource(preferences);
		findPreference("filePicker").setOnPreferenceClickListener(this);
		findPreference("filePicker").setOnPreferenceChangeListener(this);
		String filename = findPreference("filePicker").getSharedPreferences().getString("filePicker","");
		findPreference("filePicker").setSummary(filename.substring(filename.lastIndexOf("/")+1));
		findPreference("filePicker").setIcon(Drawable.createFromPath(filename));

		findPreference("backupPreference").setOnPreferenceClickListener(this);
		findPreference("backupPreference").setOnPreferenceChangeListener(this);
		findPreference("backupPreference").setSummary(findPreference("backupPreference").getSharedPreferences().getString("backupPreference",""));

		findPreference("notifications_ringtone").setOnPreferenceClickListener(this);
		findPreference("notifications_ringtone").setOnPreferenceChangeListener(this);
		findPreference("notifications_ringtone").setSummary(findPreference("notifications_ringtone").getSharedPreferences().getString("notifications_ringtone",""));

		findPreference("website_key").setOnPreferenceClickListener(this);
		findPreference("about_key").setOnPreferenceClickListener(this);
	}

	@Override
	public boolean onPreferenceClick(Preference preference) {

		String key = preference.getKey();
		if(key.equalsIgnoreCase("account_key")){

		}
		else if(key.equalsIgnoreCase("filePicker")){
			Intent intent = new Intent(Intent.ACTION_PICK);
			intent.setType("image/*");
			startActivityForResult(intent,TAKE_GALERY);
			return true;
		}
		else if(key.equalsIgnoreCase("notifications_ringtone")){
			preference.setSummary(preference.getSharedPreferences().getString("notifications_ringtone",""));
			return true;
		}
		else if(key.equalsIgnoreCase("website_key")){
			Intent intent = new Intent(this, WebSiteActivity.class);
			startActivity(intent);
		}
		else if(key.equalsIgnoreCase("about_key")){
			Intent intent = new Intent(this, AboutActivity.class);
			startActivity(intent);
		}
		return false;
	}

	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		String key = preference.getKey();
		if(key.equalsIgnoreCase("backupPreference")) {
			preference.setSummary((String)newValue);
			return true;
		}
		return false;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.d(TAG, "onActivityResult");
		if (resultCode == RESULT_OK) {
			if (requestCode == TAKE_GALERY) {
				try {
					Log.d(TAG, "onActivityResult : TAKE_GALERY");
					Uri uri = data.getData();
					Log.d(TAG, uri.toString());
					String filename = uri.toString();
					if (filename.startsWith("content://")) {
						Cursor cursor = null;
						try {
							cursor = getContentResolver().query(uri, null, null, null, null);
							if (cursor != null && cursor.moveToFirst()) {
								filename = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA));
								SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
								SharedPreferences.Editor editor = preferences.edit();
								editor.putString("filePicker", filename);
								editor.commit();
								Preference filePreference = findPreference("filePicker");
								filePreference.setSummary(filename.substring(filename.lastIndexOf("/")+1));
								filePreference.setIcon(Drawable.createFromPath(filename));
							}
						} finally {
							cursor.close();
						}
					}
					Log.d(TAG, filename);
				} catch (Exception e) {
					Log.e(TAG, e.getMessage());
				}
			}
		}
	}
}
