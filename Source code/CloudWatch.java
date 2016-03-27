import java.io.IOException;
import java.io.InputStream;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.cloudwatch.AmazonCloudWatchClient;


public class CloudWatch {
	
public static void EnableClouWathc() {
	//gets the number of Amazaon instance running and the size of message and cretes 16 amazon instances ata atime when the condiion satisfies. 
		
		String key = null;
		String secret = null;
		InputStream credentialsAsStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("AwsCredentials.properties");
		AWSCredentials credentials = null;
		try {
			credentials = new PropertiesCredentials(credentialsAsStream);
			key =(credentials.getAWSAccessKeyId());
			secret =(credentials.getAWSSecretKey());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		AmazonCloudWatchClient cloudWatch = new AmazonCloudWatchClient((AWSCredentials) new BasicAWSCredentials(key, secret));
		    cloudWatch.setEndpoint("monitoring.eu-west-1.amazonaws.com");
		    
		   int sizeOfMessages =  SimpleQueueServiceSample.getNumberOFTasksRunning();
		   int noAmazonInstances = AmazonInstance.getNumberOfInstances();
		   
		   if(sizeOfMessages <= 500  && noAmazonInstances == 1000 && sizeOfMessages != 0)
			  AmazonInstance.create(16);
		   else if(sizeOfMessages > 1000 && sizeOfMessages <= 1500 && noAmazonInstances == 16)
			   AmazonInstance.create(16);
		   else if(sizeOfMessages > 1000 && sizeOfMessages <= 2000 && noAmazonInstances == 32)
			   AmazonInstance.create(16);

		  
		
	}

public static void main(String args[]){
	
	/*
	 *keeps on polling  cloudwatch after 100 sec  
	 * */
	while(true){
	EnableClouWathc();
	try {
		System.out.println("try poling again");
		Thread.currentThread().sleep(100000);
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}
}

}
