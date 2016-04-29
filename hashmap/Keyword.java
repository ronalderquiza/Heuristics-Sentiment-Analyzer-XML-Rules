package hashmap;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Created by Ronald Erquiza on 4/21/2016.
 */
public class Keyword {
    public String value;
    public boolean isExcluded;

    public Keyword(String value, boolean isExcluded) {
        this.value = value;
        this.isExcluded = isExcluded;
    }

    public Keyword(Node wordNode) {
        if (wordNode.getNodeType() == Node.ELEMENT_NODE) {
            Element word = (Element) wordNode;
            this.value = word.getTextContent().trim();
            this.isExcluded = word.getAttribute("exclude").equals("true");
        }
    }
}
