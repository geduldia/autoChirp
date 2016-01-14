package autoChirp;

import java.util.List;
import java.util.Map;
import java.util.TimerTask;

import org.springframework.social.twitter.api.Tweet;

public class TwitterTask extends TimerTask {
	
	private static List<String> toTweet;
	private static int user_id;

	public TwitterTask(int user_id, List<String> tweet){
		TwitterTask.user_id = user_id;
		TwitterTask.toTweet = tweet;
	}

	@Override
	public void run() {
		TwitterConnection tc = new TwitterConnection();
		for (String tweet : toTweet) {
		    tc.run(user_id, tweet);
		}
		 
	}

}
