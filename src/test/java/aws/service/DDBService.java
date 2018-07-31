package aws.service;

import aws.test.ClientSetUp;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;

public class DDBService {

    DynamoDB dynamoDB = new DynamoDB((new ClientSetUp()).setUpDDBClientWithValidAwsCredentials());

}
