import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Scheduler 
{
	public static float start;
	
	@SuppressWarnings({ "unchecked", "rawtypes", "unused" })
	public static void main(String[] args)
	{ 
		// wait for the connection from client and sents the task from the client file to SQS
		//scheduler –s <PORT> -lw <NUM> -rw
		
		try 
		{
	        int i=1,cnt=1;
	        int portno=Integer.parseInt(args[2]);
	        String typeOfBackEndWorker =  args[3];
 	
	        	//backend worker is a local worker
	        	 int num  =  Integer.parseInt(args[4]);
		        ServerSocket ss = new ServerSocket(portno);
		        Socket connection = null;
		        String job,fname;
		        ObjectOutputStream out;
		        ObjectInputStream in;
		       while(true){

			        System.out.println("Waiting for connection");
			        connection = ss.accept();
			        System.out.println("Connection received from " + connection.getInetAddress().getHostName());
			   
			        out = new ObjectOutputStream(connection.getOutputStream());
			        in = new ObjectInputStream(connection.getInputStream());
			        
			        System.out.println("connected");
			        Queue queues = new Queue();
			        
			        boolean flag1 = true;
					job = (String)in.readUTF();
			        System.out.println("job from Client is : " + job);
			        String[] data = job.split("%");
			  	    int j =0;
			  	    while(!data[j].equals("END")){
			  	        	HashMap<String, Integer> a = new HashMap<String, Integer>();
			  	        	a.put(data[j], j);
			  	        	queues.insert(a);
			  	        	j++;
			  	     }
			  	    out.writeInt(1);
			  	    out.flush();
					System.out.println("File Recieved");
			       
			        System.out.println("Ack sent");

			        if(typeOfBackEndWorker.equals("lw")){
			        	//local workers
				         start = System.currentTimeMillis();
				        System.out.println(start);
				        ThreadPoolService.init(num);
				      Queue result =  LocalWorkers.ProcessTask(queues);
				       result.display();
				        
			        }else{
			        	queues.processElement();
			        	start = System.currentTimeMillis();
			        	int size = SimpleQueueServiceSample.ResultQueue();
			        	if(size > 0)
			        		System.out.println("Result Queue received "+size);
			        
			        	//remote worker
			        	//String tasks = SimpleQueueServiceSample.show();
			        	 /*tasks =  tasks.substring(1, tasks.length()-1);
			             String[] que = tasks.split(",");
			            
			             start = System.currentTimeMillis();
			            System.out.println(start);
			           
			            ThreadPoolService.init(32);
			        	for(int k=0;k<que.length ; k++){
			        		String key = que[k].split("=")[0];
			        		int value = Integer.parseInt(que[k].split("=")[1]);
			        			HashMap<String, Integer> task =  new HashMap<String, Integer>();
			        			task.put(key, value);
			            		ThreadPoolService.insertTasK(task);
			        	}
			        	ThreadPoolService.stop();*/
			        }
		       }
	        
		} 
	    catch (Exception e) 
	    {
	        System.out.println(e);
	    }
		}
}
