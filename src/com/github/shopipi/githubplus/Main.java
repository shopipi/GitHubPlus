package com.github.shopipi.githubplus;

import com.github.shopipi.githubplus.Config.OptionType;
import com.github.shopipi.githubplus.Language.Message;

import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.handle.obj.ActivityType;
import sx.blah.discord.handle.obj.StatusType;

public class Main
{
	// Bot Client
	public static IDiscordClient client;

	// #### Config Area ####
	public static String CONFIG_FILENAME = "config.properties";
	public static String language;
	public static String discordBotToken;
	public static long   sendChannelId;
	public static String personalAccessToken;
	public static String owner;
	public static String repo;
	public static String branch;

	public static void main(String[] args)
	{
		System.setProperty("file.encoding", "UTF-8");

		if (!Config.checkConfig())
		{
			Log.INFO("Configファイルを作成しました、編集した後もう一度起動してください - Generated Config, Edit it and Run again");
			return;
		}

		if (Config.loadConfig())
		{
			Log.INFO(Language.getMsg(Message.Configをロードしました));
			printConfigResult();
		}
		else
		{
			Log.ERROR("Configをロードできませんでした - Failed to Load Config");
			return;
		}

		initClient();
	}

	public static void printConfigResult()
	{
		Log.INFO("----------------------------------------------------------------------------");
		Log.INFO("# Config");
		Log.INFO(Config.getConfigOptionNameFromEnum(OptionType.LANG) + ":                  " + language);
		Log.INFO(Config.getConfigOptionNameFromEnum(OptionType.DISCORD_BOT_TOKEN) + ":     " + discordBotToken);
		Log.INFO(Config.getConfigOptionNameFromEnum(OptionType.SEND_CHANNEL_ID) + ":       " + sendChannelId);
		Log.INFO(Config.getConfigOptionNameFromEnum(OptionType.PERSONAL_ACCESS_TOKEN) + ": " + personalAccessToken);
		Log.INFO(Config.getConfigOptionNameFromEnum(OptionType.OWNER) + ":                 " + owner);
		Log.INFO(Config.getConfigOptionNameFromEnum(OptionType.REPO) + ":                  " + repo);
		Log.INFO(Config.getConfigOptionNameFromEnum(OptionType.BRANCH) + ":                " + branch);
		Log.INFO("----------------------------------------------------------------------------");
		Log.INFO("# Developers");

		for (Developer dev : Developer.devs)
		{
			Log.INFO(dev.getId() + " - " + dev.getGitHubName());
		}

		Log.INFO("----------------------------------------------------------------------------\n");
	}

	private static void initClient()
	{
		client = new ClientBuilder().withToken(discordBotToken).build();
		client.getDispatcher().registerListener(new Main());
		client.getDispatcher().registerListener(new GitHubMessageListener());
		client.getDispatcher().registerListener(new CommandListener());
		client.login();
	}

	@EventSubscriber
	public void onReady(ReadyEvent e)
	{
		Log.INFO("#################################");
		Log.INFO("GitHub+ BOT for Discord");
		Log.INFO("                    Started!!");
		Log.INFO("#################################");

		client.changePresence(StatusType.ONLINE, ActivityType.PLAYING, "/? for Help");
	}

	public static void print(Object message)
	{
		System.out.println(message);
	}

	public static void clearConsole()
	{
		System.out.print("\033[H\033[2J");
        System.out.flush();
	}
}
