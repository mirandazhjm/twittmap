
import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;
import io.searchbox.core.Index;


import static org.elasticsearch.common.xcontent.XContentFactory.*;
import twitter4j.Status;

import java.io.IOException;
import java.net.UnknownHostException;

import org.elasticsearch.common.xcontent.XContentBuilder;

public class EsHelper {
    private Keywords keywordHelper = new Keywords();

    public void uploadTweet(Status status ) {
        String index = keywordHelper.keyword(status.getText());
        //String id = status.getSource();
        JestClient client = null;
        if (index != null) {
            try {
                JestClientFactory factory = new JestClientFactory();
                factory.setHttpClientConfig(new HttpClientConfig
                        .Builder("********************************")
                        .multiThreaded(true)
                        .build());
                client = factory.getObject();

                String source = jsonBuilder()
                        .startObject()
                        .field("geo", status.getGeoLocation().getLatitude() +"," + 
                                status.getGeoLocation().getLongitude())
                        .field("text", status.getText())
                        .field("time", status.getCreatedAt())
                        .field("user", status.getUser().getName())
                        .endObject().string();
                Index putIndex = new Index.Builder(source).index("twittmap").type("tweets").build();
                System.out.println("\n");
                System.out.println("uploaded!");
                client.execute(putIndex);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}