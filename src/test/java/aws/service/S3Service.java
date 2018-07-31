package aws.service;

import aws.test.ClientSetUp;
import aws.utils.AwsS3FileUploadProgress;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.services.s3.transfer.*;
import java.io.File;

public class S3Service {

    private AmazonS3 s3 = (new ClientSetUp()).setUpS3ClientWithValidAwsCredentials();

    public Boolean createAwsS3Bucket(String bucketName) {
        Bucket b = null;
        if (s3.doesBucketExistV2(bucketName)) {
            System.out.format("ERROR! Bucket with the name - '%s' already exists on AWS S3.\n", bucketName);
            System.exit(1);
        } else {
            try {
                b = s3.createBucket(bucketName);
                System.out.format("Bucket with the name - '%s' was successfully created on AWS S3.\n", bucketName);
            } catch (AmazonS3Exception e) {
                System.err.println(e.getErrorMessage());
            }
        }
        return s3.doesBucketExistV2(bucketName);
    }

    public Boolean uploadFileToAwsS3Bucket(String bucketName, String pathToFile, String fileName){
        AwsS3FileUploadProgress awsS3FileUploadProgress = new AwsS3FileUploadProgress();
        File file = new File(String.format(pathToFile, fileName));
        TransferManager xfer_mgr = TransferManagerBuilder.standard().withS3Client(s3).build();
        try {
            Upload xfer = xfer_mgr.upload(bucketName, file.getName(), file.getAbsoluteFile());
            awsS3FileUploadProgress.waitForCompletion(xfer);
            System.out.format("Local file by path - '%s' was successfully uploaded to AWS S3 Bucket - '%s'.\n", file.getAbsoluteFile(), bucketName);
        } catch (AmazonServiceException e) {
            System.err.println(e.getErrorMessage());
            System.exit(1);
        }
        xfer_mgr.shutdownNow(false);
        return s3.doesObjectExist(bucketName, fileName);
    }

    public Boolean deleteFileFromAwsS3Bucket(String bucketName){
        ListObjectsV2Request req = new ListObjectsV2Request().withBucketName(bucketName);
        ListObjectsV2Result result = s3.listObjectsV2(req);
        String fileName = result.getObjectSummaries().get(0).getKey();
        try {
            if(s3.doesObjectExist(bucketName, fileName)){
                s3.deleteObject(new DeleteObjectRequest(bucketName, fileName));
                System.out.format("File with the name - '%s' was successfully deleted from AWS S3 Bucket - '%s'.\n", fileName, bucketName);
            }else{
                System.out.format("WARN: File with the name - '%s' doesn't exist on AWS S3 or has been already deleted.\n", fileName);
            }
        } catch(SdkClientException e) {
            e.printStackTrace();
        }
        return s3.doesObjectExist(bucketName, fileName);
    }

    public Boolean deleteAwsS3Bucket(String bucketName){
        try {
            if(s3.doesBucketExistV2(bucketName)){
                ObjectListing object_listing = s3.listObjects(bucketName);
                if(object_listing.getObjectSummaries().size() > 0){
                    System.out.format("ERROR! AWS S3 Bucket - '%s' contains files. Please empty Bucket before it deleting.\n", bucketName);
                    System.exit(1);
                } else {
                    s3.deleteBucket(bucketName);
                    System.out.format("Bucket with the name - '%s' was successfully deleted from AWS S3.\n", bucketName);
                }
            } else if(!s3.doesBucketExistV2(bucketName)){
                System.out.format("WARN: AWS S3 Bucket - '%s' doesn't exist or is already deleted.\n", bucketName);
                }
        } catch (AmazonServiceException e) {
            System.err.println(e.getErrorMessage());
            System.exit(1);
        }
        return s3.doesBucketExistV2(bucketName);
    }
}
