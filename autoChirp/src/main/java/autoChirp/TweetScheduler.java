package autoChirp;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class TweetScheduler {

	public static void scheduleInitialTweets(Map<Integer, Map<String, List<String>>> initialTweets) {

		for (Integer user : initialTweets.keySet()) {
			System.out.println("user: " + user);
			Map<String, List<String>> tweetsForUser = initialTweets.get(user);
			for (String date : tweetsForUser.keySet()) {
				System.out.println("date: " + date);
				LocalDateTime ldt = LocalDateTime.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
				LocalDateTime now;
				Duration d;
				long delay;
				now = LocalDateTime.now();
				d = Duration.between(now, ldt);
				delay = d.getSeconds();
				System.out.println("tweet in: " + delay);
				List<String> tweetsForDate = tweetsForUser.get(date);
				System.out.print("toTweet: ");
				for (String string : tweetsForDate) {
					System.out.print(string+", ");
				}
				System.out.println();
				ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
			}
		}
	}

	public static void scheduleGroup(Map<String, List<String>> tweets, int user_id) {
		LocalDateTime now;
		Duration d;
		long delay;
		for (String date : tweets.keySet()) {
			LocalDateTime ldt = LocalDateTime.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
			now = LocalDateTime.now();
			d = Duration.between(now, ldt);
			delay = d.getSeconds();
			ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(5);
			scheduler.schedule(new TwitterTask(user_id, tweets.get(date)), delay, TimeUnit.SECONDS);
		}
	}

}
