package api;

import static api.Connection.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.logging.*;
import javax.net.ssl.*;
import org.scribe.services.*;
import org.apache.commons.lang3.*;
import org.json.simple.*;
import org.json.simple.parser.*;

/**
 * Class for aggregating Twitter data. 
 * OAuth and HTTPS is built-in.
 * Uses Twitter's Search API using App authentication.
 * @author Etienne
 */
public class TwitterFeed {// If you feel like checking: http://codebeautify.org/jsonvalidate
	
	private URL url;	
	private HttpsURLConnection connection;
	private JSONParser parser;
	
	// TODO: regenerate and hide these -
	// "Keep the "Consumer Secret" a secret. This key should never be human-readable in your application." - Twitter
	private static final String CONSUMER_KEY = "sDRtOlkEE8L1SvjGdVMaj9s1Y";
	private static final String CONSUMER_SECRET = "OZgde6l5P0teMYkNoyQ4LvxCnuE0d3HIsZGhXnNVRth4RxH5bd";
	
	private static final String API_URL = "https://api.twitter.com/";
	
	/** The URL for requesting data about the amount of requests left for the current timeframe. */
	private static final String RATE_LIMIT_STATUS = "https://api.twitter.com/1.1/application/rate_limit_status.json";
	
	/** The base URL for requesting data. */
	private static final String SEARCH = "https://api.twitter.com/1.1/search/tweets.json";
	
