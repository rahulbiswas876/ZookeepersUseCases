
import java.io.IOException;
import java.util.List;

import org.apache.zookeeper.*;

public class Hello {

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		System.out.println("HI");
		
		String connectString = "localhost:2181";
		ZooKeeper zk = null;
		try {
			zk = new ZooKeeper(connectString, 2000, null);
			if(zk != null) {
				List<String> children = zk.getChildren("/", false);
				for(String child: children) {
					System.out.println(child);
				}
			}
				
			
		} catch (IOException | KeeperException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(zk != null)
				zk.close();
		}

	}

}
