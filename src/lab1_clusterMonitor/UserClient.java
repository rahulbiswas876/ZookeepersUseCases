package lab1_clusterMonitor;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class UserClient {
	public static void main(String[] args) {
		try {
			Scanner scn = new Scanner(System.in); 
			
			InetAddress ip = InetAddress.getByName("localhost"); 
			Socket client = new Socket(ip, 2549);
			
			DataInputStream dis = new DataInputStream(client.getInputStream()); 
            DataOutputStream dos = new DataOutputStream(client.getOutputStream()); 
            
            System.out.println("Give Name or Exit");
            
            while(true) {
            	String tosend = scn.nextLine(); 
                dos.writeUTF(tosend); 
                System.out.println(dis.readUTF() + "\n");
                
                if(tosend.equals("Exit")) {
                	if(client != null) client.close();
                	if(dis != null ) dis.close();
                	if(dos != null ) dos.close();
                	break;
                }
            }

			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
