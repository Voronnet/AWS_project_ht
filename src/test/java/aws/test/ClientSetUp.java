package aws.test;

import aws.utils.PropertyReader;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

public class ClientSetUp {

    private PropertyReader propertyReader = new PropertyReader();
    BasicAWSCredentials awsCreds = new BasicAWSCredentials(propertyReader.getAwsAccessKey(), propertyReader.getAwsSecretKey());

    public AmazonS3 setUpS3ClientWithValidAwsCredentials(){
        AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .withRegion(Regions.US_EAST_1)
                .build();
        return s3Client;
    }

    public AmazonDynamoDB setUpDDBClientWithValidAwsCredentials(){
        AmazonDynamoDB DDBClient = AmazonDynamoDBClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .withRegion(Regions.US_EAST_1)
                .build();
        return DDBClient;
    }


}