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
	

	

	public Map<Timex3,String> detectDates(List<String> sentences, Language lang) throws DocumentCreationTimeMissingException{
		Map<Timex3,String> toReturn = new HashMap<Timex3,String>();
		HeidelTimeStandalone ht = new HeidelTimeStandalone(lang, DocumentType.NARRATIVES, OutputType.TIMEML, "config.props",  POSTagger.TREETAGGER, false);
		for (String string : sentences) {
			ht.process(string);
		}
		return toReturn;
	}
	
		
}
