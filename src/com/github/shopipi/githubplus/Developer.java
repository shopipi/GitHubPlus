package com.github.shopipi.githubplus;

import java.util.ArrayList;
import java.util.List;

public class Developer
{
	private long id;
	private String gitHubName;

	public static List<Developer> devs = new ArrayList<Developer>();

	public static boolean init()
	{
		try
		{
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

	public Developer(long id, String gitHubName)
	{
		this.id = id;
		this.gitHubName = gitHubName;
	}

	public long getId()
	{
		return this.id;
	}

	public String getGitHubName()
	{
		return this.gitHubName;
	}

	public String mention()
	{
		return "<@" + this.id + ">";
	}

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
