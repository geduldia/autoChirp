package autoChirp.tweeting;

import java.util.List;
import java.util.Map;

import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;

import autoChirp.DBConnector;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

public class TwitterConnection {

	public void run(int user_id, String toTweet) {
		System.out.println("prepare for tweeting: " + toTweet);
		
		String[] userConfig = DBConnector.getUserConfig(user_id);
		String[] appConfig= DBConnector.getAppConfig();
		String consumerKey = appConfig[1];
		String consumerSecret = appConfig[2];
		String token = userConfig[1];
		String tokenSecret = userConfig[2];

		
		Authentication auth = new OAuth1(consumerKey, consumerSecret, token,
				tokenSecret);
		
		Twitter twitter = new TwitterFactory().getInstance();
		twitter.setOAuthConsumer(consumerKey, consumerSecret);
		twitter.setOAuthAccessToken(new AccessToken(token, tokenSecret));

	    Status status;
		try {
			status = twitter.updateStatus(toTweet);
			System.out.println("Successfully updated the status to [" + status.getText() + "].");
		} catch (TwitterException e) {
			e.printStackTrace();
		}
	    
		
	}

}
