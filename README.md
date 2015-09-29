Run rmiregistry command from shell

Running zookeeper standalone

1. Download and unzip zookeeper
2. Rename or copy /config/zoo-sample.cfg  to /config/zoo.cfg
4. Add following configurations to zoo.cfg
	tickTime=2000
	dataDir=/Installs/<zookeeper dir>/data
	clientPort=2887 # server listening port should be unique for each server.
	initLimit=5
	syncLimit=2
5. create a data folder as defined in dataDir configuration
6. open a cmd shell goto <zookeeper>\bin dir.
10. run zkServer.cmd 

Running Replicated ZooKeeper

1. Download and unzip zookeeper
2. Replicate zookeeper install folder e.g. 
	-zookeeper-3.4.6
	-zookeeper-3.4.6-copy1
	-zookeeper-3.4.6-copy2
3. Rename or copy /config/zoo-sample.cfg  to /config/zoo.cfg
4. Add following configurations to each zookeeper instances
	tickTime=2000
	dataDir=/Installs/<zookeeper dir>/data
	clientPort=2887 # server listening port should be unique for each server.
	initLimit=5
	syncLimit=2
	server.1=localhost:2887:3887
	server.2=localhost:2888:3888
	server.3=localhost:2889:3889
5. create a data folder as defined in dataDir configuration
6. create a file myid inside the data folder
7. open myid in text editor and enter 1 
	value in myid identifies the server instance as defined in the server.1, server.2, server.3 configs.
8. Repeat step 4 to 7 for each zookeeper instances.
9. open separate cmd shell for each instance and goto bin dir.
10. run zkServer.cmd in each shell
