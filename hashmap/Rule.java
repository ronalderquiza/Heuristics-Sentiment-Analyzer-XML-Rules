package hashmap;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Ronald Erquiza on 4/21/2016.
 */
public class Rule extends ArrayList<Keyword> {

    public Rule(NodeList rule){
        for(int wordInd = 0; wordInd < rule.getLength(); wordInd++){
            this.add(new Keyword(rule.item(wordInd)));
        }
    }

    public boolean findsRule(String content) {
        for (Keyword keywords : this) {//rules
            String word = keywords.value;
            boolean excluded = keywords.isExcluded;
            if (word != null && condition(word, content, excluded)) {
                return true;
            }
        }
        return false;
    }

    private boolean condition(String word, String content, boolean excluded){
        return excluded != content.contains(word);
    }
}