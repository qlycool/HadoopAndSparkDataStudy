# 第十九章 Hadoop相关资源

##Hadoop查询引擎
一、Phoenix

贡献者：：Salesforce

简介：这是一个Java中间层，可以让开发者在Apache HBase上执行SQL查询。Phoenix完全使用Java编写，代码位于GitHub上，并且提供了一个客户端可嵌入的JDBC驱动。

Phoenix查询引擎会将SQL查询转换为一个或多个HBase scan，并编排执行以生成标准的JDBC结果集。直接使用HBase API、协同处理器与自定义过滤器，对于简单查询来说，其性能量级是毫秒，对于百万级别的行数来说，其性能量级是秒。

Phoenix最值得关注的一些特性有：

❶嵌入式的JDBC驱动，实现了大部分的java.sql接口，包括元数据API
❷可以通过多部行键或是键/值单元对列进行建模
❸完善的查询支持，可以使用多个谓词以及优化的扫描键
❹DDL支持：通过CREATE TABLE、DROP TABLE及ALTER TABLE来添加/删除列
❺版本化的模式仓库：当写入数据时，快照查询会使用恰当的模式
❻DML支持：用于逐行插入的UPSERT VALUES、用于相同或不同表之间大量数据传输的UPSERT ❼SELECT、用于删除行的DELETE
❽通过客户端的批处理实现的有限的事务支持
❾单表——还没有连接，同时二级索引也在开发当中
➓紧跟ANSI SQL标准


二、Stinger

贡献者：：Hortonworks

简介：原叫Tez，下一代Hive,Hortonworks主导开发，运行在YARN上的DAG计算框架。

某些测试下，Stinger能提升10倍左右的性能，同时会让Hive支持更多的SQL，其主要优点包括：

❶让用户在Hadoop获得更多的查询匹配。其中包括类似OVER的字句分析功能，支持WHERE查询，让Hive的样式系统更符合SQL模型。

❷优化了Hive请求执行计划，优化后请求时间减少90%。改动了Hive执行引擎，增加单Hive任务的被秒处理记录数。

❸在Hive社区中引入了新的列式文件格式（如ORC文件），提供一种更现代、高效和高性能的方式来储存Hive数据。

❹引入了新的运行时框架——Tez，旨在消除Hive的延时和吞吐量限制。Tez通过消除不必要的task、障碍同步和对HDFS的读写作业来优化Hive job。这将优化Hadoop内部的执行链，彻底加速Hive负载处理。

Stinger官方网站>>>

三、Presto

贡献者：：Facebook

简介：Facebook开源的数据查询引擎Presto ，可对250PB以上的数据进行快速地交互式分析。该项目始于 2012 年秋季开始开发，目前该项目已经在超过 1000 名 Facebook 雇员中使用，运行超过 30000 个查询，每日数据在 1PB 级别。Facebook 称 Presto 的性能比诸如 Hive 和 Map*Reduce 要好上 10 倍有多。

Presto 当前支持 ANSI SQL 的大多数特效，包括联合查询、左右联接、子查询以及一些聚合和计算函数；支持近似截然不同的计数(DISTINCT COUNT)等。


四、Shark

简介：Shark即Hive on Spark，本质上是通过Hive的HQL解析，把HQL翻译成Spark上的RDD操作，然后通过Hive的metadata获取数据库里的表信息，实际HDFS上的数据和文件，会由Shark获取并放到Spark上运算。Shark的特点就是快，完全兼容Hive，且可以在shell模式下使用rdd2sql()这样的API，把HQL得到的结果集，继续在scala环境下运算，支持自己编写简单的机器学习或简单分析处理函数，对HQL结果进一步分析计算。

❶Shark速度快的原因除了Spark平台提供的基于内存迭代计算外，在设计上还存在对Spark上进行了一定的改造，主要有

❷partial DAG execution：对join优化，调节并行粒度，因为Spark本身的宽依赖和窄依赖会影响并行计算和速度

基于列的压缩和存储：把HQL表数据按列存，每列是一个array，存在JVM上，避免了JVM GC低效，而压缩和解压相关的技术是Yahoo!提供的。

结来说，Shark是一个插件式的东西，在我现有的Spark和Hive及hadoop-client之间，在这两套都可用的情况下，Shark只要获取Hive的配置（还有metastore和exec等关键包），Spark的路径，Shark就能利用Hive和Spark，把HQL解析成RDD的转换，把数据取到Spark上运算和分析。在SQL on Hadoop这块，Shark有别于Impala，Stringer，而这些系统各有自己的设计思路，相对于对MR进行优化和改进的思路，Shark的思路更加简单明了些。

Shark官方网站>>>

五、Pig

简介：Pig是一种编程语言，它简化了Hadoop常见的工作任务。Pig可加载数据、表达转换数据以及存储最终结果。Pig内置的操作使得半结构化数据变得有意义（如日志文件）。同时Pig可扩展使用Java中添加的自定义数据类型并支持数据转换。

Pig最大的作用就是对mapreduce算法(框架)实现了一套shell脚本 ，类似我们通常熟悉的SQL语句，在Pig中称之为Pig Latin，在这套脚本中我们可以对加载出来的数据进行排序、过滤、求和、分组(group by)、关联(Joining)，Pig也可以由用户自定义一些函数对数据集进行操作，也就是传说中的UDF(user-defined functions)。

Pig官方网站>>>

六、Cloudera Impala

贡献者：:Cloudera

简介：Cloudera Impala 可以直接为存储在HDFS或HBase中的Hadoop数据提供快速，交互式的SQL查询。除了使用相同的存储平台外， Impala和Apache Hive一样也使用了相同的元数据，SQL语法（Hive SQL），ODBC驱动和用户接口（Hue Beeswax），这就很方便的为用户提供了一个相似并且统一的平台来进行批量或实时查询。

Cloudera Impala 是用来进行大数据查询的补充工具。 Impala 并没有取代像Hive这样基于MapReduce的分布式处理框架。Hive和其它基于MapReduce的计算框架非常适合长时间运行的批处理作业，例如那些涉及到批量 Extract、Transform、Load ，即需要进行ETL作业。

Impala 提供了：

❶数据科学家或数据分析师已经熟知的SQL接口

❷能够在Apache Hadoop 的大数据中进行交互式数据查询

❸ Single system for big data processing and analytics so customers can avoid costly modeling and ETL just for analytics

Cloudera Impala官方网站>>>

七、Apache Drill

贡献者：：MapR

简介：Apache Drill是是一个能够对大数据进行交互分析、开源的分布式系统，且基于Google Dremel实现，它能够运行在上千个节点的服务器集群上，且能在几秒内处理PB级或者万亿条的数据记录。Drill能够帮助企业用户快速、高效地进行Hadoop数据查询和企业级大数据分析。Drill于2012年8月份由Apache推出。

从Drill官方对其架构的介绍中得知，其具有适于实时的分析和快速的应用开发、适于半结构化/嵌套数据的分析、兼容现有的SQL环境和Apache Hive等特征。另外，Drill的核心模块是Drillbit服务，该服务模块包括远程访问子模块、SQL解析器、查询优化器、任务计划执行引擎、存储插件接口（DFS、HBase、Hive等的接口）、分布式缓存模块等几部分，如下图所示：

36大数据

Apache Drill官方网站>>>

八、Apache Tajo

简介：Apache Tajo项目的目的是在HDFS之上构建一个先进的数据仓库系统。Tajo将自己标榜为一个“大数据仓库”，但是它好像和之前介绍的那些低延迟查询引擎类似。虽然它支持外部表和Hive数据集（通过HCatalog），但是它的重点是数据管理，提供低延迟的数据访问，以及为更传统的ETL提供工具。它也需要在数据节点上部署Tajo特定的工作进程。

Tajo的功能包括：

❶ANSI SQL兼容
❷JDBC 驱动
❸集成Hive metastore能够访问Hive数据集
❹一个命令行客户端
❺一个自定义函数API

Apache Tajo官方网站>>>

九、Hive

简介：hive是基于Hadoop的一个数据仓库工具，可以将结构化的数据文件映射为一张数据库表，并提供简单的sql查询功能，可以将sql语句转换为MapReduce任务进行运行。 其优点是学习成本低，可以通过类SQL语句快速实现简单的MapReduce统计，不必开发专门的MapReduce应用，十分适合数据仓库的统计分析。

Hive官方网站>>>

##流式计算
一、Facebook Puma

贡献者：Facebook

简介：实时数据流分析

二、Twitter Rainbird

贡献者：Twitter

简介：Rainbird一款基于Zookeeper, Cassandra, Scribe, Thrift的分布式实时统计系统，这些基础组件的基本功能如下：

❶ Zookeeper，Hadoop子项目中的一款分布式协调系统，用于控制分布式系统中各个组件中的一致性。

❷Cassandra，NoSQL中一款非常出色的产品，集合了Dynamo和Bigtable特性的分布式存储系统，用于存储需要进行统计的数据，统计数据，并且提供客户端进行统计数据的查询。（需要使用分布式Counter补丁CASSANDRA-1072）

❸ Scribe，Facebook开源的一款分布式日志收集系统，用于在系统中将各个需要统计的数据源收集到Cassandra中。

