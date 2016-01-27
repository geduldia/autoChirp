
package autoChirp;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

import autoChirp.preProcessing.Document;
import autoChirp.preProcessing.SentenceSplitter;
import autoChirp.preProcessing.parser.WikipediaParser;
import autoChirp.tweetCreation.Tweet;
import autoChirp.tweetCreation.TweetFactory;
import autoChirp.tweetCreation.TweetGroup;

public class TweetCreationWorkflow {
	
	/**
	 * @author Alena
	 * 
	 * junit test-class for the data-mining workflow
	 * - insert and read urls to parse
	 * - parse urls and extract dates and tweets
	 * - store tweets and tweet-date
	 */

	private static String dbPath = "src/test/resources/";
	private static String dbFileName = "autoChirp.db";
	private static String dbCreationFileName = "src/main/resources/database/createDatabaseFile.sql";
	private static WikipediaParser parser = new WikipediaParser();
	private static TweetFactory tweetFactory = new TweetFactory();

	@BeforeClass
	public static void dbConnection() {
		DBConnector.connect(dbPath + dbFileName);
		DBConnector.createOutputTables(dbCreationFileName);
	}

	@Test
	public void dataMiningTest() throws SQLException, IOException {
		DBConnector.isertUrl("https://en.wikipedia.org/wiki/History_of_New_York", 5);
		Map<String, List<Integer>> urlsAndUserIDs = DBConnector.getUrls();
		if (urlsAndUserIDs.isEmpty())
			return;
		for (String url : urlsAndUserIDs.keySet()) {
			TweetGroup group = tweetFactory.getTweetsFromUrl(url, parser );
			for (int user : urlsAndUserIDs.get(url)) {
				DBConnector.insertTweetGroup(group, user);
			}
			for (Tweet tweet : group.tweets) {
				System.out.print(tweet.getTweetDate()+": ");
				System.out.println(tweet.getContent());
			}
			System.out.println();
		}
	}
}
