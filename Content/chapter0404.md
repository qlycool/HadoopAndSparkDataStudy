
#Hadoop 2.7.2完全分布式的搭建

##一 生产环境描述
###1 网络环境

* hadoopmaster 192.168.1.159
* hadoopslave1 192.168.1.76
* hadoopslave2 192.168.1.166

###2 软件环境

* JDK 7U79
* Hadoop 2.7.2

## 二 hadoopmaster主机配置
###1 JDK设置

```
chu888chu888@hadoopmaster:~$ ls
jdk-7u79-linux-x64.gz
chu888chu888@hadoopmaster:~$ sudo tar xvfz jdk-7u79-linux-x64.gz

chu888chu888@hadoopmaster:~$ ls 
jdk1.7.0_79 jdk-7u79-linux-x64.gz
chu888chu888@hadoopmaster:~$ sudo cp -r jdk1.7.0_79/ /usr/lib/jvm/
chu888chu888@hadoopmaster:~$ cd /usr/lib/jvm
chu888chu888@hadoopmaster:/usr/lib/jvm$ ls
bin db jre LICENSE README.html src.zip THIRDPARTYLICENSEREADME.txt
COPYRIGHT include lib man release THIRDPARTYLICENSEREADME-JAVAFX.txt
chu888chu888@hadoopmaster:/usr/lib/jvm$ 

chu888chu888@ubuntu1:/usr/lib/jvm$ sudo nano /etc/profile

修改内容如下,注意大小写,在环境变量中的配置中,有一点需要指出就是如果只是编辑~/.profile的话这个变量的生效只是针对当前用户的.如果想要其在全局生效的话,建议更新/etc/profile,这是一个全局的.

```

**/etc/profile的内容如下:**

```
export JAVA_HOME=/usr/lib/jvm/
export JRE_HOME=${JAVA_HOME}/jre
export CLASSPATH=.:${JAVA_HOME}/lib:${JRE_HOME}/lib
export PATH=${JAVA_HOME}/bin:$PATH
```

**这里面有一个小的体验技巧,我建议将所有需要的环境变量配置加入到/etc/profile中,这是全局变量.**

```
export JAVA_HOME=/usr/lib/jvm/
export JRE_HOME=${JAVA_HOME}/jre
export CLASSPATH=.:${JAVA_HOME}/lib:${JRE_HOME}/lib
export PATH=${JAVA_HOME}/bin:$PATH


export JAVA_HOME=/usr/lib/jvm/
export HADOOP_INSTALL=/usr/local/hadoop
export PATH=$PATH:$HADOOP_INSTALL/bin
export PATH=$PATH:$JAVA_HOME/bin
export PATH=$PATH:$HADOOP_INSTALL/sbin
export HADOOP_MAPRED_HOME=$HADOOP_INSTALL
export HADOOP_COMMON_HOME=$HADOOP_INSTALL
export HADOOP_HDFS_HOME=$HADOOP_INSTALL
export YARN_HOME=$HADOOP_INSTALL
export HADOOP_COMMON_LIB_NATIVE_DIR=$HADOOP_INSTALL/lib/native
export HADOOP_OPTS="-Djava.library.path=$HADOOP_INSTALL/lib"

```

还有一个问题就是,在启动hadoop的时候经常会出现,找不到JAVA_HOME的问题,这个问题可以通过修改hadoop环境变量来解决,直接写死变量就可以了.


**测试环境变量是不是生效**

