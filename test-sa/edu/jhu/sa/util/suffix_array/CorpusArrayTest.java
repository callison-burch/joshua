package edu.jhu.sa.util.suffix_array;

import java.io.IOException;


import org.testng.Assert;
import org.testng.annotations.Test;

import edu.jhu.sa.util.sentence.Vocabulary;
import edu.jhu.sa.util.suffix_array.CorpusArray;
import edu.jhu.sa.util.suffix_array.MemoryMappedCorpusArray;
import edu.jhu.sa.util.suffix_array.SuffixArrayFactory;


public class CorpusArrayTest {

	@Test
	public void writeToDisk() {
		
		String filename = "data/tiny.en";
		int numSentences = 5;  // Should be 5 sentences in tiny.en
		int numWords = 89;     // Should be 89 words in tiny.en
		
		Vocabulary vocab = new Vocabulary();
		
		try {
			
			SuffixArrayFactory.createVocabulary(filename, vocab);
			CorpusArray corpus = SuffixArrayFactory.createCorpusArray(filename, vocab, numWords, numSentences);
			corpus.writeVocabToFile(filename+".bin");
			corpus.writeSentencesToFile(filename+".sbin");
			
			MemoryMappedCorpusArray mmCorpus = new MemoryMappedCorpusArray(vocab, filename+".bin", numWords*4, filename+".sbin", numSentences*4);
			
			for (int i=0; i<corpus.size(); i++) {
				Assert.assertEquals(mmCorpus.getWordID(i), corpus.getWordID(i));
			}
			
			for (int i=0; i<corpus.sentences.length; i++) {
				Assert.assertEquals(corpus.getSentencePosition(i), mmCorpus.getSentencePosition(i));
			}
			
		} catch (IOException e) {
			Assert.fail(e.getLocalizedMessage());
		}
		
	}
	
}
