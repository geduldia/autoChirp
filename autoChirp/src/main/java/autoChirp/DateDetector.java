package autoChirp;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.unihd.dbs.heideltime.standalone.DocumentType;
import de.unihd.dbs.heideltime.standalone.HeidelTimeStandalone;
import de.unihd.dbs.heideltime.standalone.OutputType;
import de.unihd.dbs.heideltime.standalone.POSTagger;
import de.unihd.dbs.heideltime.standalone.exceptions.DocumentCreationTimeMissingException;
import de.unihd.dbs.uima.types.heideltime.Timex3;
import de.unihd.dbs.uima.annotator.heideltime.resources.Language;

public class DateDetector {
	

	
	HeidelTimeStandalone ht = new HeidelTimeStandalone(Language.GERMAN, DocumentType.COLLOQUIAL	, OutputType.TIMEML, "src/main/resources/config.props");

	public Map<Timex3,String> detectDates(List<String> sentences) throws DocumentCreationTimeMissingException{
		Map<Timex3,String> toReturn = new HashMap<Timex3,String>();
		for (String string : sentences) {
			String s = ht.process(string, new Date(2015, 11, 12));
			System.out.println(s);
		}
		return toReturn;
	}
	
		
}
