package org.twitterSentimentAnalysis;

import org.deeplearning4j.iterator.LabeledSentenceProvider;
import org.nd4j.common.collection.CompactHeapStringList;
import org.nd4j.common.primitives.Pair;
import org.nd4j.common.util.MathUtils;

import java.util.Map.Entry;
import java.util.*;

public class StringLabeledSentenceProvider implements LabeledSentenceProvider {

    private final int totalCount;
    private final List<String> filePaths;
    private final int[] fileLabelIndexes;
    private final Random rng;
    private final int[] order;
    private final List<String> allLabels;
    private int cursor;

    public StringLabeledSentenceProvider(Map<String, List<String>> stringWLabel, Random rng) {
        this.cursor = 0;
        int totalCount = 0;
        List l;
        for (Iterator var4 = stringWLabel.values().iterator(); var4.hasNext(); totalCount += l.size()) {
            l = (List)var4.next();
        }

        this.totalCount = totalCount;
        this.rng = rng;
        if (rng == null) {
            this.order = null;
        } else {
            this.order = new int[totalCount];

            for(int i = 0; i < totalCount; this.order[i] = i++){
            }

            MathUtils.shuffleArray(this.order, rng);
        }

        this.allLabels = new ArrayList(stringWLabel.keySet());
        Collections.sort(this.allLabels);
        Map<String, Integer> labelsToIdx = new HashMap<>();

        int position;
        for(position = 0; position < this.allLabels.size(); ++position) {
            labelsToIdx.put(this.allLabels.get(position),position);
        }

        this.filePaths = new CompactHeapStringList();
        this.fileLabelIndexes = new int[totalCount];
        position = 0;
        Iterator var6 = stringWLabel.entrySet().iterator();

        while (var6.hasNext()) {

            Entry<String, List<String>> entry = (Entry) var6.next();
            int labelIdx = (Integer) labelsToIdx.get(entry.getKey());

            for(Iterator var9 = ((List)entry.getValue()).iterator(); var9.hasNext();++position) {
                String f = (String) var9.next();
                this.filePaths.add(f);
                this.fileLabelIndexes[position] = labelIdx;
            }

        }
    }


    @Override
    public boolean hasNext() {
        return this.cursor < this.totalCount;
    }

    @Override
    public Pair<String, String> nextSentence() {
        int idx;
        if (this.rng == null) {
            idx = this.cursor++;
        } else {
            idx = this.order[this.cursor++];
        }
        String f = this.filePaths.get(idx);
        String label = this.allLabels.get(this.fileLabelIndexes[idx]);

        return new Pair(f, label);

    }

    @Override
    public void reset() {
        this.cursor = 0;
        if (this.rng != null) {
            MathUtils.shuffleArray(this.order, this.rng);
        }
    }

    @Override
    public int totalNumSentences() {
        return this.totalCount;
    }

    @Override
    public List<String> allLabels() {
        return this.allLabels;
    }

    @Override
    public int numLabelClasses() {
        return this.allLabels.size();
    }
}
