package com.github.shopipi.githubplus;

import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.github.shopipi.githubplus.Language.Message;

import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IEmbed;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.EmbedBuilder;

public class GitHubMessageListener
{
	@EventSubscriber
	public void onBotMsgRecieved(MessageReceivedEvent e)
	{
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd - HH:mm:ss");
		String fDate = format.format(date);

		try
		{
			IMessage msg = e.getMessage();
			IUser user   = e.getAuthor();

			if (user == Main.client) return;
			if (!user.isBot()) return;
			if (!user.getName().contains("GitHub")) return;

			IEmbed embed = msg.getEmbeds().get(0);

			String[] embedDescs = embed.getDescription().split("\n");

			if (!embed.getTitle().toLowerCase().contains(Main.repo.toLowerCase())) return;

			// コミットの数だけ実行
			for (String desc : embedDescs)
			{
				// ハッシュIDを取得
				String sha = desc.substring
				(
					desc.indexOf("`") + 1,
					desc.lastIndexOf("`")
				);

				GitHubAPI api = new GitHubAPI(Main.personalAccessToken, Main.owner, Main.repo, sha);

				String summary  = api.getValue("message");
				String author   = api.getValue("login");
				String url      = api.getValue("html_url");
				String commitID = "`" + api.branch + "`";

				// Get Developer from Commit Author
				Developer dev = Developer.getDevByGitHubName(author);

				if (dev == null)
				{
					dev = new Developer(0, "N/A");
				}

				// The Commit is on Target Repo?
				if (!embed.getTitle().contains(api.repo)) return;

				StringBuilder builder = new StringBuilder();

				String[] filenames = api.getValues("filename");

				for (String filename : filenames)
				{
					builder.append("[`" + GitHubContents.getFilename(filename)        + "`]");
					builder.append("(" +  GitHubContents.getContentURL(api, filename) + ")");
					builder.append(", ");
				}

				int fileCount = filenames.length;

				Color color = new Color(0xFFF8F0);

				if (fileCount == 2) color = new Color(0x00AA00);
				if (fileCount == 3) color = new Color(0xFFAA00);
				if (fileCount >= 4) color = new Color(0xFF5555);

				// ---------------------------------------------
				// Filecount more than 10
				// ---------------------------------------------
				if (fileCount > 10)
				{
					// ---------------------------------------------
					// Title and Commit Message EMBED
					EmbedBuilder embed01 = new EmbedBuilder()
							.withTitle(Language.getMsg(Message.ファイルが更新されました))
							.appendDesc("```" + summary + "```\n")
							.withColor(color);

					Main.client.getChannelByID(Main.sendChannelId).sendMessage("----------", embed01.build());
					// ---------------------------------------------

					// ---------------------------------------------
					// Get Filename per 10
					for (int start_fIndex = 0; start_fIndex < fileCount; start_fIndex += 10)
					{
						StringBuilder file10 = new StringBuilder();

						// ---------------------------------------------
						// Get Filename in per_10 + j index
						for (int j = start_fIndex; j < start_fIndex + 10; j++)
						{
							try
							{
								file10.append("[`" + GitHubContents.getFilename(filenames[j])        + "`]");
								file10.append("(" +  GitHubContents.getContentURL(api, filenames[j]) + ")");
								file10.append(", ");
							}
							catch (ArrayIndexOutOfBoundsException e2)
							{
								break;
							}
						}
						// ---------------------------------------------

						// ---------------------------------------------
						// File names EMBED
						EmbedBuilder embed02 = new EmbedBuilder();

						if (start_fIndex == 0)
						{
							embed02.appendDesc(":scroll: **" + Language.getMsg(Message.ファイル) + "**： ");
						}

						embed02.appendDesc(file10.toString().substring(0, file10.toString().length() - 2) + "\n\n");
						embed02.withColor(color);

						Main.client.getChannelByID(Main.sendChannelId).sendMessage(embed02.build());
						// ---------------------------------------------

						try
						{
							// Wait 1sec for Discord Rate Limit
							Thread.sleep(1000);
						}
						catch (InterruptedException e1)
						{
						}
					}
					// ---------------------------------------------

					EmbedBuilder embed03 = new EmbedBuilder()
							.appendDesc(":link: **"   + Language.getMsg(Message.詳細) + "**： [" + commitID + "](" + url + ")\n\n")
							.appendDesc(":bust_in_silhouette: " + dev.mention() + " - " + author)
							.withFooterText(fDate)
							.withColor(color);

					Main.client.getChannelByID(Main.sendChannelId).sendMessage(embed03.build());

					Main.client.getChannelByID(Main.sendChannelId).sendMessage("----------");
				}
				else
				{
					// if Filecount <= 10
					EmbedBuilder embedBuilder = new EmbedBuilder()
							.withTitle(Language.getMsg(Message.ファイルが更新されました))
							.appendDesc("```" + summary + "```\n")
							.appendDesc(":scroll: **" + Language.getMsg(Message.ファイル) + "**： " + builder.toString().substring(0, builder.toString().length() - 2) + "\n\n")
							.appendDesc(":link: **"   + Language.getMsg(Message.詳細) + "**： [" + commitID + "](" + url + ")\n\n")
							.appendDesc(":bust_in_silhouette: " + dev.mention() + " - " + author)
							.withFooterText(fDate)
							.withColor(color);

					Main.client.getChannelByID(Main.sendChannelId).sendMessage(embedBuilder.build());
				}
			}
		}
		catch (IndexOutOfBoundsException ie)
		{
			ie.printStackTrace();
		}
	}
}
