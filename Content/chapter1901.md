# 一 github相关资源收集

## Hadoop

* Apache Tez – 它是一个针对Hadoop数据处理应用程序的新分布式执行框架，该框架基于YARN；
* SpatialHadoop – SpatialHadoop是Apache Hadoop的MapReduce扩展，专门用于处理空间数据；
* GIS Tools for Hadoop –用于Hadoop框架的大数据空间分析；
* Elasticsearch Hadoop – Elasticsearch与Hadoop深度集成，可用于实时搜索和分析，支持Map/Reduce、  Cascading、Apache Hive和Apache Pig；
* dumbo - Python模块，使Hadoop程序的编写和运行更为容易；
* hadoopy – 用Cython写的Python MapReduce库；
* mrjob - mrjob是一个Python2.5+程序包，可以帮助编写和运行Hadoop工作流；
* pydoop -为Hadoop提供Python API的程序包；
* hdfs-du -Hadoop分布式文件系统（HDFS）的交互可视化；
* White Elephant - Hadoop的日志聚合器和仪表板；
* Genie - Genie提供REST-ful API，以便运行Hadoop、Hive和Pig jobs，还管理多个Hadoop资源，并在它们之间进行作业提交；
* Apache Kylin –最初来自eBay公司的开源分布式分析引擎，能提供Hadoop之上的SQL查询接口及多维分析（OLAP），以支持超大规模数据集；
* Crunch -基于Go的工具包，用于在Hadoop上的ETL和特征提取；
* Apache Ignite -分布式内存平台。

##YARN
* Apache Slider - Apache Slider是Apache软件基金会的孵化项目，旨在能够轻松地实现现有应用程序到YARN集群的部署；
* Apache Twill - Apache Twill是Apache Hadoop® YARN的抽象层，降低了开发分布式应用程序的复杂度，让开发者更专注于自己的应用逻辑；
* mpich2-yarn –在YARN上运行MPICH2。

##NoSQL
* Apache HBase - Apache HBase；
* Apache Phoenix – Hbase的SQL驱动，支持辅助索引；
* happybase -一个开发者友好型的Python库，用于Apache HBase的交互；
* Hannibal –用于监测和维护HBase 集群的工具；
* Haeinsa –用于HBase的线性可扩展多行多表交易库；
* hindex – Hbase的辅助索引；
* Apache Accumulo - Apache Accumulo可排序分布式键/值存储，是一个强大的、可扩展高性能数据存储和检索
* OpenTSDB -可扩展时间序列数据库；
* Apache Cassandra 

##Hadoop中的SQL
* Apache Hive
* Apache Phoenix - Hbase的SQL驱动，支持辅助索引；
* Pivotal HAWQ – Hadoop上的并行数据库；
* Lingual -用于级联的SQL接口（MR / TEZ工作发生器）；
* Cloudera Impala
* Presto –用于大数据的分布式SQL查询引擎，该查询引擎由Facebook开发，现已开源；
* Apache Tajo - Apache Hadoop的数据仓库系统；
* Apache Drill


##数据管理
* Apache Calcite -动态数据管理框架；
* Apache Atlas -用于元数据标记及类群捕获，支持复杂的商业数据分类。 

##工作流，生命周期及管理
* Apache Oozie - Apache Oozie；
* Azkaban
* Apache Falcon -数据管理与处理平台；
* Apache NiFi -数据流系统；
* AirFlow – AirFlow是以编程方式建立、调度和监控数据管道的平台；
* Luigi - Python包，用于构建批处理作业的复杂管道。

##数据提取及整合
* Apache Flume - Apache Flume；
* Suro - Netflix分布式数据管道；
* Apache Sqoop - Apache Sqoop；
  Apache Kafka - Apache Kafka；
  Gobblin from LinkedIn – Hadoop的通用数据提取框架；

##DSL
* Apache Pig - Apache Pig
* Apache DataFu – Hadoop中用于处理大规模数据的库的集合；
* vahara –基于Apache Pig的机器学习和自然语言处理；
* packetpig -用于开源大数据安全性分析；
* akela – Mozilla的实用工具库，用于Hadoop、HBase、Pig等等；
* seqpig -Hadoop中用于大型定序数据集的简单可扩展脚本（bioinfomation除外）；
* Lipstick – Pig工作流程可视化工具；A(pache)的Lipstick简介；
* PigPen - PigPen 是Clojure或分布式Clojure的Map-reduce，能够编译Apache Pig，但是不需要过多了解Pig也可以使用PigPen。

##库和工具
* Kite Software Development Kit –一组库、工具、示例和文档；
* gohadoop - Apache Hadoop YARN的本地Go客户端；
* Hue – 用Apache Hadoop分析数据的Web界面；
* Apache Zeppelin -基于Web的笔记，可进行交互式数据分析；
* Jumbune - Jumbune是为分析Hadoop集群和MapReduce作业而构建的开源产品；
* Apache Thrift
* Apache Avro - Apache Avro是一个数据序列化系统；
* Elephant Bird – Twitter中LZO、缓冲协议相关的Hadoop、Pig、Hive和HBase代码的集合；
* Spring for Apache Hadoop
* hdfs - A native go client for HDFS
* Oozie Eclipse Plugin -Eclipse中用于编辑Apache Oozie工作流的图形编辑器。

