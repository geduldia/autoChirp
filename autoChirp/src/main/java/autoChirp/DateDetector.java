package autoChirp;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cyberneko.html.parsers.DOMParser;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import de.unihd.dbs.heideltime.standalone.DocumentType;
import de.unihd.dbs.heideltime.standalone.HeidelTimeStandalone;
import de.unihd.dbs.heideltime.standalone.OutputType;
import de.unihd.dbs.heideltime.standalone.POSTagger;
import de.unihd.dbs.heideltime.standalone.exceptions.DocumentCreationTimeMissingException;
import de.unihd.dbs.uima.types.heideltime.Timex3;
import de.unihd.dbs.uima.annotator.heideltime.resources.Language;

public class DateDetector {
	

	

	public  void detectDates(Document doc) throws DocumentCreationTimeMissingException, SAXException, IOException{
		Map<String, List<String>> sentencesByDate = new HashMap<String,List<String>>();
		HeidelTimeStandalone ht = new HeidelTimeStandalone(doc.getLanguage(), DocumentType.NARRATIVES, OutputType.TIMEML, "config.props",  POSTagger.TREETAGGER, false);
		for (String sentence : doc.getSentences()) {
			String processed = ht.process(sentence);
			if(processed.contains("<TIMEX3 ")){
				List<String> dates = getDates(processed);
				System.out.println(processed);
			}
		}
		
		
	}

	private List<String> getDates(String processed) {
		// TODO Auto-generated method stub
		return null;
	}

	
	
		
}
