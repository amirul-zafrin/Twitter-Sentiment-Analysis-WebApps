/**
 This datasetIterator is not fully build yet, still in progress as for 28/3/2022
 This iterator is for Model2.java
 */


//package org.deeplearning4j.examples.sample;
//
//import org.deeplearning4j.iterator.CnnSentenceDataSetIterator;
//import org.deeplearning4j.iterator.LabeledSentenceProvider;
//import org.deeplearning4j.iterator.provider.FileLabeledSentenceProvider;
//import org.deeplearning4j.models.embeddings.wordvectors.WordVectors;
//import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.*;
//
//public class CNNIterator {
//
//    private ArrayList<String> positiveFiles;
//    private ArrayList<String> negativeFiles;
//    private ArrayList<String> neutralFiles;
//
//    public DataSetIterator CNNIterator(Boolean train, WordVectors wordVectors, int minibatchSize, int maxSentenceLength) throws IOException {
//
//        String dataDir = "C:\\Users\\zafri\\OneDrive\\Desktop\\NLP-Project\\dataset\\twitterSA";
//        String subDir = (train) ? "train" : "test";
//        String dir = "C:\\Users\\zafri\\OneDrive\\Desktop\\NLP-Project\\dataset\\" + subDir;
//
//        String subDirectory = (train) ? "/train/" : "/test/";
//
//        String dataDirectory = dataDir + subDirectory;
//
//        positiveFiles = TextPreprocessor.readTXT(new File(dataDirectory,"positive.txt"));
//        negativeFiles = TextPreprocessor.readTXT(new File(dataDirectory,"negative.txt"));
//        neutralFiles = TextPreprocessor.readTXT(new File(dataDirectory,"neutral.txt"));
//
//        Map<String, List<String> reviewFilesMap = new HashMap<>();
//        reviewFilesMap.put("Positive", positiveFiles);
//        reviewFilesMap.put("Negative", negativeFiles);
//        reviewFilesMap.put("Neutral", neutralFiles);
//
//
//        Random rng = new Random(12345);
//        LabeledSentenceProvider sentenceProvider = new FileLabeledSentenceProvider(reviewFilesMap, rng);
//        Labeled
//
//        return new CnnSentenceDataSetIterator.Builder(CnnSentenceDataSetIterator.Format.CNN2D)
//                .sentenceProvider(sentenceProvider)
//                .wordVectors(wordVectors)
//                .minibatchSize(minibatchSize)
//                .maxSentenceLength(maxSentenceLength)
//                .useNormalizedWordVectors(false)
//                .build();
//    }
//}
