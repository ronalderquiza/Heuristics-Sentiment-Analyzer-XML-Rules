package hashmap;

import java.util.ArrayList;

/**
 * Created by Ronald Erquiza on 4/22/2016.
 */
public class PNNSentiments extends ArrayList<Sentiment>{
    public String type;
    public PNNSentiments(String type){
        this.type = type;
    }

    public void addSentiment(Sentiment pnn){
        this.add(pnn);
    }

    public boolean findsPNN(String content) {
        for (Sentiment sentiment : this) {//sentimentList
            if (sentiment.findsSentiment(content))
                return true;
        }
        return false;
    }
}
