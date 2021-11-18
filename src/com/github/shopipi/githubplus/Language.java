package com.github.shopipi.githubplus;

/**
 *
 * 日本語と英語の2言語に対応
 * @author shopipi
 *
 */
public class Language
{
	/**
	 *対応させるメッセージ
	 */
	enum Message
	{
		Configをロードしました,
		Configをリロードしました,
		Configをリロードできませんでした,
		ファイルが更新されました,
		ファイル,
		詳細
	}

	public static String getMsg(Message message)
	{
		String msg = null;

		if (Main.language.equalsIgnoreCase("en"))
		{
			if (message == Message.Configをロードしました) msg = "Successfully Loaded Config";
			if (message == Message.Configをリロードしました) msg = "Successfully Reloaded Config";
			if (message == Message.Configをリロードできませんでした) msg = "Failed to Reload Config";
			if (message == Message.ファイルが更新されました) msg = "File(s) Updated";
			if (message == Message.ファイル) msg = "File(s)";
			if (message == Message.詳細) msg = "Detail";
		}
		else if (Main.language.equalsIgnoreCase("ja"))
		{
			return message.name();
		}

		return msg;
	}
}
