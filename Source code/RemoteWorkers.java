import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;


public class RemoteWorkers {
	public static void ProcessTask(List<String> tasks) {
		// TODO Auto-generated method stub
		ThreadPoolService.init(5);

        Scheduler.start = System.currentTimeMillis();
        System.out.println(Scheduler.start);

	       
        Iterator<String> i = tasks.iterator();
				while(i.hasNext()){
					String d = i.next();
					String task = d.substring(1,d.length()-1 );
					String key = task.split("=")[0];
					int value = Integer.parseInt(task.split("=")[1]);
			   		System.out.println(key+ " : " + value);
			   			HashMap<String, Integer> t =  new HashMap<String, Integer>();
			   			t.put(key, value);
			       		ThreadPoolService.insertTasK(t);
				}
			
       ThreadPoolService.stop();
   		}
 
	public static void main(String args[]){
		
		/*
		 * gets the task from SQS and adds it into a list and then send the list to the process task to start thread pooling
		 * */
		System.out.println("print");
		int  nOfTaskToExecute = Integer.parseInt(args[0]);
		List<String> tasks = new ArrayList<String>();
		boolean flag = true;
		 while (flag) {
				String task = SimpleQueueServiceSample.executeRemoteWorkers(nOfTaskToExecute);
		    	System.out.println(task);
		    	if(!task.equals(""))
		    		tasks.add(task);
		    	else{
		    		 if(!tasks.isEmpty()){
		    			 flag = false;	
		    			 ProcessTask(tasks);
		    		 }
		    		 else{
		    			 System.out.println("trying after 2 min");
						try {
							Thread.currentThread().sleep(12000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
		    		 }
		    	}
		 }
	}
}
