package aws.test;

import aws.awsS3_file.bm.FileFactory;
import aws.utils.PropertyReader;
import aws.service.S3Service;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import com.kncept.junit.dataprovider.ParameterSource;
import com.kncept.junit.dataprovider.ParameterisedTest;

import java.util.Collection;

import static com.kncept.junit.dataprovider.testfactory.TestFactoryCallback.instanceProvider;
import static org.junit.jupiter.api.Assertions.*;

public class S3Test {

    private S3Service s3Service = new S3Service();

    @TestFactory
    public Collection<DynamicTest> testFactory() {
        return instanceProvider(this);
    }

    @ParameterSource(name = "awsS3BucketName")
    public Object[][] awsS3BucketName(){
        return new Object[][]{
                {(new PropertyReader()).gets3BucketName(), (new FileFactory()).getFilePath(), (new FileFactory()).getFileName()},
        };
    }

    @ParameterisedTest(source = "awsS3BucketName")
    public void verifyAwsS3WorkFlow(String bucketName, String pathToFile, String fileName){
        // CreateAwsS3BucketTest
        assertTrue(s3Service.createAwsS3Bucket(bucketName));
        // UploadFileToAwsS3Bucket
        assertTrue(s3Service.uploadFileToAwsS3Bucket(bucketName, pathToFile, fileName));
        // DeleteFileFromAwsS3Bucket
        assertFalse(s3Service.deleteFileFromAwsS3Bucket(bucketName));
        // DeleteAwsS3Bucket
        assertFalse(s3Service.deleteAwsS3Bucket(bucketName));
    }
}