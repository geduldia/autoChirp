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
		setUserID(user_id);
		setTweet(tweet);
	}

	private void setTweet(Tweet tweet) {
		this.toTweet = tweet;
	}

	private void setUserID(int user_id) {
		this.user_id = user_id;
	}

	@Override
	public void run() {
		TwitterConnection tc = new TwitterConnection();
		tc.run(user_id, toTweet.getContent());
	}

}
