package autoChirp;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import de.unihd.dbs.uima.annotator.heideltime.resources.Language;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;

/**
 * @author geduldia
 * 
 * A class to split text into sentences using the OpenNLP SentenceDetector
 *
 */


public  class SentenceSplitter {
	
	
	private SentenceModel sentenceModel;
	private Language language;
	
	/**
	 * @param language
	 */
	public SentenceSplitter(Language language){
			setSentenceSplittingModel(language);
	}
	
	public SentenceSplitter(){
		
	}
	
	/**
	 * @param language
	 * loads the sentencemodel for a given language
	 * 
	 */
	private void setSentenceSplittingModel(Language language){
		String model = "/opennlp/"+language.toString()+"-sent.bin";
		InputStream modelIn = null;
		try {
			modelIn = getClass().getResourceAsStream(model);
			System.out.println(modelIn);
			sentenceModel = new SentenceModel(modelIn);
			this.language = language;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (modelIn != null) {
				try {
					modelIn.close();
				} catch (IOException e) {
				}
			}
		}
	}

	/**
	 * @param text - text to split
	 * @param language
	 * @return
	 */
	public List<String> splitIntoSentences(String text, Language language){
		if(this.language != language){
			setSentenceSplittingModel(language);
		}
		String[] sentences = null;
		SentenceDetectorME detector = new SentenceDetectorME(sentenceModel);
		sentences = detector.sentDetect(text);	
		return Arrays.asList(sentences);	
	}
}
