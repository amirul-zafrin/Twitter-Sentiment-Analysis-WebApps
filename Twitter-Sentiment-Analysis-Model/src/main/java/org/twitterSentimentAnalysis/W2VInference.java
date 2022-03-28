package org.twitterSentimentAnalysis;

import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.deeplearning4j.text.sentenceiterator.BasicLineIterator;
import org.deeplearning4j.text.sentenceiterator.SentenceIterator;
import org.deeplearning4j.text.sentenceiterator.SentencePreProcessor;
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

public class W2VInference {
    private static Logger log = LoggerFactory.getLogger(W2VInference.class);
    private static File savedPath =  new File("C:\\Users\\zafri\\Downloads\\Compressed\\w2vInf_v5.zip");
    private static File trainedW2V =  new File("C:\\Users\\zafri\\Downloads\\Compressed\\malay_word2vec\\mswiki.vector");

    public static void main(String[] args) throws Exception {

        Word2Vec word2Vec = null;

        if(!savedPath.exists()){
            log.info("{} is not exist! Building ....", savedPath);
            File dataPath = new File("C:\\Users\\zafri\\OneDrive\\Desktop\\NLP-Project\\dataset\\allDataLite.txt");
            log.info("Loading data from {}", dataPath);

            SentenceIterator dataIter = new BasicLineIterator(dataPath);
            //Preprocess data using customize text preprocessor
            dataIter.setPreProcessor(new SentencePreProcessor() {
                @Override
                public String preProcess(String s) {
                    try {
                        TextPreprocessor.twitterPreprocessor(s);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return s;
                }
            });

            //Get words
            log.info("Vectorize sentences");
            TokenizerFactory tokenizer = new DefaultTokenizerFactory();

            tokenizer.setTokenPreProcessor(new CommonPreprocessor());

            //Load and config
            word2Vec = WordVectorSerializer.readWord2VecModel(trainedW2V);
            word2Vec.setTokenizerFactory(tokenizer);
            word2Vec.setSentenceIterator(dataIter);
            word2Vec.getConfiguration().setNegative(5);
            word2Vec.getConfiguration().setSampling(5);

            log.info("Uptraining {}", trainedW2V);
            word2Vec.fit();

            log.info("Save Word2Vec Model to {}", savedPath);
            WordVectorSerializer.writeWord2VecModel(word2Vec, savedPath);

            log.info("Model saved at {}", savedPath);

        }
        else {
            log.info("Word2Vec already exist! Loading trained Word2Vec");
            word2Vec = WordVectorSerializer.readWord2VecModel(savedPath); //Load retrained model
        }

        //test the word2Vec
        String testWord = "banjir";
        String testWord2 = "saya";

        INDArray vector = word2Vec.getWordVectorMatrix(testWord);
        log.info("\n");
        log.info("Word vector length: " + vector.columns() +"\n");
        sanityCheck(word2Vec, testWord);
        sanityCheck(word2Vec, testWord2);

    }

    public static void sanityCheck(Word2Vec model, String word) throws Exception {
        int numWordsNearest = 10;

        if(!model.hasWord(word)) throw new Exception(("Word trained with not found!"));

        Collection<String> list = model.wordsNearestSum(word, numWordsNearest + 1);
        list.remove(word); //remove itself

        log.info("10 Words closest to {}: {}\n", word,list);

    }

}
