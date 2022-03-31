package org.fetchTweet;

import org.backEnd.twitterSA.model.Twitter;
import org.deeplearning4j.iterator.CnnSentenceDataSetIterator;
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.embeddings.wordvectors.WordVectors;
import org.deeplearning4j.nn.graph.ComputationGraph;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.twitterSentimentAnalysis.CNN.CNNIterator;
import org.twitterSentimentAnalysis.W2V.TextPreprocessor;
import twitter4j.Status;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class SAInference {

    private static final Logger log = LoggerFactory.getLogger(SAInference.class);
    private final File savedPath = new File("C:\\Users\\zafri\\Downloads\\Compressed\\w2vInf_v5.zip");
    private final File modelPath = new File("C:\\Users\\zafri\\OneDrive\\Desktop\\NLP-Project\\model\\CNN_v7.zip");
    private final TokenizerFactory t = new DefaultTokenizerFactory();
    public static final int TRUNCATE_LENGTH = 200; //original = 100
    public static final int BATCH_SIZE = 64;
    private static DataSetIterator testIter;
    private static ComputationGraph net;
    private static WordVectors wordVectors;

    private static PrintStream consolePrint;
    private List<Status> statuses;
    static Twitterer twt = new Twitterer(consolePrint);
    static SAInference inf = new SAInference();


    public SAInference()  {
        log.info("Initialize constructor, loading word2vec");
        try {
            this.wordVectors = WordVectorSerializer.readWord2VecModel(savedPath);
            String dataDir = "C:\\Users\\zafri\\OneDrive\\Desktop\\NLP-Project\\dataset\\twitterSA";
            this.testIter = new CNNIterator(dataDir, false, wordVectors, BATCH_SIZE, TRUNCATE_LENGTH).getIter();
            this.net = ModelSerializer.restoreComputationGraph(modelPath, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Twitter> getSA(String kw) throws IOException {
        ArrayList<List> str = twt.getTextForm(kw);
        ArrayList<Twitter> res = new ArrayList<>();
        for(List lst : str) {
            ArrayList<String> sentiment = inf.getSentiment((String) lst.get(0));
            res.add(new Twitter((String) lst.get(0), (String) lst.get(1), (String) lst.get(2), (String) lst.get(3),
                    (String) lst.get(4),sentiment.get(0),sentiment.get(1)));
        }
        return res;
    }

    public ArrayList<String> getSentiment(String twt) throws IOException {
        String processedTwt = TextPreprocessor.twitterPreprocessor(twt);
        INDArray data = ((CnnSentenceDataSetIterator)testIter).loadSingleSentence(processedTwt);
        INDArray predictor = net.outputSingle(data);
        List<String> labels = testIter.getLabels();
        ArrayList<String> fetch = new ArrayList<>();

        if (predictor.getDouble(0) > predictor.getDouble(1)) {
            fetch.add(String.valueOf(predictor.getDouble(0)));
            fetch.add(labels.get(0));
        } else if (predictor.getDouble(1) > predictor.getDouble(2)) {
            fetch.add(String.valueOf(predictor.getDouble(1)));
            fetch.add(labels.get(1));
        } else {
            fetch.add(String.valueOf(predictor.getDouble(2)));
            fetch.add(labels.get(2));
        }

        return fetch;
    }

}
