package in.apt.assetmanager.service.impl;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import in.apt.assetmanager.service.ObjectStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.net.URL;
import java.util.Date;

@Service
public class S3StorageServiceImpl implements ObjectStorageService<File> {

    @Autowired
    private AmazonS3 amazonS3;

    @Value("${aws.s3.bucket.name}")
    private String bucket;

    @Value("${asset.url.default.expiry}")
    private Long defaultExpiry;

    @Override
    public void upload(String filename, File file) {
            amazonS3.putObject(new PutObjectRequest(bucket,filename, file)
                    .withCannedAcl(CannedAccessControlList.Private));
    }

    @Override
    public String getExpirableLocation(String s3Identifer, String ttl) {
        Date expiryTime = fetchExpirationTime(ttl);
        GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucket, s3Identifer)
                .withMethod(HttpMethod.GET)
                .withExpiration(expiryTime);
        URL url = amazonS3.generatePresignedUrl(generatePresignedUrlRequest);
        return url.toString();
    }

    private Date fetchExpirationTime(String ttl) {
        Long ttlValue = ttl != null ? Long.parseLong(ttl) : defaultExpiry;
        Date expiryTime = new Date();
        long expiryTimeMillis = expiryTime.getTime();
        expiryTimeMillis += 1000 * ttlValue;
        expiryTime.setTime(expiryTimeMillis);
        return expiryTime;
    }
}
