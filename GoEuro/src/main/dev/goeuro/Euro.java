package main.dev.goeuro;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import javax.json.JsonArray;
import javax.json.JsonObject;



public class Euro {
	
	//URL
	URL url ;
	//URL connection handler
	URLConnection connection;
	//Endpoint url
	String endpoint;
	//Json object keys
	String[] headers = {"_id","name","type","geo_position"};
	//Keys for csv file
	String[] fileheaders = {"_id","name","type","latitude","longitude"};
	//Result file name
	String resultFileName = null;
	
	
	public Euro(){		
		endpoint = "http://api.goeuro.com/api/v2/position/suggest/en/";
	}
	/** 
	 * 
	 * @param city
	 */
	
	public void setEndpoint(String city){
		endpoint = endpoint + city;
	}
	
	/**
	 * 
	 * @return Complete Url
	 */
	public String getEndpoint(){
		return endpoint;
	}
	
	/**
	 * 
	 * @return String containing json details
	 * @throws IOException
	 */
	
	public String fetchCityDetails() throws IOException{
		url = new URL(endpoint);
		connection = url.openConnection();
		connection.setDoOutput(true);
		InputStream input = connection.getInputStream();
		BufferedReader reader = new BufferedReader(
				new InputStreamReader(input,"UTF-8"));
		String line=null;
		StringBuilder builder = new StringBuilder();
		while((line = reader.readLine())!=null){
			builder.append(line);
		}
		reader.close();
		return builder.toString();		
	}
	
	
	/**
	 * 
	 * @return Result file name
	 */
	
	private String getResultFileName() {
		return resultFileName;
	}
	
	/**
	 * 
	 * 
	 * @param resultFileName
	 */

	private void setResultFileName(String resultFileName) {
		this.resultFileName = resultFileName;
	}
	
	/**
	 * Generate file name for result and set it.
	 */
	private void generateResultFileName(){
		Date date = new Date();
		SimpleDateFormat formatType = new
				SimpleDateFormat("yyyy-dd-mm-hh-mm-ss");
		setResultFileName(formatType.format(date)+".csv");
	}
	
	/**
	 * 
	 * 
	 * @param object
	 * @return Array containing json object
	 */
	
	private Object[] fetchObjectDetails(JsonObject object){
		ArrayList<String> arrayList = new ArrayList<String>();
		for(String header:headers){			
			if(object.get(header)!=null){			
				if(header.equals("geo_position")){
					try{
					JsonObject locationArray = (JsonObject) object.get(header);
					Map map = (Map) locationArray;
					arrayList.add(String.valueOf(map.get("latitude").toString()));
					arrayList.add(String.valueOf(map.get("longitude").toString()));
					}catch(Exception e){
						arrayList.add(null);
						e.printStackTrace();
					}
					
				}else{
					try{
						arrayList.add(String.valueOf(object.get(header).toString()));
					}catch(Exception e){
						arrayList.addAll(null);
						e.printStackTrace();
					}
				}
			}
		}
		return arrayList.toArray();	
	}
	
	/**
	 * Begin program
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		Euro euro = new Euro();
		ReadJSONData readJsonData = new ReadJSONData();
		String cityDetails=null;
		if(args.length>0){
			euro.setEndpoint(args[0].trim());
			cityDetails = euro.fetchCityDetails();
			if(!cityDetails.equals(null)){
				JsonArray array = readJsonData.getJsonArray(cityDetails);
				euro.generateResultFileName();
				GenerateCSV generateCSV = new GenerateCSV(euro.getResultFileName());
				generateCSV.writeToFile(euro.fileheaders);
				for(int i=0;i<array.size();i++){
					JsonObject jsonObject = (JsonObject) array.get(i);
					Object[] values = euro.fetchObjectDetails(jsonObject);
					generateCSV.writeToFile(values);
				}
				
			}
			
		}else{
			System.out.println("Pass a valid city name as the first argument.");
		}
		
	}

	

}
