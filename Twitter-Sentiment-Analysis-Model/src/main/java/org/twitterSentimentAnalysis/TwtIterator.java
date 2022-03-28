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

import static org.twitterSentimentAnalysis.TextPreprocessor.*;

public class TwtIterator implements DataSetIterator {

    private final WordVectors wordVectors;
    private final int batchSize;
    private final int vectorSize;
    private final int truncateLength;
    private final TokenizerFactory tokenizerFactory;
    public static int dataLen;
    private int cursor = 0;

    public final File posFile;
    public final File negFile;
    public ArrayList<String> posData;
    public ArrayList<String> negData;


    private double posNegRatio;

    public TwtIterator(WordVectors wordVectors, int batchSize, int truncateLength, boolean train) throws IOException {

        this.batchSize = batchSize;
        this.vectorSize = wordVectors.getWordVector(wordVectors.vocab().wordAtIndex(0)).length;

        this.wordVectors = wordVectors;
        this.truncateLength = truncateLength;

        tokenizerFactory = new DefaultTokenizerFactory();
        tokenizerFactory.setTokenPreProcessor(new CommonPreprocessor());

        String subDir = (train) ? "train" : "test";
        String dir = "C:\\Users\\zafri\\OneDrive\\Desktop\\NLP-Project\\dataset\\" + subDir;

        posFile = new File(dir,"positive.txt");
        negFile = new File(dir, "negative.txt");

        posData =  readTXT(posFile);
        negData = readTXT(posFile);

        this.posNegRatio = posData.size() / (double) negData.size();

    }

    @Override
    public DataSet next(int batchSize) {
        if((cursor == posData.size() + negData.size())) {
            throw new NoSuchElementException();
        }
        try {
            return nextDataSet(batchSize);
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    private DataSet nextDataSet(int batchSize) throws IOException {
        List<String> twtArray = new ArrayList<>(batchSize);
        boolean[] positive = new boolean[batchSize];

        for(int i = 0; i < batchSize && cursor<totalExamples(); i++) {
            if(cursor % 2 == 0 ) {
                int posTwt = cursor / 2;
                twtArray.add(posData.get(posTwt));
                positive[i] = true;
            } else {
                int negTwt = cursor/2;
                twtArray.add(negData.get(negTwt));
                positive[i] = false;
            }
            cursor++;
        }

        List<List<String>> allTokens = new ArrayList<>(twtArray.size());
        int maxLength = 0;
        for(String s : twtArray) {
            List<String> tokens = tokenizerFactory.create(s).getTokens();
            List<String> tokensFiltered = new ArrayList<>();
            for (String t : tokens) {
                if(wordVectors.hasWord(t)) tokensFiltered.add(t);
            }
            allTokens.add(tokensFiltered);
            maxLength = Math.max(maxLength, tokensFiltered.size());
        }

        if(maxLength > truncateLength) maxLength = truncateLength;

        INDArray features = Nd4j.create(new int[]{twtArray.size(),vectorSize, maxLength},'f');
        INDArray labels = Nd4j.create(new int[]{twtArray.size(),2,maxLength},'f');

        INDArray featureMask = Nd4j.zeros(twtArray.size(),maxLength);
        INDArray labelsMask = Nd4j.zeros(twtArray.size(),maxLength);

        for(int i = 0; i < twtArray.size();i++) {
            List<String> tokens = allTokens.get(i);

            int seqLength = Math.min(tokens.size(), maxLength);
            INDArray vectors = wordVectors.getWordVectors(tokens.subList(0,seqLength)).transpose();

            features.put(
              new INDArrayIndex[] {
                      NDArrayIndex.point(i), NDArrayIndex.all(), NDArrayIndex.interval(0, seqLength)
              }, vectors);

            featureMask.get(new INDArrayIndex[] {NDArrayIndex.point(i), NDArrayIndex.interval(0,seqLength)}).assign(1);

            int idx = (positive[i] ? 0 : 1);
            int lastIdx = Math.min(tokens.size(),maxLength);
            labels.putScalar(new int[]{i,idx,lastIdx-1},1.0);
            labelsMask.putScalar(new int[]{i,lastIdx-1},1.0);

        }

        return new DataSet(features,labels,featureMask,labelsMask);

    }

    public int totalExamples() {
        return negData.size() + posData.size();
    }

    @Override
    public int inputColumns() {
        return vectorSize;
    }

    @Override
    public int totalOutcomes() {
        return 2;
    }

    @Override
    public boolean resetSupported() {
        return true;
    }

    @Override
    public boolean asyncSupported() {
        return true;
    }

    @Override
    public void reset() {
        cursor = 0;
    }

    @Override
    public int batch() {
        return batchSize;
    }

    @Override
    public void setPreProcessor(DataSetPreProcessor dataSetPreProcessor) {
        throw new UnsupportedOperationException();
    }

    @Override
    public DataSetPreProcessor getPreProcessor() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public List<String> getLabels() {
        return Arrays.asList("positive","negative");
    }

    @Override
    public boolean hasNext() {
        return cursor < totalExamples();
    }

    @Override
    public DataSet next() {
        return next(batchSize);
    }

    @Override
    public void remove() {
    }

    public boolean isPositive(int idx) {
        return idx%2==0;
    }

}