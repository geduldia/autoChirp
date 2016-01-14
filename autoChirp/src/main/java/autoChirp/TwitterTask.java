package autoChirp;

import java.util.List;
import java.util.Map;
import java.util.TimerTask;

import org.springframework.social.twitter.api.Tweet;

public class TwitterTask extends TimerTask {
	
	private  List<String> toTweet;
	private  int user_id;

	public TwitterTask(int user_id, List<String> tweets){
		setUserID(user_id);
		setTweets(tweets);
	}

	private void setTweets(List<String> tweets) {
		this.toTweet = tweets;
	}

	private void setUserID(int user_id) {
		this.user_id = user_id;
	}

	@Override
	public void run() {
		TwitterConnection tc = new TwitterConnection();
		for (String tweet : toTweet) {
		    tc.run(user_id, tweet);
		}
		 
	}

}