	/**
	 * Creates a new TwitterFeed instance.
	 */
	public TwitterFeed() {
		parser = new JSONParser();
		try {
			this.url = new URL(API_URL);
		} catch (MalformedURLException ex) {
			Logger.getLogger(TwitterFeed.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	//<editor-fold defaultstate="collapsed" desc="Actual API shit.">
	
	/**
	 * Find a given number of most recent tweets containing the given keyword.
	 * @param keyword a word to search for. For hashtags, see getByHashtag().
	 * @param count the amount of tweets to return. Twitter counts the amount of requests, not the amount of data requested. Use sensibly.
	 * @return a list of tweets found by most recent date.
	 */
	public ArrayList<String> getTweets(String keyword, int count){
		ArrayList<String> tweets = new ArrayList<>();
		String query = "?q=" + keyword; // Builds the search query.
		query += "&result_type=recent";
		query += "&count=" + count; // 
		//JSONObject json = (JSONObject) request(SEARCH + query);
		//tweets.add(json.toJSONString());
		//tweets.add("\n");
		//return tweets; // TODO check API docs for search returns
		
		// TODO: loop this.
		JSONObject json = (JSONObject) request(SEARCH + query);
		JSONArray a = (JSONArray) json.get("statuses");
		json = (JSONObject) a.get(1);
		json.get("text");
		tweets.add((String)json.get("text"));
		return tweets;
		/*
		ArrayList<String> tweets = new ArrayList<>();
		String text_tag = "\"text\":\""; // Marks the beginning of the tweet in the JSON feed.
		String query = "?q=" + keyword; // Builds the search query.
		query += "&result_type=recent";
		query += "&count=" + 25; // Twitter only counts the amount of requests, not the amount of data requested.

		String data = request(SEARCH + query);
		if (data.isEmpty()) {
			System.out.println("TWITTER // DATA // Data is empty!");
			return tweets;
		}
		
		data = data.replaceAll("/'/gi", "\'"); // Replace _'_ with _\'_   

		// Sanitize output.
		int start = 1;
		int end = 1;
		while (start > 0 && end > 0) {
			start = data.indexOf(text_tag, end);
			end = data.indexOf("\",", start);
			System.out.println("TWITTER // DATA // " + data);
			if(!data.isEmpty()){
				String tweet = data.substring(start + text_tag.length(), end);
				tweet = tweet.replaceAll("/\n/gi", ""); // Replace _'_ with _\'_
				tweet = tweet.replace("\\/", "/"); // Replace _\/_ with _/_
				tweet = unescape(tweet);
				tweet = StringEscapeUtils.unescapeJava(tweet);
				tweets.add(tweet);
			}
			else
				tweets.add("Empty string.");
		}
		tweets.remove(tweets.size() - 1); // Remove junk data.		
		return tweets;
		*/
	}

	/**
	 * Returns tweets based on their GPS coordinates. TODO Currently being debugged (23-03)
	 * @param latitude
	 * @param longitude
	 * @param radius range from the coordinates in kilometres.
	 * @return
	 */	
	public ArrayList<String> getByLocation(double latitude, double longitude, int radius, String keyword, int count){
		String geolocating = "&geocode=" + latitude + "," + longitude + "," + radius + "km";
		
		ArrayList<String> tweets = new ArrayList<>();
		String text_tag = "\"text\":\""; // Marks the beginning of the tweet in the JSON feed.
		String query = "?q=" + keyword; // Builds the search query.
		query += "&result_type=recent";
		query += "&count=" + 25; // Twitter only counts the amount of requests, not the amount of data requested.
		query += geolocating;
		return tweets;//////
		/*
		String data = request(SEARCH + query);
		if (data.isEmpty()) {
			System.out.println("TWITTER // DATA // Data is empty! :(");
			return tweets;
		}
		
		data = data.replaceAll("/'/gi", "\'"); // Replace _'_ with _\'_   

		// Sanitize output.
		int start = 1;
		int end = 1;
		while (start > 0 && end > 0) {
			start = data.indexOf(text_tag, end);
			end = data.indexOf("\",", start);
			System.out.println("TWITTER // DATA // " + data);
			if(!data.isEmpty()){
				String tweet = data.substring(start + text_tag.length(), end);
				tweet = tweet.replaceAll("/\n/gi", "<br>"); // Replace _'_ with _\'_
				tweet = tweet.replace("\\/", "/"); // Replace _\/_ with _/_
				tweet = unescape(tweet);
				tweet = StringEscapeUtils.unescapeJava(tweet);
				tweets.add(tweet);
			}
			else				
				tweets.add("Empty string.");
		}
		tweets.remove(tweets.size() - 1); // Remove junk data.
		return tweets;
		*/
	}

	/**
	 * Search tweets by hashtag.
	 * @param tags an hashtag without the '#' sign.
	 */
	public ArrayList<String> getByTag(String tag){
		String query = /*SEARCH + */"%23" + tag /*+ "%20"*/;		
		//return request(query);
		return getTweets(query, 25);
	}
	
	/**
	 * Search tweets by hashtags.
	 * @param tags an array of hashtags without the '#' sign.
	 */
	public ArrayList<String> getByTags(String[] tags){
		String query = ""; //= SEARCH;
		for (String tag : tags) {
			query += "%23" + tag + "%20";
		}
		//return request(query);
		return getTweets(query, 25);
	}
	
	/**
	 * Gets the amount of requests still available for the current timeframe.
	 * @return a formatted String with the rate limit status of the feed
	 * in the format of [Available / Remaining].
	 */
	public String getStatus(){
		String query = RATE_LIMIT_STATUS + "?resources=search";
		JSONObject out = (JSONObject) request(query);
		System.out.println(out.toString());
		out = (JSONObject) out.get("resources");
		out = (JSONObject) out.get("search");
		System.out.println(out.toJSONString());
		out = (JSONObject) out.get("/search/tweets");
		long limit = (long) out.get("limit");
		long remaining = (long) out.get("remaining");
		return "API usage status: " + limit + " / " + remaining + " remaining.";
		/*
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
		* */
	}

	//</editor-fold>	
	
	//<editor-fold defaultstate="collapsed" desc="Miscellanous.">
	
	/**
	 * Replaces double slash in javacode with single slashes.
	 * @param s
	 * @return 
	 */
	String unescape(String s) {
		int i = 0, len = s.length();
		char c;
		StringBuilder sb = new StringBuilder(len);
		while (i < len) {
			c = s.charAt(i++);
			if (c == '\\') {
				if (i < len) {
					c = s.charAt(i++);
					if (c == 'u') {
						// TODO: check that 4 more chars exist and are all hex digits
						c = (char) Integer.parseInt(s.substring(i, i + 4), 16);
						i += 4;
					} // add other cases here as desired...
				}
			} // fall through: \ escapes itself, quotes any character but u
			sb.append(c);
		}
		return sb.toString();
	}			
		
	/**
	 * Requests data for a given query from the API.
	 * @param query the full URL including the search parameters.
	 * @return the full JSON feed. This can either be a JSONObject or JSONArray.
	 */
	@SuppressWarnings("LocalVariableHidesMemberVariable")
	public Object request(String query){
		try {
			// Authenticate.
			URL oauth = new URL("https://api.twitter.com/oauth2/token");
			HttpsURLConnection conn = (HttpsURLConnection) oauth.openConnection();			
			String token = requestBearerToken("https://api.twitter.com/oauth2/token");
			writeRequest(conn, token);
			
			// Request data.	
			URL url = new URL(query);
			connection = (HttpsURLConnection) url.openConnection();          
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setUseCaches(false);
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Host", "api.twitter.com");
			connection.setRequestProperty("User-Agent", "CIMS");
		        connection.setRequestProperty("Authorization", "Bearer " + token);
			return parser.parse(readResponse(connection));
		} catch (IOException | ParseException ex) {
			Logger.getLogger(TwitterFeed.class.getName()).log(Level.SEVERE, null, ex);
		} finally {
			if (connection != null)
	            connection.disconnect();
		}
		return null;
	}
	
	//</editor-fold>
	
	//<editor-fold defaultstate="collapsed" desc="Authorization.">
	
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
	 * @throws IOException either if JSON is broken or the endpoint URL is incorrect.
	 * @return the Bearer token.
	 */
	@SuppressWarnings("LocalVariableHidesMemberVariable")
	private String requestBearerToken(String endPointUrl) throws IOException {
	    HttpsURLConnection connection = null;
	    String encodedCredentials = encodeKeys(CONSUMER_KEY, CONSUMER_SECRET);
	         
	    try {
	        URL url = new URL(endPointUrl);
	        connection = (HttpsURLConnection) url.openConnection();
	        connection.setDoOutput(true);
	        connection.setDoInput(true);
	        connection.setRequestMethod("POST");
	        connection.setRequestProperty("Host", "api.twitter.com");
	        connection.setRequestProperty("User-Agent", "CIMS");
	        connection.setRequestProperty("Authorization", "Basic " + encodedCredentials);
	        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
	        connection.setRequestProperty("Content-Length", "29");
	        connection.setUseCaches(false);
	             
	        writeRequest(connection, "grant_type=client_credentials");
	             
	        JSONObject json = (JSONObject) parser.parse(readResponse(connection));
		
		if (json != null) {
		    String tokenType = (String) json.get("token_type");
		    String token = (String) json.get("access_token");
		    
		    return ((tokenType.equals("bearer")) && (token != null)) ? token : "";
		}
	        return "";
	    } catch (MalformedURLException e) {
	        throw new IOException("Invalid endpoint URL specified.", e);
	    }	catch (ParseException e) {
		throw new IOException("Invalid JSON response.", e);
	    } finally {
	        if (connection != null)
	            connection.disconnect();
	    }
	}	

	//</editor-fold>
	
	/** fuck unit tests
	 * Prints some output for testing/demonstration and reference.
	 */
	public static void main(String[] args) throws Exception {
		TwitterFeed t = new TwitterFeed();
		
		// TODO: make this a unit test.
		JSONArray out = (JSONArray) t.request("https://api.twitter.com/1.1/statuses/user_timeline.json?screen_name=twitterapi&count=2");
		for (int i = 0; i < out.size(); i++) {
			if (!(out.get(i) instanceof JSONArray)) {
				JSONObject obj = (JSONObject) out.get(i);
				System.out.println(obj.get("text"));
			}
			System.out.println("");
		}		
		
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
		//System.out.println(t.getByLocation(51.450922, 5.479748, 15)); // Rachelsmolen, Eindhoven.
		//System.out.println(out); // 400 ~ 403 = Unauthorized.
    }
}
