package com.os4.ecb;

import java.io.File;

import com.os4.ecb.misc.Properties;

import android.os.Environment;

public class Constant {

	public static final String COMPANY_NAME = "ID-IoT";
	public static final String SecretKey = "jg5BGbeIM29zPye";
	public static final String DATABASE_FILE_PATH = Environment.getExternalStorageDirectory().getPath() + File.separator + COMPANY_NAME + File.separator + Properties.resource + File.separator;
	public static final String IMAGES_FILE_PATH = Environment.getExternalStorageDirectory().getPath() + File.separator + COMPANY_NAME + File.separator + Properties.resource + File.separator + Properties.ImagePath + File.separator;
	public static final String THUMBNAILS_PATH = Environment.getExternalStorageDirectory().getPath() + File.separator + COMPANY_NAME + File.separator + Properties.resource + File.separator + Properties.ThumbnailPath + File.separator;
	public static final String ICONS_PATH = Environment.getExternalStorageDirectory().getPath() + File.separator + COMPANY_NAME + File.separator + Properties.resource + File.separator + Properties.IconPath + File.separator;

	public static final int RESULT_NOT_OK = 0;
	public static final int RESULT_OK = 1;	
	
	public static final int NOTIFICATION_CHAT = 100;
	public static final int NOTIFICATION_CHATGROUP = 101;
	public static final int NOTIFICATION_INVITATION = 102;
	public static final int NOTIFICATION_INVITATIONGROUP = 103;
	public static final int NOTIFICATION_EFORM = 200;

	public static final int MAIN_ACTIVITY = 100;
	public static final int LOGIN_ACTIVITY = 101;
	public static final int SETTING_ACTIVITY = 102;
	public static final int MENU_ACTIVITY = 103;
	public static final int REGISTER_ACTIVITY = 104;
	public static final int PROFILE_ACTIVITY = 105;
	public static final int ONLINE_ACTIVITY = 106;
	public static final int CONTACT_ACTIVITY = 107;
	public static final int GROUP_ACTIVITY = 108;
	public static final int PUBLIC_ACTIVITY = 109;
	public static final int STORES_ACTIVITY = 110;
	public static final int APPLY_ACTIVITY = 111;
	public static final int SHOP_ACTIVITY = 112;
	public static final int GPS_ACTIVITY = 113;
	public static final int CONTACTLIST_ACTIVITY = 114;
	public static final int FRIENDLIST_ACTIVITY = 115;
	public static final int SALES_ACTIVITY = 116;
}
