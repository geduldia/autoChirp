package autoChirp;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;

import de.unihd.dbs.heideltime.standalone.exceptions.DocumentCreationTimeMissingException;
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
	public void getAndParseURLsFromDB() throws ClassNotFoundException, SQLException, DocumentCreationTimeMissingException{
		Set<String> urls = DBConnector.getURLs();
		if(urls.isEmpty()) return;
		Map<Language, List<Document>> docsByLanguage = new HashMap<Language,List< Document>>();	
		for (String url : urls) {
			Document doc = parser.parse(url);
			List<Document> docsWithLang = docsByLanguage.get(doc.getLanguage());
			if(docsWithLang == null) docsWithLang = new ArrayList<Document>();
			docsWithLang.add(doc);
			docsByLanguage.put(doc.getLanguage(), docsWithLang);
		}
		for (Language lang : docsByLanguage.keySet()) {
			SentenceSplitter st = new SentenceSplitter(lang);
			for (Document doc : docsByLanguage.get(lang)) {
				doc.setSentences(st.splitIntoSentences(doc.getText(),lang));
				System.out.println(doc.getTitle());
				DateDetector d = new DateDetector();
				d.detectDates(doc.getSentences(), doc.getLanguage());
			}
		}
	}


	


}
