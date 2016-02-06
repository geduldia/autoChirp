package autoChirp.tweetCreation;

import java.util.ArrayList;
import java.util.List;

public class TweetGroup {
	public List<Tweet> tweets = new ArrayList<Tweet>();;
	public String title;
	public String description;
	public boolean enabled;

	
	public TweetGroup(String title, String description) {
		this.title = title;
		this.description = description;
	}
	
	public TweetGroup(String title, String description, boolean enabled){
		this.title = title;
		this.description = description;
		this.enabled = enabled;
	}
	
	public void setTweets(List<Tweet> tweets){
		this.tweets.addAll(tweets);
	}
	
	public void addTweet(Tweet tweet){
		tweets.add(tweet);
	}

}
