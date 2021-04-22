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

	/**
	 * GitHubAPIを使ってレポジトリの情報を取得します
	 * @param personalAccessToken パーソナルアクセストークン
	 * @param owner オーナー名
	 * @param repo レポジトリ名
	 * @param branch ブランチ
	 */
	public GitHubAPI(String personalAccessToken, String owner, String repo, String branch)
	{
		this.personalAccessToken = personalAccessToken;
		this.owner = owner;
		this.repo = repo;
		this.branch = branch;
	}

	/**
	 * ConsoleCommandを生成します
	 * @return ProcessBuilder用の String[]型
	 */
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

			StringBuilder builder = new StringBuilder();

			while (0 <= (line = reader.read(b)))
			{
				String text = new String(b, 0, line);
				builder.append(text);
			}

			for (String ln : builder.toString().split("\r|\n"))
			{
				ln = ln.replaceAll(" ", "").replaceAll(",", "");

				if (ln.indexOf("\"") == -1) continue;
				if (ln.contains("{"))       continue;

				// Get Key Name
				String key = ln.substring(ln.indexOf("\"") + 1, ln.indexOf("\"", 1));

				if (key.equalsIgnoreCase(name))
				{
					String value = ln.substring(ln.lastIndexOf("\"", ln.length() - 2) + 1, ln.lastIndexOf("\""));
					result += value + ",";
				}
			}

			reader.close();
			input.close();
			process.destroy();
		}
		catch (ArrayIndexOutOfBoundsException ae)
		{
			Main.print("IOExceptArrayIndexOutOfBoundsExceptionion Error!!");
			ae.printStackTrace();

			//Main.client.getChannelByID(Main.sendChannelId).sendMessage(":warning: 大量なファイルの更新がありました");
		}
		catch (IOException e)
		{
			//Main.client.getChannelByID(Main.sendChannelId).sendMessage(":warning: 更新がありましたが読み込めませんでした");
		}

		return result.split(",");
	}
}
