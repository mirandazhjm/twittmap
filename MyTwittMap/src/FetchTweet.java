

//import org.json.JSONException;
//import org.json.JSONObject;

import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;
//import java.util.*;

public class FetchTweet {

    private EsHelper esHelper = new EsHelper();
	private static double latitude;
	private static double longtitude;
	private static int count = 0;
	private static String name;
	private static String place;
	private static String message;
	private static String id;
	private static String date;
	private static GeoLocation loc;


    void fetchTwits() {

        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
        	.setOAuthConsumerKey("********************************")
   			.setOAuthConsumerSecret("********************************")
   			.setOAuthAccessToken("********************************")
   			.setOAuthAccessTokenSecret("********************************");
        TwitterStream twitterStream = new TwitterStreamFactory(cb.build()).getInstance();

        StatusListener listener = new StatusListener() {
            @Override
            public void onStatus(Status status) {
                if (status.getGeoLocation() != null && status.getUser() != null) {
                	loc = status.getGeoLocation();
                	latitude = status.getGeoLocation().getLatitude();
					longtitude = status.getGeoLocation().getLongitude();
					place = status.getPlace().getCountry() + ","
							+ status.getPlace().getFullName();
					date = status.getCreatedAt().toString();
					id = Integer.toString(++count);
					name = status.getUser().getScreenName();
					message = status.getText();

					System.out.println("---------------------------");
					System.out.println("geo" + loc);
					System.out.println("ID:" + count);
					System.out.println("latitude:" + latitude);
					System.out.println("longtitude:" + longtitude);
					System.out.println("place:" + place);
					System.out.println("name:" + name);
					System.out.println("message:" + message);
					System.out.println("data:" + date);
					System.out.println("-------------8-------------");
//                	System.out.print(status);
                    esHelper.uploadTweet(status);
                }
            }

            @Override
            public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
                //System.out.println("Got a status deletion notice id:" + statusDeletionNotice.getStatusId());
            }

            @Override
            public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
//                System.out.println("Got track limitation notice:" + numberOfLimitedStatuses);
            }

            @Override
            public void onScrubGeo(long userId, long upToStatusId) {
                System.out.println("Got scrub_geo event userId:" + userId + " upToStatusId:" + upToStatusId);
            }

            @Override
            public void onStallWarning(StallWarning warning) {
                System.out.println("Got stall warning:" + warning);
            }

            @Override
            public void onException(Exception ex) {
                ex.printStackTrace();
            }
        };

        twitterStream.addListener(listener);
        // Filter
        FilterQuery filtre = new FilterQuery();
        String[] keywordsArray = { "food", "trump","hillary","vegas","newyork" ,"love","job","fashion","lol","vegas"};
        filtre.track(keywordsArray);
        twitterStream.filter(filtre);
    }

    public static void main(String[] args) {
        FetchTweet fetcher = new FetchTweet();
        fetcher.fetchTwits();
    }
}