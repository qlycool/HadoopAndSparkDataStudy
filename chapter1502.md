##ubuntu14.04下关于CDH5离线安装教程


在安装一系列的安装包之前，首先要解决的是ubuntu14.04的无密码传输的root用户的权限问题。

####1. 修改 root 密码
    sudo passwd root
######1.1如果使用普通用户安装时，首先创建用户和用户组：
    sudo addgroup cdh
    sudo adduser -ingroup cdh cdh
    赋予用户sudo权限：
    $ sudo nano /etc/sudoers  #也可以使用visudo编辑
    # User privilege specification
    root    ALL=(ALL:ALL) ALL
    cdh     ALL=(ALL:ALL) ALL

####2. 以其他账户登录，通过 sudo vim 修改 /etc/ssh/sshd_config :

    xxx@ubuntu14:~$ su - root

    Password:
    root@ubuntu14:~# vi /etc/ssh/sshd_config
####3. 注释掉 #PermitRootLogin without-password，添加 PermitRootLogin yes
    # Authentication:
    LoginGraceTime 120
    #PermitRootLogin without-password
    PermitRootLogin yes
    StrictModes yes
####4. 重启 ssh  服务
    root@ubuntu14:~# sudo service ssh restart
    ssh stop/waiting
    ssh start/running, process 1499
    root@ubuntu14:~#

然后切换至root用户下：

    ####1.设置Host(所有节点)
    127.0.0.1       localhost
    #127.0.1.1      ubuntu1
    192.168.1.190   ubuntu1.cdh
    192.168.1.135   ubuntu2.cdh
    192.168.1.145   ubuntu3.cdh

    # The following lines are desirable for IPv6 capable hosts
    ::1     localhost ip6-localhost ip6-loopback
    fe00::0 ip6-localnet
    ff00::0 ip6-mcastprefix
    ff02::1 ip6-allnodes
    ff02::2 ip6-allrouters
 
     
    执行命令    sudo vi /etc/hostname
    在文件里写入自己修改后的名字，与上一步ip对应的名字一样。

    
    执行命令   shutdown -r now 使配置生效。

####2.打通SSH，设置ssh无密码登陆（所有节点）
    注意如果是以普通用户安装，配置的SSH免密码登录的对象是普通用户而不是root。
    在主节点上执行 ssh-keygen -t rsa 一路回车，生成无密码的密钥对。
    将公钥添加到认证文件中： cat ~/.ssh/id_rsa.pub >> ~/.ssh/authorized_keys ，并设置
    authorized_keys的访问权限：chmod 600 ~/.ssh/authorized_keys 。
     在每个节点上都执行以上命令，然后scp文件到所有datenode节点：

    scp ~/.ssh/id_rsa.pub root@ubuntu2:/usr/local
    cat /usr/local/id_rsa.pub>>~/.ssh/authorized_keys
    chmod 600 ~/.ssh/authorized_keys
    测试： 在主节点上ssh ubuntu2.cdh，正常情况下，不需要密码就能直接登陆进去了。
####3关闭防火墙
    root@m1:~# ufw disable
###安装jdk
    scp hadoop@192.168.1.110:/home/hadoop/Hadoop/
    CDH/CDHUbuntu14.04/jdk-7u79-linux-x64.gz /usr/
    local
    
执行上述命令，获取jdk安装包

    tar -zxvf jdk-7u79-linux-x64.gz

    mv jdk1.7.0_79/ jdk
解压jdk压缩包并改名（注意如果是普通用户安装的话不要改名了，而且最好安装路径为/usr/java/jdk1.7.0_79,而且配置环境变量要在此用户的.bashrc文件里配一下，应为即使使用sudo他也是先到.bashrc找环境配置）

    vim  ~/.bashrc

    在打开的文件的末尾添加

    export JAVA_HOME=/usr/local/jdk
    export JRE_HOME=${JAVA_HOME}/jre 
    export CLASSPATH=.:${JAVA_HOME}/lib:
    ${JRE_HOME}/lib
    export PATH=${JAVA_HOME}/bin:$PATH

    保存退出，然后输入下面的命令来使之生效

    source ~/.bashrc

