import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import hashmap.*;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Ronald Erquiza on 4/21/2016.
 */
public class HeuristicsSentimentAnalyzer {
    private DomainList list;

    public HeuristicsSentimentAnalyzer(String xmlFileName) {
        try {
            File fXmlFile = new File(xmlFileName);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();
            //System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

            NodeList dNL = doc.getElementsByTagName("domains");
            list = getDomainList(dNL);
            //printList(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private DomainList getDomainList(NodeList dNL) {
        DomainList domList = new DomainList();
        for (int domInd = 0; domInd < dNL.getLength(); domInd++) {
            Node domainsNode = dNL.item(domInd);
            if (domainsNode.getNodeType() == Node.ELEMENT_NODE) {
                Element domains = (Element) domainsNode;
                NodeList domainNL = domains.getChildNodes();
                domList.add(getDomains(domainNL));
            }
        }
        return domList;
    }

    private Domain getDomains(NodeList domainNL) {
        Domain dom = new Domain();
        for (int domInd = 0; domInd < domainNL.getLength(); domInd++) {
            Node domainNode = domainNL.item(domInd);
            if (domainNode.getNodeType() == Node.ELEMENT_NODE) {
                Element domain = (Element) domainNode;
                String domainName = domain.getAttribute("name").trim();
                NodeList topicsNL = domain.getChildNodes();
                dom.put(domainName, getTopicList(topicsNL));
            }
        }
        return dom;
    }

    private TopicList getTopicList(NodeList topicsNL) {
        TopicList topicList = new TopicList();
        for (int topInd = 0; topInd < topicsNL.getLength(); topInd++) {
            Node topicsNode = topicsNL.item(topInd);
            if (topicsNode.getNodeType() == Node.ELEMENT_NODE) {
                Element topics = (Element) topicsNode;
                NodeList topicNL = topics.getChildNodes();
                topicList.add(getTopics(topicNL));
            }
        }
        return topicList;
    }

    private Topic getTopics(NodeList topicNL) {
        Topic topicObj = new Topic();
        for (int topInd = 0; topInd < topicNL.getLength(); topInd++) {
            Node topicNode = topicNL.item(topInd);
            if (topicNode.getNodeType() == Node.ELEMENT_NODE) {
                Element topic = (Element) topicNode;
                //System.out.println("topic name: " + topic.getAttribute("name").trim());
                String topicName = topic.getAttribute("name").trim();
                NodeList sentNL = topic.getChildNodes();
                topicObj.put(topicName, getSentimentList(sentNL));
            }
        }
        return topicObj;
    }

    private TopicSentiments getSentimentList(NodeList sentNL) {
        TopicSentiments sentObj = new TopicSentiments();
        for (int sentInd = 0; sentInd < sentNL.getLength(); sentInd++) {
            Node sentNode = sentNL.item(sentInd);
            if (sentNode.getNodeType() == Node.ELEMENT_NODE) {
                Element sent = (Element) sentNode;
                NodeList posNL = sent.getElementsByTagName("positive");
                sentObj.addPositive(getTopicSentiments(posNL, "positive"));
                NodeList negNL = sent.getElementsByTagName("negative");
                sentObj.addNegative(getTopicSentiments(negNL, "negative"));
                NodeList neuNL = sent.getElementsByTagName("neutral");
                sentObj.addNeutral(getTopicSentiments(neuNL, "neutral"));
            }
        }
        return sentObj;
    }

    private PNNSentiments getTopicSentiments(NodeList topSentNL, String sentType) {
        PNNSentiments pnnSentObj = new PNNSentiments(sentType);
        for (int topSentInd = 0; topSentInd < topSentNL.getLength(); topSentInd++) {
            Node topSentNode = topSentNL.item(topSentInd);
            if (topSentNode.getNodeType() == Node.ELEMENT_NODE) {
                Element topSent = (Element) topSentNode;
                NodeList ruleNL = topSent.getChildNodes();
                switch (sentType) {
                    case "positive":
                        pnnSentObj.addSentiment(getSentiment(ruleNL));
                        break;
                    case "negative":
                        pnnSentObj.addSentiment(getSentiment(ruleNL));
                        break;
                    case "neutral":
                        pnnSentObj.addSentiment(getSentiment(ruleNL));
                        break;
                }
            }
        }
        return pnnSentObj;
    }

    private Sentiment getSentiment(NodeList ruleNL) {
        Sentiment sent = new Sentiment();
        for (int ruleInd = 0; ruleInd < ruleNL.getLength(); ruleInd++) {
            Node ruleNode = ruleNL.item(ruleInd);
            if (ruleNode.getNodeType() == Node.ELEMENT_NODE) {
                Element rule = (Element) ruleNode;
                NodeList wordNL = rule.getChildNodes();
                sent.add(getRule(wordNL));
            }
        }
        return sent;
    }

    private Rule getRule(NodeList wordNL) {
        return new Rule(wordNL);
    }

    private String seekKeyword(DomainList domain, String content, String keyDomain, String keyTopic) {
        boolean neu = findNeutral(domain, content, keyDomain, keyTopic);
        boolean pos = findPositive(domain, content, keyDomain, keyTopic);
        boolean neg = findNegative(domain, content, keyDomain, keyTopic);
        String res;
        /*System.out.println("CONTENT: " + content);
        System.out.println("DOMAIN: " + keyDomain);
        System.out.println("TOPIC: "+keyTopic);
        */
        if (neu) {
            res = "neutral";
        } else if (neg) {
            res = "negative";
        } else if (pos) {
            res = "positive";
        } else
            res = "not found";
        return res;
    }

    private boolean findNeutral(DomainList domain, String content, String keyDomain, String keyTopic) {
        return domain.get(0).get(keyDomain).get(0).get(keyTopic).findsNeutral(content);
    }

    private boolean findPositive(DomainList domain, String content, String keyDomain, String keyTopic) {
        return domain.get(0).get(keyDomain).get(0).get(keyTopic).findsPositive(content);
    }

    private boolean findNegative(DomainList domain, String content, String keyDomain, String keyTopic) {
        return domain.get(0).get(keyDomain).get(0).get(keyTopic).findsNegative(content);
    }

    private boolean condition(String word, String content, boolean excluded, boolean sent) {
        if (word != null && !excluded) {
            sent = content.contains(word);
        } else if (word != null) {
            sent = !content.contains(word);
        }
        return sent;
    }

    public String call(String content, String domain, String topic) {
        content = content.toLowerCase();
        domain = domain.toLowerCase();
        topic = topic.toLowerCase();
        return seekKeyword(list, content, domain, topic);
    }
}
