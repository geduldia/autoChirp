package autoChirp;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;

import de.unihd.dbs.uima.annotator.heideltime.resources.Language;

public class WorkflowTest {
	
	private static String dbPath = "C:/sqlite/";
	private static String dbFileName = "autoChirp.db";
	private static WikipediaParser parser = new WikipediaParser();
	
	
	@BeforeClass
	public static void dbConnection(){
		try {
			DBConnector.connect(dbPath+dbFileName);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	@Test
	public void checkForUrls() throws ClassNotFoundException, SQLException{
		Set<String> urls = DBConnector.getURLs();
		System.out.println(urls.size());
		Map<Language, List<Document>> docsByLanguage = new HashMap<Language,List< Document>>();
		if(urls.isEmpty()) return;
		for (String url : urls) {
			Document doc = parser.parse(url);
			List<Document> docs = docsByLanguage.get(doc.getLanguage());
			if(docs == null) docs = new ArrayList<Document>();
			docs.add(doc);
			docsByLanguage.put(doc.getLanguage(), docs);
		}
		for (Language lang : docsByLanguage.keySet()) {
			SentenceSplitter st = new SentenceSplitter(lang);
			for (Document doc : docsByLanguage.get(lang)) {
				doc.setSentences(st.splitIntoSentences(doc.getText()));
				System.out.println(doc.getTitle());
			}
		}
	}
	
//	@Test
//	public void parseURLTest(){
//		WikipediaParser parser = new WikipediaParser();
//		Document document = parser.parse(url);
//		System.out.println("title: "+ document.getTitle());
////		System.out.println(document.getText());
////		System.out.println(document.getUrl());
//		Assert.assertTrue(document.getTitle() != null);
//		Assert.assertTrue(document.getUrl().equals(url));
//		Assert.assertTrue(document.getText()!= null);
//		Assert.assertTrue(document.getLanguage() == Language.GERMAN);
//	}
//	
//	@Test
//	public void sentenceSplitterTest(){
//		WikipediaParser parser = new WikipediaParser();
//		Document document = parser.parse(url);
//		SentenceSplitter splitter = new SentenceSplitter(Language.GERMAN);
//		document.setSentences(splitter.splitIntoSentences(document.getText()));
//		for (String s : document.getSentences()) {
//			System.out.println(s);
//			System.out.println();
//		}
//	}
//	

	


}
