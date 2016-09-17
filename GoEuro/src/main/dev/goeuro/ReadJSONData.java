package main.dev.goeuro;


import java.io.StringReader;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

public class ReadJSONData {
	
	public ReadJSONData(){
		
	}
	
	/**
	 * 
	 * @param content
	 * @return Get Json array from the string
	 */
	public JsonArray getJsonArray(String content){
		JsonReader reader = Json.createReader(new StringReader(content));
		JsonArray array = reader.readArray();	
		reader.close();		
		return array;
		
	}

}
