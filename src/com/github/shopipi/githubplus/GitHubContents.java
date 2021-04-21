package com.github.shopipi.githubplus;

public class GitHubContents
{
	public static String getFilename(String api_filename)
	{
		Main.print(api_filename);
		return api_filename.substring(api_filename.lastIndexOf("/") + 1);
	}

	public static String getFilePath(String api_filename)
	{
		return api_filename.substring(api_filename.indexOf("/") + 1);
	}

	public static String getContentURL(GitHubAPI gitHubAPI, String filename)
	{
		return "https://github.com/" + gitHubAPI.owner + "/" + gitHubAPI.repo + "/blob/" + gitHubAPI.branch + "/" + filename;
	}
}
