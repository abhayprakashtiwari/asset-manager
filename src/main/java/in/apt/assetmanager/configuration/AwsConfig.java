package in.apt.assetmanager.configuration;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AwsConfig {

    @Value("${aws.access.key.id}")
    private String awsAccessKey;

    @Value("${aws.access.key.secret}")
    private String awsAccessKeySecret;

    @Value("${aws.region}")
    private String awsRegion;

    @Bean("awsCredentials")
    public AWSCredentials getAWSCredentials(){
        return new BasicAWSCredentials(awsAccessKey, awsAccessKeySecret);
    }

    @Bean
    public AmazonS3 amazonS3(AWSCredentials awsCredentials){
        return AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .withRegion(awsRegion)
                .build();
    }

}
