package com.github.shopipi.githubbotd4j;

import java.text.SimpleDateFormat;
import java.util.Date;

import sx.blah.discord.Discord4J.Discord4JLogger;
import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.ActivityType;
import sx.blah.discord.handle.obj.IEmbed;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.StatusType;
import sx.blah.discord.util.EmbedBuilder;

public class Main
{
    private static String TOKEN = "*****************************************";
    private static IDiscordClient client;
    private static long sendChannelId = *****************L;

    public static void main(String[] args)
    {
        initClient();
    }

    private static void initClient()
    {
        client = new ClientBuilder().withToken(TOKEN).build();
        client.getDispatcher().registerListener(new Main());
        client.login();
    }

    @EventSubscriber
    public void onReady(ReadyEvent e)
    {
        print("###########");
        print("BOT STARTED");
        print("###########");
        client.changePresence(StatusType.ONLINE, ActivityType.PLAYING, "Display Simple GitHub Log");
    }

    @EventSubscriber
    public void onMsgRecv(MessageReceivedEvent e)
    {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd - HH:mm:ss");
        String fDate = format.format(date);

        try
        {
            IMessage msg = e.getMessage();
            IUser user   = e.getAuthor();

            if (user == client) return;
            if (!user.isBot()) return;
            if (!user.getName().contains("GitHub")) return;

            IEmbed embed = msg.getEmbeds().get(0);

            String[] descs = embed.getDescription().split("\n");

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

                EmbedBuilder embedBuilder = new EmbedBuilder()
                        .withTitle(desc).withUrl(url)
                        .withDesc("by " + getMentionString(author))
                        .withFooterText(fDate)
                        .withColor(125, 37, 138);

                client.getChannelByID(sendChannelId).sendMessage(embedBuilder.build());
            }
        }
        catch (IndexOutOfBoundsException ie)
        {
        }
    }

    public static void print(Object message)
    {
        System.out.println(message);
    }

    public static String getMentionString(String githubName)
    {
        if (githubName.equalsIgnoreCase("shopipi"))
		{
            return "<@*************>";
		}
        return "";
    }
}
