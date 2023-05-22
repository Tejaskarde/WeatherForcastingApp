package org.weatherForcast;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.text.html.parser.Entity;
import java.io.IOException;
import java.net.StandardSocketOptions;
import java.net.URISyntaxException;
import java.util.Scanner;

public class WeatherForcastApp {

    public static void main(String[] args) {
        try {
            callWeatherForcastApi();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }  catch(IOException e)
        {
            throw new RuntimeException(e);
        }

        // System.out.println("Hello world!");
    }



    public static void callWeatherForcastApi() throws URISyntaxException, IOException {
        // Taking input for location to be forcast
         System.out.println("Please enter the location for which you want to check the weather forecast informations");
        Scanner sc = new Scanner(System.in);
        String location  = sc.nextLine();

             URIBuilder builder = new URIBuilder("https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/weatherdata/forecast");
             builder.setParameter("aggregateHours","24");
             builder.setParameter("contentType","json");
             builder.setParameter("unitGroup","metric");
             builder.setParameter("locationMode","single");
             builder.setParameter("key","1PYNQ6AWUDJE9AFERDCHJHSXK");
             builder.setParameter("location",location);

             HttpGet getData = new HttpGet(builder.build());

             // crating new object
        // to close connection also open thread if close not ocuupy not more memory
        CloseableHttpClient httpClient = HttpClients.createDefault();

        CloseableHttpResponse response = httpClient.execute(getData);
        httpClient.close();
        //System.out.println(response);
         /// fetch respose from api
        if(response.getStatusLine().getStatusCode()==200) {
            HttpEntity responseEntity = response.getEntity();
            // for get responseEntity to in readible format
            //Converting object to Stirng

            String result = EntityUtils.toString(responseEntity);
            //System.out.println(result);

            // we want to pull or extract data from result of 5 attribute
            //mintemp, maxtemp, humidity , visblity , datetimeStr

            // this value is in the result obj and resposeobj is in JSON format to read Json format we have to call class
            JSONObject responseObject = new JSONObject(result);

            // that attributes are inside the result -> location -> value in values we got that attribute
            JSONObject locationObject = responseObject.getJSONObject("location");

            // values is in Arry json format so we need to call another class
            JSONArray valueObject = locationObject.getJSONArray("values");
            System.out.println("     datetimeStr \t\t\tmint \t    maxt \t visiblity\t\thumidity");

            // to iterate on jason array we need to use loop

            for(int i = 0 ; i < valueObject.length(); i++){
                JSONObject value = valueObject.getJSONObject(i);

            String dateTime = value.getString("datetimeStr");
            Double minTemp = value.getDouble("mint");
            Double maxTemp = value.getDouble("maxt");
            Double humidity = value.getDouble("humidity");
            Double visiblity = value.getDouble("visibility");

            System.out.println(dateTime+"\t"+minTemp+"\t\t"+maxTemp+"\t\t"+humidity+"\t\t"+visiblity);

            }

        }
        else{
             System.out.println("Something went wrong");
             return;
        }
    }

}