###安装配置MySql（主节点）

    1.scp hadoop@192.168.1.110:/home/hadoop/ 
      Hadoop/CDH/CDHUbuntu14.04/mysql-
      server_5.7.12-1ubuntu14.04_amd64.deb-
      bundle.tar /usr/local/

    2.scp hadoop@192.168.1.110:/home/hadoop/
      Hadoop/CDH/CDHCentOS6/mysql-connector-
      java-5.1.36.tar.gz /usr/local/

执行上述两行命令，分别获取MySQL安装包和MySQL的连接jar包，并且存放在/usr/local目录下

     1.scp hadoop@192.168.1.110:/home/hadoop/ 
       Hadoop/CDH/CDHUbuntu14.04/libaio1_0.3.107-3ubuntu2_amd64.deb
       /usr/local/
     2.scp hadoop@192.168.1.110:/home/hadoop/ 
       Hadoop/CDH/CDHUbuntu14.04/libmecab2_0.996-1.1_amd64.deb
       /usr/local/
上述两条命令分别获取后面安装数据库需要用到的libaio1包和libmecab2包

    tar -xvf mysql-server_5.7.12-1ubuntu14.04_amd64.deb-bundle.tar
执行上述命令解压缩包

解压开来后，一共有11个deb包，用sudo dpkg -i [包名]命令逐个安装，因为包与包中间存在依赖关系，这里安装有个先后顺序。
我的安装的顺序是：

     1.mysql-common_5.7.12-1ubuntu14.04_amd64.deb

     2.libmysqlclient20_5.7.12-1ubuntu14.04_amd64.deb

     3.libmysqlclient-dev_5.7.12-1ubuntu14.04_amd64.deb

     4.libmysqld-dev_5.7.12-1ubuntu14.04_amd64.deb

     5.而后需要安装依赖包libaio1,即  libaio1_0.3.107-3ubuntu2_amd64.deb

     而后继续：
     5.mysql-community-client_5.7.12-1ubuntu14.04_amd64.deb

     6.mysql-client_5.7.12-1ubuntu14.04_amd64.deb

     7.mysql-community-source_5.7.12-1ubuntu14.04_amd64.deb

     6.这里需要再安装一个依赖包叫libmecab2,即libmecab2_0.996-1.1_amd64.deb

     安装好后，继续安装最后一个：
     8.mysql-community-server_5.7.12-1ubuntu14.04_amd64.deb

     安装过程中需要设置数据库密码。

使用命令：mysql -uroot -p123456登录mysql，进入mysql命令行，创建以下数据库：
    
    #hive
    create database hive DEFAULT CHARSET utf8 COLLATE utf8_general_ci;
    #activity monitor
    create database amon DEFAULT CHARSET utf8 COLLATE utf8_general_ci;
    
设置root授权访问以上所有的数据库：
    
    #授权root用户在主节点拥有所有数据库的访问权限
    grant all privileges on *.* to 'root'@'ubuntu1.cdh' identified by '123456' with grant option;
    flush privileges;

####配置MySql的监听地址
    root@m1:~# cp /etc/mysql/my.cnf /etc/mysql/my.cnf.bak 
    root@m1:~# vi /etc/mysql/my.cnf 
    #bind-address = 127.0.0.1 
    bind-address = 0.0.0.0
这一步是必须的，如果不配置，后面再web上面安装的时候将会连接不到数据库

然后执行命令
   
    sudo service mysql restart
关闭防火墙   
 
    ufw disable

设置每台机器时间与主节点相同，即执行以下命令

    date -s 主节点时间


#####安装 Cloudera Manager Server 和 Agents 
    scp hadoop@192.168.1.110:/home/hadoop/Hadoop/CDH/cloudera-manager-trusty-cm5.3.9_amd64.tar.gz /opt/ 
    
将cloudera-manager的安装包放在/opt 

#
#####进入/opt目录下，对cloudera-manager安装包进行解压，使用命令：
    tar -zxvf cloudera-manager-trusty-cm5.3.9_amd64.tar.gz

解压完成之后会出现一个目录 cm-5.3.9

在所有节点创建cloudera-scm用户

    useradd --system --home=/opt/cm-5.3.9/run/cloudera-scm-server/ --no-create-home --shell=/bin/false --comment "Clousera SCM User" cloudera-scm

####为Cloudera Manager 5建立数据库
将刚才下载的MySQL的连接jar包进行解压，文件位置是在：/usr/local,解压完成之后，使用命令：

    mv /usr/local/mysql-connector-java-5.1.36/mysql-connector-java-5.1.36-bin.jar /opt/cm-5.3.9/share/cmf/lib/

