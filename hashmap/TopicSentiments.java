package hashmap;

import java.util.ArrayList;

/**
 * Created by Ronald Erquiza on 4/21/2016.
 */
public class TopicSentiments {
    public ArrayList<PNNSentiments> positive = new ArrayList<>();
    public ArrayList<PNNSentiments> negative = new ArrayList<>();
    public ArrayList<PNNSentiments> neutral = new ArrayList<>();

    public TopicSentiments(){
    }

    public void addPositive(PNNSentiments pos){
        positive.add(pos);
    }

    public void addNegative(PNNSentiments neg){
        negative.add(neg);
    }

    public void addNeutral(PNNSentiments neu){
        neutral.add(neu);
    }

    public boolean findsNeutral(String content) {
        for (PNNSentiments pnn : this.neutral) {
            if (pnn.findsPNN(content))
                return true;
        }
        return false;
    }

    public boolean findsPositive(String content) {
        for (PNNSentiments pnn : this.positive) {
            if (pnn.findsPNN(content))
                return true;
        }
        return false;
    }

    public boolean findsNegative(String content) {
        for (PNNSentiments pnn : this.negative) {
            if (pnn.findsPNN(content))
                return true;
        }
        return false;
    }
}
