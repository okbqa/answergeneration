package org.openqa.openqasimpleanswergeneration;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.Syntax;
import com.hp.hpl.jena.sparql.engine.http.QueryEngineHTTP;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This class is a simple implementation of the answer generator module of 
 * the OpenQA architecture. It runs the best query from a list of queries on all
 * endpoints and returns the merged results from these endpoints. Note that it 
 * assumes SPARQL1.1 queries where the SERVICE clause is used. Thus, it does not
 * attempt any form of smart federation.
 * @author ngonga
 *
 */
public class AnswerGenerator 
{
    //default SPARQL endpoint in case none is given
    public static String DEFAULT = "http://dbpedia.org/sparql";
    
    public static Set<Set<Result>> getResults(String query, Set<String> endpoints)
    {
        
        //set default endpoint to DEFAULT if none is given
        if(endpoints==null)
            endpoints = new HashSet<String>();
        if(endpoints.isEmpty())
            endpoints.add(DEFAULT);
        
        //generate output for each endpoint
        Query sparqlQuery = QueryFactory.create(query);
        Set<Set<Result>> output = new HashSet<Set<Result>>();    
        for(String endpoint: endpoints)
        {
            QueryEngineHTTP engine = QueryExecutionFactory.createServiceRequest(endpoint, sparqlQuery);
                    ResultSet rs = engine.execSelect();
            while(rs.hasNext())
            {
                QuerySolution qs = rs.next();
                Set<Result> results = new HashSet<Result>();
                List<String> vars = rs.getResultVars();
                for(String var: vars)
                {
                    results.add(new Result(var, qs.get(var).toString(), endpoint));
                }
                output.add(results);
            }
        }
        return output;
    }
}
