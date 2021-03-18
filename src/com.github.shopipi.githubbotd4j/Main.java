package com.github.shopipi.githubplus;

import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.ActivityType;
import sx.blah.discord.handle.obj.StatusType;
import sx.blah.discord.util.EmbedBuilder;

public class Main
{
    private static String TOKEN = "***********************************************************";
    public static IDiscordClient client;

    public static long sendChannelId = ******************L;

    public static void main(String[] args)
    {
        initClient();
    }

    private static void initClient()
    {
        client = new ClientBuilder().withToken(TOKEN).build();
        client.getDispatcher().registerListener(new Main());
        client.getDispatcher().registerListener(new GitHubMessageListener());
        client.login();
    }

    @EventSubscriber
    public void onReady(ReadyEvent e)
    {
        print("###########");
        print("BOT STARTED");
        print("###########");

        client.changePresence(StatusType.ONLINE, ActivityType.PLAYING, "/? for Help");
    }

    @EventSubscriber
    public void onCommand(MessageReceivedEvent e)
    {
        if (e.getAuthor().getLongID() != 725140295330824203L) return;

        if (e.getMessage().getContent().startsWith("/"))
        {
            String command = e.getMessage().getContent().substring(1);

            if (command.equalsIgnoreCase("stop"))
            {
                if (e.getAuthor().getLongID() == Developer.getDevByGitHubName("shopipi").getId())
                {
                    e.getChannel().sendMessage("Bye bye");
                    client.logout();
                }
            }

            if (command.equalsIgnoreCase("?"))
            {
                EmbedBuilder builder = new EmbedBuilder()
                        .withAuthorIcon(client.getApplicationIconURL())
                        .withAuthorName("GitHub+ BOT for Discord")
                        .withAuthorUrl("https://github.com/shopipi/GitHubPlus")
                        .withColor(0x6A2786)
                        .appendDesc("```Summaryのコメントと更新があったファイル名を表示します \n")
                        .appendDesc("プライベートレポジトリ対応 ```\n\n")
                        .appendDesc("**ソースコード** \n")
                        .appendDesc("https://github.com/shopipi/GitHubPlus");

                e.getChannel().sendMessage(builder.build());
            }
        }
    }

    public static void print(Object message)
    {
        System.out.println(message);
    }
}