❹ Thrift，Facebook开源的一款跨语言C/S网络通信框架，开发人员基于这个框架可以轻易地开发C/S应用。

用处

Rainbird可以用于实时数据的统计：

❶统计网站中每一个页面，域名的点击次数

❷内部系统的运行监控（统计被监控服务器的运行状态）

❸记录最大值和最小值


三、Yahoo S4

贡献者：Yahoo

简介：S4（Simple Scalable Streaming System）最初是Yahoo!为提高搜索广告有效点击率的问题而开发的一个平台，通过统计分析用户对广告的点击率，排除相关度低的广告，提升点击率。目前该项目刚启动不久，所以也可以理解为是他们提出的一个分布式流计算（Distributed Stream Computing）的模型。

S4的设计目标是：

·提供一种简单的编程接口来处理数据流

·设计一个可以在普通硬件之上可扩展的高可用集群。

·通过在每个处理节点使用本地内存，避免磁盘I/O瓶颈达到最小化延迟

·使用一个去中心的，对等架构；所有节点提供相同的功能和职责。没有担负特殊责任的中心节点。这大大简化了部署和维护。

·使用可插拔的架构，使设计尽可能的即通用又可定制化。

·友好的设计理念，易于编程，具有灵活的弹性

Yahoo S4官方网站>>>

四、Twitter Storm

贡献者：Twitter

简介：Storm是Twitter开源的一个类似于Hadoop的实时数据处理框架，它原来是由BackType开发，后BackType被Twitter收购，将Storm作为Twitter的实时数据分析系统。

实时数据处理的应用场景很广泛，例如商品推荐，广告投放，它能根据当前情景上下文（用户偏好，地理位置，已发生的查询和点击等）来估计用户点击的可能性并实时做出调整。

storm的三大作用领域：

1.信息流处理（Stream Processing）

Storm可以用来实时处理新数据和更新数据库，兼具容错性和可扩展性,它 可以用来处理源源不断的消息，并将处理之后的结果保存到持久化介质中。

2.连续计算（Continuous Computation）

Storm可以进行连续查询并把结果即时反馈给客户，比如将Twitter上的热门话题发送到客户端。

3.分布式远程过程调用（Distributed RPC）

除此之外，Storm也被广泛用于以下方面：

精确的广告推送
实时日志的处理
Twitter Storm官方网站>>>



##离线计算
一、Hadoop MapReduce

简介：MapReduce是一种编程模型，用于大规模数据集（大于1TB）的并行运算。概念”Map（映射）”和”Reduce（归约）”，和它们的主要思想，都是从函数式编程语言里借来的，还有从矢量编程语言里借来的特性。它极大地方便了编程人员在不会分布式并行编程的情况下，将自己的程序运行在分布式系统上。 当前的软件实现是指定一个Map（映射）函数，用来把一组键值对映射成一组新的键值对，指定并发的Reduce（归约）函数，用来保证所有映射的键值对中的每一个共享相同的键组。

Hadoop MapReduce官方网站>>>

二、Berkeley Spark

简介：Spark是UC Berkeley AMP lab所开源的类Hadoop MapReduce的通用的并行，Spark，拥有Hadoop MapReduce所具有的优点；但不同于MapReduce的是Job中间输出结果可以保存在内存中，从而不再需要读写HDFS，因此Spark能更好地适用于数据挖掘与机器学习等需要迭代的map reduce的算法。

三、DataTorrent

简介：DataTorrent基于Hadoop 2.x构建，是一个实时的、有容错能力的数据流式处理和分析平台，它使用本地Hadoop应用程序，而这些应用程序可以与执行其它任务，如批处理，的应用程序共存。该平台的架构如下图所示：

DataTorrent

相关文章：DataTorrent 1.0每秒处理超过10亿个实时事件

DataTorrent 将数据分析速度从“实时”提升至“现在时”

##键值存储
一、LevelDB

LevelDB

贡献者：Google

简介：Leveldb是一个google实现的非常高效的kv数据库，目前的版本1.2能够支持billion级别的数据量了。 在这个数量级别下还有着非常高的性能，主要归功于它的良好的设计。特别是LMS算法。

LevelDB 是单进程的服务，性能非常之高，在一台4核Q6600的CPU机器上，每秒钟写数据超过40w，而随机读的性能每秒钟超过10w。

此处随机读是完全命中内存的速度，如果是不命中 速度大大下降。

LevelDB官方网站>>>

二、RocksDB

贡献者：facebook

简介：RocksDB虽然在代码层面上是在LevelDB原有的代码上进行开发的，但却借鉴了Apache HBase的一些好的idea。在云计算横行的年代，开口不离Hadoop，RocksDB也开始支持HDFS，允许从HDFS读取数据。RocksDB支持一次获取多个K-V，还支持Key范围查找。LevelDB只能获取单个Key。

RocksDB除了简单的Put、Delete操作，还提供了一个Merge操作，说是为了对多个Put操作进行合并。

RocksDB提供一些方便的工具，这些工具包含解析sst文件中的K-V记录、解析MANIFEST文件的内容等。RocksDB支持多线程合并，而LevelDB是单线程合并的。

RocksDB官方网站>>>

三、HyperDex

贡献者：Facebook

 HyperDex 

HyperDex是一个分布式、可搜索的键值存储系统，特性如下：

分布式KV存储，系统性能能够随节点数目线性扩展
吞吐和延时都能秒杀现在风头正劲的MonogDB，吞吐甚至强于Redis
使用了hyperspace hashing技术，使得对存储的K-V的任意属性进行查询成为可能
官网：http://hyperdex.org/

四、TokyoCabinet

大数据

日本人Mikio Hirabayashi（平林干雄）开发的一款DBM数据库。Tokyo Cabinet 是一个DBM的实现。这里的数据库由一系列key-value对的记录构成。key和value都可以是任意长度的字节序列,既可以是二进制也可以是字符串。这里没有数据类型和数据表的概念。
当 做为Hash表数据库使用时，每个key必须是不同的,因此无法存储两个key相同的值。提供了以下访问方法:提供key,value参数来存储，按 key删除记录，按key来读取记录，另外，遍历key也被支持，虽然顺序是任意的不能被保证。这些方法跟Unix标准的DBM,例如GDBM,NDBM 等等是相同的，但是比它们的性能要好得多（因此可以替代它们) 。下一代KV存储系统，支持strings、integers、floats、lists、maps和sets等丰富的数据类型。
TokyoCabinet官方网站>>>
五、Voldemort
大数据
Voldemort是一个分布式键值存储系统，是Amazon’s Dynamo的一个开源克隆。特性如下：
支持自动复制数据到多个服务器上。
支持数据自动分割所以每个服务器只包含总数据的一个子集。
提供服务器故障透明处理功能。
支持可拨插的序化支持，以实现复杂的键-值存储，它能够很好的5.集成常用的序化框架如：Protocol Buffers、Thrift、Avro和Java Serialization。
数据项都被标识版本能够在发生故障时尽量保持数据的完整性而不会影响系统的可用性。
每个节点相互独立，互不影响。
支持可插拔的数据放置策略
官网：http://project-voldemort.com/

六、Amazon Dynamo
贡献者：亚马逊
简介：Amazon Dynamo 是一个经典的分布式Key-Value 存储系统，具备去中心化，高可用性，高扩展性的特点，但是为了达到这个目标在很多场景中牺牲了一致性。Dynamo在Amazon中得到了成功的应用，能够跨数据中心部署于上万个结点上提供服务，它的设计思想也被后续的许多分布式系统借鉴。如近来火热的Cassandra，实际上就是基本照搬了Dynamo的P2P架构，同时融合了BigTable的数据模型及存储算法。
Amazon Dynamo官方网站>>>
七、Tair
贡献者：淘宝
简介：tair 是淘宝自己开发的一个分布式 key/value 存储引擎. tair 分为持久化和非持久化两种使用方式. 非持久化的 tair 可以看成是一个分布式缓存. 持久化的 tair 将数据存放于磁盘中. 为了解决磁盘损坏导致数据丢失, tair 可以配置数据的备份数目, tair 自动将一份数据的不同备份放到不同的主机上, 当有主机发生异常, 无法正常提供服务的时候, 其于的备份会继续提供服务.tair 的总体结构
Tairtair 作为一个分布式系统, 是由一个中心控制节点和一系列的服务节点组成. 我们称中心控制节点为config server. 服务节点是data server. config server 负责管理所有的data server, 维护data server的状态信息. data server 对外提供各种数据服务, 并以心跳的形式将自身状况汇报给config server. config server是控制点, 而且是单点, 目前采用一主一备的形式来保证其可靠性. 所有的 data server 地位都是等价的.
八、Apache Accumulo
Apache Accumulo
Apache Accumulo 是一个可靠的、可伸缩的、高性能的排序分布式的 Key-Value 存储解决方案，基于单元访问控制以及可定制的服务器端处理。Accumulo使用 Google BigTable 设计思路，基于 Apache Hadoop、Zookeeper 和 Thrift 构建。
 
官网：http://accumulo.apache.org/
九、Redis

Redis

