package api;

import java.io.*;
import java.net.*;
import java.util.logging.*;

/**
 * Weather API for looking up feed stats.
 * Uses OpenWeatherMaps API.
 * @author Etienne
 */
public class WeatherFeed{

	private String location;
	private Query query;
	private final String QUERY_URL = "http://api.openweathermap.org/data/2.5/weather?q=";

	public static enum Query{
		TEMPERATURE("temp"),
		DESCRIPTION("description"),
		HUMIDITY("humidity"),
		PRESSURE("pressure"),
		WINDSPEED("speed"),
		WINDDIRECTION("deg");
		
		// Le boilerplate code for toString() implementation.
		private final String keyword;		
		private Query(String keyword){ this.keyword = keyword; }
		
		@Override
		public String toString(){ return this.keyword; }
	};
	
	/**	
	 * Creates a new weatherfeed that can return specific weatherdata for any given city.
	 * @param location the name of the city the feed data needs to be found for.
	 * @param query what to search for, null defaults to temperature (in celsius).
	 */
	public WeatherFeed(String location, Query query) {
		this.location = location;
		this.query = query != null ? query : Query.TEMPERATURE;
	}
	
	/**
	 * Returns the data from the API. 
	 * @return the specific value + unit(km/h, %, etc) as a String.
	 */
	public String getData(){
		switch(query){
			case DESCRIPTION:
				return getFeed() + ".";
			case HUMIDITY:
				return getFeed() + "%";
			case PRESSURE:
				return getFeed() + "hpa";
			case WINDDIRECTION:
				return getFeed() + "\u00b0";
			case WINDSPEED:
				return getFeed() + "m/s";
			case TEMPERATURE:
			default:
				return getFeed() + "\u00b0"+"C";
		}
	}
	
	/**
	 * Returns the feed stats for its set location and search query.
	 * @return 
	 */
	private String getFeed(){
		String feed = "";
		
		try {
			// Call API and ask for data in Dutch and metric units.
			URL apicall = new URL(QUERY_URL+location+",nl&units=metric&lang=nl");
			
			try (BufferedReader in = new BufferedReader(new InputStreamReader(
				apicall.openStream()))) {
				
				while ((feed = in.readLine()) != null) {
					//System.out.println(feed); // Prints the full JSON feed,
					
					// Check if the city can be found.
					if (feed.equals("{\"message\":\"Error: Not found city\",\"cod\":\"404\"}")) {
						feed = "Weather Unavailable. (Location not found.)";
						System.out.println(feed);
						return feed;
					}
					
					// Get the data from the field that is searched for.
					int index = feed.indexOf(query.toString()) + query.toString().length() + 2;
					feed = feed.substring(index, feed.indexOf(",\"", index));
					
					// Filter out the quotes.
					feed = feed.replace("\"", "");
					feed = feed.replace("}", "");
					
					//System.out.println(feed); // Prints the filtered value.

					return feed;
				}
			}
		} catch (IOException ex) {
			System.out.println("IOException raised in WeatherFeed. [Line 104]");
			//Logger.getLogger(WeatherFeed.class.getName()).log(Level.SEVERE, null, ex);
			feed = "Weather Unavailable. (Unknown Error.)";
		}	
		
		return feed;
	}
	
	/**
	 * Sets the location for getData().
	 * @param location The name of the city.
	 */
	public void setLocation(String location){
		this.location = location.trim();
	}
	
	/**
	 * Sets the search query for getData().
	 * @param query a Query enum. Null defaults to temperature.
	 */
	public void setQuery(Query query){
		this.query = query != null ? query : Query.TEMPERATURE;
	}
	
	/**
	 * Runs all API calls and outputs to the console for review.
	 * @param args 
	 */
	public static void main(String[] args) {
		WeatherFeed wf = new WeatherFeed("Eindhoven", null);
		System.out.println(wf.getData());
		wf.setQuery(Query.DESCRIPTION);
		System.out.println(wf.getData());
		wf.setQuery(Query.HUMIDITY);
		System.out.println(wf.getData());
		wf.setQuery(Query.PRESSURE);
		System.out.println(wf.getData());
		wf.setQuery(Query.TEMPERATURE);
		System.out.println(wf.getData());
		wf.setQuery(Query.WINDDIRECTION);
		System.out.println(wf.getData());
		wf.setQuery(Query.WINDSPEED);
		System.out.println(wf.getData());
	}
}