将jar包放在/opt/cm-5.3.9/share/cmf/lib/目录下

在主节点初始化CM5的数据库：

    /opt/cm-5.3.9/share/cmf/schema/scm_prepare_database.sh mysql cm -hlocalhost
     -uroot -p123456 --scm-host localhost scm scm scm

####修改/opt/cm-5.3.9/etc/cloudera-scm-agent/config.ini中的server_host为主节点的主机名，例如本机主机名为server-host=ubuntu1.cdh

执行以下命令，把cm-5.3.9拷贝到其他机器的/opt文件夹

    scp -r /opt/cm-5.3.9 root@ubuntu2.cdh:/opt/

####准备Parcels，用以安装CDH5
将CHD5相关的Parcel包放到主节点的/opt/cloudera/parcel-repo/目录中（cloudera/parcel-repo需要手动创建）。

    CDH-5.3.9-1.cdh5.3.9.p0.8-trusty.parcel
    CDH-5.3.9-1.cdh5.3.9.p0.8-trusty.parcel.sha1
    manifest.json
使用命令：

    scp hadoop@192.168.1.110:/home/hadoop/Hadoop/CDH/CDH-5.3.9-1.cdh5.3.9.p0.8-trusty.parcel /opt/cloudera/parcel-repo/ 

将ftp服务器中的CDH-5.3.9-1.cdh5.3.9.p0.8-trusty.parcel放到/opt/cloudera/parcel-repo/ 目录下。然后执行以下命令把CDH-5.3.9-1.cdh5.3.9.p0.8-trusty.parcel.改名

    mv CDH-5.3.9-1.cdh5.3.9.p0.8-trusty.parcel.sha1 CDH-5.3.9-1.cdh5.3.9.p0.8-trusty.parcel.sha       

相关启动脚本

通过 /opt/cm-5.3.9/etc/init.d/cloudera-scm-server start 启动服务端。

通过 /opt/cm-5.3.9/etc/init.d/cloudera-scm-agent start 启动Agent服务。（注意普通用户安装时，从机上的Agent服务要用root用户启动，不然会有一个incepter的服务起不了，用sudo也不好使的，主机的要用cdh用户启动，因为之前配置的ssh配的就是cdh的不然又找不到其它主机）
###1.第一步

输入http://192.168.1.190:7180(ip为自己的主机ip)

![](http://i.imgur.com/GAFgmdv.png)
###2.第二步
![](http://i.imgur.com/I66Lln5.png)
###3.第三步
![](http://i.imgur.com/LgtbwCB.png)
###4.第四步
![](http://i.imgur.com/ns7hiso.png)

###5.第五步
这个是检查主机是的常见错误，我们只需要执行以下命令

    echo 0 > /proc/sys/vm/swappiness
    #貌似这句只能由root执行
![](http://i.imgur.com/gXyx2OY.png)
###6.第六步
![](http://i.imgur.com/g8J9Gl7.png)
###7.第七步
![](http://i.imgur.com/SO3eyhs.png)
###8.第八步
![](http://i.imgur.com/3cH4Ze5.png)
###9.第九步
![](http://i.imgur.com/ge9ufkA.png)
###10.第十步
终于到安装各个服务的地方了，注意，这里安装Hive的时候可能会报错，因为我们使用了MySql作为hive的元数据存储，hive默认没有带mysql的驱动，通过以下命令拷贝一个就行了：

    cp /opt/cm-5.3.9/share/cmf/lib/mysql-connector-java-5.1.36-bin.jar /opt/cloudera/parcels/CDH-5.3.9-1.cdh5.3.9.p0.8/lib/hive/lib/

另外hue数据库server启动会失败，查看日志是因为snappy导入失败造成的，这是因为snappy是用Python写成的，我们这里缺少了一个Python-libxslt1包，逐步执行以下命令即可

    aptitude install python

    aptitude update

    apt-get install Python-libxslt1


![](http://i.imgur.com/LRCDMln.png)
###11.第十一步
![](http://i.imgur.com/MYFGblV.png)

###12.第十二步
    如果hdfs上有感叹好，提示运行不良有不足的块，可在主机上执行：
    
    sudo -u oozie bash
    hadoop fs -setrep -R 1 /
    执行完稍等一会就好了。