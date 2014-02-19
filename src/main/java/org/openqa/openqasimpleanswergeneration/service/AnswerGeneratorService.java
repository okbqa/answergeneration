/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openqa.openqasimpleanswergeneration.service;

import com.sun.grizzly.http.SelectorThread;
import com.sun.jersey.api.container.grizzly.GrizzlyWebContainerFactory;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.openqasimpleanswergeneration.AnswerGenerator;
import org.openqa.openqasimpleanswergeneration.Result;

/**
 *
 * @author ngonga
 */
@Path("/getAnswer")
public class AnswerGeneratorService {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getResults(@QueryParam("queries") String queries, @QueryParam("endpoints") String endpoints) {
        if(queries == null) return "{}";
        String score = "score";
        String query = "query";
        JSONArray queryList = new JSONArray(queries);
        JSONArray endpointList = null;
        if (endpoints != null) {
            endpointList = new JSONArray(endpoints);
        }
        
        double max = 0, currentScore = 0;
        String bestQuery = "";
        for (int i = 0; i < queryList.length(); i++) {            
            currentScore = queryList.getJSONObject(i).getDouble(score);
            if (currentScore > max) {
                max = currentScore;
                   bestQuery = queryList.getJSONObject(i).getString(query);
            }
        }

        Set<String> eps = new HashSet<String>();
        if (endpointList != null) {
            for (int i = 0; i < endpointList.length(); i++) {
                eps.add(endpointList.get(i).toString());
            }
        } else {
            eps = null;
        }
        
       return generateJson(AnswerGenerator.getResults(bestQuery.toString(), eps)).toString();
    }
    
    /*@GET
    @Produces(MediaType.APPLICATION_JSON)
        public String getPostResults(@FormParam("queries") String queries, @FormParam("endpoints") String endpoints) {
        return getResults(queries, endpoints);
    }*/

    public JSONArray generateJson(Set<Set<Result>> results) {
        JSONArray result = new JSONArray();
        for (Set<Result> resultSet : results) {
            JSONArray array = new JSONArray();
            for (Result r : resultSet) {
                array.put(r.getJSON());
            }
            result.put(array);
        }
        return result;
    }

    public static void main(String[] args) throws IOException {

        final String baseUri = "http://localhost:9998/";
        final Map<String, String> initParams =
                new HashMap<String, String>();

        initParams.put("com.sun.jersey.config.property.packages",
                "org.openqa.openqasimpleanswergeneration.service");

        System.out.println("Starting grizzly...");
        SelectorThread threadSelector;
        threadSelector = GrizzlyWebContainerFactory.create(baseUri, initParams);
        System.out.println(String.format(
                "Jersey app started with WADL available at %sapplication.wadl\n "
                + "Try out %shelloworld\nHit enter to stop it...", baseUri, baseUri));
        System.in.read();
        threadSelector.stopEndpoint();
        System.exit(0);
    }
}
