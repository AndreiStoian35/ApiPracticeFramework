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
            System.out.println("Properties file loaded");
        } catch (Exception e) {
           throw new RuntimeException("Could not load config.properties " + "\n" + e);
        }

    }


    public static String getBaseUrl(){
        return getUrl("baseUrl");
    }

    public static String getTestDataUrl(){
        return getUrl("testDataUrl");
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
        String configValue = properties.getProperty(key);

        if (systemValue == null || systemValue.isBlank()){
            if(configValue != null && !configValue.isBlank()){
                try{
                    return configValue;

                }
                catch(Exception e){
                    throw new RuntimeException("Property" + key + " key or value not found in configs file");
                }
            }

        }

        return systemValue;
    }

}

