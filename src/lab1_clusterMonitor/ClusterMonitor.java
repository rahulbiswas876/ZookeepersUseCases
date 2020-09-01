package lab1_clusterMonitor;

import java.io.IOException;
import java.util.List;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;

//This class instances create cluster if not exists, register themselves as watcher and keep monitoring.
//Under the hood, instances of this class register with zookeeper service and any change in the registered
//member ephemeral znode trigger call backs to this watchers.
public class ClusterMonitor implements Runnable {
	
	private static String clusterRoot;
	private ZooKeeper zk = null;
	private Watcher childrenWatcher;
	private Watcher connectionWatcher;
	public ClusterMonitor(String clusterRoot) throws IOException, KeeperException, InterruptedException {
		this.clusterRoot = clusterRoot;
		
		connectionWatcher = new Watcher() {
			
			@Override
			public void process(WatchedEvent event) {
				// TODO Auto-generated method stub
				if(event.getType() == Watcher.Event.EventType.None && 
						event.getState() == Watcher.Event.KeeperState.SyncConnected) {
					System.out.println("Membership Changed");
				}
			}
		};
		
		//in real production, this configurations is read from file
		String connectString = "localhost:2181"; 		
		zk  = new ZooKeeper(connectString, 2000, null);
		
		if(zk.exists(clusterRoot, false) == null) {
			zk.create(clusterRoot, "ClusterMonitor".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		}
		
		childrenWatcher = new Watcher() {
			
			@Override
			public void process(WatchedEvent event) {
				// TODO Auto-generated method stub
				if(event.getType() == Watcher.Event.EventType.NodeChildrenChanged) {
					try {
						List<String> children = zk.getChildren(clusterRoot, this);
						System.out.println("Members: " + children);
					} catch (KeeperException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}
		};
		
		List<String> children = zk.getChildren(clusterRoot, childrenWatcher);
		System.out.println("Members: " + children);

	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			new ClusterMonitor("/" + args[0]).run();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (KeeperException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void run() {
	    try {
	        synchronized (this) {
	            while (true) {
	                wait();
	            }
	        }
	    } catch (InterruptedException e) {
	    }
	}

}