```
chu888chu888@hadoopmaster:/usr/lib/jvm$ source /etc/profile
chu888chu888@hadoopmaster:/usr/lib/jvm$ env
XDG_SESSION_ID=1
TERM=xterm-256color
SHELL=/bin/bash
SSH_CLIENT=192.168.1.23 49818 22
OLDPWD=/home/chu888chu888
SSH_TTY=/dev/pts/0
JRE_HOME=/usr/lib/jvm//jre
USER=chu888chu888
LS_COLORS=rs=0:di=01;34:ln=01;36:mh=00:pi=40;33:so=01;35:do=01;35:bd=40;33;01:cd=40;33;01:or=40;31;01:su=37;41:sg=30;43:ca=30;41:tw=30;42:ow=34;42:st=37;44:ex=01;32:*.tar=01;31:*.tgz=01;31:*.arj=01;31:*.taz=01;31:*.lzh=01;31:*.lzma=01;31:*.tlz=01;31:*.txz=01;31:*.zip=01;31:*.z=01;31:*.Z=01;31:*.dz=01;31:*.gz=01;31:*.lz=01;31:*.xz=01;31:*.bz2=01;31:*.bz=01;31:*.tbz=01;31:*.tbz2=01;31:*.tz=01;31:*.deb=01;31:*.rpm=01;31:*.jar=01;31:*.war=01;31:*.ear=01;31:*.sar=01;31:*.rar=01;31:*.ace=01;31:*.zoo=01;31:*.cpio=01;31:*.7z=01;31:*.rz=01;31:*.jpg=01;35:*.jpeg=01;35:*.gif=01;35:*.bmp=01;35:*.pbm=01;35:*.pgm=01;35:*.ppm=01;35:*.tga=01;35:*.xbm=01;35:*.xpm=01;35:*.tif=01;35:*.tiff=01;35:*.png=01;35:*.svg=01;35:*.svgz=01;35:*.mng=01;35:*.pcx=01;35:*.mov=01;35:*.mpg=01;35:*.mpeg=01;35:*.m2v=01;35:*.mkv=01;35:*.webm=01;35:*.ogm=01;35:*.mp4=01;35:*.m4v=01;35:*.mp4v=01;35:*.vob=01;35:*.qt=01;35:*.nuv=01;35:*.wmv=01;35:*.asf=01;35:*.rm=01;35:*.rmvb=01;35:*.flc=01;35:*.avi=01;35:*.fli=01;35:*.flv=01;35:*.gl=01;35:*.dl=01;35:*.xcf=01;35:*.xwd=01;35:*.yuv=01;35:*.cgm=01;35:*.emf=01;35:*.axv=01;35:*.anx=01;35:*.ogv=01;35:*.ogx=01;35:*.aac=00;36:*.au=00;36:*.flac=00;36:*.mid=00;36:*.midi=00;36:*.mka=00;36:*.mp3=00;36:*.mpc=00;36:*.ogg=00;36:*.ra=00;36:*.wav=00;36:*.axa=00;36:*.oga=00;36:*.spx=00;36:*.xspf=00;36:
MAIL=/var/mail/chu888chu888
PATH=/usr/lib/jvm//bin:/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin:/usr/games:/usr/local/games
PWD=/usr/lib/jvm
JAVA_HOME=/usr/lib/jvm/
LANG=en_US.UTF-8
SHLVL=1
HOME=/home/chu888chu888
LANGUAGE=en_US:en
LOGNAME=chu888chu888
CLASSPATH=.:/usr/lib/jvm//lib:/usr/lib/jvm//jre/lib
SSH_CONNECTION=192.168.1.23 49818 192.168.1.159 22
LESSOPEN=| /usr/bin/lesspipe %s
XDG_RUNTIME_DIR=/run/user/1000
LESSCLOSE=/usr/bin/lesspipe %s %s
_=/usr/bin/env
chu888chu888@hadoopmaster:/usr/lib/jvm$ java -version
java version "1.7.0_79"
Java(TM) SE Runtime Environment (build 1.7.0_79-b15)
Java HotSpot(TM) 64-Bit Server VM (build 24.79-b02, mixed mode)
chu888chu888@hadoopmaster:/usr/lib/jvm$ 
```
### 2 IP设置

```
chu888chu888@hadoopmaster:/etc/network$ ls
if-down.d  if-post-down.d  if-pre-up.d  if-up.d  interfaces  interfaces.d  run
chu888chu888@hadoopmaster:/etc/network$ sudo nano interfaces

```
**interfaces的内容如下**

```
auto lo
iface lo inet loopback

auto eth0
iface eth0 inet static
address 192.168.1.159
netmask 255.255.255.0
gateway 192.168.1.1

```
**/etc/resolv.conf的内容**

```
nameserver 192.168.1.1
```

### 3 Hadoop相关环境变量的设置

```
创建hadoop用户组
创建hadoop用户
chu888chu888@hadoopmaster:~$ sudo addgroup hadoop
[sudo] password for chu888chu888: 
Adding group `hadoop' (GID 1001) ...
Done.
chu888chu888@hadoopmaster:~$ sudo adduser -ingroup hadoop hadoop
Adding user `hadoop' ...
Adding new user `hadoop' (1001) with group `hadoop' ...
Creating home directory `/home/hadoop' ...
Copying files from `/etc/skel' ...
Enter new UNIX password: 
Retype new UNIX password: 
passwd: password updated successfully
Changing the user information for hadoop
Enter the new value, or press ENTER for the default
	Full Name []: 
	Room Number []: 
	Work Phone []: 
	Home Phone []: 
	Other []: 
