package api;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.logging.*;
import javax.net.ssl.*;
import org.scribe.services.*;

/**
 * Class for aggregating Twitter data.
 * Uses Twitter's Search API using App authentication.
 * @author Etienne
 */
public class TwitterFeed {
	
	private URL url;
	private HttpsURLConnection connection;
	
	private static final String ACCESS_TOKEN = "sDRtOlkEE8L1SvjGdVMaj9s1Y";
	private static final String SECRET_ACCESS_TOKEN = "OZgde6l5P0teMYkNoyQ4LvxCnuE0d3HIsZGhXnNVRth4RxH5bd";
	
	/**
	 * The URL for requesting data about the amount of requests left for the current timeframe.
	 */
	private static final String RATE_LIMIT_STATUS = "https://api.twitter.com/1.1/application/rate_limit_status.json";
	
	/**
	 * The URL for requesting data about a specific search.
	 * Basic URL for query without any parameters.
	 */
	private static final String SEARCH = "https://api.twitter.com/1.1/search/tweets.json";
	
	/**
	 * A custom feed for Twitter API v1.1.
	 */
	public TwitterFeed() {
		try {
			this.url = new URL("https://api.twitter.com/");
		} catch (MalformedURLException ex) {
			Logger.getLogger(TwitterFeed.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	/**
	 * Connects to the stream.
	 */
	private HttpsURLConnection connect(){
		try {
			connection = (HttpsURLConnection)url.openConnection();
			return connection;
		} catch (IOException ex) {
			Logger.getLogger(TwitterFeed.class.getName()).log(Level.SEVERE, null, ex);
			return null;
		}
	}
	
	/**
	 * Reconnects to the stream.
	 */
	private void reconnect(){
		if (connection != null) {
			connection.disconnect();
		}
		connect();
	}
	
	/**
	 *
	 * @param keyword
	 * @param count the amount of tweets to return.
	 * @return a list of tweets found by most recent date.
	 */
	public ArrayList<String> getTweets(String keyword, int count){
		ArrayList<String> tweets = new ArrayList<>();
		String text_tag = "\"text\":\""; // Marks the beginning of the tweet in the JSON feed.
		String query = "?q=" + keyword; // Builds the search query.
		query += "&result_type=recent";
		query += "&count=" + count;
		
		// Escape single quotes properly.
		String data = request(SEARCH + query);
		data = data.replaceAll("/'/gi", "\'"); // Replace _'_ with _\'_
		
		// Start looping through the data, adding all found tweets to the list.
		int start = 1; 
		int end = 1;
		while ( start > 0 && end > 0) {
			start = data.indexOf(text_tag, end);
			end = data.indexOf("\",", start);
			String tweet = data.substring(start + text_tag.length(), end);
			tweets.add(tweet);
		}
		return tweets;
	}
		
	/**
	 * Returns tweets based on their GPS coordinates. Currently being debugged (23-03)
	 * @param latitude
	 * @param longitude
	 * @param radius range from the coordinates in kilometres.
	 * @return
	 */	
	public String getByLocation(double latitude, double longitude, int radius){
		String query = "&geocode=" + latitude + "," + longitude + "," + radius + "km";
		return request(SEARCH + query);/*
		ArrayList<String> tweets = new ArrayList<>();
		String text_tag = "\"text\":\""; // Marks the beginning of the tweet in the JSON feed.
		
		// Escape single quotes properly.
		String data = request(SEARCH + query);
		data = data.replaceAll("/'/gi", "\'"); // Replace _'_ with _\'_
		
		// Start looping through the data, adding all found tweets to the list.
		int start = 1; 
		int end = 1;
		while (start > 0 && end > 0) {
			start = data.indexOf(text_tag, end) + text_tag.length();
			end = data.indexOf("\",", start);
			String tweet = data.substring(start, end);
			tweets.add(tweet);
		}
		return tweets;*/
	}

	/**
	 * Search tweets by hashtag.
	 * @param tags an hashtag without the '#' sign.
	 */
	public String getByTag(String tag){
		String query = SEARCH + "%23" + tag + "%20";		
		return request(query);
	}
	
	/**
	 * Search tweets by hashtags.
	 * @param tags an array of hashtags without the '#' sign.
	 */
	public String getByTags(String[] tags){
		String query = SEARCH;
		for (String tag : tags) {
			query += "%23" + tag + "%20";
		}
		return request(query);
	}
	
	/**
	 * Returns the rate limit status of the feed.
	 * @return a JSON feed with the data.
	 */
	public String getStatus(){		
		String query = RATE_LIMIT_STATUS;
		String remaining_tag = "\"remaining\":";
		String limit_tag = "\"limit\":";
		String search_tag = "\"search\":";
		
		String data = request(query);
		
		int start = 1;
		int end = 1;
		
		// Find amount of requests remaining for the Search API.
		start = data.indexOf(search_tag); 
		
		start = data.indexOf(limit_tag, start) + limit_tag.length();
		end = data.indexOf(",", start);
		String limit = data.substring(start, end);
		
		start = data.indexOf(remaining_tag, start) + remaining_tag.length();
		end = data.indexOf(",", start);
		String remaining = data.substring(start, end);
		
		return "Usage: " + remaining + " / " + limit + " requests.";
	}

	/**
	 * Requests data for a given query from the API.
	 * @param query what to search for.
	 * @return the JSON feed as a String.
	 */
	public String request(String query){
		try {
			// Connect
			URL url2 = new URL("https://api.twitter.com/oauth2/token");
			HttpsURLConnection conn = (HttpsURLConnection) url2.openConnection();
			// Authenticate.
			String token = requestBearerToken("https://api.twitter.com/oauth2/token");
			
			writeRequest(conn, token);
			
			System.out.println(readResponse(conn));
			
			// Request data.						
			URL url = new URL(query);
	        connection = (HttpsURLConnection) url.openConnection();          
	        connection.setDoOutput(true);
	        connection.setDoInput(true);
	        connection.setRequestMethod("GET");
	        connection.setRequestProperty("Host", "api.twitter.com");
	        connection.setRequestProperty("User-Agent", "CIMS");
	        connection.setRequestProperty("Authorization", "Bearer " + token);
	        connection.setUseCaches(false);
	        System.out.println("Connection: " + connection.toString()); // If you feel like checking: http://codebeautify.org/jsonvalidate
			String out = readResponse(connection);
			return out;   
		} catch (IOException ex) {
			Logger.getLogger(TwitterFeed.class.getName()).log(Level.SEVERE, null, ex);
		} finally {
			if (connection != null)
	            connection.disconnect();
		}
		return null;
	}
	
	//<editor-fold defaultstate="collapsed" desc="/***** Encoding and authorization *****************************************/">
	
	/** 
	 * Encodes the consumer key and secret to create the basic authorization key.
	 * Used for authorizing for the Twitter API.
	 * @param consumerKey the Access Token fom the app.
	 * @param consumerSecret the Secret Access Token from the app.
	 * @return the encoded pair.
	 */
	private String encodeKeys(String consumerKey, String consumerSecret) {
	    try {
	        String encodedConsumerKey = URLEncoder.encode(consumerKey, "UTF-8");
	        String encodedConsumerSecret = URLEncoder.encode(consumerSecret, "UTF-8");
	        String pair = encodedConsumerKey + ":" + encodedConsumerSecret;
	        Base64Encoder b = Base64Encoder.getInstance();
			String encodedBytes = b.encode(pair.getBytes());
	        return encodedBytes; 
	    }
	    catch (UnsupportedEncodingException e) {
	        return new String();
	    }
	}
	
	/** 
	 * Constructs the request for requesting a bearer token and returns that token as a string.
	 * Used for authorizing for the Twitter API.
	 * @param endPointUrl the API URL.
	 * @throws IOException 
	 * @return the Bearer token.
	 */
	private String requestBearerToken(String endPointUrl) throws IOException {
	    HttpsURLConnection connection2 = null;
	    String encodedCredentials = encodeKeys(ACCESS_TOKEN, SECRET_ACCESS_TOKEN);
	         
	    try {
	        URL url = new URL(endPointUrl);
	        connection2 = (HttpsURLConnection) url.openConnection();          
	        connection2.setDoOutput(true);
	        connection2.setDoInput(true);
	        connection2.setRequestMethod("POST");
	        connection2.setRequestProperty("Host", "api.twitter.com");
	        connection2.setRequestProperty("User-Agent", "CIMS");
	        connection2.setRequestProperty("Authorization", "Basic " + encodedCredentials);
	        connection2.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
	        connection2.setRequestProperty("Content-Length", "29");
	        connection2.setUseCaches(false);
	             
	        writeRequest(connection2, "grant_type=client_credentials");
	             
	        String json = readResponse(connection2);
	        /*
	        if (json != null) {
	            String tokenType = (String)obj.get("token_type");
	            String token = (String)obj.get("access_token");
	         
	            return ((tokenType.equals("bearer")) && (token != null)) ? token : "";
	        }*/
			//System.out.println("JSON feed: ");
			//System.out.println(json;
			String field = "\"access_token\":\"";
			int len = field.length();
			String value = json.substring(json.indexOf(field) + len);
			//System.out.println(value);
			field = value.trim().substring(0, (value.length()-4));
			//System.out.println(field);
	        return field.trim();
	    } catch (MalformedURLException e) {
	        throw new IOException("Invalid endpoint URL specified.", e);
	    } finally {
	        if (connection2 != null)
	            connection2.disconnect();
	    }
	}
	
	/** 
	 * Writes a request to a connection
	 * @param connection the connection to write the request over.
	 * @param request the httprequest for a response.
	 * @return true if succeeded, false otherwise.
	 */
	private boolean writeRequest(HttpsURLConnection connection, String request) {
	    try {
	        BufferedWriter wr = new BufferedWriter
					(new OutputStreamWriter(connection.getOutputStream()));
	        wr.write(request);
	        wr.flush();
	        wr.close();
	             
	        return true;
	    }
	    catch (IOException e) { return false; }
	}
	     	  
	/** 
	 * Reads a response for a given connection and returns it as a string.
	 * @param connection the connection to read from.
	 * @return 
	 */
	private String readResponse(HttpsURLConnection connection) {
	    try {
	        StringBuilder str = new StringBuilder();
	             
	        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	        String line = "";
	        while((line = br.readLine()) != null) {
	            str.append(line + System.getProperty("line.separator"));
	        }
	        return str.toString();
	    }
	    catch (IOException e) { return new String(); }
	}

	//</editor-fold>
	
	/** 
	 * Prints some output for testing/demonstration and reference.
	 */
	public static void main(String[] args) throws Exception {
		TwitterFeed t = new TwitterFeed();
		String out = t.request("https://api.twitter.com/1.1/statuses/user_timeline.json?screen_name=twitterapi&count=2");
		
		// Commented functions still work - they merely return the full JSON.
		
		// TwitterFeed.getStatus():
		System.out.println(t.getStatus());

		// TwitterFeed.getByTag():
		System.out.println(t.getByTag("CIMS"));
		
		// TwitterFeed.getByTags():
		System.out.println(t.getByTags(new String[]{"fontysict", "fontys"}));
		
		// TwitterFeed.getTweets():
		ArrayList<String> tweets = t.getTweets("incident", 5);
		for (String tweet : tweets) {System.out.println(tweet);}
		
		// TwitterFeed.getByLocation():
		//ArrayList<String> geotweets = t.getByLocation(51.450922, 5.479748, 15); // Rachelsmolen, Eindhoven.
		//for (String tweet : geotweets) {System.out.println(tweet);}
		// DIY: http://www.gps-coordinates.net/
		System.out.println(t.getByLocation(51.450922, 5.479748, 15)); // Rachelsmolen, Eindhoven.
		//System.out.println(out); // 400 ~ 403 = Unauthorized.
    }
}
