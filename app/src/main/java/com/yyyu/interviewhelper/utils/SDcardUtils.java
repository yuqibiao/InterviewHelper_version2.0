package com.yyyu.interviewhelper.utils;

import android.os.Environment;

public class SDcardUtils {
	
	/*
	 * 判断SDK是否存在
	 */
	public static boolean hasSdcard() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}
}
