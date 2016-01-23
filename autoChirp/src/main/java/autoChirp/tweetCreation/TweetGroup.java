package autoChirp.tweetCreation;

import java.util.ArrayList;
import java.util.List;

public class TweetGroup {
	public List<Tweet> tweets;
	public String title;

	
	public TweetGroup(String title) {
		this.title = title;
	}
	
	public void setTweets(List<Tweet> tweets){
		if(this.tweets == null){
			this.tweets = new ArrayList<Tweet>();
		}
		this.tweets.addAll(tweets);
	}
	
	public void addTweet(Tweet tweet){
		if(this.tweets == null){
			tweets = new ArrayList<Tweet>();
		}
		tweets.add(tweet);
	}

}
