package org.twitterSentimentAnalysis;

import org.deeplearning4j.models.embeddings.wordvectors.WordVectors;
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.DataSetPreProcessor;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.indexing.INDArrayIndex;
import org.nd4j.linalg.indexing.NDArrayIndex;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

public class TwitterIteratorV3 implements DataSetIterator {
    private final WordVectors wordVectors;
    private final int batchSize;
    private final int vectorSize;

    private int positiveCursor = 0;
    private int negativeCursor = 0;
    private int neutralCursor = 0;

    private final ArrayList<String> positiveFiles;
    private final ArrayList<String> negativeFiles;
    private final ArrayList<String> neutralFiles;
    private final TokenizerFactory tokenizerFactory;

    /**
     * @param dataDir the directory of the Tweets data
     * @param wordVectors       WordVectors object
     * @param batchSize         Size of each minibatch for training
     * @param train             If true: return the training data. If false: return the testing data.
     */

    public TwitterIteratorV3(String dataDir, WordVectors wordVectors, int batchSize, boolean train) throws IOException {
        this.batchSize = batchSize;
        this.vectorSize = wordVectors.getWordVector(wordVectors.vocab().wordAtIndex(0)).length;

        //Training or testing dataset
        String subDirectory = (train) ? "/train/" : "/test/";

        String dataDirectory = dataDir + subDirectory;

        positiveFiles = TextPreprocessor.readTXT(new File(dataDirectory,"positive.txt"));
        negativeFiles = TextPreprocessor.readTXT(new File(dataDirectory,"negative.txt"));
        neutralFiles = TextPreprocessor.readTXT(new File(dataDirectory,"neutral.txt"));

        this.wordVectors = wordVectors;

        tokenizerFactory = new DefaultTokenizerFactory();
        tokenizerFactory.setTokenPreProcessor(new CommonPreprocessor());
    }


    @Override
    public DataSet next(int num) {
        if ((positiveCursor == positiveFiles.size()) && (negativeCursor == negativeFiles.size()) ) {
            throw new NoSuchElementException();
        }
        try {
            return nextDataSet(num);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private DataSet nextDataSet(int batchSize) throws IOException {

        List<String> twitterArray = new ArrayList<>(batchSize);
        int[] labelArr = new int[batchSize];

        int splitBatchSize = batchSize/3;
        if (positiveCursor < positiveFiles.size()) {

            for (int i = 0; i < splitBatchSize && positiveCursor < positiveFiles.size(); ++i) {
                twitterArray.add(positiveFiles.get(positiveCursor));
                labelArr[i] = 0;
                ++positiveCursor;
            }
        }

        for (int i = splitBatchSize; i < splitBatchSize*2 && negativeCursor < negativeFiles.size(); ++i) {
            twitterArray.add(negativeFiles.get(negativeCursor));
            labelArr[i] = 2;
            ++negativeCursor;
        }

        for(int i = splitBatchSize*2; i < splitBatchSize*3 && neutralCursor < neutralFiles.size(); ++i) {
            twitterArray.add(neutralFiles.get(neutralCursor));
            labelArr[i] = 1;
            ++neutralCursor;
        }

        List<List<String>> allTokens = new ArrayList<>(twitterArray.size());
        int maxLength = 0;
        for (String s : twitterArray) {
            List<String> tokens = tokenizerFactory.create(s).getTokens();
            List<String> tokensFiltered = new ArrayList<>();
            for (String t : tokens) {
                if (wordVectors.hasWord(t)) tokensFiltered.add(t);
            }
            allTokens.add(tokensFiltered);
            maxLength = Math.max(maxLength, tokensFiltered.size());
        }

        //Create data for training
        INDArray features = Nd4j.create(twitterArray.size(), vectorSize, maxLength);
        //Three labels: positive(0) / neutral(1) / negative(2)
        INDArray labels = Nd4j.create(twitterArray.size(), 3, maxLength);

        //Mask arrays contain 1 if data is present at that time step for that example, or 0 if data is just padding
        INDArray featuresMask = Nd4j.zeros(twitterArray.size(), maxLength);
        INDArray labelsMask = Nd4j.zeros(twitterArray.size(), maxLength);

        for (int i = 0; i < twitterArray.size(); ++i) {
            List<String> tokens = allTokens.get(i);

            int seqLength = Math.min(tokens.size(), maxLength);

            for (int j = 0; j < tokens.size() && j < maxLength; ++j) {
                String token = tokens.get(j);
                INDArray vector = wordVectors.getWordVectorMatrix(token);
                features.put(new INDArrayIndex[]{NDArrayIndex.point(i), NDArrayIndex.all(), NDArrayIndex.point(j)}, vector);

                // Assign "1" to each position where a feature is present, that is, in the interval of [0, seqLength)
                featuresMask.get(new INDArrayIndex[]{NDArrayIndex.point(i), NDArrayIndex.interval(0, seqLength)}).assign(1);
            }

            int lastIdx = Math.min(tokens.size(), maxLength);
            labels.putScalar(new int[]{i, labelArr[i], lastIdx - 1}, 1.0);
            labelsMask.putScalar(new int[]{i, lastIdx - 1}, 1.0);
        }

        return new DataSet(features, labels, featuresMask, labelsMask);
    }

    public int totalExamples() {
        return positiveFiles.size() + negativeFiles.size() + neutralFiles.size();
    }

    @Override
    public int inputColumns() {
        return vectorSize;
    }

    @Override
    public int totalOutcomes() {
        return 3;
    }

    @Override
    public void reset() {
        positiveCursor = 0;
        negativeCursor = 0;
        neutralCursor = 0;
    }

    public boolean resetSupported() {
        return true;
    }

    @Override
    public boolean asyncSupported() {
        return true;
    }

    @Override
    public int batch() {
        return batchSize;
    }

    public int numExamples() {
        return totalExamples();
    }

    @Override
    public void setPreProcessor(DataSetPreProcessor preProcessor) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<String> getLabels() {
        return Arrays.asList("positive", "neutral", "negative");
    }

    @Override
    public boolean hasNext() {
        return (positiveCursor + negativeCursor + neutralCursor) < numExamples();
    }

    @Override
    public DataSet next() {
        return next(batchSize);
    }

    @Override
    public void remove() {

        throw new UnsupportedOperationException();
    }

    @Override
    public DataSetPreProcessor getPreProcessor() {
        throw new UnsupportedOperationException("Not implemented");
    }
}