Is the information correct? [Y/n] y


给hadoop用户添加权限,打开/etc/sudoers文件
root    ALL=(ALL:ALL) ALL
hadoop  ALL=(ALL:ALL) ALL
```

### 4 hosts文件修改
**所有的主机的hosts都需要修改,在这里我吃了一个大亏,如果在etc配置文件中直接用Ip的话,可能会出现Datanode链接不上Namenode的现象.**

```
127.0.0.1       localhost
192.168.1.159   hadoopmaster
192.168.1.76    hadoopslave1
192.168.1.166   hadoopslave2

```


### 5 SSH无密码登录
**步骤一用ssh-key-gen在hadoopmaster主机上创建公钥与密钥**

```
需要注意一下,一定要用hadoop用户生成公钥,因为我们是免密钥登录用的是hadoop

hadoop@hadoopmaster:~$ ssh-keygen -t rsa
Generating public/private rsa key pair.
Enter file in which to save the key (/home/hadoop/.ssh/id_rsa): 
Created directory '/home/hadoop/.ssh'.
Enter passphrase (empty for no passphrase): 
Enter same passphrase again: 
Your identification has been saved in /home/hadoop/.ssh/id_rsa.
Your public key has been saved in /home/hadoop/.ssh/id_rsa.pub.
The key fingerprint is:
cf:98:06:01:29:81:ff:82:5b:d3:6d:5d:53:b6:a7:75 hadoop@hadoopmaster
The key's randomart image is:
+--[ RSA 2048]----+
| .....           |
|. . ..      o    |
| . .  .    o .   |
|  .    .  o . o E|
| . o ...S. . + . |
|. + o o..=  .    |
| o o .  + o      |
|.      .         |
|                 |
+-----------------+


```

**步骤二保证hadoopmaster登录自已是有效的**
```
cd .ssh
cat ./id_rsa.pub >> ./authorized_keys
```

**步骤三将公钥拷贝到其他主机上**
```
scp ~/.ssh/id_rsa.pub hadoop@hadoopslave1:/home/hadoop/
scp ~/.ssh/id_rsa.pub hadoop@hadoopslave2:/home/hadoop/
```

**步骤四 在其他二个节点上做的工作**
```
mkdir ~/.ssh       # 如果不存在该文件夹需先创建，若已存在则忽略
cat ~/id_rsa.pub >> ~/.ssh/authorized_keys
rm ~/id_rsa.pub    # 用完就可以删掉了
```
## 三 hadoopslave1 2主机配置

### 1 JDK安装
同上
### 2 IP设置
同上
### 3 Hadoop相关环境变量的设置
同上
### 4 hosts文件修改
同上
### 5 SSH无密码登录
同上

## 四 hadoopmaster的安装

### 1 安装
**以下操作都要以hadoop用户身份进行**

```
chu888chu888@hadoopslave1:~$ sudo tar xvfz jdk-7u79-linux-x64.gz
hadoop@hadoopmaster:~$ sudo cp -r hadoop-2.7.2 /usr/local/hadoop
hadoop@hadoopmaster:~$ sudo chmod -R 775 /usr/local/hadoop/
hadoop@hadoopmaster:~$ sudo chown -R hadoop:hadoop /usr/local/hadoop
```

**这里面有一个小的体验技巧,我建议将所有需要的环境变量配置加入到/etc/profile中,这是全局变量.**

```
export JAVA_HOME=/usr/lib/jvm/
export JRE_HOME=${JAVA_HOME}/jre
export CLASSPATH=.:${JAVA_HOME}/lib:${JRE_HOME}/lib
export PATH=${JAVA_HOME}/bin:$PATH


