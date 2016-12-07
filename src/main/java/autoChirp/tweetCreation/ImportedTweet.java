package autoChirp.tweetCreation;

public class ImportetTweet extends Tweet {
	
	boolean trimmed = false;


	public ImportetTweet(String tweetDate, String content, String imageUrl, float longitude, float latitude){
		super(tweetDate, content, imageUrl, longitude, latitude);
	}
	public boolean isTrimmed() {
		return trimmed;
	}

	public void setTrimmed(boolean trimmed) {
		this.trimmed = trimmed;
	}
	

	
	

}
