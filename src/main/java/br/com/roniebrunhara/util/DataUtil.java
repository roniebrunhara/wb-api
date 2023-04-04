package br.com.roniebrunhara.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DataUtil {

	public static String formatarDateEmString(Date data, String mask) {
		DateFormat format = new SimpleDateFormat(mask);
		return format.format(data);
	}
}
