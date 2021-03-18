package com.github.shopipi.githubplus;

import java.util.Arrays;
import java.util.List;

public class Developer
{
    private long id;
    private String gitHubName;

    public static List<Developer> devs = Arrays.asList
    (
        new Developer(******************L, "*******"),
        new Developer(******************L, "*********"),
        new Developer(******************L, "*********"),
        new Developer(******************L, "********")
    );

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
