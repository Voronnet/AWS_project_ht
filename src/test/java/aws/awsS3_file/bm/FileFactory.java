package aws.awsS3_file.bm;

import org.apache.commons.lang3.RandomStringUtils;

import java.io.File;
import java.io.IOException;

public class FileFactory {

    public String getFileName() {
        return fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    private String fileName;
    private String filePath;

    public FileFactory(){
        File f = null;
        File dir = new File("src/test/resources/").getAbsoluteFile();
        String prefix = String.format("%s", RandomStringUtils.randomAlphanumeric(3));
        String suffix = String.format(".%s", RandomStringUtils.randomAlphanumeric(3));
        try{
            f = File.createTempFile(prefix, suffix, dir);
            f.deleteOnExit();
        } catch (IOException e){
            e.printStackTrace();
        }
        fileName = f.getName();
        filePath = dir + File.separator + "%s";
    }
}