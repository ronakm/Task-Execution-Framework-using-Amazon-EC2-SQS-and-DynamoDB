import java.io.*;
import java.util.HashMap;
import java.util.List;

import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.SendMessageRequest;
class Node{
    public HashMap<String, Integer> item;
    public Node next;
    public Node(HashMap<String, Integer> val){ 
        item = val; 
    }
    public void displayNode(){ 
        System.out.print("[" + item + "]"); 
    }
	public HashMap<String, Integer> getData() {
		return item;
	}
}
class LinkedList{
    private Node start;
    private Node end;
    public LinkedList(){
        start = null;
        end = null;
    }
    public boolean isEmpty(){ 
        return start==null; 
    }
    public void insertEnd(HashMap<String, Integer> val){
    	//Insert node at the end of list
        Node newNode = new Node(val);
        if( isEmpty() )
            start = newNode;
        else
            end.next = newNode;
        end = newNode;
    }
    public HashMap<String, Integer> deQueue(){
    	//delete the node from the beginning of the list
        HashMap<String, Integer> temp = start.item;
        if(start.next == null)
            end = null;
        start = start.next;
        return temp;
    }
    public void displayList(){
        Node current = start;
        while(current != null)
        {
            current.displayNode();
            current = current.next;
        }
        System.out.println("");
    }
	public void readandProcessElement(String Queuename) {
		Node current = start;
		AmazonSQS sqs = null;
        while(current != null)
        {
           HashMap<String, Integer> data =  current.getData();
            System.out.println("element "+data);
            try {
            	 sqs =SimpleQueueServiceSample.push(data,Queuename);
            	 //creating workers
            	
			} catch (Exception e) {
				e.printStackTrace();
			}
            current = current.next;
        }
        System.out.println();System.out.println();System.out.println();
	}
	public void pushIntoSQS() {
		// TODO Auto-generated method stub
		Node current = start;
		AmazonSQS sqs = null;
           HashMap<String, Integer> data =  current.getData();
            System.out.println("element "+data);
            try {
            	 sqs =SimpleQueueServiceSample.push(data, "resultQueue");
            	 //creating workers
            	
			} catch (Exception e) {
				e.printStackTrace();
			}
           
        
	}
}
class Queue{
    private LinkedList listObj;
    public Queue(){
        listObj = new LinkedList(); 
    }
    public boolean isEmpty(){ 
        return listObj.isEmpty(); 
    }
    public void insert(HashMap<String, Integer> k){ 
        listObj.insertEnd(k); 
    }
    public HashMap<String, Integer> deQueue(){ 
        return listObj.deQueue(); 
    }
    public void display(){
        System.out.print("Queue [FIFO]: ");
        listObj.displayList();
    }
	public void processElement() {
		listObj.readandProcessElement("taskQueue");
	}
	public void pushIntoSQS() {
		// TODO Auto-generated method stub
		listObj.pushIntoSQS();
	}
}
class QueApp{
	public static long start;
	public static Queue result = new Queue();
    public static void main(String[] args){ 
        Queue demo = new Queue();
        int i = 1;
        System.out.println("Inserting  elements in the queue");
        HashMap<String, Integer> a = new HashMap<String, Integer>();
        a.put("sleep 1000", i);
        i++;
        demo.insert(a);
        a.put("sleep 200", i);
        i++;
        demo.insert(a);
        a.put("sleep 300", i);
        i++;
        demo.insert(a);
        a.put("sleep 600", i);
        i++;
        demo.insert(a);
        a.put("sleep 1300", i);
        i++;
        demo.insert(a);
                
        demo.display();
        
        System.out.println("Reading each element for processing");
     
        //For remote workers
       demo.processElement();
        String tasks = SimpleQueueServiceSample.show();
       tasks =  tasks.substring(1, tasks.length()-1);
        System.out.println(tasks.split(",")[4]);
        //For local server
        /*start = System.currentTimeMillis();
        System.out.println(start);
    	while(!demo.isEmpty()){
    			HashMap<String, Integer> task =  demo.deQueue();
        		ThreadPoolService.insertTasK(task);
    	 }
    	    
    	ThreadPoolService.stop();*/
    	
       	       
    	
    }
			
	 
}