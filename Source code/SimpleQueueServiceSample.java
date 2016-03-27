/*
 * Copyright 2010-2013 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleworkflow.flow.annotations.Wait;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.DeleteQueueRequest;
import com.amazonaws.services.sqs.model.GetQueueAttributesRequest;
import com.amazonaws.services.sqs.model.GetQueueAttributesResult;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageRequest;

/**
 * This sample demonstrates how to make basic requests to Amazon SQS using the
 * AWS SDK for Java.
 * <p>
 * <b>Prerequisites:</b> You must have a valid Amazon Web
 * Services developer account, and be signed up to use Amazon SQS. For more
 * information on Amazon SQS, see http://aws.amazon.com/sqs.
 * <p>
 * <b>Important:</b> Be sure to fill in your AWS access credentials in the
 *                   AwsCredentials.properties file before you try to run this
 *                   sample.
 * http://aws.amazon.com/security-credentials
 */
public class SimpleQueueServiceSample {
	public static String QueuePath = null;
    public static AmazonSQS push(HashMap<String, Integer> data,String Queuename) throws Exception {
        /*
         * This credentials provider implementation loads your AWS credentials
         * from a properties file at the root of your classpath.
         *
         * Important: Be sure to fill in your AWS access credentials in the
         *            AwsCredentials.properties file before you try to run this
         *            sample.
         * http://aws.amazon.com/security-credentials
         */
        AmazonSQS sqs = new AmazonSQSClient(new ClasspathPropertiesFileCredentialsProvider());
		/*Region usWest2 = Region.getRegion(Regions.US_WEST_2);
		sqs.setRegion(usWest2);
*/
        System.out.println("===========================================");
        System.out.println("Getting Started with Amazon SQS");
        System.out.println("===========================================\n");

        try {
            // Create a queue
           
            	String myQueueUrl = null;
               
               System.out.println("Creating/ a new SQS queue called "+Queuename);
	                CreateQueueRequest createQueueRequest = new CreateQueueRequest(Queuename);
	             /**
	                 * the AWS API, the call to createQueue, shown in Listing 2, 
	                 * doesn't necessarily create a new queue every time. If the queue already exists, its handle is returned. In SQS, queues are just URLs; consequently, a queue handle is also just a URL. Note that in the AWS SDK API,
	                 *  the Queue URL is a String type and not the Java URL type
	                 * */
	                
	            myQueueUrl = sqs.createQueue(createQueueRequest).getQueueUrl();
	           QueuePath = myQueueUrl;
           System.out.println();

           // Send a message
            System.out.println("Sending a message to MyQueue.\n");
            sqs.sendMessage(new SendMessageRequest(myQueueUrl, data.toString()));
            
            
            return sqs;
            
          
        } catch (AmazonServiceException ase) {
            System.out.println("Caught an AmazonServiceException, which means your request made it " +
                    "to Amazon SQS, but was rejected with an error response for some reason.");
            System.out.println("Error Message:    " + ase.getMessage());
            System.out.println("HTTP Status Code: " + ase.getStatusCode());
            System.out.println("AWS Error Code:   " + ase.getErrorCode());
            System.out.println("Error Type:       " + ase.getErrorType());
            System.out.println("Request ID:       " + ase.getRequestId());
        } catch (AmazonClientException ace) {
            System.out.println("Caught an AmazonClientException, which means the Client encountered " +
                    "a serious internal problem while trying to communicate with SQS, such as not " +
                    "being able to access the network.");
            System.out.println("Error Message: " + ace.getMessage());
        }
		return sqs;
    }

