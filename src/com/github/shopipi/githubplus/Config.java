package com.github.shopipi.githubplus;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 *
 * Configクラス
 * @author shopipi
 *
 */
public class Config
{
	enum OptionType
	{
		LANG,
		DISCORD_BOT_TOKEN,
		SEND_CHANNEL_ID,
		PERSONAL_ACCESS_TOKEN,
		OWNER,
		REPO,
		BRANCH
	}

	/**
	 * コンフィグが存在するかどうか
	 * @return 存在
	 */
	public static boolean checkConfig()
	{
		if (Files.exists(Paths.get(Main.CONFIG_FILENAME))) return true;

		if (!downloadFile("https://raw.githubusercontent.com/shopipi/GitHubPlus/main/config.properties", Main.CONFIG_FILENAME)) return false;

		return true;
	}

	/**
	 * Configファイルから設定を読み込み
	 * @return 読み込み結果
	 */
	public static boolean loadConfig()
	{
		try
		{
			Main.language            = getValue(OptionType.LANG);
			Main.discordBotToken     = getValue(OptionType.DISCORD_BOT_TOKEN);
			Main.sendChannelId       = Long.parseLong(getValue(OptionType.SEND_CHANNEL_ID));
			Main.personalAccessToken = getValue(OptionType.PERSONAL_ACCESS_TOKEN);
			Main.owner               = getValue(OptionType.OWNER);
			Main.repo                = getValue(OptionType.REPO);
			Main.branch              = getValue(OptionType.BRANCH);
		}
		catch (NumberFormatException e)
		{
			return false;
		}

		if (!Developer.init()) return false;

		return true;
	}

	/**
	 * 設定を取得
	 * @param optionType 設定項目
	 * @return 設定値
	 */
	public static String getValue(OptionType optionType)
	{
		for (String line : readAllLine(Main.CONFIG_FILENAME))
		{
			if (line.startsWith(getConfigOptionNameFromEnum(optionType)))
			{
				return line.substring(line.indexOf("=") + 1);
			}
		}

		return null;
	}

	/**
	 * 文字列から設定を取得
	 * @param option 文字列
	 * @return 設定値
	 */
	public static String getValue(String option)
	{
		for (String line : readAllLine(Main.CONFIG_FILENAME))
		{
			if (line.startsWith(option))
			{
				return line.substring(line.indexOf("=") + 1);
			}
		}

		return null;
	}

	/**
	 * ファイルをダウンロードする
	 * @param url URL
	 * @param outFile 出力ファイル名
	 * @return 結果
	 */
	public static boolean downloadFile(String url, String outFile)
	{
		try
		{
			URL u = new URL(url);
			URLConnection conn = u.openConnection();
			InputStream in = conn.getInputStream();

			FileOutputStream out = new FileOutputStream(outFile, false);
			int b;

			while ((b = in.read()) != -1)
			{
			    out.write(b);
			}

			out.flush();
			out.close();
			in.close();
		}
		catch (IOException e)
		{
			return false;
		}

		return false;
	}

	/**
	 * ファイルの全ての行を取得
	 * @param path ファイルパス
	 * @return
	 */
	public static List<String> readAllLine(String path)
	{
		try
		{
			return Files.readAllLines(Paths.get(path));
		}
		catch (IOException e)
		{
		}

		return null;
	}

	/**
	 * OptionTypeを文字列に変換
	 * @param optionType
	 * @return
	 */
	public static String getConfigOptionNameFromEnum(OptionType optionType)
	{
		return optionType.name().replaceAll("_", "-").toLowerCase();
	}
}
