package org.twitterSentimentAnalysis;

import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.embeddings.wordvectors.WordVectors;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.LSTM;
import org.deeplearning4j.nn.conf.layers.RnnOutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.evaluation.classification.Evaluation;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.learning.config.Adam;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.io.IOException;

public class Model {
    private static Logger log = LoggerFactory.getLogger(Model.class);

    private static File savedPath =  new File("C:\\Users\\zafri\\Downloads\\Compressed\\w2vInf_v5.zip");
    static File modelPath = new File("C:\\Users\\zafri\\OneDrive\\Desktop\\NLP-Project\\model\\LSTM_v2.zip");

    public static final int WORD_VEC_LENGTH = 300;
    public static final int SEED = 123;
    public static final int TRUNCATE_LENGTH = 100;
    public static final int CLASSES = 3;
    public static final int EPOCHS = 100;
    public static final int BATCH_SIZE = 900;


    public static void main(String[] args) throws IOException {

        log.info("Loading trained word2vec! This would take a while.");

        WordVectors wordVectors = WordVectorSerializer.readWord2VecModel(savedPath);

        String dataDir = "C:\\Users\\zafri\\OneDrive\\Desktop\\NLP-Project\\dataset\\twitterSA";

        TwitterIteratorV3 trainData= new TwitterIteratorV3(dataDir,wordVectors, BATCH_SIZE,TRUNCATE_LENGTH,true);
        TwitterIteratorV3 testData= new TwitterIteratorV3(dataDir,wordVectors, BATCH_SIZE,TRUNCATE_LENGTH,false);

        //For managing GarbageCollector
        Nd4j.getMemoryManager().setAutoGcWindow(10000);
        Nd4j.getMemoryManager().togglePeriodicGc(false);

        //Network Config
        MultiLayerConfiguration config = new NeuralNetConfiguration.Builder()
                .seed(SEED)
                .updater(new Adam(1e-3))
                .l2(1e-5)
                .weightInit(WeightInit.XAVIER)
                .list()

                .layer(new LSTM.Builder()
                        .nIn(WORD_VEC_LENGTH)
                        .nOut(TRUNCATE_LENGTH)
                        .activation(Activation.TANH)
                        .build())

                .layer(new LSTM.Builder()
                        .nOut(TRUNCATE_LENGTH)
                        .activation(Activation.TANH)
                        .build())

                .layer(new RnnOutputLayer.Builder()
                        .nOut(CLASSES)
                        .activation(Activation.SOFTMAX)
                        .lossFunction(LossFunctions.LossFunction.MCXENT)
                        .build())

                .build();

        log.info("Training model...");
        MultiLayerNetwork model = new MultiLayerNetwork(config);
        model.init();

        //UI
//        UIServer uiServer = UIServer.getInstance();
//        StatsStorage statsStorage = new InMemoryStatsStorage();
//        uiServer.attach(statsStorage);
//        model.setListeners(new StatsListener(statsStorage));

        model.setListeners(new ScoreIterationListener(10));

        log.info("Training model.....");
        model.fit(trainData, EPOCHS);

        //Evaluation
        log.info("Evaluating.....");
        Evaluation eval = model.evaluate(testData);
        System.out.println("Test evaluation: "+eval.stats());

        log.info("Saving model.....");
        ModelSerializer.writeModel(model,modelPath, true);
        log.info("Model saved at {}", modelPath);

    }

}
