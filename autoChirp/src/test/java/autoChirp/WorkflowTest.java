package autoChirp;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;
import org.xml.sax.SAXException;

import de.unihd.dbs.heideltime.standalone.DocumentType;
import de.unihd.dbs.heideltime.standalone.HeidelTimeStandalone;
import de.unihd.dbs.heideltime.standalone.OutputType;
import de.unihd.dbs.heideltime.standalone.POSTagger;
import de.unihd.dbs.heideltime.standalone.exceptions.DocumentCreationTimeMissingException;
import de.unihd.dbs.uima.annotator.heideltime.resources.Language;

public class WorkflowTest {

	private static String dbPath = "src/test/resources/";
	private static String dbFileName = "autoChirp.db";
	private static WikipediaParser parser = new WikipediaParser();
	private static TweetFactory tweetFactory = new TweetFactory();

	@BeforeClass
	public static void dbConnection() {
		try {
			DBConnector.connect(dbPath + dbFileName);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void workFlowTest() throws  SQLException,  IOException {
		DBConnector.createOutputTables();
		DBConnector.insertURL("https://de.wikipedia.org/wiki/Geschichte_der_Stadt_Köln", 5);
		//DBConnector.insertURL("https://en.wikipedia.org/wiki/Woody_Allen", 2);
		Map<String, List<Integer>> urlsAndUserIDs = DBConnector.getURLs();
		if (urlsAndUserIDs.isEmpty())
			return;
		for (String url : urlsAndUserIDs.keySet()) {
			Document doc = parser.parse(url);
			SentenceSplitter st = new SentenceSplitter(doc.getLanguage());
			doc.setSentences(st.splitIntoSentences(doc.getText(), doc.getLanguage()));
			Map<String, List<String>> tweetsByDate = tweetFactory.getTweets(doc);
			DBConnector.addTweets(tweetsByDate, urlsAndUserIDs.get(url), doc.getTitle());
			System.out.print("usersIDs:");
			for (int u : urlsAndUserIDs.get(url)) {
				System.out.print(u + "  ");
			}
			System.out.println();
			System.out.println(doc.getTitle());
			System.out.println(doc.getUrl());
			System.out.println(doc.getLanguage());
			for (String date : tweetsByDate.keySet()) {
				for (String sentence : tweetsByDate.get(date)) {
					System.out.println(sentence);
					System.out.println();
				}
			}
			System.out.println();
		}
	}
}
