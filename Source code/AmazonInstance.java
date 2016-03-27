import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.cloudwatch.AmazonCloudWatchClient;
import com.amazonaws.services.cloudwatch.model.Datapoint;
import com.amazonaws.services.cloudwatch.model.Dimension;
import com.amazonaws.services.cloudwatch.model.GetMetricStatisticsRequest;
import com.amazonaws.services.cloudwatch.model.GetMetricStatisticsResult;
import com.amazonaws.services.cloudwatch.model.Metric;
import com.amazonaws.services.cloudwatch.model.StandardUnit;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.AuthorizeSecurityGroupIngressRequest;
import com.amazonaws.services.ec2.model.CreateKeyPairRequest;
import com.amazonaws.services.ec2.model.CreateKeyPairResult;
import com.amazonaws.services.ec2.model.CreateSecurityGroupRequest;
import com.amazonaws.services.ec2.model.CreateSecurityGroupResult;
import com.amazonaws.services.ec2.model.DescribeAvailabilityZonesResult;
import com.amazonaws.services.ec2.model.DescribeInstancesRequest;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.IpPermission;
import com.amazonaws.services.ec2.model.KeyPair;
import com.amazonaws.services.ec2.model.MonitorInstancesRequest;
import com.amazonaws.services.ec2.model.Monitoring;
import com.amazonaws.services.ec2.model.Reservation;
import com.amazonaws.services.ec2.model.RunInstancesRequest;
import com.amazonaws.services.ec2.model.RunInstancesResult;


public class AmazonInstance {

	public static void create(int noAmazonInstances){
		
		AmazonEC2Client amazonEC2Client = CreateAmazonEc2Client();
		
	    /*Create and initialize a RunInstancesRequest instance.	 
			 * Specify the Amazon Machine Image (AMI) (withImageId), the instance type (withInstanceType), the minimum (withMinCount) and maximum (withMaxCount) number of instances to run, key pair name (withKeyName), and the name of one or more security groups (withSecurityGroups), as follows:
			 */

						  RunInstancesRequest runInstancesRequest = 
							  new RunInstancesRequest();
						        	
						  runInstancesRequest.withImageId("ami-2769054e")
						                     .withInstanceType("m1.small")
						                     .withMinCount(1)
						                     .withMaxCount(noAmazonInstances)
						                     .withKeyName("priyazPair")
						                     .withSecurityGroups("PriyaInstancSecurityGroup");
						  
						  RunInstancesResult runInstancesResult =  amazonEC2Client.runInstances(runInstancesRequest);
						  
						  System.out.println("Insatnce created");
							/**Image Id:	ami-f1026198
						Owner:	979382823631
						Manifest:	bitnami-cloud/rubystack/bitnami-rubystack-1.9.3-8-linux-ubuntu-12.04.2-i386-s3.manifest.xml
						Platform:	Ubuntu
						Architecture:	i386
						Root Device Type:	instance-store*/	
	}
	
	public static void main(String args[]){
		
		/*Security Group and key pair are generated once for all the instance*/
		AmazonEC2Client ec2 = CreateAmazonEc2Client();
		CreateSecurityGroupAndKeyPairOnce(ec2);

		//create();
		//EnableClouWathc();
	}

	

	private static AmazonEC2Client CreateAmazonEc2Client() {
		// TODO Auto-generated method stub
		/*Create and initialize an AWSCredentials	 instance.	 
		 * Specify the AwsCredentials.properties file you created, as follows:
		 */

		InputStream credentialsAsStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("AwsCredentials.properties");
		AWSCredentials credentials = null;
		try {
			credentials = new PropertiesCredentials(credentialsAsStream);
			System.out.println(credentials.getAWSAccessKeyId());
			System.out.println(credentials.getAWSSecretKey());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
				
				AmazonEC2Client amazonEC2Client=   new AmazonEC2Client(credentials);
						amazonEC2Client.setEndpoint("ec2.us-east-1.amazonaws.com");
					
		return amazonEC2Client;
	}

	private static void CreateSecurityGroupAndKeyPairOnce(AmazonEC2Client amazonEC2Client) {
		// TODO Auto-generated method stub
		
		/*Create and initialize a CreateSecurityGroupRequest instance.
		 *  Use the withGroupName method to set the security group name, and the withDescription method to set the security group description, as follows:
		 
*/
			CreateSecurityGroupRequest createSecurityGroupRequest = 
				new CreateSecurityGroupRequest();
			        	
			createSecurityGroupRequest.withGroupName("PriyaInstancSecurityGroup")
				.withDescription("My Java Security Group");
			
			/*The security group name must be unique within the AWS region in which you initialize your Amazon EC2 Client.
			 *  You must use US-ASCII characters for the security group name and description.
			 */

			  CreateSecurityGroupResult createSecurityGroupResult = amazonEC2Client.createSecurityGroup(createSecurityGroupRequest);
	/*		  
	Create and initialize an IpPermission instance. Use the withIpRanges method to set the range of IP addresses
		   *  to authorize ingress for, and use the withIpProtocol method to set the IP protocol. Use the withFromPort and withToPort methods to specify range of ports to authorize ingress for, as follows:
		   */

				  IpPermission ipPermission =  new IpPermission();
				      	
				  ipPermission.withIpRanges("111.111.111.111/32", "150.150.150.150/32")
				              .withIpProtocol("tcp")
				              .withFromPort(22)
				              .withToPort(22);
				  
				  AuthorizeSecurityGroupIngressRequest authorizeSecurityGroupIngressRequest =
				  	new AuthorizeSecurityGroupIngressRequest();
				      	
				  authorizeSecurityGroupIngressRequest.withGroupName("PriyaInstancSecurityGroup")
				                                      .withIpPermissions(ipPermission);
				 // Pass the request object into the authorizeSecurityGroupIngress method, as follows:

				  amazonEC2Client.authorizeSecurityGroupIngress(authorizeSecurityGroupIngressRequest);
				  
	/*
	 * Create and initialize a CreateKeyPairRequest instance. Use the withKeyName method to set the key pair name, as follows:
	 * */
				  CreateKeyPairRequest createKeyPairRequest = 	new CreateKeyPairRequest();
				createKeyPairRequest.withKeyName("priyazPair");					
				
				CreateKeyPairResult createKeyPairResult = amazonEC2Client.createKeyPair(createKeyPairRequest);

				KeyPair keyPair = new KeyPair();
				keyPair = createKeyPairResult.getKeyPair();
				String privateKey = keyPair.getKeyMaterial();
				
	}

	public static int getNumberOfInstances() {
		// gets the instances and returns the number of instance in the amazon 

		AmazonEC2Client ec2 = CreateAmazonEc2Client();
		
		
            DescribeInstancesResult describeInstancesRequest = ec2.describeInstances();
            List<Reservation> reservations = describeInstancesRequest.getReservations();
            Set<Instance> instances = new HashSet<Instance>();

            for (Reservation reservation : reservations) {
                instances.addAll(reservation.getInstances());
            }
            
            System.out.println("You have " + instances.size() + " Amazon EC2 instance(s) running.");
		return instances.size();
		}
	
}
