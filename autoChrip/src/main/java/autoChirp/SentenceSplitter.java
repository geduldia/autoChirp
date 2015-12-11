package autoChirp;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.unihd.dbs.uima.annotator.heideltime.resources.Language;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;

public class SentenceSplitter {
	private SentenceModel sentenceModel;
	private Language language;
	
	public SentenceSplitter(Language language){
			setSentenceSplittingModel(language);
	}
	
	public SentenceSplitter(){
		
	}
	
	private void setSentenceSplittingModel(Language language){
		String model = "data/OpenNLP_SentenceModels/"+language.toString()+"-sent.bin";
		System.out.println(model);
		InputStream modelIn = null;
		try {
			modelIn = new FileInputStream(model);
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

	public List<String> splitIntoSentences(String text, Language lang){
		if(this.language != lang){
			setSentenceSplittingModel(lang);
		}
		String[] sentences = null;
		SentenceDetectorME detector = new SentenceDetectorME(sentenceModel);
		sentences = detector.sentDetect(text);	
		return Arrays.asList(sentences);
	}
}
