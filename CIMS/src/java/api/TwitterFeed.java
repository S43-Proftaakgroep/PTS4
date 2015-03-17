package api;

import java.io.*;
import java.net.*;
import java.util.logging.*;
import javax.net.ssl.*;
import org.scribe.services.*;

/**
 * Class for aggregating Twitter data.
 * @author Etienne
 */
public class TwitterFeed {
	
	private URL url;
	private HttpsURLConnection connection;
	
	private static final String ACCESS_TOKEN = "sDRtOlkEE8L1SvjGdVMaj9s1Y";
	private static final String SECRET_ACCESS_TOKEN = "OZgde6l5P0teMYkNoyQ4LvxCnuE0d3HIsZGhXnNVRth4RxH5bd";
	
	/**
	 * The URL for requesting data about the amount of request left for the current timeframe.
	 */
	private static final String RATE_LIMIT_STATUS = "https://api.twitter.com/1.1/application/rate_limit_status.json";
	
	/**
	 * The URL for requesting data about a specific search.
	 * Basic URL for query without any parameters.
	 */
	private static final String SEARCH = "https://api.twitter.com/1.1/search/tweets.json";
	
	/**
	 * Creates a new feed.
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
	 * @param geocode
	 * @param result_type
	 * @param count
	 */
	public String getTweets(String keyword, int geocode, String result_type, int count){
		throw new UnsupportedOperationException("Still working on this, sorry! :(");
		//set query
		//String query;
		//return request(query);		
	}
		
	/**
	 *
	 * @param location 
	 */	
	public String getByLocation(String location){
		throw new UnsupportedOperationException("Still working on this, sorry! :(");
		//set query
		//String query;
		//return request(query);
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
		return request(query);
	}

	/**
	 * Requests data for a given query from the API.
	 * @param query what to search for.
	 * @return the JSON feed as a String.
	 */
	public String request(String query){
		try {
			// Connect
			URL url2 = new URL("https://api.twitter.com/oauth2/token");//Unauthorized?
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
	public String encodeKeys(String consumerKey, String consumerSecret) {
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
	public String requestBearerToken(String endPointUrl) throws IOException {
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
	             
	        // Receive JSON, get moneez.
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
	public boolean writeRequest(HttpsURLConnection connection, String request) {
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
	public String readResponse(HttpsURLConnection connection) {
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
	 * Prints some output for testing and reference.
	 */
	public static void main(String[] args) throws Exception {
		TwitterFeed t = new TwitterFeed();
		String out = t.request("https://api.twitter.com/1.1/statuses/user_timeline.json?screen_name=twitterapi&count=2");
		System.out.println(t.getStatus());
		System.out.println(t.getByTag("henk"));
		System.out.println(t.getByTags(new String[]{"fontysict", "fontys"}));
		//System.out.println(t.getTweets(out, geocode, out, count));
		//System.out.println(t.getByLocation(out);
		System.out.println(out); // 400 ~ 403 = Unauthorized.
    }
}
