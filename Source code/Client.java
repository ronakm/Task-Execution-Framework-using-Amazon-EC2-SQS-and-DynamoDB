import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client
{
	/*
	 * creates a connection with server and sents the file to the server and waits for the result queue from SQS.
	 * */
	public static void main(String[] args)
	{
		try
		{
			Socket ss;
	        ObjectOutputStream out;
	        ObjectInputStream in;
	        String fname,msg;
	        
	        BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
	        String hostname=args[0];
	        int portno=Integer.parseInt(args[1]);
	        ss = new Socket(hostname,portno);
	        out = new ObjectOutputStream(ss.getOutputStream());
	        in = new ObjectInputStream(ss.getInputStream());
	       
	        System.out.println("enter the job file name : ");
	        fname = (String)br.readLine();
	        int flag ;
			
	 	       
	        BufferedReader br_file = new BufferedReader(new FileReader(fname));
	        String line="" , data = "";
	        line = br_file.readLine();
	        while(line != null){
	       			        data = data + "%" + line;
	       			     line = br_file.readLine();
	        }
	        data = data +"%END";
	        out.writeUTF(data);
	        out.flush();
	        
	        System.out.println(data);
	        System.out.println("File Sent");
	        
	        flag = in.read();
	        
	        if(flag == 1){
			in.close();
	        out.close();
	        br.close();         
	        ss.close();
	        System.out.println("Ack received..");
	        System.out.println("Client end");
	        System.out.println("connection closed");
	        }
		} 
		catch (Exception ex) 
		{
			System.out.println(ex);
		}
	}
}
