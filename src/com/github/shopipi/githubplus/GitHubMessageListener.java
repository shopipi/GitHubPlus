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

			String[] descs = embed.getDescription().split("\n");

			if (!embed.getTitle().toLowerCase().contains(Main.repo.toLowerCase())) return;

			for (int i = 0; i < descs.length; i++)
			{
				// ---------------------------------------------
				// Get Summary Message
				String desc   = descs[i];
				int start     = desc.indexOf(" ") + 1;
				desc          = desc.substring(start);
				String author = desc.split(" - ")[1];
				int end       = desc.lastIndexOf("-") - 1;
				desc          = desc.substring(0, end);
				// ---------------------------------------------

				// ---------------------------------------------
				// Get URL String
				String url = descs[i];
				int start1 = url.indexOf("(") + 1;
				int end1   = url.indexOf(")");
				url        = url.substring(start1, end1);
				// ---------------------------------------------

				// ---------------------------------------------
				// Get Commit ID
				String commitID = descs[i];
				int start2      = commitID.indexOf("[") + 1;
				int end2        = commitID.indexOf("]");
				commitID        = commitID.substring(start2, end2);
				// ---------------------------------------------

				Developer dev = Developer.getDevByGitHubName(author);

				if (dev == null) return;

				GitHubAPI api = new GitHubAPI(Main.personalAccessToken, Main.owner, Main.repo, Main.branch);

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

				Main.print("File Count: " + fileCount);

				EmbedBuilder embedBuilder = new EmbedBuilder()
						.withTitle(Language.getMsg(Message.ファイルが更新されました))
						.appendDesc("```" + desc + "```\n")
						.appendDesc(":scroll: **" + Language.getMsg(Message.ファイル) + "**： " + builder.toString().substring(0, builder.toString().length() - 2) + "\n\n")
						.appendDesc(":link: **"   + Language.getMsg(Message.詳細) + "**： [" + commitID + "](" + url + ")\n\n")
						.appendDesc(":bust_in_silhouette: " + dev.mention() + " - " + author)
						.withFooterText(fDate)
						.withColor(color);

				Main.client.getChannelByID(Main.sendChannelId).sendMessage(embedBuilder.build());
			}
		}
		catch (IndexOutOfBoundsException ie)
		{
			ie.printStackTrace();
		}
	}
}