Redis是一个高性能的key-value存储系统，和Memcached类似，它支持存储的value类型相对更多，包括string（字符串）、list（链表）、set（集合）和zset（有序集合）。与memcached一样，为了保证效率，数据都是缓存在内存中，区别的是Redis会周期性的把更新的数据写入磁盘或者把修改操作写入追加的记录文件，并且在此基础上实现了主从同步。

Redis的出现，很大程度补偿了memcached这类key/value存储的不足，在部分场合可以对关系数据库起到很好的补充作用。它提供了Python、Ruby、Erlang、PHP客户端，使用很方便。

官网：http://redis.io/

 

##表格存储
 一、OceanBase

贡献者：阿里巴巴

相关文章：26页PPT解密支撑支付宝交易的分布式数据库系统——OceanBase

简介：OceanBase是一个支持海量数据的高性能分布式数据库系统，实现了数千亿条记录、数百TB数据上的跨行跨表事务，由淘宝核心系统研发部、运维、DBA、广告、应用研发等部门共同完成。在设计和实现OceanBase的时候暂时摒弃了不紧急的DBMS的功能，例如临时表，视图(view)，研发团队把有限的资源集中到关键点上，当前 OceanBase主要解决数据更新一致性、高性能的跨表读事务、范围查询、join、数据全量及增量dump、批量数据导入。

目前OceanBase已经应用于淘宝收藏夹，用于存储淘宝用户收藏条目和具体的商品、店铺信息，每天支持4～5千万的更新操作。等待上线的应用还包括CTU、SNS等，每天更新超过20亿，更新数据量超过2.5TB，并会逐步在淘宝内部推广。

OceanBase 0.3.1在Github开源，开源版本为Revision:12336。

官网：http://alibaba.github.io/oceanbase/

二、Amazon SimpleDB

贡献者：亚马逊

Amazon SimpleDB是一个分散式数据库，以Erlang撰写。同与Amazon EC2和亚马逊的S3一样作为一项Web 服务，属于亚马逊网络服务的一部分。

Amazon SimpleDB

正如EC2和S3，SimpleDB的按照存储量，在互联网上的传输量和吞吐量收取费用。 在2008年12月1日，亚马逊推出了新的定价策略，提供了免费1 GB的数据和25机器小时的自由层(Free Tire)。 将其中的数据转移到其他亚马逊网络服务是免费的。

它是一个可大规模伸缩、用 Erlang 编写的高可用数据存储。

官网：http://aws.amazon.com/cn/simpledb/

三、Vertica

贡献者：惠普

简介：惠普2011年2月份起始3月21号完成收购Vertica。Vertica基于列存储。基于列存储的设计相比传统面向行存储的数据库具有巨大的优势。同时Vertica支持MPP（massively parallel processing）等技术，查询数据时Vertica只需取得需要的列，而不是被选择行的所有数据，其平均性能可提高50x-1000x倍。（查询性能高速度快）

Vertica的设计者多次表示他们的产品围绕着高性能和高可用性设计。由于对MPP技术的支持，可提供对粒度，可伸缩性和可用性的优势。每个节点完全独立运作，完全无共享架构，降低对共享资源的系统竞争。

Vertica的数据库使用标准的SQL查询，同时Vertica的架构非常适合云计算，包括虚拟化，分布式多节点运行等，并且可以和Hadoop/MapReduce进行集成。

Vertica官网：http://www.vertica.com/

四、Cassandra

贡献者：facebook

相关文章：开源分布式NoSQL数据库系统——Cassandra   Cassandra与HBase的大数据对决 谁是胜者？

简介：Cassandra是一套开源分布式NoSQL数据库系统。它最初由Facebook开发，用于储存收件箱等简单格式数据，集GoogleBigTable的数据模型与Amazon Dynamo的完全分布式的架构于一身Facebook于2008将 Cassandra 开源，此后，由于Cassandra良好的可扩放性，被Digg、Twitter等知名Web 2.0网站所采纳，成为了一种流行的分布式结构化数据存储方案。

Cassandra

Cassandra是一个混合型的非关系的数据库，类似于Google的BigTable。其主要功能比Dynamo （分布式的Key-Value存储系统）更丰富，但支持度却不如文档存储MongoDB（介于关系数据库和非关系数据库之间的开源产品，是非关系数据库当中功能最丰富，最像关系数据库的。支持的数据结构非常松散，是类似json的bjson格式，因此可以存储比较复杂的数据类型）。Cassandra最初由Facebook开发，后转变成了开源项目。它是一个网络社交云计算方面理想的数据库。以Amazon专有的完全分布式的Dynamo为基础，结合了Google BigTable基于列族（Column Family）的数据模型。P2P去中心化的存储。很多方面都可以称之为Dynamo 2.0。

Cassandra官网：http://cassandra.apache.org/

 五、HyperTable

Hypertable

简介：Hypertable是一个开源、高性能、可伸缩的数据库，它采用与Google的Bigtable相似的模型。在过去数年中，Google为在PC集群 上运行的可伸缩计算基础设施设计建造了三个关键部分。

Hypertable

第一个关键的基础设施是Google File System（GFS），这是一个高可用的文件系统，提供了一个全局的命名空间。它通过跨机器（和跨机架）的文件数据复制来达到高可用性，并因此免受传统 文件存储系统无法避免的许多失败的影响，比如电源、内存和网络端口等失败。第二个基础设施是名为Map-Reduce的计算框架，它与GFS紧密协作，帮 助处理收集到的海量数据。第三个基础设施是Bigtable，它是传统数据库的替代。Bigtable让你可以通过一些主键来组织海量数据，并实现高效的 查询。Hypertable是Bigtable的一个开源实现，并且根据我们的想法进行了一些改进。

HyperTable官网：http://hypertable.org/

六、FoundationDB

简介：支持ACID事务处理的NoSQL数据库，提供非常好的性能、数据一致性和操作弹性。

2015年1月2日，FoundationDB已经发布了其key-value数据库的3.0版本，主要专注于可伸缩性和性能上的改善。FoundationDB的CEO David Rosenthal在一篇博客上宣布了新的版本，其中展示了FoundationDB 3.0在可伸缩性方面的数据，它可以在一个32位的c3.8xlarge EC2实例上每秒写入1440万次；这在性能上是之前版本的36倍。

除了性能和可伸缩性的改善之外，FoundationDB 3.0还包含了对监控支持的改善。这种监控机制不仅仅是简单的机器检查，它添加了对多种潜在的硬件瓶颈的诊断，并且把那些高层级的信息整合到现有监控基础架构中。

官网：https://foundationdb.com/

七：HBase

贡献者： Fay Chang 所撰写的“Bigtable

HBase

简介：HBase是一个分布式的、面向列的开源数据库，该技术来源于 Fay Chang 所撰写的Google论文“Bigtable：一个结构化数据的分布式存储系统”。就像Bigtable利用了Google文件系统（File System）所提供的分布式数据存储一样，HBase在Hadoop之上提供了类似于Bigtable的能力。HBase是Apache的Hadoop项目的子项目。HBase不同于一般的关系数据库，它是一个适合于非结构化数据存储的数据库。另一个不同的是HBase基于列的而不是基于行的模式。

官网：http://hbase.apache.org/

##文件存储
一、CouchDB

简介：CouchDB是用Erlang开发的面向文档的数据库系统，最近刚刚发布了1.0版本（2010年7月14日）。CouchDB不是一个传统的关系数据库，而是面向文档的数据库，其数据存储方式有点类似lucene的index文件格式，CouchDB最大的意义在于它是一个面向web应用的新一代存储系统，事实上，CouchDB的口号就是：下一代的Web应用存储系统。

CouchDB

特点：

一、CouchDB是分布式的数据库，他可以把存储系统分布到n台物理的节点上面，并且很好的协调和同步节点之间的数据读写一致性。这当然也得靠Erlang无与伦比的并发特性才能做到。对于基于web的大规模应用文档应用，分布式可以让它不必像传统的关系数据库那样分库拆表，在应用代码层进行大量的改动。

二、CouchDB是面向文档的数据库，存储半结构化的数据，比较类似lucene的index结构，特别适合存储文档，因此很适合CMS，电话本，地址本等应用，在这些应用场合，文档数据库要比关系数据库更加方便，性能更好。

三、CouchDB支持REST API，可以让用户使用JavaScript来操作CouchDB数据库，也可以用JavaScript编写查询语句，我们可以想像一下，用AJAX技术结合CouchDB开发出来的CMS系统会是多么的简单和方便。

其实CouchDB只是Erlang应用的冰山一角，在最近几年，基于Erlang的应用也得到的蓬勃的发展，特别是在基于web的大规模，分布式应用领域，几乎都是Erlang的优势项目。

官网：http://couchdb.apache.org/

二、MongoDB

简介：MongoDB 是一个基于分布式文件存储的数据库。由C++语言编写。旨在为WEB应用提供可扩展的高性能数据存储解决方案。

MongoDB是一个介于关系数据库和非关系数据库之间的产品，是非关系数据库当中功能最丰富，最像关系数据库的。他支持的数据结构非常松散，是类似json的bson格式，因此可以存储比较复杂的数据类型。Mongo最大的特点是他支持的查询语言非常强大，其语法有点类似于面向对象的查询语言，几乎可以实现类似关系数据库单表查询的绝大部分功能，而且还支持对数据建立索引。

