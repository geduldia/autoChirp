package autoChirp.tweetCreation;

/**
 * 
 * @author Alena
 *represents a single tweet
 */
public class Tweet implements Comparable<Tweet> {
	
	public Tweet(String tweetDate, String content){
		this.tweetDate = tweetDate;
		this.content = content;
	}
	
	private String tweetDate;
	public String getTweetDate() {
		return tweetDate;
	}
	public void setTweetDate(String tweetDate) {
		this.tweetDate = tweetDate;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}

	private String content;
	@Override
	public int compareTo(Tweet t) {
		return this.tweetDate.compareTo(t.tweetDate);
	}
}
