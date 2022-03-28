package org.twitterSentimentAnalysis;

import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.embeddings.wordvectors.WordVectors;
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

public class MyW2V {
    private static Logger log = LoggerFactory.getLogger(MyW2V.class);
//    private static File savedPath = new File("C:\\Users\\zafri\\OneDrive\\Desktop\\NLP-Project\\dataset\\w2v.zip");
    private static File savedPath =  new File("C:\\Users\\zafri\\Downloads\\Compressed\\malay_word2vec\\mswiki.vector");

    public static void main(String[] args) throws Exception {

        Word2Vec word2Vec = null;
        WordVectors wordVectors = null;

        if(!savedPath.exists()){
            File dataPath = new File("C:\\Users\\zafri\\OneDrive\\Desktop\\NLP-Project\\dataset\\allDataLite.txt");
            log.info("Load & Vectorize Sentences...");

            SentenceIterator dataIter = new BasicLineIterator(dataPath);
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
            TokenizerFactory tokenizer = new DefaultTokenizerFactory();

            tokenizer.setTokenPreProcessor(new CommonPreprocessor());

            log.info("Building model...");
            int layerSize = 80; //Word dimensionality: typically 50-100
            int minWordFrequency = 100;
            int seed = 123;
            int windowSize = 5;
            int batchSize = 1024;
            int EPOCHS = 1;
            int limit = 500000;

            word2Vec = new Word2Vec.Builder()
                    .limitVocabularySize(limit)
                    .allowParallelTokenization(true)
                    .epochs(EPOCHS)
                    .iterate(dataIter)
                    .layerSize(layerSize)
                    .minWordFrequency(minWordFrequency)
                    .seed(seed)
                    .windowSize(windowSize)
                    .tokenizerFactory(tokenizer)
                    .batchSize(batchSize)
                    .build();

            log.info("Fitting Word2Vec model");
            word2Vec.fit();

            log.info("Save Word2Vec Model to {}", savedPath);
            WordVectorSerializer.writeWord2VecModel(word2Vec, savedPath);

            log.info("Model saved at {}", savedPath);
        }
        else {
            log.info("Word2Vec already exist! Loading trained Word2Vec");
            word2Vec = WordVectorSerializer.readWord2VecModel(savedPath); //First choice [200 MB](Projected)

//            word2Vec = WordVectorSerializer.readWord2VecModel("C:\\Users\\zafri\\Downloads\\Compressed\\ms.vector");
//            word2Vec = WordVectorSerializer.loadFullModel("C:\\Users\\zafri\\Downloads\\Compressed\\ms.vector"); //larger model [1.35 GB] (Projected)

//            wordVectors = WordVectorSerializer.loadStaticModel(new File("C:\\Users\\zafri\\Downloads\\Compressed\\cc-ms-300.bin.gz"));

        }

        String testWord = "banjir";
        String testWord2 = "saya";

        INDArray vector = word2Vec.getWordVectorMatrix(testWord);
        log.info("\n");
        log.info("Word vector length: " + vector.columns() +"\n");
        sanityCheck(word2Vec, testWord);
        sanityCheck(word2Vec, testWord2);

        INDArray vector2 = wordVectors.getWordVectorMatrix(testWord);
        log.info("\n");
        log.info("Word vector length: " + vector2.columns() +"\n");
        sanityCheck(wordVectors, testWord);
        sanityCheck(wordVectors, testWord2);

    }

    public static void sanityCheck(Word2Vec model, String word) throws Exception {
        int numWordsNearest = 10;

        if(!model.hasWord(word)) throw new Exception(("Word trained with not found!"));

        Collection<String> list = model.wordsNearestSum(word, numWordsNearest + 1);
        list.remove(word); //remove itself

        log.info("10 Words closest to {}: {}\n", word,list);

    }

    public static void sanityCheck(WordVectors model, String word) throws Exception {
        int numWordsNearest = 10;

        if(!model.hasWord(word)) throw new Exception(("Word trained with not found!"));

        Collection<String> list = model.wordsNearest(word, numWordsNearest + 1);

        //remove itself from list
        list.remove(word);

        log.info("10 Words closest to {}: {}\n", word,list);

    }


}