相关文章：MongoDB的基本特性与内部构造  大数据吃香 创业公司MongoDB估值达16亿美元

mongodb

特点

它的特点是高性能、易部署、易使用，存储数据非常方便。主要功能特性有：

*面向集合存储，易存储对象类型的数据。

mongodb集群参考

mongodb集群参考

*模式自由。

*支持动态查询。

*支持完全索引，包含内部对象。

*支持查询。

*支持复制和故障恢复。

*使用高效的二进制数据存储，包括大型对象（如视频等）。

*自动处理碎片，以支持云计算层次的扩展性。

*支持RUBY，PYTHON，JAVA，C++，PHP，C#等多种语言。

*文件存储格式为BSON（一种JSON的扩展）。

*可通过网络访问。

官网：https://www.mongodb.org/

三、Tachyon

贡献者：Haoyuan Li（李浩源）

李浩源

简介：Tachyon是一个分布式内存文件系统，可以在集群里以访问内存的速度来访问存在tachyon里的文件。把Tachyon是架构在最底层的分布式文件存储和上层的各种计算框架之间的一种中间件。主要职责是将那些不需要落地到DFS里的文件，落地到分布式内存文件系统中，来达到共享内存，从而提高效率。同时可以减少内存冗余，GC时间等。

Tachyon

Tachyon架构

Tachyon的架构是传统的Master—slave架构，这里和Hadoop类似，TachyonMaster里WorkflowManager是 Master进程，因为是为了防止单点问题，通过Zookeeper做了HA，可以部署多台Standby Master。Slave是由Worker Daemon和Ramdisk构成。这里个人理解只有Worker Daemon是基于JVM的，Ramdisk是一个off heap memory。Master和Worker直接的通讯协议是Thrift。

下图来自Tachyon的作者Haoyuan Li：

Tachyon

下载地址：https://github.com/amplab/tachyon

四、KFS

简介：GFS的C++开源版本，Kosmos distributed file system (KFS)是一个专门为数据密集型应用（搜索引擎，数据挖掘等）而设计的存储系统，类似于Google的GFS和Hadoop的HDFS分布式文件系统。 KFS使用C++实现，支持的客户端包括C++，Java和Python。KFS系统由三部分组成，分别是metaserver、chunkserver和client library。

官网：http://code.google.com/p/kosmosfs/

五、HDFS

大数据

简介：Hadoop分布式文件系统(HDFS)被设计成适合运行在通用硬件(commodity hardware)上的分布式文件系统。它和现有的分布式文件系统有很多共同点。但同时，它和其他的分布式文件系统的区别也是很明显的。HDFS是一个高度容错性的系统，适合部署在廉价的机器上。HDFS能提供高吞吐量的数据访问，非常适合大规模数据集上的应用。HDFS放宽了一部分POSIX约束，来实现流式读取文件系统数据的目的。HDFS在最开始是作为Apache Nutch搜索引擎项目的基础架构而开发的。HDFS是Apache Hadoop Core项目的一部分。

官网：http://hadoop.apache.org/

##资源管理
一、Twitter Mesos

开发者：Twitter研发人员John Oskasson

Twitter Mesos

简介：Apache Mesos是由加州大学伯克利分校的AMPLab首先开发的一款开源群集管理软件，支持Hadoop、ElasticSearch、Spark、Storm 和Kafka等架构，由于其开源性质越来越受到一些大型云计算公司的青睐，例如Twitter、Facebook等。

参考文章：Mesos渐入主流,Twitter模式有望“无限复制”-CSDN.NET

官网：http://mesos.apache.org/

二、Hadoop Yarn

Hadoop 新 MapReduce 框架 Yarn。为从根本上解决旧 MapReduce 框架的性能瓶颈，促进 Hadoop 框架的更长远发展，从 0.23.0 版本开始，Hadoop 的 MapReduce 框架完全重构，发生了根本的变化。新的 Hadoop MapReduce 框架命名为 MapReduceV2 或者叫 Yarn，其架构图如下图所示：

Hadoop Yarn

Yarn 框架相对于老的 MapReduce 框架什么优势呢？我们可以看到：

1、这个设计大大减小了 JobTracker（也就是现在的 ResourceManager）的资源消耗，并且让监测每一个 Job 子任务 (tasks) 状态的程序分布式化了，更安全、更优美。

2、在新的 Yarn 中，ApplicationMaster 是一个可变更的部分，用户可以对不同的编程模型写自己的 AppMst，让更多类型的编程模型能够跑在 Hadoop 集群中，可以参考 hadoop Yarn 官方配置模板中的 mapred-site.xml 配置。

3、对于资源的表示以内存为单位 ( 在目前版本的 Yarn 中，没有考虑 cpu 的占用 )，比之前以剩余 slot 数目更合理。

4、老的框架中，JobTracker 一个很大的负担就是监控 job 下的 tasks 的运行状况，现在，这个部分就扔给 ApplicationMaster 做了，而 ResourceManager 中有一个模块叫做 ApplicationsMasters( 注意不是 ApplicationMaster)，它是监测 ApplicationMaster 的行状况，如果出问题，会将其在其他机器上重启。

5、Container 是 Yarn 为了将来作资源隔离而提出的一个框架。这一点应该借鉴了 Mesos 的工作，目前是一个框架，仅仅提供 java 虚拟机内存的隔离 ,hadoop 团队的设计思路应该后续能支持更多的资源调度和控制 , 既然资源表示成内存量，那就没有了之前的 map slot/reduce slot 分开造成集群资源闲置的尴尬情况。

官网：http://hadoop.apache.org/
##日志收集系统
一、Facebook Scribe

scribe

贡献者：Facebook

简介：Scribe是Facebook开源的日志收集系统，在Facebook内部已经得到大量的应用。它能够从各种日志源上收集日志，存储到一个中央存储系统（可以是NFS，分布式文件系统等）上，以便于进行集中统计分析处理。它为日志的“分布式收集，统一处理”提供了一个可扩展的，高容错的方案。当中央存储系统的网络或者机器出现故障时，scribe会将日志转存到本地或者另一个位置，当中央存储系统恢复后，scribe会将转存的日志重新传输给中央存储系统。其通常与Hadoop结合使用，scribe用于向HDFS中push日志，而Hadoop通过MapReduce作业进行定期处理。

Scribe的系统架构

scribe

代码托管：https://github.com/facebook/scribe

二、Cloudera FlumeCloudera Flume

贡献者：Cloudera

简介：Flume是Cloudera提供的一个高可用的，高可靠的，分布式的海量日志采集、聚合和传输的系统，Flume支持在日志系统中定制各类数据发送方，用于收集数据；同时，Flume提供对数据进行简单处理，并写到各种数据接受方（可定制）的能力。

Flume提供了从console（控制台）、RPC（Thrift-RPC）、text（文件）、tail（UNIX tail）、syslog（syslog日志系统，支持TCP和UDP等2种模式），exec（命令执行）等数据源上收集数据的能力。

当前Flume有两个版本Flume 0.9X版本的统称Flume-og，Flume1.X版本的统称Flume-ng。由于Flume-ng经过重大重构，与Flume-og有很大不同，使用时请注意区分。

Cloudera Flume构架：

Cloudera Flume

官网：http://flume.apache.org/

三、logstash

简介：logstash 是一个应用程序日志、事件的传输、处理、管理和搜索的平台。你可以用它来统一对应用程序日志进行收集管理，提供 Web 接口用于查询和统计。他可以对你的日志进行收集、分析，并将其存储供以后使用（如，搜索），您可以使用它。说到搜索，logstash带有一个web界面，搜索和展示所有日志。

logstash

官网：http://www.logstash.net/

四、kibana

简介：Kibana 是一个为 Logstash 和 ElasticSearch 提供的日志分析的 Web 接口。可使用它对日志进行高效的搜索、可视化、分析等各种操作。kibana 也是一个开源和免费的工具，他可以帮助您汇总、分析和搜索重要数据日志并提供友好的web界面。他可以为 Logstash 和 ElasticSearch 提供的日志分析的 Web 界面。

主页： http://kibana.org/

代码托管： https://github.com/rashidkpc/Kibana/downloads

##消息系统
一、StormMQ

简介：MQMessageQueue消息队列产品 StormMQ，是一种服务程序。

官网：http://stormmq.com/

二、ZeroMQ

简介：这是个类似于Socket的一系列接口，他跟Socket的区别是：普通的socket是端到端的（1:1的关系），而ZMQ却是可以N：M 的关系，人们对BSD套接字的了解较多的是点对点的连接，点对点连接需要显式地建立连接、销毁连接、选择协议（TCP/UDP）和处理错误等，而ZMQ屏蔽了这些细节，让你的网络编程更为简单。ZMQ用于node与node间的通信，node可以是主机或者是进程。

引用官方的说法： “ZMQ(以下ZeroMQ简称ZMQ)是一个简单好用的传输层，像框架一样的一个socket library，他使得Socket编程更加简单、简洁和性能更高。是一个消息处理队列库，可在多个线程、内核和主机盒之间弹性伸缩。ZMQ的明确目标是“成为标准网络协议栈的一部分，之后进入Linux内核”。现在还未看到它们的成功。但是，它无疑是极具前景的、并且是人们更加需要的“传统”BSD套接字之上的一 层封装。ZMQ让编写高性能网络应用程序极为简单和有趣。”

