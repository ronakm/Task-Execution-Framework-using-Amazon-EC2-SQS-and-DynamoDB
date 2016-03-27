import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class ThreadPoolService {
	public static Set<Callable<String>> callables = new HashSet<Callable<String>>();
	public static ExecutorService executorService;
	//public static ExecutorService executorService = Executors.newCachedThreadPool();
	public static  Queue resultQueue = new Queue();
	

	public static void insertTasK(final HashMap<String, Integer> task) {

		//insert a task to the executer service as a runnable task which will execute the sleep method and pushes the reult into result Queue
		
		String key = null;
		Integer value;
		HashMap<String, Integer> a = new HashMap<String, Integer>();
		for(Map.Entry<String, Integer> e : task.entrySet()){
			key = e.getKey();
			value = e.getValue();
		}
		System.out.println("Task submittied "+key);
		final String data = key;
		Future f = executorService.submit(new Runnable() {
			    public void run() {
			    	try{
						
			    		System.out.println(data);
			    		int time = Integer.parseInt(data.split(" ")[1]);
			    		System.out.println("thread sleeping for "+time+" ms");
			    		Thread.sleep(time);
			    		HashMap<String, Integer> a = new HashMap<String, Integer>();
			            a.put(data, 0);
			            
			    		resultQueue.insert(a);
			    		
			    		System.out.println("thread awake");
			    		}catch(InterruptedException e){
			    			HashMap<String, Integer> a = new HashMap<String, Integer>();
			    			a.put(data, 1);
			    			resultQueue.insert(a);
			    			e.printStackTrace();
			    		}
			    }
		});
		try {
			if(f.get() == null)
			resultQueue.pushIntoSQS();	
						
				resultQueue.display();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ExecutionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
			
				
	}



	public static void stop() {
		// TODO Auto-generated method stub
		executorService.shutdown();
		System.out.println((System.currentTimeMillis() - Scheduler.start)/1000);
		System.out.println("Executore Stooped");
		
	}



	public static void init(int n) {
		// TODO Auto-generated method stub
		executorService = Executors.newFixedThreadPool(n);
		System.out.println("Executor initiaited");
	}



	public static void insertTasKInLW(HashMap<String, Integer> t) {
		// /insert a task to the executer service as a runnable task which will execute the sleep method and pushes the result into result Queue
		
		String key = null;
		Integer value;
		HashMap<String, Integer> a = new HashMap<String, Integer>();
		for(Map.Entry<String, Integer> e : t.entrySet()){
			key = e.getKey();
			value = e.getValue();
		}
		System.out.println("Task submittied "+key);
		final String data = key;
		Future f = executorService.submit(new Runnable() {
			    public void run() {
			    	try{
						
			    		System.out.println(data);
			    		int time = Integer.parseInt(data.split(" ")[1]);
			    		System.out.println("thread sleeping for "+time+" ms");
			    		Thread.sleep(time);
			    		HashMap<String, Integer> a = new HashMap<String, Integer>();
			            a.put(data, 0);
			            
			    		resultQueue.insert(a);
			    		
			    		System.out.println("thread awake");
			    		}catch(InterruptedException e){
			    			HashMap<String, Integer> a = new HashMap<String, Integer>();
			    			a.put(data, 1);
			    			resultQueue.insert(a);
			    			e.printStackTrace();
			    		}
			    }
		});
				resultQueue.display();
		
			
	}
}
