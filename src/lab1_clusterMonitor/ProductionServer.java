package lab1_clusterMonitor;

import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import org.apache.zookeeper.*;
import org.apache.zookeeper.ZooDefs.Ids;

public class ProductionServer {
//    ServerSocket one for listening new requests. Socket for reading contents
	private Socket socket = null;
	private ServerSocket server = null;
	private DataInputStream in = null;
	private String clusterName = null;
	private ZooKeeper zk = null;
	
	public ProductionServer(int port, String clusterName) throws IOException {
		try {
			registerToClusterManagement(clusterName);
			server = new ServerSocket(port);
			System.out.println("Server Started");
			System.out.println("Waiting for a client ..."); 
			
			while(true) {
				socket = server.accept();
				System.out.println("Client accepted"); 
				
				new ClientHandler(socket).start();
		
			}
		}catch (Exception e) {
			// TODO: handle exception
			if(socket != null ) socket.close();
			if(server != null ) server.close();
			System.out.println("Server fails");
			e.printStackTrace();
		}
	}
	
	private void registerToClusterManagement(String clusterName) throws IOException, KeeperException, InterruptedException {
		this.clusterName = clusterName;
		
		//in real production, this configurations is read from file
		String connectString = "localhost:2181"; 
		
		zk = new ZooKeeper(connectString, 2000, null);
		
		String nodeId = ManagementFactory.getRuntimeMXBean().getName();
		
		zk.create("/" + this.clusterName + "/" + nodeId, nodeId.getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
	}
	
	public static void main(String[] args) throws NumberFormatException, IOException {
		String nodeId = ManagementFactory.getRuntimeMXBean().getName();
		System.out.println("production Server " + nodeId + " running at: " + args[0]);
		System.out.println("Server is registered with cluster name: " + args[1]);
		new ProductionServer(Integer.valueOf(args[0]), args[1]);
	}

}

class ClientHandler extends Thread {
	Socket s;
	DataInputStream dis;
	DataOutputStream dos;
	public ClientHandler(Socket s) {
		this.s = s;
		try {
			this.dis = new DataInputStream(s.getInputStream());
			this.dos = new DataOutputStream(s.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		String received;
		String toreturn;
		
//		dos.writeUTF("Give Name or Exit");
		while(true) {
			try {
				received = dis.readUTF();
				
				if(received.equalsIgnoreCase("Exit")) {
					dis.close();
					dos.close();
					s.close();
					break;
				}
				
				toreturn = "Welcome " + received;
				dos.writeUTF(toreturn);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				break;
			}
		}
	}
}