官网：http://zeromq.org/

三、RabbitMQ

简介：RabbitMQ是一个受欢迎的消息代理，通常用于应用程序之间或者程序的不同组件之间通过消息来进行集成。本文简单介绍了如何使用 RabbitMQ，假定你已经配置好了rabbitmq服务器。

RabbitMQ

RabbitMQ是用Erlang，对于主要的编程语言都有驱动或者客户端。我们这里要用的是Java，所以先要获得Java客户端。

像RabbitMQ这样的消息代理可用来模拟不同的场景，例如点对点的消息分发或者订阅/推送。我们的程序足够简单，有两个基本的组件，一个生产者用于产生消息，还有一个消费者用来使用产生的消息。

官网：https://www.rabbitmq.com/

四、Apache ActiveMQ

简介：ActiveMQ 是Apache出品，最流行的，能力强劲的开源消息总线。ActiveMQ 是一个完全支持JMS1.1和J2EE 1.4规范的 JMS Provider实现，尽管JMS规范出台已经是很久的事情了，但是JMS在当今的J2EE应用中间仍然扮演着特殊的地位。

Apache ActiveMQ

特性：

⒈ 多种语言和协议编写客户端。语言: Java,C,C++,C#,Ruby,Perl,Python,PHP。应用协议： OpenWire,Stomp REST,WS Notification,XMPP,AMQP

⒉ 完全支持JMS1.1和J2EE 1.4规范 （持久化，XA消息，事务)

⒊ 对Spring的支持，ActiveMQ可以很容易内嵌到使用Spring的系统里面去，而且也支持Spring2.0的特性

⒋ 通过了常见J2EE服务器（如 Geronimo,JBoss 4,GlassFish,WebLogic)的测试，其中通过JCA 1.5 resource adaptors的配置，可以让ActiveMQ可以自动的部署到任何兼容J2EE 1.4 商业服务器上

⒌ 支持多种传送协议：in-VM,TCP,SSL,NIO,UDP,JGroups,JXTA

⒍ 支持通过JDBC和journal提供高速的消息持久化

⒎ 从设计上保证了高性能的集群，客户端-服务器，点对点

⒏ 支持Ajax

⒐ 支持与Axis的整合

⒑ 可以很容易得调用内嵌JMS provider，进行测试

官网：http://activemq.apache.org/

五、Jafka

贡献者：LinkedIn

简介：Jafka 是一个开源的、高性能的、跨语言分布式消息系统，使用GitHub托管。Jafka 最早是由Apache孵化的Kafka（由LinkedIn捐助给Apache）克隆而来。由于是一个开放式的数据传输协议，因此除了Java开发语言受到支持，Python、Ruby、C、C++等其他语言也能够很好的得到支持。

特性：

1、消息持久化非常快，服务端存储消息的开销为O(1)，并且基于文件系统，能够持久化TB级的消息而不损失性能。

2、吞吐量取决于网络带宽。

3、完全的分布式系统，broker、producer、consumer都原生自动支持分布式。自动实现复杂均衡。

4、内核非常小，整个系统（包括服务端和客户端）只有一个272KB的jar包，内部机制也不复杂，适合进行内嵌或者二次开发 。整个服务端加上依赖组件共3.5MB。

5、消息格式以及通信机制非常简单，适合进行跨语言开发。目前自带的Python3.x的客户端支持发送消息和接收消息。

官网：http://kafka.apache.org/

六、Apache Kafka

贡献者：LinkedIn

简介：Apache Kafka是由Apache软件基金会开发的一个开源消息系统项目，由Scala写成。Kafka最初是由LinkedIn开发，并于2011年初开源。2012年10月从Apache Incubator毕业。该项目的目标是为处理实时数据提供一个统一、高通量、低等待的平台。

Kafka是一个分布式的、分区的、多复本的日志提交服务。它通过一种独一无二的设计提供了一个消息系统的功能。

Kafka集群可以在一个指定的时间内保持所有发布上来的消息，不管这些消息有没有被消费。打个比方，如果这个时间设置为两天，那么在消息发布的两天以内，这条消息都是可以被消费的，但是在两天后，这条消息就会被系统丢弃以释放空间。Kafka的性能不会受数据量的大小影响，因此保持大量的数据不是一个问题。

官网：http://kafka.apache.org/

##分布式服务
一、ZooKeeper

贡献者：Google

简介：ZooKeeper是一个分布式的，开放源码的分布式应用程序协调服务，是Google的Chubby一个开源的实现，是Hadoop和Hbase的重要组件。它是一个为分布式应用提供一致性服务的软件，提供的功能包括：配置维护、名字服务、分布式同步、组服务等。

ZooKeeper是以Fast Paxos算法为基础的，paxos算法存在活锁的问题，即当有多个proposer交错提交时，有可能互相排斥导致没有一个proposer能提交成功，而Fast Paxos作了一些优化，通过选举产生一个leader，只有leader才能提交propose，具体算法可见Fast Paxos。因此，要想弄懂ZooKeeper首先得对Fast Paxos有所了解。

架构：

zookeeper

官网：http://zookeeper.apache.org/

 

RPC
（Remote Procedure Call Protocol）——远程过程调用协议

一、Apache Avro

简介：Apache Avro是Hadoop下的一个子项目。它本身既是一个序列化框架，同时也实现了RPC的功能。Avro官网描述Avro的特性和功能如下：

丰富的数据结构类型；
快速可压缩的二进制数据形式；
存储持久数据的文件容器；
提供远程过程调用RPC；
简单的动态语言结合功能。
相比于Apache Thrift 和Google的Protocol Buffers，Apache Avro具有以下特点：

支持动态模式。Avro不需要生成代码，这有利于搭建通用的数据处理系统，同时避免了代码入侵。
数据无须加标签。读取数据前，Avro能够获取模式定义，这使得Avro在数据编码时只需要保留更少的类型信息，有利于减少序列化后的数据大小。
官网：http://avro.apache.org/

二、Facebook Thrift

贡献者：Facebook

简介：Thrift源于大名鼎鼎的facebook之手，在2007年facebook提交Apache基金会将Thrift作为一个开源项目，对于当时的facebook来说创造thrift是为了解决facebook系统中各系统间大数据量的传输通信以及系统之间语言环境不同需要跨平台的特性。

thrift可以支持多种程序语言，例如: C++, C#, Cocoa, Erlang, Haskell, Java, Ocami, Perl, PHP, Python, Ruby, Smalltalk. 在多种不同的语言之间通信thrift可以作为二进制的高性能的通讯中间件，支持数据(对象)序列化和多种类型的RPC服务。

Thrift适用于程序对程 序静态的数据交换，需要先确定好他的数据结构，他是完全静态化的，当数据结构发生变化时，必须重新编辑IDL文件，代码生成，再编译载入的流程，跟其他IDL工具相比较可以视为是Thrift的弱项，Thrift适用于搭建大型数据交换及存储的通用工具，对于大型系统中的内部数据传输相对于JSON和xml无论在性能、传输大小上有明显的优势。

Thrift 主要由5个部分组成：

· 类型系统以及 IDL 编译器：负责由用户给定的 IDL 文件生成相应语言的接口代码

· TProtocol：实现 RPC 的协议层，可以选择多种不同的对象串行化方式，如 JSON, Binary。

· TTransport：实现 RPC 的传输层，同样可以选择不同的传输层实现，如socket, 非阻塞的 socket, MemoryBuffer 等。

· TProcessor：作为协议层和用户提供的服务实现之间的纽带，负责调用服务实现的接口。

· TServer：聚合 TProtocol, TTransport 和 TProcessor 几个对象。

上述的这5个部件都是在 Thrift 的源代码中通过为不同语言提供库来实现的，这些库的代码在 Thrift 源码目录的 lib 目录下面，在使用 Thrift 之前需要先熟悉与自己的语言对应的库提供的接口。

Facebook Thrift构架：

Thrift

官网：http://thrift.apache.org/

集群管理
一、Nagios

简介：Nagios是一款开源的免费网络监视工具，能有效监控Windows、Linux和Unix的主机状态，交换机路由器等网络设置，打印机等。在系统或服务状态异常时发出邮件或短信报警第一时间通知网站运维人员，在状态恢复后发出正常的邮件或短信通知。

Nagios可运行在Linux/Unix平台之上，同时提供一个可选的基于浏览器的WEB界面以方便系统管理人员查看网络状态，各种系统问题，以及日志等等。

官网：http://www.nagios.org/

二、Ganglia

简介：Ganglia是UC Berkeley发起的一个开源集群监视项目，设计用于测量数以千计的节点。Ganglia的核心包含gmond、gmetad以及一个Web前端。主要是用来监控系统性能，如：cpu 、mem、硬盘利用率， I/O负载、网络流量情况等，通过曲线很容易见到每个节点的工作状态，对合理调整、分配系统资源，提高系统整体性能起到重要作用。

Ganglia

官网：http://ganglia.sourceforge.net/

三、Apache Ambari

