package autoChirp.tweeting;

import java.util.List;
import java.util.Map;

import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.impl.TwitterTemplate;

import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;

import autoChirp.DBConnector;

public class TwitterConnection {

	public void run(int user_id, String toTweet) {
		System.out.println("prepare for tweeting: " + toTweet);
		
		String[] userConfig = DBConnector.getUserConfig(user_id);
		String[] appConfig= DBConnector.getAppConfig();
		String consumerKey = appConfig[1];
		String consumerSecret = appConfig[2];
		String token = userConfig[1];
		String tokenSecret = userConfig[2];
		//tweeting with spring-social
		Twitter twitter = new TwitterTemplate(consumerKey, consumerSecret, token, tokenSecret);
		twitter.timelineOperations().updateStatus(toTweet);


		//tweetig with twitter4j
//		Twitter twitter = new TwitterFactory().getInstance();
//		twitter.setOAuthConsumer(consumerKey, consumerSecret);
//		twitter.setOAuthAccessToken(new AccessToken(token, tokenSecret));
//
//	    Status status;
//		try {
//			status = twitter.updateStatus(toTweet);
//			System.out.println("Successfully updated the status to [" + status.getText() + "].");
//		} catch (TwitterException e) {
//			e.printStackTrace();
//		}
	    
		
	}

}
