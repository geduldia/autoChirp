package autoChirp.tweeting;

import java.util.List;
import java.util.Map;
import java.util.TimerTask;

import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.impl.TwitterTemplate;

import autoChirp.DBConnector;
import autoChirp.tweetCreation.Tweet;



public class TwitterTask extends TimerTask {

	private Tweet toTweet;
	private int user_id;

	public TwitterTask(int user_id, Tweet tweet) {
		this.user_id = user_id;
		this.toTweet = tweet;
	}

	@Override
	public void run() {
		TwitterConnection tc = new TwitterConnection();
		tc.run(user_id, toTweet);
	}

}
