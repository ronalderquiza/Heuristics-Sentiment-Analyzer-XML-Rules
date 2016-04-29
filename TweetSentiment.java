/**
 * Created by Ronald Erquiza on 4/25/2016.
 */
public class TweetSentiment {
    public String content;
    public String domain;
    public String topic;
    public String sentiment;

    public TweetSentiment(String content, String domain, String topic, String sentiment){
        this.content = content;
        this.domain = domain;
        this.topic = topic;
        this.sentiment = sentiment;
    }
}