##实时数据处理
* Apache Storm
* Apache Samza
* Apache Spark
* Apache Flink - Apache Flink是高效的分布式通用数据处理的平台，用于精准的流处理。

##分布式计算和编程

### Apache Spark
* Spark Packages - Apache Spark中程序包的community（社区）索引；
* SparkHub - Apache Spark的社区；
* Apache Crunch
* Cascading - Cascading是在Hadoop上构建数据应用的成熟的应用开发平台；
 * Apache Flink - Apache Flink是高效的分布式通用数据处理的平台；
 * Apache Apex (incubating) -企业级的统一流处理和批处理引擎。 
  
### 包装，配置与监测

* Apache Bigtop - 用于Apache Hadoop生态系统的包装和测试；
* Apache Ambari - Apache Ambari
* Ganglia Monitoring System
* ankush -一个大数据集群管理工具，用于创建和管理不同的技术集群；
* Apache Zookeeper - Apache Zookeeper
* Apache Curator - 用于ZooKeeper的客户端简化包装和丰富ZooKeeper框架； 
* Buildoop - Hadoop生态系统生成器；
* Deploop - Hadoop的部署系统；
* Jumbune -一个用于开源MapReduce分析，MapReduce流程调试，HDFS数据质量校验和Hadoop集群监测的工具；
* inviso - Inviso是一个轻量级的工具，它提供搜索Hadoop作业，可视化性能，查看集群利用率的能力。

### 搜索

* ElasticSearch
* SenseiDB
* Apache Solr -开源、分布式、实时、半结构化的数据库；
* Banana - Apache Solr的Kibana端口。

### 搜索引擎框架=
* Apache Nutch –Apache Nutch是一个高度可扩展的，可伸缩的开源网络爬虫软件项目。

### 安全性

* Apache Ranger - Ranger是一个框架，能够跨Hadoop平台启用、监控和全面管理数据安全性；
* Apache Sentry - Hadoop的一个授权模块；
* Apache Knox Gateway –用于与Hadoop集群交互的REST API网关。

### 基准
* Big Data Benchmark
* HiBench
* Big-Bench
* hive-benchmarks
* hive-testbench –一个测试平台，用于进行任何规模数据的Apache Hive实验；
* YCSB -雅虎云服务基准（YCSB）是一个开源规范和程序套件，用于评估计算机程序的检索和维护功能；它常被用于比较NoSQL数据库管理系统的相对性能。

### 机器学习和大数据分析

* Apache Mahout
* Oryx 2 –基于Spark、Kafka的Lambda架构，用于实时大规模的机器学习；
* MLlib - MLlib是Apache Spark的可扩展机器学习库；
* R - R是用于统计计算和图形的自由软件环境；
* RHadoop -包括RHDFS、RHBase、RMR2和plyrmr；
* RHive –用于从R中开始Hive查询；
* Apache Lens

### Hive Plugins
```
  http://nexr.github.io/hive-udf/
  https://github.com/edwardcapriolo/hive_cassandra_udfs
  https://github.com/livingsocial/HiveSwarm
  https://github.com/ThinkBigAnalytics/Hive-Extensions-from-Think-Big-Analytics
  https://github.com/karthkk/udfs
  https://github.com/twitter/elephant-bird - Twitter
  https://github.com/lovelysystems/ls-hive
  https://github.com/stewi2/hive-udfs
  https://github.com/klout/brickhouse
  https://github.com/markgrover/hive-translate (PostgreSQL translate())
  https://github.com/deanwampler/HiveUDFs
  https://github.com/myui/hivemall (Machine Learning UDF/UDAF/UDTF)
  https://github.com/edwardcapriolo/hive-geoip (GeoIP UDF)
  https://github.com/Netflix/Surus
```

###Storage Handler
  
```
  https://github.com/dvasilen/Hive-Cassandra
  https://github.com/yc-huang/Hive-mongo
  https://github.com/balshor/gdata-storagehandler
  https://github.com/karthkk/hive-hbase-json
  https://github.com/sunsuk7tp/hive-hbase-integration
  https://bitbucket.org/rodrigopr/redisstoragehandler
  https://github.com/zhuguangbin/HiveJDBCStorageHanlder
  https://github.com/chimpler/hive-solr
  https://github.com/bfemiano/accumulo-hive-storage-manager
  https://github.com/rcongiu/Hive-JSON-Serde
  https://github.com/mochi/hive-json-serde
  https://github.com/ogrodnek/csv-serde
  https://github.com/parag/HiveJsonSerde
  https://github.com/johanoskarsson/hive-json-serde
  https://github.com/electrum/hive-serde - JSON
  https://github.com/karthkk/hive-hbase-json
```
  
### Libraries and tools
  
```
  https://github.com/forward3d/rbhive
  https://github.com/synctree/activerecord-hive-adapter
  https://github.com/hrp/sequel-hive-adapter
  https://github.com/forward/node-hive
  https://github.com/recruitcojp/WebHive
  shib - WebUI for query engines: Hive and Presto
  clive - Clojure library for interacting with Hive via Thrift
  https://github.com/anjuke/hwi
  https://code.google.com/a/apache-extras.org/p/hipy/
  https://github.com/dmorel/Thrift-API-HiveClient2 (Perl - HiveServer2)
  PyHive - Python interface to Hive and Presto
  https://github.com/recruitcojp/OdbcHive
```


