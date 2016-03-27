import java.util.HashMap;


public class LocalWorkers {
	public static Queue ProcessTask(Queue queues) {
		// call the thread pool service and send the Queue for execution of local workers.
		
    	while(!queues.isEmpty()){
    			HashMap<String, Integer> task =  queues.deQueue();
    			System.out.println("Task going into threadpool "+task);
        		ThreadPoolService.insertTasKInLW(task);
    	}
    	ThreadPoolService.stop();
    	return ThreadPoolService.resultQueue;
   		}
 
	
	
}
