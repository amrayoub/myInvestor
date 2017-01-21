kafka-topics.bat --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic myinvestor.exchange
kafka-topics.bat --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic myinvestor.stock

kafka-topics.bat --list --zookeeper localhost:2181