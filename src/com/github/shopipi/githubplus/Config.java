package com.github.shopipi.githubplus;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

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

	public static boolean checkConfig()
	{
		if (Files.exists(Paths.get(Main.CONFIG_FILENAME))) return true;

		if (!downloadFile("https://raw.githubusercontent.com/shopipi/GitHubPlus/main/config.properties", Main.CONFIG_FILENAME)) return false;

		return true;
	}

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

	public static List<String> readAllLine(String path)
	{
		try
		{
			List<String> lines = Files.readAllLines(Paths.get(path));
			return lines;
		}
		catch (IOException e)
		{
		}

		return null;
	}

	public static String getConfigOptionNameFromEnum(OptionType optionType)
	{
		return optionType.name().replaceAll("_", "-").toLowerCase();
	}
}