export JAVA_HOME=/usr/lib/jvm/
export HADOOP_INSTALL=/usr/local/hadoop
export PATH=$PATH:$HADOOP_INSTALL/bin
export PATH=$PATH:$JAVA_HOME/bin
export PATH=$PATH:$HADOOP_INSTALL/sbin
export HADOOP_MAPRED_HOME=$HADOOP_INSTALL
export HADOOP_COMMON_HOME=$HADOOP_INSTALL
export HADOOP_HDFS_HOME=$HADOOP_INSTALL
export YARN_HOME=$HADOOP_INSTALL
export HADOOP_COMMON_LIB_NATIVE_DIR=$HADOOP_INSTALL/lib/native
export HADOOP_OPTS="-Djava.library.path=$HADOOP_INSTALL/lib"

```

还有一个问题就是,在启动hadoop的时候经常会出现,找不到JAVA_HOME的问题,这个问题可以通过修改hadoop环境变量来解决,直接写死变量就可以了.

```
hadoop@hadoopmaster:/usr/local/hadoop/etc/hadoop$ ls
capacity-scheduler.xml  hadoop-metrics2.properties  httpfs-signature.secret  log4j.properties            ssl-client.xml.example
configuration.xsl       hadoop-metrics.properties   httpfs-site.xml          mapred-env.cmd              ssl-server.xml.example
container-executor.cfg  hadoop-policy.xml           kms-acls.xml             mapred-env.sh               yarn-env.cmd
core-site.xml           hdfs-site.xml               kms-env.sh               mapred-queues.xml.template  yarn-env.sh
hadoop-env.cmd          httpfs-env.sh               kms-log4j.properties     mapred-site.xml.template    yarn-site.xml
hadoop-env.sh           httpfs-log4j.properties     kms-site.xml             slaves
hadoop@hadoopmaster:/usr/local/hadoop/etc/hadoop$ sudo nano hadoop-env.sh 

$ more hadoop-env.sh
export JAVA_HOME=/usr/lib/jvm/
```

### 2 配置集群环境
集群/分布式模式需要修改 /usr/local/hadoop/etc/hadoop 中的5个配置文件，更多设置项可点击查看官方说明，这里仅设置了正常启动所必须的设置项： slaves、core-site.xml、hdfs-site.xml、mapred-site.xml、yarn-site.xml 。

### 3 配置文件内容-slaves
文件 slaves，将作为 DataNode 的主机名写入该文件，每行一个，默认为 localhost，所以在伪分布式配置时，节点即作为 NameNode 也作为 DataNode。分布式配置可以保留 localhost，也可以删掉，让 hadoopmaster 节点仅作为 NameNode 使用。  
本教程让 hadoopmaster 节点仅作为 NameNode 使用，因此将文件中原来的 localhost 删除，只添加二行内容：hadoopslave1 hadoopslave2。

```
hadoop@hadoopmaster:/usr/local/hadoop/etc/hadoop$ more slaves 
hadoopslave1
hadoopslave2
```


###4 配置文件内容-core-site.xml
```
<configuration>
    <property>
        <name>fs.defaultFS</name>
        <value>hdfs://hadoopmaster:9000</value>
    </property>
    <property>
        <name>hadoop.tmp.dir</name>
        <value>file:/usr/local/hadoop/tmp</value>
        <description>Abase for other temporary directories.</description>
    </property>
</configuration>

```

###5 配置文件内容 hdfs-site.xml
**dfs.replication 一般设为 3，但我们只有二个 Slave 节点，所以 dfs.replication 的值还是设为 2：**

```
<configuration>
    <property>
        <name>dfs.namenode.secondary.http-address</name>
        <value>hadoopmaster:50090</value>
    </property>
    <property>
        <name>dfs.replication</name>
        <value>2</value>
    </property>
    <property>
        <name>dfs.namenode.name.dir</name>
        <value>file:/usr/local/hadoop/tmp/dfs/name</value>
    </property>
    <property>
        <name>dfs.datanode.data.dir</name>
        <value>file:/usr/local/hadoop/tmp/dfs/data</value>
    </property>
</configuration>
```
###6 配置文件 - mapred-site.xml 
**（可能需要先重命名，默认文件名为 mapred-site.xml.template），然后配置修改如下：**

```
<configuration>
    <property>
        <name>mapreduce.framework.name</name>
        <value>yarn</value>
    </property>
    <property>
        <name>mapreduce.jobhistory.address</name>
        <value>hadoopmaster:10020</value>
    </property>
    <property>
        <name>mapreduce.jobhistory.webapp.address</name>
        <value>hadoopmaster:19888</value>
    </property>
