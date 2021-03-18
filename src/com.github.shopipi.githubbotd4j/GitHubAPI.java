package com.github.shopipi.githubplus;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class GitHubAPI
{
    public String personalAccessToken;
    public String owner;
    public String repo;
    public String branch;

    public GitHubAPI(String personalAccessToken, String owner, String repo, String branch)
    {
        this.personalAccessToken = personalAccessToken;
        this.owner = owner;
        this.repo = repo;
        this.branch = branch;
    }

    public String[] getCommand()
    {
        String command = "curl -u :%P.A.T% https://api.github.com/repos/%owner%/%repo%/commits/%branch%"
            .replaceAll("%P.A.T%", this.personalAccessToken)
            .replaceAll("%owner%", this.owner)
            .replaceAll("%repo%", this.repo)
            .replaceAll("%branch%", this.branch);

        return command.split(" ");
    }

    public String[] getValues(String name)
    {
        String result = "";

        try
        {
            ProcessBuilder processBuilder = new ProcessBuilder(this.getCommand());
            Process process = processBuilder.start();

            InputStream input = process.getInputStream();
            InputStreamReader reader = new InputStreamReader(input, "UTF-8");

            char[] b = new char[1024];
            int line;

            while (0 <= (line = reader.read(b)))
            {
                String text = new String(b, 0, line);

                for (String ln : text.split("\n"))
                {
                    if (ln.contains(name))
                    {
                        String split = ln.split("\": \"")[1];
                        int end = split.lastIndexOf("\"");
                        String value = split.substring(0, end);
                        result += value + ",";
                    }
                }
            }

            reader.close();
            input.close();
            process.destroy();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return result.split(",");
    }
}
