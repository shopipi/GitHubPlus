package com.github.shopipi.githubplus;

import com.github.shopipi.githubplus.Language.Message;

import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.EmbedBuilder;

public class CommandListener
{
	@EventSubscriber
	public void onCommand(MessageReceivedEvent e)
	{
		if (e.getMessage().getContent().startsWith("/"))
		{
			Log.INFO(e.getAuthor().getName() + " -> " + e.getMessage().getContent());

			String command = e.getMessage().getContent().substring(1);

			if (command.equalsIgnoreCase("stop"))
			{
				if (Developer.getDevById(e.getAuthor().getLongID()) != null)
				{
					e.getChannel().sendMessage("Bye bye");
					Main.client.logout();
				}
			}

			if (command.equalsIgnoreCase("?") || command.equalsIgnoreCase("help"))
			{
				EmbedBuilder builder = new EmbedBuilder()
						.withAuthorIcon(Main.client.getApplicationIconURL())
						.withAuthorName("GitHub+ BOT for Discord")
						.withAuthorUrl("https://github.com/shopipi/GitHubPlus")
						.withColor(0x6A2786)
						.appendDesc("```Summaryのコメントと更新があったファイル名を表示します \n")
						.appendDesc("プライベートレポジトリ対応 ```\n\n")
						.appendDesc("**ソースコード** \n")
						.appendDesc("https://github.com/shopipi/GitHubPlus");

				e.getChannel().sendMessage(builder.build());
			}

			if (command.equalsIgnoreCase("reload") || command.equalsIgnoreCase("rl"))
			{
				if (Config.loadConfig())
				{
					e.getChannel().sendMessage(Language.getMsg(Message.Configをリロードしました));
					Log.INFO(Language.getMsg(Message.Configをリロードしました));
					Main.printConfigResult();
				}
				else
				{
					e.getChannel().sendMessage(Language.getMsg(Message.Configをリロードできませんでした));
					Log.INFO(Language.getMsg(Message.Configをリロードできませんでした));
				}
			}
		}
	}
}
