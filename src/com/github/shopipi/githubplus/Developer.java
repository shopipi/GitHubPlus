package com.github.shopipi.githubplus;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * デベロッパークラス、Discordのユーザー情報とGitHubのユーザー情報を共有し取り扱う
 * @author shoippi
 *
 */
public class Developer
{
	private long id;
	private String gitHubName;

	public static List<Developer> devs;

	public static boolean init()
	{
		devs = new ArrayList<Developer>();

		try
		{
			// Configから取得
			for (int i = 1; Config.getValue("dev" + i + "-discord-id") != null; i++)
			{
				if (Config.getValue("dev" + i + "-discord-id") == null) return false;

				long id = Long.parseLong(Config.getValue("dev" + i + "-discord-id"));
				String githubName = Config.getValue("dev" + i + "-github-name");
				Developer dev = new Developer(id, githubName);
				devs.add(dev);
			}

			return true;
		}
		catch (NumberFormatException e)
		{
		}

		return false;
	}

	/**
	 * Constructor
	 * @param id Discord ID
	 * @param gitHubName GitHubユーザー名
	 */
	public Developer(long id, String gitHubName)
	{
		this.id = id;
		this.gitHubName = gitHubName;
	}

	/**
	 * Discord IDを取得
	 * @return Discord ID
	 */
	public long getId()
	{
		return this.id;
	}

	/**
	 * GitHubユーザー名を取得
	 * @return GitHubユーザー名
	 */
	public String getGitHubName()
	{
		return this.gitHubName;
	}

	/**
	 * Discordでのメンション時の文字列を取得
	 * @return メンション文字列
	 */
	public String mention()
	{
		if (this.gitHubName.equalsIgnoreCase("N/A"))
		{
			return "@N/A";
		}

		return "<@" + this.id + ">";
	}

	/**
	 * DiscordのIDからDeveloperを取得
	 * @param id Discord ID
	 * @return Developer
	 */
	public static Developer getDevById(long id)
	{
		for (Developer dev : devs)
		{
			if (dev.getId() == id)
			{
				return dev;
			}
		}

		return null;
	}

	/**
	 * GitHubユーザー名からDeveloperを取得
	 * @param gitHubName GitHubユーザー名
	 * @return Developer
	 */
	public static Developer getDevByGitHubName(String gitHubName)
	{
		for (Developer dev : devs)
		{
			if (dev.getGitHubName().equalsIgnoreCase(gitHubName))
			{
				return dev;
			}
		}

		return null;
	}
}