简介：Apache Ambari是一种基于Web的工具，支持Apache Hadoop集群的供应、管理和监控。Ambari目前已支持大多数Hadoop组件，包括HDFS、MapReduce、Hive、Pig、 Hbase、Zookeper、Sqoop和Hcatalog等。

Apache Ambari 支持HDFS、MapReduce、Hive、Pig、Hbase、Zookeper、Sqoop和Hcatalog等的集中管理。也是5个顶级hadoop管理工具之一。

Apache Ambari

Ambari主要取得了以下成绩：

通过一步一步的安装向导简化了集群供应。
预先配置好关键的运维指标（metrics），可以直接查看Hadoop Core（HDFS和MapReduce）及相关项目（如HBase、Hive和HCatalog）是否健康。
支持作业与任务执行的可视化与分析，能够更好地查看依赖和性能。
通过一个完整的RESTful API把监控信息暴露出来，集成了现有的运维工具。
用户界面非常直观，用户可以轻松有效地查看信息并控制集群。
Ambari使用Ganglia收集度量指标，用Nagios支持系统报警，当需要引起管理员的关注时（比如，节点停机或磁盘剩余空间不足等问题），系统将向其发送邮件。

此外，Ambari能够安装安全的（基于Kerberos）Hadoop集群，以此实现了对Hadoop 安全的支持，提供了基于角色的用户认证、授权和审计功能，并为用户管理集成了LDAP和Active Directory。

官网：http://ambari.apache.org/

 

基础设施
一、LevelDB

贡献者：Jeff Dean和Sanjay Ghemawat

简介：Leveldb是一个google实现的非常高效的kv数据库，目前的版本1.2能够支持billion级别的数据量了。 在这个数量级别下还有着非常高的性能，主要归功于它的良好的设计。特别是LMS算法。LevelDB 是单进程的服务，性能非常之高，在一台4核Q6600的CPU机器上，每秒钟写数据超过40w，而随机读的性能每秒钟超过10w。

Leveldb框架：

Leveldb

官网：http://code.google.com/p/leveldb/

二、SSTable

简介：如果说Protocol Buffer是谷歌独立数据记录的通用语言 ，那么有序字符串表（SSTable，Sorted String Table）则是用于存储，处理和数据集交换的最流行​​的数据输出格式。正如它的名字本身，SSTable是有效存储大量键-值对的简单抽象，对高吞吐量顺序读/写进行了优化。

SSTable是Bigtable中至关重要的一块，对于LevelDB来说也是如此。

三、RecordIO

贡献者：Google

简介：我们大家都在用文件来存储数据。文件是存储在磁盘上的。如果在一些不稳定的介质上，文件很容损坏。即时文件某个位置出现一点小小的问题，整个文件就废了。

下面我来介绍Google的一个做法，可以比较好的解决这个问题。那就是recordio文件格式。recoidio的存储单元是一个一个record。这个record可以根据业务的需要自行定义。但Google有一种建议的处理方式就是使用protobuf。

reocordio底层的格式其实很简单。一个record由四部分组成：

MagicNumber (32 bits)
Uncompressed data payload size (64 bits)
Compressed data payload size (64 bits), or 0 if the data is not compressed
Payload, possibly compressed.
详细格式如下图所示：

RecordIO

到这里，大家可能已经知道，recordio之所以能对付坏数据，其实就是在这个MagicNumber（校验值）。

四、Flat Buffers

贡献者：Google

简介：谷歌开源高效、跨平台的序列化库FlatBuffers。

该库的构建是专门为游戏开发人员的性能需求提供支持，它将序列化数据存储在缓存中，这些数据既可以存储在文件中，又可以通过网络原样传输，而不需要任何解析开销。

FlatBuffers有如下一些关键特性——

访问序列化数据不需要打包/拆包
节省内存而且访问速度快——缓存只占用访问数据所需要的内存；不需要任何额外的内存。
灵活性——通过可选字段向前向后兼容
代码规模小
强类型——错误在编译时捕获，而不是在运行时
便利性——生成的C++头文件代码简洁。如果需要，有一项可选功能可以用来在运行时高效解析Schema和JSON-like格式的文本。
跨平台——使用C++编写，不依赖STL之外的库，因此可以用于任何有C++编辑器的平台。当前，该项目包含构建方法和在Android、Linux、Windows和OSX等操作系统上使用该库的示例。
与Protocol Buffers或JSON Parsing这样的可选方案相比，FlatBuffers的优势在于开销更小，这主要是由于它没有解析过程。

代码托管：https://github.com/google/flatbuffers

五、Protocol Buffers

贡献者：Google

简介：Protocol Buffers是Google公司开发的一种数据描述语言，类似于XML能够将结构化数据序列化，可用于数据存储、通信协议等方面。它不依赖于语言和平台并且可扩展性极强。现阶段官方支持C++、JAVA、Python等三种编程语言，但可以找到大量的几乎涵盖所有语言的第三方拓展包。

通过它，你可以定义你的数据的结构，并生成基于各种语言的代码。这些你定义的数据流可以轻松地在传递并不破坏你已有的程序。并且你也可以更新这些数据而现有的程序也不会受到任何的影响。

Protocol Buffers经常被简称为protobuf。

官网：http://code.google.com/p/protobuf/

六、Consistent Hashing（哈希算法）

简介：一致性哈希算法在1997年由麻省理工学院提出的一种分布式哈希（DHT）实现算法，设计目标是为了解决因特网中的热点(Hot spot)问题，初衷和CARP十分类似。一致性哈希修正了CARP使用的简 单哈希算法带来的问题，使得分布式哈希（DHT）可以在P2P环境中真正得到应用。

Consistent Hashing

一致性hash算法提出了在动态变化的Cache环境中，判定哈希算法好坏的四个定义：

1、平衡性(Balance)：平衡性是指哈希的结果能够尽可能分布到所有的缓冲中去，这样可以使得所有的缓冲空间都得到利用。很多哈希算法都能够满足这一条件。

2、单调性(Monotonicity)：单调性是指如果已经有一些内容通过哈希分派到了相应的缓冲中，又有新的缓冲加入到系统中。哈希的结果应能够保证原有已分配的内容可以被映射到原有的或者新的缓冲中去，而不会被映射到旧的缓冲集合中的其他缓冲区。

3、分散性(Spread)：在分布式环境中，终端有可能看不到所有的缓冲，而是只能看到其中的一部分。当终端希望通过哈希过程将内容映射到缓冲上时，由于不同终端所见的缓冲范围有可能不同，从而导致哈希的结果不一致，最终的结果是相同的内容被不同的终端映射到不同的缓冲区中。这种情况显然是应该避免的，因为它导致相同内容被存储到不同缓冲中去，降低了系统存储的效率。分散性的定义就是上述情况发生的严重程度。好的哈希算法应能够尽量避免不一致的情况发生，也就是尽量降低分散性。

4、负载(Load)：负载问题实际上是从另一个角度看待分散性问题。既然不同的终端可能将相同的内容映射到不同的缓冲区中，那么对于一个特定的缓冲区而言，也可能被不同的用户映射为不同 的内容。与分散性一样，这种情况也是应当避免的，因此好的哈希算法应能够尽量降低缓冲的负荷。

在分布式集群中，对机器的添加删除，或者机器故障后自动脱离集群这些操作是分布式集群管理最基本的功能。如果采用常用的hash(object)%N算法，那么在有机器添加或者删除后，很多原有的数据就无法找到了，这样严重的违反了单调性原则。

七、Netty

贡献者：JBOSS

简介：Netty是由JBOSS提供的一个java开源框架。Netty提供异步的、事件驱动的网络应用程序框架和工具，用以快速开发高性能、高可靠性的网络服务器和客户端程序。

Netty

也就是说，Netty 是一个基于NIO的客户，服务器端编程框架，使用Netty 可以确保你快速和简单的开发出一个网络应用，例如实现了某种协议的客户，服务端应用。Netty相当简化和流线化了网络应用的编程开发过程，例如，TCP和UDP的socket服务开发。

“快速”和“简单”并不意味着会让你的最终应用产生维护性或性能上的问题。Netty 是一个吸收了多种协议的实现经验，这些协议包括FTP,SMTP,HTTP，各种二进制，文本协议，并经过相当精心设计的项目，最终，Netty 成功的找到了一种方式，在保证易于开发的同时还保证了其应用的性能，稳定性和伸缩性。

官网：http://netty.io/

八、BloomFilter

简介：Bloom filter 是由 Howard Bloom 在 1970 年提出的二进制向量数据结构，它具有很好的空间和时间效率，被用来检测一个元素是不是集合中的一个成员。如果检测结果为是，该元素不一定在集合中；但如果检测结果为否，该元素一定不在集合中。因此Bloom filter具有100%的召回率。这样每个检测请求返回有“在集合内（可能错误）”和“不在集合内（绝对不在集合内）”两种情况，可见 Bloom filter 是牺牲了正确率和时间以节省空间。

Bloom filter 优点就是它的插入和查询时间都是常数，另外它查询元素却不保存元素本身，具有良好的安全性。

##搜索引擎
一、Nutch

简介：Nutch 是一个开源Java 实现的搜索引擎。它提供了我们运行自己的搜索引擎所需的全部工具。包括全文搜索和Web爬虫。

