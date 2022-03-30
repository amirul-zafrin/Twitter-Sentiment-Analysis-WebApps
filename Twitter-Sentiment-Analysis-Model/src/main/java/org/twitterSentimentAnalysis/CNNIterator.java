/**
 This datasetIterator is not fully build yet, still in progress as for 28/3/2022
 This iterator is for Model2.java
 */

package org.twitterSentimentAnalysis;

import org.deeplearning4j.iterator.CnnSentenceDataSetIterator;
import org.deeplearning4j.models.embeddings.wordvectors.WordVectors;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class CNNIterator {

    private ArrayList<String> positiveFiles;
    private ArrayList<String> negativeFiles;
    private ArrayList<String> neutralFiles;
    private WordVectors wordVectors;
    private int minibatchSize;
    private int maxSentenceLength;

    public CNNIterator(String dataDir, Boolean train, WordVectors wordVectors, int minibatchSize, int maxSentenceLength) throws IOException {

//        String dataDir = "C:\\Users\\zafri\\OneDrive\\Desktop\\NLP-Project\\dataset\\twitterSA";
        String subDirectory = (train) ? "/train/" : "/test/";

        String dataDirectory = dataDir + subDirectory;

        positiveFiles = TextPreprocessor.readTXT(new File(dataDirectory,"positive.txt"));
        negativeFiles = TextPreprocessor.readTXT(new File(dataDirectory,"negative.txt"));
        neutralFiles = TextPreprocessor.readTXT(new File(dataDirectory,"neutral.txt"));

        this.minibatchSize = minibatchSize;
        this.wordVectors = wordVectors;
        this.maxSentenceLength = maxSentenceLength;
    }

    public DataSetIterator getIter() {
        Map<String, List<String>> tweetsMap = new HashMap<>();
        tweetsMap.put("Positive", positiveFiles);
        tweetsMap.put("Negative", negativeFiles);
        tweetsMap.put("Neutral", neutralFiles);

        Random rng = new Random(123);

        StringLabeledSentenceProvider sentenceProvider = new StringLabeledSentenceProvider(tweetsMap,rng);
        return new CnnSentenceDataSetIterator.Builder(CnnSentenceDataSetIterator.Format.CNN2D)
                .sentenceProvider(sentenceProvider)
                .wordVectors(wordVectors)
                .minibatchSize(minibatchSize)
                .maxSentenceLength(maxSentenceLength)
                .useNormalizedWordVectors(false)
                .build();
    }
}