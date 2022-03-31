package org.twitterSentimentAnalysis.CNN;

import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.embeddings.wordvectors.WordVectors;
import org.deeplearning4j.nn.conf.*;
import org.deeplearning4j.nn.conf.graph.MergeVertex;
import org.deeplearning4j.nn.conf.inputs.InputType;
import org.deeplearning4j.nn.conf.layers.*;
import org.deeplearning4j.nn.graph.ComputationGraph;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.evaluation.classification.Evaluation;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.learning.config.Adam;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class CNNModel {
    private static Logger log = LoggerFactory.getLogger(CNNModel.class);
    private static File savedPath =  new File("C:\\Users\\zafri\\Downloads\\Compressed\\w2vInf_v5.zip");
    static File modelPath = new File("C:\\Users\\zafri\\OneDrive\\Desktop\\NLP-Project\\model\\CNN_v7.zip");

    public static final int WORD_VEC_LENGTH = 300;
    public static final int SEED = 123;
    public static final int TRUNCATE_LENGTH = 200; //original = 100
    public static final int CLASSES = 3;
    public static final int EPOCHS = 10;
    public static final int BATCH_SIZE = 64;
    public static final int CNN_LAYER_FEATURE_MAPS = 200;
    public static PoolingType GLOBAL_POOLING = PoolingType.MAX;


    public static void main(String[] args) throws IOException {

        log.info("Loading trained word2vec! This would take a while.");

        WordVectors wordVectors = WordVectorSerializer.readWord2VecModel(savedPath);

        String dataDir = "C:\\Users\\zafri\\OneDrive\\Desktop\\NLP-Project\\dataset\\twitterSA";

        DataSetIterator trainData = new CNNIterator(dataDir,true, wordVectors, BATCH_SIZE, TRUNCATE_LENGTH).getIter();
        DataSetIterator testData = new CNNIterator(dataDir, false, wordVectors, BATCH_SIZE, TRUNCATE_LENGTH).getIter();

        Nd4j.getMemoryManager().setAutoGcWindow(10000);
        Nd4j.getMemoryManager().togglePeriodicGc(false);

        //Network Config
        ComputationGraphConfiguration config = new NeuralNetConfiguration.Builder()
                .seed(SEED)
                .updater(new Adam(1e-3))
                .l2(1e-5)
                .weightInit(WeightInit.XAVIER)
                .activation(Activation.RELU)
                .convolutionMode(ConvolutionMode.Same)
                .graphBuilder()
                .addInputs("input")
                .addLayer("cnn3", new ConvolutionLayer.Builder()
                        .kernelSize(3, WORD_VEC_LENGTH)
                        .stride(1,WORD_VEC_LENGTH)
                        .nOut(CNN_LAYER_FEATURE_MAPS)
                        .build(),"input")

                .addLayer("cnn4", new ConvolutionLayer.Builder()
                        .kernelSize(4, WORD_VEC_LENGTH)
                        .stride(1, WORD_VEC_LENGTH)
                        .nOut(CNN_LAYER_FEATURE_MAPS)
                        .build(), "input")

                .addLayer("cnn5", new ConvolutionLayer.Builder()
                        .kernelSize(5, WORD_VEC_LENGTH)
                        .stride(1, WORD_VEC_LENGTH)
                        .nOut(CNN_LAYER_FEATURE_MAPS)
                        .build(),"input")

                .addVertex("merge", new MergeVertex(), "cnn3","cnn4","cnn5")

//                //Additional Layer for bottleneck [Experiment]
                .addLayer("cnn1", new ConvolutionLayer.Builder()
                        .kernelSize(1,1)
                        .stride(1,1)
                        .nOut(CNN_LAYER_FEATURE_MAPS)
                        .build(), "merge")

                .addLayer("globalPool", new GlobalPoolingLayer.Builder()
                        .poolingType(GLOBAL_POOLING)
                        .dropOut(0.5)
                        .build(), "cnn1")

                //Additional dense layer [Experiment]
                .addLayer("dense1", new DenseLayer.Builder()
                        .nOut(200)
                        .activation(Activation.TANH)
                        .dropOut(0.5)
                        .build(), "globalPool")

//                Additional dense layer [Experiment]
                .addLayer("dense2",new DenseLayer.Builder()
                        .nOut(100)
                        .activation(Activation.TANH)
                        .dropOut(0.5)
                        .build(),"dense1")

//                .addLayer("dense3",new DenseLayer.Builder()
//                        .nOut(50)
//                        .activation(Activation.TANH)
//                        .dropOut(0.5)
//                        .build(),"dense2")

                .addLayer("out", new OutputLayer.Builder()
                        .lossFunction(LossFunctions.LossFunction.MCXENT)
                        .nOut(CLASSES)
                        .build(), "dense2")

                .setOutputs("out")
                .setInputTypes(InputType.convolutional(TRUNCATE_LENGTH, WORD_VEC_LENGTH,1))
                .build();

        ComputationGraph model = new ComputationGraph(config);
        model.init();

        //UI
//        UIServer uiServer = UIServer.getInstance();
//        StatsStorage statsStorage = new InMemoryStatsStorage();
//        uiServer.attach(statsStorage);
//        model.setListeners(new StatsListener(statsStorage));

        model.setListeners(new ScoreIterationListener(10));

//        System.out.println(config.toString());

        log.info("Training model.....");
        model.fit(trainData, EPOCHS);

        //Evaluation
        log.info("Evaluating.....");
        Evaluation evalTrain = model.evaluate(trainData);
        log.info("Training evaluation: {}", evalTrain.stats());
        System.out.println("Training evaluation: " + evalTrain.stats());
//
        Evaluation eval = model.evaluate(testData);
        log.info("Test evaluation: {}", eval.stats());
        System.out.println("Test evaluation: "+eval.stats());
//
        log.info("Saving model at {}",modelPath);
        ModelSerializer.writeModel(model,modelPath, true);
        log.info("Model saved at {}", modelPath);
        System.out.println("Done");
//

    }

}
