# ZookeeperUseCases

Coordinating tasks and processes in a large distributed system is a very tough problem when it comes to implementing them correctly in a fault-tolerant manner. Apache ZooKeeper, a project of the Apache Software Foundation, aims to solve these coordination problems in the design and development of distributed systems by providing a set of reliable primitives through simple APIs.
In this project using Zookeeper APIs, I implemented  various Common Distributed System Tasks:
  - Cluster monitoring
  - Leader election
  - Group membership
  - Service discovery
#### Cluster monitoring :
 - The aim of Cluster monitoring is to detect the failure of the production servers in real time, and accordingly notify the administrator.
 - Developed a minimalistic distributed cluster monitor model  using the ***ephemeral znode*** concept of ZooKeeper.
 - As a Demo, Production Server(ProductionServer.class) provides a service of  ***greeting msg : Welcome [Client Given Name]***. When any of the production server fails, instances of clusterMonitors (ClusterMonitor.class) get notified.

#### Leader election :