尽管Web搜索是漫游Internet的基本要求, 但是现有web搜索引擎的数目却在下降. 并且这很有可能进一步演变成为一个公司垄断了几乎所有的web搜索为其谋取商业利益.这显然 不利于广大Internet用户.

Nutch

Nutch为我们提供了这样一个不同的选择. 相对于那些商用的搜索引擎, Nutch作为开放源代码 搜索引擎将会更加透明, 从而更值得大家信赖. 现在所有主要的搜索引擎都采用私有的排序算法, 而不会解释为什么一个网页会排在一个特定的位置. 除此之外, 有的搜索引擎依照网站所付的 费用, 而不是根据它们本身的价值进行排序. 与它们不同, Nucth没有什么需要隐瞒, 也没有 动机去扭曲搜索的结果. Nutch将尽自己最大的努力为用户提供最好的搜索结果.

Nutch目前最新的版本为version v2.2.1。

官网：https://nutch.apache.org/

二、Lucene

开发者：Doug Cutting（Hadoop之父，你懂的）

简介：Lucene是apache软件基金会4 jakarta项目组的一个子项目，是一个开放源代码的全文检索引擎工具包，即它不是一个完整的全文检索引擎，而是一个全文检索引擎的架构，提供了完整的查询引擎和索引引擎，部分文本分析引擎（英文与德文两种西方语言）。Lucene的目的是为软件开发人员提供一个简单易用的工具包，以方便的在目标系统中实现全文检索的功能，或者是以此为基础建立起完整的全文检索引擎。

Lucene

官网：http://lucene.apache.org/

三、SolrCloud

简介：SolrCloud是Solr4.0版本以后基于Solr和Zookeeper的分布式搜索方案。SolrCloud是Solr的基于Zookeeper一种部署方式。Solr可以以多种方式部署，例如单机方式，多机Master-Slaver方式。

原理图：

SolrCloud

SolrCloud有几个特色功能：

集中式的配置信息使用ZK进行集中配置。启动时可以指定把Solr的相关配置文件上传

Zookeeper，多机器共用。这些ZK中的配置不会再拿到本地缓存，Solr直接读取ZK中的配置信息。配置文件的变动，所有机器都可以感知到。另外，Solr的一些任务也是通过ZK作为媒介发布的。目的是为了容错。接收到任务，但在执行任务时崩溃的机器，在重启后，或者集群选出候选者时，可以再次执行这个未完成的任务。

自动容错SolrCloud对索引分片，并对每个分片创建多个Replication。每个Replication都可以对外提供服务。一个Replication挂掉不会影响索引服务。更强大的是，它还能自动的在其它机器上帮你把失败机器上的索引Replication重建并投入使用。

近实时搜索立即推送式的replication（也支持慢推送）。可以在秒内检索到新加入索引。

查询时自动负载均衡SolrCloud索引的多个Replication可以分布在多台机器上，均衡查询压力。如果查询压力大，可以通过扩展机器，增加Replication来减缓。

自动分发的索引和索引分片发送文档到任何节点，它都会转发到正确节点。

事务日志事务日志确保更新无丢失，即使文档没有索引到磁盘。

四、Solr

简介：Solr是一个独立的企业级搜索应用服务器，它对外提供类似于Web-service的API接口。用户可以通过http请求，向搜索引擎服务器提交一定格式的XML文件，生成索引；也可以通过Http Get操作提出查找请求，并得到XML格式的返回结果。

Solr

Solr是一个高性能，采用Java5开发，基于Lucene的全文搜索服务器。同时对其进行了扩展，提供了比Lucene更为丰富的查询语言，同时实现了可配置、可扩展并对查询性能进行了优化，并且提供了一个完善的功能管理界面，是一款非常优秀的全文搜索引擎。

官网：https://lucene.apache.org/solr/

五、ElasticSearch

简介：ElasticSearch是一个基于Lucene的搜索服务器。它提供了一个分布式多用户能力的全文搜索引擎，基于RESTful web接口。Elasticsearch是用Java开发的，并作为Apache许可条款下的开放源码发布，是第二最流行的企业搜索引擎。设计用于云计算中，能够达到实时搜索，稳定，可靠，快速，安装使用方便。

官网：http://www.elasticsearch.org/

六、Sphinx

简介：Sphinx是一个基于SQL的全文检索引擎，可以结合MySQL,PostgreSQL做全文搜索，它可以提供比数据库本身更专业的搜索功能，使得应用程序更容易实现专业化的全文检索。Sphinx特别为一些脚本语言设计搜索API接口，如PHP,Python,Perl,Ruby等，同时为MySQL也设计了一个存储引擎插件。

Sphinx单一索引最大可包含1亿条记录，在1千万条记录情况下的查询速度为0.x秒（毫秒级）。Sphinx创建索引的速度为：创建100万条记录的索引只需 3～4分钟，创建1000万条记录的索引可以在50分钟内完成，而只包含最新10万条记录的增量索引，重建一次只需几十秒。

官网：http://sphinxsearch.com

七、SenseiDB

贡献者：linkedin

简介：SenseiDB是一个NoSQL数据库，它专注于高更新率以及复杂半结构化搜索查询。熟悉Lucene和Solor的用户会发现，SenseiDB背后有许多似曾相识的概念。SenseiDB部署在多节点集群中，其中每个节点可以包括N块数据片。Apache Zookeeper用于管理节点，它能够保持现有配置，并可以将任意改动（如拓扑修改）传输到整个节点群中。SenseiDB集群还需要一种模式用于定义将要使用的数据模型。

从SenseiDB集群中获取数据的唯一方法是通过Gateways（它 没有“INSERT”方法）。每个集群都连接到一个单一gateway。你需要了解很重要的一点是，由于SenseiDB本身没法处理原子性 （Atomicity）和隔离性（Isolation），因此只能通过外部在gateway层进行限制。另外，gateway必须确保数据流按照预期的方 式运作。内置的gateway有以下几种形式：

来自文件
来自JMS队列
通过JDBC
来自Apache Kafka
官网：http://senseidb.com

##数据挖掘
一、Mahout

Mahout

简介：Apache Mahout 是 Apache Software Foundation (ASF) 开发的一个全新的开源项目，其主要目标是创建一些可伸缩的机器学习算法，供开发人员在 Apache 在许可下免费使用。该项目已经发展到了它的最二个年头，目前只有一个公共发行版。Mahout 包含许多实现，包括集群、分类、CP 和进化程序。此外，通过使用 Apache Hadoop 库，Mahout 可以有效地扩展到云中。

虽然在开源领域中相对较为年轻，但 Mahout 已经提供了大量功能，特别是在集群和 CF 方面。Mahout 的主要特性包括：

Taste CF。Taste 是 Sean Owen 在 SourceForge 上发起的一个针对 CF 的开源项目，并在 2008 年被赠予 Mahout。
一些支持 Map-Reduce 的集群实现包括 k-Means、模糊 k-Means、Canopy、Dirichlet 和 Mean-Shift。
Distributed Naive Bayes 和 Complementary Naive Bayes 分类实现。
针对进化编程的分布式适用性功能。
Matrix 和矢量库。
上述算法的示例。
官网：http://mahout.apache.org/

##Iaas
IaaS（Infrastructure as a Service），即基础设施即服务。

一、OpenStack

简介：OpenStack是一个由NASA（美国国家航空航天局）和Rackspace合作研发并发起的，以Apache许可证授权的自由软件和开放源代码项目。

OpenStack是一个开源的云计算管理平台项目，由几个主要的组件组合起来完成具体工作。OpenStack支持几乎所有类型的云环境，项目目标是提供实施简单、可大规模扩展、丰富、标准统一的云计算管理平台。OpenStack通过各种互补的服务提供了基础设施即服务（IaaS）的解决方案，每个服务提供API以进行集成。

OpenStack

6个核心项目：Nova（计算，Compute），Swift（对象存储，Object），Glance（镜像，Image），Keystone（身份，Identity），Horizon（自助门户，Dashboard），Quantum & Melange（网络&地址管理），另外还有若干社区项目，如Rackspace（负载均衡）、Rackspace（关系型数据库）。


二、Docker

贡献者：dotCloud

Docker

简介：Docker 是一个开源的应用容器引擎，让开发者可以打包他们的应用以及依赖包到一个可移植的容器中，然后发布到任何流行的 Linux 机器上，也可以实现虚拟化。容器是完全使用沙箱机制，相互之间不会有任何接口（类似 iPhone 的 app）。几乎没有性能开销,可以很容易地在机器和数据中心中运行。最重要的是,他们不依赖于任何语言、框架或包括系统。

官网：http://www.docker.io/

三、Kubernetes

贡献者：Google

简介：Kubernetes是Google开源的容器集群管理系统。它构建Ddocker技术之上，为容器化的应用提供资源调度、部署运行、服务发现、扩容缩容等整一套功能，本质上可看作是基于容器技术的mini-PaaS平台。

Kubernetes从另一个角度对资源进行抽象，它让开发人员和管理人员共同着眼于服务的行为和性能的提升，而不是仅仅关注对单一的组件或者是基础资源。