</configuration>
```
###7 配置文件 - yarn-site.xml

```
<configuration>
    <property>
        <name>yarn.resourcemanager.hostname</name>
        <value>hadoopmaster</value>
    </property>
    <property>
        <name>yarn.nodemanager.aux-services</name>
        <value>mapreduce_shuffle</value>
    </property>
</configuration>
```



## 五 hadoopslave1 2节点需要做的
首先通过sftp把hadoop配置好的hadoop打包,之后转输到Slave节点上,配置好环境变量JDK PATH SSH 基本上与Master是一样的.  
配置好后，将 Master 上的 /usr/local/Hadoop 文件夹复制到各个节点上。因为之前有跑过伪分布式模式，建议在切换到集群模式前先删除之前的临时文件。  
在 Master 节点上执行：  

```
cd /usr/local
sudo rm -r ./hadoop/tmp
sudo rm -r ./hadoop/logs/*  
tar -cvfz ~/hadoop.master.tar.gz ./hadoop   
```

在Slave节点上执行:

```
sudo rm -r /usr/local/hadoop    # 删掉旧的（如果存在）
sudo tar -xvfz ~/hadoop.master.tar.gz -C /usr/local
sudo chown -R hadoop:hadoop /usr/local/hadoop

```

## 六 开始启动集群
```
hdfs namenode -format       # 首次运行需要执行初始化，之后不需要
在Master上执行:
$start-dfs.sh
$start-yarn.sh
$mr-jobhistory-daemon.sh start historyserver

Centos6.X需要关闭防火墙
sudo service iptables stop   # 关闭防火墙服务
sudo chkconfig iptables off  # 禁止防火墙开机自启，就不用手动关闭了
Cent7
systemctl stop firewalld.service    # 关闭firewall
systemctl disable firewalld.service # 禁止firewall开机启动

```

之后分别在Master与Slave上执行jps,会看到不同的结果.缺少任一进程都表示出错。另外还需要在 Master 节点上通过命令 hdfs dfsadmin -report 查看 DataNode 是否正常启动，如果 Live datanodes 不为 0 ，则说明集群启动成功。例如我这边一共有 1 个 Datanodes：

```

$jps
$hdfs dfsadmin -report

可以访问http://192.168.1.159:50070/ 查看结果

```

![](../images/4/jps1.png)
![](../images/4/jps2.png)



## 七 执行分布式的实验-分布式存储
```
执行分布式实例过程与伪分布式模式一样，首先创建 HDFS 上的用户目录：
$ hdfs dfs -mkdir -p /user/hadoop
将 /usr/local/hadoop/etc/hadoop 中的配置文件作为输入文件复制到分布式文件系统中：
$ hdfs dfs -mkdir input
$ hdfs dfs -put /usr/local/hadoop/etc/hadoop/*.xml input

```
通过查看 DataNode 的状态（占用大小有改变），输入文件确实复制到了 DataNode 中，如下图所示：
![](../images/4/hdfssave.png)

## 八 执行分布式的实验-MapReduce
执行MapReduce作业

```
hadoop jar /usr/local/hadoop/share/hadoop/mapreduce/hadoop-mapreduce-examples-*.jar grep /user/hadoop/input /user/hadoop/output 'dfs[a-z.]+'
查看http://192.168.1.159:8088/cluster看结果
```

运行时的输出信息与伪分布式类似，会显示 Job 的进度。

可能会有点慢，但如果迟迟没有进度，比如 5 分钟都没看到进度，那不妨重启 Hadoop 再试试。若重启还不行，则很有可能是内存不足引起，建议增大虚拟机的内存，或者通过更改 YARN 的内存配置解决。   
同样可以通过 Web 界面查看任务进度 http://master:8088/cluster，在 Web 界面点击 “Tracking UI” 这一列的 History 连接，可以看到任务的运行信息，如下图所示：

![](../images/4/yarnfinish.png)

```
关闭集群
stop-yarn.sh
stop-dfs.sh
mr-jobhistory-daemon.sh stop historyserver
```

## 九 同步时间

在安装hadoop环境的过程中,时间同步是非常重要的一个服务.因为分布式系统如果时间不同步会造成系统中的许多问题.

### 1 在线安装
ntp在线安装的方式很简单，只需要执行以下命令即可帮你安装好ntp以及所有的依赖包

```
sudo apt-get install ntp
```


### 2 离线安装
如果要离线安装，那么就需要下载ntp安装包和依赖包。我们可以在一个有线环境下运行上面的在线安装，然后到/var/cache/apt/archives这个目录下拷贝完整的ntp安装包和依赖包。

```
dpkg  -i  libopts25_1%3a5.12-0.1ubuntu1_amd64.deb 
dpkg  -i  ntp_1%3a4.2.6.p3+dfsg-1ubuntu3.1_amd64.deb 
```
当然还有更加简单的方法，将下载的deb包拷贝到/var/cache/apt/archives目录下，然后在执行一下命令同样可以安装。

```
sudo apt-get install ntp

```

安装完毕以后我们可以查看服务是否启动，执行以下命令：

```
hadoop@hadoopmaster:~$ sudo service --status-all
 [ + ]  acpid
 [ + ]  apparmor
 [ ? ]  apport
 [ + ]  atd
 [ ? ]  console-setup
 [ + ]  cron
 [ - ]  dbus
 [ ? ]  dns-clean
 [ + ]  friendly-recovery
 [ - ]  grub-common
 [ ? ]  irqbalance
 [ ? ]  killprocs
 [ ? ]  kmod
 [ ? ]  networking
 [ + ]  ntp
 [ ? ]  ondemand
 [ ? ]  pppd-dns
 [ - ]  procps
 [ ? ]  rc.local
 [ + ]  resolvconf
 [ - ]  rsync
 [ + ]  rsyslog
 [ ? ]  screen-cleanup
 [ ? ]  sendsigs
 [ - ]  ssh
 [ - ]  sudo
 [ + ]  udev
 [ ? ]  umountfs
 [ ? ]  umountnfs.sh
 [ ? ]  umountroot
 [ - ]  unattended-upgrades
 [ - ]  urandom

```
可以看到ntp服务已经启动（[+]表示已经启动。）

### 3 配置文件 /etc/ntp.conf
建议在配置之前进行备份

```
driftfile /var/lib/ntp/ntp.drift
statistics loopstats peerstats clockstats
filegen loopstats file loopstats type day enable
filegen peerstats file peerstats type day enable
filegen clockstats file clockstats type day enable
server ntp.ubuntu.com
restrict -4 default kod notrap nomodify nopeer noquery
restrict -6 default kod notrap nomodify nopeer noquery
restrict 192.168.1.0 mask 255.255.255.0 nomodify
restrict 127.0.0.1
restrict ::1
```

### 4 同步时间

```
sudo ntpdate 192.168.1.159

```
## 十 FAQ
### 1 出现的问题

```

hadoop@hadoopmaster:~$ start-dfs.sh
16/07/18 20:45:04 WARN util.NativeCodeLoader: Unable to load native-hadoop library for your platform... using builtin-java classes where applicable
Starting namenodes on [hadoopmaster]
hadoopmaster: starting namenode, logging to /usr/local/hadoop/logs/hadoop-hadoop-namenode-hadoopmaster.out
hadoopslave1: starting datanode, logging to /usr/local/hadoop/logs/hadoop-hadoop-datanode-hadoopslave1.out
hadoopslave2: starting datanode, logging to /usr/local/hadoop/logs/hadoop-hadoop-datanode-hadoopslave2.out
Starting secondary namenodes [hadoopmaster]
hadoopmaster: starting secondarynamenode, logging to /usr/local/hadoop/logs/hadoop-hadoop-secondarynamenode-hadoopmaster.out
16/07/18 20:45:20 WARN util.NativeCodeLoader: Unable to load native-hadoop library for your platform... using builtin-java classes where applicable


```

**解决方法**

```
首先下载hadoop-native-64-2.4.0.tar：
http://dl.bintray.com/sequenceiq/sequenceiq-bin/hadoop-native-64-2.4.0.tar
如果你是hadoop2.6的可以下载下面这个：
http://dl.bintray.com/sequenceiq/sequenceiq-bin/hadoop-native-64-2.6.0.tar
下载完以后，解压到hadoop的native目录下，覆盖原有文件即可。操作如下：
tar -x hadoop-native-64-2.4.0.tar -C  hadoop/lib/native/

再将环境变量导入到系统中.修改/etc/profile

export JAVA_LIBRARY_PATH=/usr/local/hadoop/lib/native

```
