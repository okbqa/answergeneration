/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openqa.openqasimpleanswergeneration;

import org.json.JSONObject;

/**
 *
 * @author ngonga
 */
public class Result {
    public String var, binding, endpoint;
    
    public Result(String var, String binding, String endpoint)
        {
            this.var = var;
            this.binding = binding;
            this.endpoint = endpoint;
        }
    
    public JSONObject getJSON()
    {
        JSONObject object = new JSONObject();
        object.accumulate("var", var);
        object.accumulate("binding", binding);
        object.accumulate("endpoint", endpoint);
        return object;
    }
    
    public static void main(String args[])
    {
        Result r = new Result ("a", "b", "c");
        System.out.println(r.getJSON());
    }
}
