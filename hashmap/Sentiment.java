package hashmap;

import java.util.ArrayList;

/**
 * Created by Ronald Erquiza on 4/21/2016.
 */
public class Sentiment extends ArrayList<Rule> {
    public Sentiment(){

    }

    public boolean findsSentiment(String content) {
        for (Rule rule : this) {//sentiments
            if (rule.findsRule(content))
                return true;
        }
        return false;
    }
}
