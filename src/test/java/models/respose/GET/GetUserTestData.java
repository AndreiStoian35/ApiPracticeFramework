package models.respose.GET;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GetUserTestData {

    @JsonProperty("results")
    public List<Results> results;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Results{

        @JsonProperty("name")
       public Name name;
   }


    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Name{

        @JsonProperty("first")
       public String first;

        @JsonProperty("last")
       public String last;
   }



}
