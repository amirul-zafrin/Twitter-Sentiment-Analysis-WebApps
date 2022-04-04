# DeepLearning4j Model

This folder include DeepLearning4j model that implement LSTM and CNN for sentiment analysis, word2vec and java application that fetch tweets from Twitter using Twitter4j.

## LSTM

Long Short Term Memory (LSTM) are a special kind type of RNN that can learn long-term dependencies. They don't have to work hard to remember knowledge for lengthy periods of time; it's like second nature to them.

All recurrent neural networks are made up of a series of repeated neural network modules. This repeating module in ordinary RNNs will have a relatively simple structure, such as a single tanh layer.

LSTMs have a chain-like structure as well, but the repeating module is different. Instead of a single neural network layer, there are four, each of which interacts in a unique way.

1. Cell State
   > The cell state is similar to that of a conveyor belt. With only a few tiny linear interactions, it flows straight down the entire chain. It's incredibly easy for data to simply travel along it unaltered.The LSTM can delete or add information to the cell state, which is carefully controlled by structures called gates.
2. Forget gate
   > A sigmoid layer. This gate output a number in between 0 and 1 for each number in the cell state. A 1 reprensents "completely keep this" while 0 represents "compeletely forget this".
3. Input gate
   > A sigmoid layer that decides which values we'll update. This layer will then combined with output from a Tanh layer that create a vector of new candidate values. This then created an ouput for an update to the state.
4. Output gate
   > A Tanh layer; to push the value to be between -1 and 1.

For further reading, Christopher Olah writes a good article on LSTM. https://colah.github.io/posts/2015-08-Understanding-LSTMs/

## Training Evaluation for LSTM:

![Model Training Evaluation LSTM](https://github.com/amirul-zafrin/Twitter-Sentiment-Analysis-WebApps/blob/main/Twitter-Sentiment-Analysis-Model/LSTM_training.png?raw=true)

## Testing Evaluation for LSTM:

![Model Training Evaluation LSTM](https://github.com/amirul-zafrin/Twitter-Sentiment-Analysis-WebApps/blob/main/Twitter-Sentiment-Analysis-Model/LSTM_test.png?raw=true)

## CNN

Convolutional Neural Netowrk (CNN) is widely used in computer vision. This method is used in image classification, object detection and segmentation. Using the same technique for a problem as varied as Natural Language Processing appears contradictory at first glance but it can perform well in some cases.

In NLP, we use a filter that can slide over the word embeddings (matrix rows). As a result, filters are often the same width as the word embeddings (in this case 300).

### Training Evaluation for CNN:

![Model Training Evaluation](https://github.com/amirul-zafrin/Twitter-Sentiment-Analysis-WebApps/blob/main/Twitter-Sentiment-Analysis-Model/CNN_v6_training.png?raw=true)

### Testing Evaluation for CNN:

![Model Testing Evaluation](https://github.com/amirul-zafrin/Twitter-Sentiment-Analysis-WebApps/blob/main/Twitter-Sentiment-Analysis-Model/CNN_v6_testing.png?raw=true)

## Evaluation metric: F1 score

F1-score is the harmonic mean of precision and recall. Both of the metrics differ as one take account the False Positive and the other False Negative.

For the case that both false positive and false negative are equally undesirable like this project, F1-score is really suitable as performance metric. Since, F1-score take account both false positive and false negative, it try to minimize both false positive and false negative.

Because of the trade-off between precision and recall, it is not possible to maximise both precision and recall at the same time in practise.

    Precision = True Positive / (True Positive + False Positive)

    Recall = True Positive / (True Positive + False Negative)

    F1-Score = 2 * (Precision * Recall) / (Precision + Recall)

The F1-score increases as precision and recall improve. The F1-score goes from 0 to 1. The model is better if it is close to 1.

## Summary

In this project, as for now, I use CNN model for the inference as it performs slightly better than LSTM. In future work, I might implement encoder such as BERT to improve the sentiment analysis.
