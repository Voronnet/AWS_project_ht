package aws.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyReader {

    public String getAwsAccessKey(){
        return getPropertyValue("accessKey");
    }

    public String getAwsSecretKey(){
        return getPropertyValue("secretKey");
    }

    public String gets3BucketName(){
        return getPropertyValue("s3BucketName");
    }

    public String lambdaFunctionName(){
        return getPropertyValue("lambdaFunctionName");
    }

    private String getPropertyValue(String key){

        Properties properties = new Properties();
        InputStream input = null;
        String filename = "awsS3.properties";

        try {
            input = getClass().getClassLoader().getResourceAsStream(filename);
            if(input==null){
                System.out.println("Sorry, unable to find " + filename);
            }
            //load a properties file from class path, inside non-static method
            properties.load(getClass().getClassLoader().getResourceAsStream(filename));
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally{
            if(input!=null){
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return properties.getProperty(key);
    }
}