	public static String show() {
		// it will view the task Queue messages
		  AmazonSQS sqs = new AmazonSQSClient(new ClasspathPropertiesFileCredentialsProvider());
		  CreateQueueRequest createQueueRequest = new CreateQueueRequest("taskQueue");
		String  myQueueUrl = sqs.createQueue(createQueueRequest).getQueueUrl();
		String taskQueue = null;
		boolean flag = true;
		 while (flag) {
			  List<Message> msgs = sqs.receiveMessage(
			     new ReceiveMessageRequest(myQueueUrl).withMaxNumberOfMessages(1)).getMessages();
			  System.out.println(msgs.size());
			  if (msgs.size() > 0) {
			   Message message = msgs.get(0);
			   System.out.println("The message is " + message.getBody());
			   taskQueue = message.getBody();
			    sqs.deleteMessage(new DeleteMessageRequest(myQueueUrl, message.getReceiptHandle()));
			    
			    
			  } else {
				  flag = false;
			   // System.out.println("nothing found, trying again in 30 seconds"); 
			  }
			  System.out.println();
			}
		 
		 System.out.println(taskQueue);
		 return taskQueue;
	}

	public static String executeRemoteWorkers(int nOfTaskToExecute) {
		// gets the task Queue from SQS and read each message and returns the messgae bode 
		 AmazonSQS sqs = new AmazonSQSClient(new ClasspathPropertiesFileCredentialsProvider());
		  CreateQueueRequest createQueueRequest = new CreateQueueRequest("taskQueue");
		String  myQueueUrl = sqs.createQueue(createQueueRequest).getQueueUrl();
		String taskQueue = null;
		Boolean flag = true;
		//while(flag){
			  List<Message> msgs = sqs.receiveMessage(
			     new ReceiveMessageRequest(myQueueUrl).withMaxNumberOfMessages(1)).getMessages();
			  System.out.println(msgs.size());
			  if (msgs.size() > 0) {
				  flag = false;
				  Message message = msgs.get(0);
				  System.out.println("The message is " + message.getBody());
				  taskQueue = message.getBody();
				  sqs.deleteMessage(new DeleteMessageRequest(myQueueUrl, message.getReceiptHandle()));
			    
			   //RemoteWorkers.ProcessTask(taskQueue);

			  } else {
			    taskQueue = "";
			  }
			  System.out.println();
			
		//}
		 return taskQueue;
	}

	public static int ResultQueue() {
		// gets the result Queue from SQS and get the messages size and returns the size
		AmazonSQS sqs = null ;
		
		 sqs = new AmazonSQSClient(new ClasspathPropertiesFileCredentialsProvider());
		  CreateQueueRequest createQueueRequest = new CreateQueueRequest("resultQueue");
		String  myQueueUrl = sqs.createQueue(createQueueRequest).getQueueUrl();
		String taskQueue = null;
		boolean flag = true;
		while(flag){
			  List<Message> msgs = sqs.receiveMessage(
			     new ReceiveMessageRequest(myQueueUrl)).getMessages();
			  System.out.println("Result Queue Size "+msgs.size());
			  int size = msgs.size();
			if(size > 0) {
				flag = false; 
				return msgs.size();
				 
			}else{
				System.out.println("trying after 10 min");
				try {
					Thread.currentThread().sleep(10000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
		return 0;
	}

	public static int getNumberOFTasksRunning() {
		// returns the number of amazon tasks running
		 AmazonSQSClient sqs = new AmazonSQSClient(new ClasspathPropertiesFileCredentialsProvider());
		  CreateQueueRequest createQueueRequest = new CreateQueueRequest("taskQueue");
		  String  myQueueUrl = sqs.createQueue(createQueueRequest).getQueueUrl();
		  
		  GetQueueAttributesRequest request = new GetQueueAttributesRequest();
	        request = request.withAttributeNames("ApproximateNumberOfMessages");
	      
	         request = request.withQueueUrl(myQueueUrl);

	            Map<String, String> attrs = sqs.getQueueAttributes(request).getAttributes();

	            // get the approximate number of messages in the queue
	            int sizeOfMessages = Integer.parseInt(attrs.get("ApproximateNumberOfMessages"));

	            System.out.println("sizeOfMessages "+sizeOfMessages);
		
		return sizeOfMessages;
	}

	
	
}
