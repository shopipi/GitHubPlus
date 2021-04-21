package com.github.shopipi.githubplus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Log
{
	private static String Log_Prefix(String level)
	{
		LocalDateTime ld = LocalDateTime.now();
		DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm:ss");
		String time = ld.format(format);
		return ("[" + time + " %level%]: ").replaceAll("%level%", level);
	}

	public static void INFO(Object msg)
	{
		Main.print(Log_Prefix("INFO") + msg);
	}

	public static void WARN(Object msg)
	{
		Main.print(Log_Prefix("WARN") + msg);
	}

	public static void ERROR(Object msg)
	{
		Main.print(Log_Prefix("ERROR") + msg);
	}
}
