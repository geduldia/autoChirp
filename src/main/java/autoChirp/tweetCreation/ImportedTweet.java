package autoChirp.tweetCreation;

public class ImportedTweet extends Tweet {

	boolean trimmed = false;

	public ImportedTweet(String tweetDate, String content, String imageUrl, float longitude, float latitude) {
		super(tweetDate, content, imageUrl, longitude, latitude);
	}

	public boolean isTrimmed() {
		return trimmed;
	}

	public void setTrimmed(boolean trimmed) {
		this.trimmed = trimmed;
	}

}
