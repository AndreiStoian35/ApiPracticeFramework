package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {

    static Properties properties = new Properties();

    static {

        try (InputStream input = ConfigReader.class
                .getClassLoader()
                .getResourceAsStream("config.properties")) {

            properties.load(input);
        } catch (Exception e) {
           throw new RuntimeException("Could not load config.properties " + "\n" + e);
        }

    }


    public static String getUrl(String urlType){
        return getValue(urlType);
    }

    public static String getAuthUsername(){
        return getValue("authUser");
    }

    public static String getAuthPassword(){
        return getValue("authPwd");
    }


    private static String getValue(String key){

        String systemValue = System.getenv(key);

        if (systemValue == null || systemValue.isBlank()){

            return properties.getProperty(key);
        }

        return systemValue;
    }

}