那么Kubernetes集群到底提供了哪些单一容器所没有功能?它主要关注的是对服务级别的控制而并非仅仅是对容器级别的控制，Kubernetes提供了一种“机智”的管理方式，它将服务看成一个整体。在Kubernete的解决方案中，一个服务甚至可以自我扩展，自我诊断，并且容易升级。例如，在Google中，我们使用机器学习技术来保证每个运行的服务的当前状态都是最高效的。

代码托管：https://github.com/GoogleCloudPlatform/kubernetes/

四、Imctfy

贡献者：Google

简介：Google开源了自己所用Linux容器系统的开源版本lmctfy，读音为lem-kut-fee。包括一个C++库（使用了C++11，文档可以参考头文件）和命令行界面。目前的版本是0.1，只提供了CPU与内存隔离。项目还在密集开发中。

mctfy本身是针对某些特定使用场景设计和实现的，目前拥有一台机器上所有容器时运行情况最好，不推荐与LXC和其他容器系统一起使用（虽然也可行）。已在Ubuntu 12.04+和Ubuntu 3.3与3.8内核上测试。

代码托管：https://github.com/google/Imctfy/

 

监控管理
一、Dapper

贡献者：Google

简介：Dapper是一个轻量的ORM(对象关系映射（英语：Object Relational Mapping，简称ORM，或O/RM，或O/R mapping）。并不单纯的是一个DBHelper.因为在Dapper中数据其实就是一个对象。Dapper扩展与IDbConnection上，所以事实上它的倾入性很低。我用了StructureMap。如果不喜欢可以自己更换，或者自己实现下。

代码就一个SqlMapper.cs文件,主要是IDbConnection的扩展方法，编译后就40K的一个很小的dll。

特性：

Dapper很快。Dapper的速度接近与IDataReader。
Dapper支持主流数据库 Mysql,SqlLite,Mssql2000,Mssql2005,Oracle等一系列的数据库
支持多表并联的对象。支持一对多 多对多的关系，并且没侵入性。
原理通过Emit反射IDataReader的序列队列，来快速的得到和产生对象
Dapper语法十分简单。并且无须迁就数据库的设计
官方站点 http://code.google.com/p/dapper-dot-net/

代码托管：http://bigbully.github.io/Dapper-translation/

二、Zipkin

贡献者：Twitter

简介：Zipkin （分布式跟踪系统）是 Twitter 的一个开源项目，允许开发者收集 Twitter 各个服务上的监控数据，并提供查询接口。该系统让开发者可通过一个 Web 前端轻松的收集和分析数据，例如用户每次请求服务的处理时间等，可方便的监测系统中存在的瓶颈。

Zipkin

官方网站：http://twitter.github.io/zipkin/

代码托管：https://github.com/twitter/zipkin

##数据交易

代表：数据堂

数据堂成立于2011年，总部位于北京，它其实最开始是由科研和人工智能大数据服务商发展而来的，是国内首家专注于互联网综合数据交易和服务的公司。提供数据定制、数据云服务、数据交易等服务，致力于融合和盘活各类大数据资源，实现数据价值最大化，推动相关技术、应用和产业的创新。

目前，数据堂的业务领域拓展到包括到智慧交通、健康医疗、金融征信、政府大数据运营等诸多领域，逐渐形成多业务多模式立体化的集团式发展。已成功为国内外多家企业提供数据定制服务，包括百度，腾讯，阿里巴巴、奇虎360、联想、Microsoft、NEC、Canon、Intel、Samsung、Nuance、Fujitsu等。

产品分析：

数据堂的产品可以概括为两个方面，一是面向 B 端客户提供定制化数据源服务，二是大数据交易平台。数据堂的 B 端客户包括百度、腾讯、阿里巴巴等，主要业务有代采集、处理和制作数据或出售和租赁数据。数据堂的 C 端客户是需要数据的个体，可以通过数据交易平台购买和租赁数据。

其中大数据交易平台的目标是打通数据拥有方和需求方的需求，通过数据拥有方合作，积累了征信、交通、健康、医疗等数十个领域的数据集。第二步数据加工处理分析，把分析结果放到云上，当前服务BAT在内的一千家企业以上。

##金融大数据

代表：BBD(数联铭品)

BBD(数联铭品)成立于2013年，至今才2年多时间，但发展势头迅速。作为大数据金融风险管理专家，这家公司还是商业大数据行业标准COSR的制定者。除了位于成都的总部，还在北京、上海、深圳和杭州设有分支机构，并在香港和新加坡设有子公司。2015年，BBD被评为国家高新技术企业，还拿到中国人民银行审批通过的《企业征信业务经营备案证》。

在大数据领域，有“南周北孙”的说法。“南周”就是指BBD创始人兼首席科学家周涛。这位27岁时就当上教授的明星科学家，刚刚当选央视年度科技创新人物，与屠呦呦一同获奖。

截至目前，BBD已覆盖2300万家企业法人主体的基本数据，建立了最全面的工商、诉讼、专利、招聘、社交、招中标数据库，尤其是对诉讼数据进行了深度挖掘，所采集的数据全部来自于公开数据。

服务对象包括银行、会计师事务所、律所、投资机构、征信评级机构、金融信息终端、媒体和咨询机构等，典型客户有毕马威、普华永道、长沙银行、重庆银行、中证信用、中经社、《财富》(中文版)、财新传媒、四川省旅游局等。

产品分析：

目前国际上领先的金融服务机构缺少标准的方法论和模型，对轻资产高研发高成长的新经济企业进行评级和分析。BBD提供可靠且可验证的金融大数据和技术方案，基于大数据框架和国际标准建立中国特色的Fin-Tech ，建设新经济框架下，中小企业信用体系尤其是轻资产高研发高成长企业信用体系。

数据科学家和金融科学家组成的团队，研究了一套新经济企业的行为模型，建立了一个企业行为数据库，模型分为七个维度：企业的基本信息;行业数据信息;法人治理结构信息;关联方信息;财务和非财务KPI;社交媒体信息;无形资产和资产质押数据。

BBD旗下的明星产品HIGGS Credit是一款企业全息画像搜索引擎，依托于全面的企业数据库，可以提供企业DNA全息画像、企业行为KPI数据与行业比对、实时动态的企业尽职调查数据、关联方异动数据监测等数据服务。公司还有BBD Finance，BBD Index，BBD Anti-Fraud, BBD Innovation、BBD Credit等针对不同领域的多条产品线。

##工业大数据

代表：美林数据

如果要给美林贴上一个标签的话，应该是“工业大数据”。这家公司位于西安，旗下有数据分析产品、行业大数据解决方案、数据运营服务三大核心业务。公司深耕大数据行业应用，面向电力、军工制造、金融领域提供定制化大数据落地解决方案。深度参与国家“中国制造2025”、“一带一路”、“大数据”战略计划，并成立国内首家“一带一路大数据交易所”，主要客户群为电力、军工制造、金融等相关领域，是国内首家军工制造业大数据落地方案提供商。

产品分析：

去年 5月，美林数据发布国内首款集数据可视化探索与数据深度发掘功能于一体的大数据分析平台(Tempo-DataAnalysis)，面向企业不同领域和不同层级的数据分析、价值应用人员，提供数据可视化探索、数据深度分析和数据应用开发的一体化服务，持续为用户打造“专业、敏捷、易用“的产品体验。

该平台的特点在于可视化(分析过程可视化、分析结果可视化、高维数据可视化)、智能化(数据模型自动识别、数图智能匹配)、增值化(发掘数据规律、获取商业洞察力、创造商业价值)、大数据支持(分布式并行计算、内存计算)、多数据源接入支持(文件数据格式、关系型数据库、HDFS、H )等。

美林的大数据产品线还包括数据挖掘平台、统计分析平台、企业门户平台、数据资源管理平台等。

##营销大数据

代表：华院数据

华院数据位于上海，提供基于数据挖掘的面向营销分析和管理、客户关系管理和决策支持的应用软件和咨询解决方案。

这家成立于2002年的企业，一直为传统运营商、银行、保险、航空等具有海量数据的机构提供数据挖掘和分析解决方案。几年前，随着数据采集、存储和传输技术的发展，华院开始了以数据分析为核心的多元化尝试，将足迹延伸到电商、制造业、医疗、安防、物流等更多的新兴行业。

华院也着力于产业大数据生态孵化，培育了数云、数创、数真等十余家围绕垂直行业的大数据企业。

产品分析：

包括大数据运营视窗系统、精准营销系统、智能推荐引擎、标签管理系统等。其中大数据视窗系统通过对企业数据及业务规则的深度挖掘分析，将企业的业务流、信息流、数据流进行全面整合分析，借助最新的数字可视化技术，为企业管理层建立统一的运营视窗，快速洞察企业运营中的风险和瓶颈。

值得一提的是，华院旗下的数云，现已成为淘宝上卖得最贵的电商CRM，数云从2011年起步，现已拓展到为上千家零售企业提供大数据营销服务，帮助品牌管理的会员超过1.5亿，从品牌层面帮助零售企业构建以客户为核心的大数据管理、分析、营销应用平台，收集和挖掘大数据的价值，拓宽流量渠道，帮助企业实现数据化营销、智能化管理。