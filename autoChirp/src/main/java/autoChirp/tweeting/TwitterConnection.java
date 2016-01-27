package autoChirp.tweeting;


import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.impl.TwitterTemplate;

import autoChirp.DBConnector;
import autoChirp.tweetCreation.Tweet;

public class TwitterConnection {

	public void run(int user_id, Tweet toTweet) {
		
		String[] userConfig = DBConnector.getUserConfig(user_id);
		String[] appConfig= DBConnector.getAppConfig();
		String consumerKey = appConfig[1];
		String consumerSecret = appConfig[2];
		String token = userConfig[1];
		String tokenSecret = userConfig[2];
		//tweeting with spring-social
		Twitter twitter = new TwitterTemplate(consumerKey, consumerSecret, token, tokenSecret);
		twitter.timelineOperations().updateStatus(toTweet.content);
		DBConnector.flagAsTweeted(toTweet, user_id);


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
