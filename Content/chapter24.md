# 第二十四章 Centos下的Oracle 11g安装

# Oracle安装文档
本次安装是在自己所建的虚拟机上安装的，用到了视图链接工具VNC
## 1.本次所用安装包：

<p>linux.x64_11gR2_database_1of2.zip、linux.x64_11gR2_database_2of2.zip</p>

## 2.参考链接如下：
Oracle安装参考链接：[http://www.linuxidc.com/Linux/2015-02/113222.html](http://www.linuxidc.com/Linux/2015-02/113222.html)  
VNC安装参考链接：[http://www.linuxidc.com/Linux/2015-01/112326.html](http://www.linuxidc.com/Linux/2015-01/112326.html)
Oracle 11G CentOS 6.5:
[http://www.aiplaypc.com/229.html](http://www.aiplaypc.com/229.html)
[http://blog.csdn.net/cafardhaibin/article/details/25071249](http://blog.csdn.net/cafardhaibin/article/details/25071249)

## 3.安装前的配置：
### (1)安装前的基本准备工作: 
1. 安装VMware Workstation
2. 安装CentOS，主机命名为：oracledb
3. 磁盘需要大于30G（经验值）
4. 内存必须大于1G（官方要求）
5. 操作系统swap分区大于2G（如果物理内存小于2G，则需要设置，设置值为物理内存的1-2倍，如果物理内存大于2G，则无需设置。）
6. 虚拟机网络连接方式：桥接模式(B)直接连接物理网络
7. 安装完成后设置虚拟机网络(ipv4)为固定IP地址(system-config-network)
8. 进行网络测试OK，则操作系统环境准备完毕
9. 安装虚拟机时一定要选择：先创建虚拟机后安装操作系统
10. 为了安装Oracle，故选择安装类型为：桌面版本。
11. 安装SSH Secure Shell Client并连接主机

### (2)基本的主机配置

1. 以下步骤中的命令太长的可通过：SSH Secure Shell Client 直接复制进行
2. vi基本命令：i--编辑状态  退出编辑并保存时先按ESC键，再按符合“:wq”或者":x"即可
3. 注意每个步骤时的当前用户，是root还是oracle

```
step-1#修改主机名
[root@oracledb ~]# sed -i "s/HOSTNAME=localhost.localdomain/HOSTNAME=oracledb/" /etc/sysconfig/network
[root@oracledb ~]# hostname oracledb

step-2#添加主机名与IP对应记录
[root@oracledb ~]# vi /etc/hosts 
192.168.1.8    oracledb

step-3#关闭防火墙Selinux
[root@oracledb ~]# sed -i "s/SELINUX=enforcing/SELINUX=disabled/" /etc/selinux/config  
[root@oracledb ~]# setenforce 0

```
修改网络配置

```
１.修改网卡配置　编辑：vi /etc/sysconfig/network-scripts/ifcfg-eth0

DEVICE=eth0 #描述网卡对应的设备别名，例如ifcfg-eth0的文件中它为eth0
#设置网卡获得ip地址的方式，可能的选项为static，dhcp或bootp，分别对应静态指定的 ip地址，通过dhcp协议获得的ip地址，通过bootp协议获得的ip地址
BOOTPROTO=static 
BROADCAST=192.168.0.255 #对应的子网广播地址
HWADDR=00:07:E9:05:E8:B4 #对应的网卡物理地址
IPADDR=192.168.1.147 #如果设置网卡获得 ip地址的方式为静态指定，此字段就指定了网卡对应的ip地址
NETMASK=255.255.255.0 #网卡对应的网络掩码
NETWORK=192.168.1.0 #网卡对应的网络地址

2.修改网关配置
编辑：vi /etc/sysconfig/network　修改后如下：　

NETWORKING=yes(表示系统是否使用网络，一般设置为yes。如果设为no，则不能使用网络，而且很多系统服务程序将无法启动)
HOSTNAME=centos(设置本机的主机名，这里设置的主机名要和/etc/hosts中设置的主机名对应)
GATEWAY=192.168.0.1(设置本机连接的网关的IP地址。)

我在修改这里打开编辑时前三项已经默认有了所以只增加了GATEWAY

3.修改DNS 配置

编辑：vi /etc/resolv.conf　修改后如下：　
nameserver　即是DNS服务器ＩＰ地址，第一个是首选，第二个是备用。

4.重启网络服务

执行命令：
service network restart 　或 　 /etc/init.d/network restart

```


### (3)安装以下RPM软件包（加32bit括号注解的是32位版本的软件包，对应同名未加注解的则是64位版本的该软件包。在64位版本平台上，两种版本都要安装）
> binutils-2.17.50.0.6  
> compat-libstdc++-33-3.2.3  
> compat-libstdc++-33-3.2.3 (32 bit)  
> elfutils-libelf-0.125  
> elfutils-libelf-devel-0.125  
> gcc-4.1.2  
> gcc-c++-4.1.2  
> glibc-2.5-24  
> glibc-2.5-24 (32 bit)  
> glibc-common-2.5  
> glibc-devel-2.5  
> glibc-devel-2.5 (32 bit)  
> glibc-headers-2.5  
> ksh-20060214  
> libaio-0.3.106  
> libaio-0.3.106 (32 bit)  
> libaio-devel-0.3.106  
> libaio-devel-0.3.106 (32 bit)  
> libgcc-4.1.2  
> libgcc-4.1.2 (32 bit)  
> libstdc++-4.1.2  
> libstdc++-4.1.2 (32 bit)  
> libstdc++-devel 4.1.2  
> make-3.81  
> sysstat-7.0.2   

**直接执行以下的yum安装命令，若已安装会有提示**  

> yum install -y binutils*  
> yum install -y compat-libstdc*  
> yum install -y elfutils-libelf*  
> yum install -y gcc*  
> yum install -y glibc*  
> yum install -y ksh*  
> yum install -y libaio*  
> yum install -y libgcc*  
> yum install -y libstdc*  
> yum install -y make*  
> yum install -y sysstat*  
> yum install libXp* -y  
> yum install -y glibc-kernheaders   

或者通过以下方法更加快一些:

```
yum install gcc libaio libaio-devel libstdc++ libstdc++-devel libgcc elfutils-libelf-devel glibc-devel glibc-devel gcc-c++ compat-libstdc++-33 unixODBC unixODBC-devel

```
有一个rpm包需要独立下载pdksh-5.2.14-37.el5_8.1.x86_64，然后rpm -ivh安装即可。下载地址:[pdksh-5.2.14-37.el5_8.1.x86_64](http://www.aiplaypc.com/wp-content/uploads/2015/01/pdksh-5.2.14-37.el5_8.1.x86_64.zip)

### （4）调整内核参数及用户限制
**以root用户进行配置文件的编辑**  
#### Ⅰ.编辑/etc/sysctl.conf文件，设置相关参数的系统默认值。如果该文件中已有相关参数的设置，则确保参数值不小于如下对应值；如果还没有相关参数的设置，则按照如下格式添加相应的参数设置行
	[root@oracledb ~]#vim /etc/sysctl.conf
	
	fs.aio-max-nr = 1048576
	fs.file-max = 6815744
	kernel.shmall = 2097152
	kernel.shmmax = 536870912
	kernel.shmmni = 4096
	kernel.sem = 250 32000 100 128
	net.ipv4.ip_local_port_range = 9000 65500
	net.core.rmem_default = 262144
	net.core.rmem_max = 4194304
	net.core.wmem_default = 262144
	net.core.wmem_max = 1048586 
    
    [root@oracledb ~]# sysctl -p (备注：用于输出配置后的结果，如果有错误会提示)
#### Ⅱ.编辑/etc/security/limits.conf文件，修改操作系统对oracle用户资源的限制。在该文件中添加如下行
	[root@localhost ~]#vim /etc/security/limits.conf

	oracle          soft    nproc  2047
	oracle          hard    nproc  16384
	oracle          soft    nofile  1024
	oracle          hard    nofile  65536
	oracle          hard    stack  10240

### （5）目录结构及空间规划可查看参考链接
[http://www.linuxidc.com/Linux/2015-02/113222.htm](http://www.linuxidc.com/Linux/2015-02/113222.html)

### （6）数据库安装用户和组的创建
**使用root用户，进行如下操作：**  
创建oinstall组

    [root@localhost ~]#groupadd -g 5000 oinstall
创建dba组

    [root@localhost ~]#groupadd -g 501 dba
创建oracle用户

    [root@localhost ~]#useradd -g oinstall -G dba oracle（执行后会在/home目录下创建oracle次目录）

### （7）创建相应的文件系统（或安装目录）并改变相应的权限（记住路径，方便之后的查找）
    [root@localhost ~]# cd /home/oracle
    [root@localhost oracle]# mkdir -p /home/oracle/app/oracle
    [root@localhost oracle]# chown -R oracle:oinstall /home/oracle/*
    [root@localhost oracle]# chmod -R 775 /home/oracle/*
    [root@localhost oracle]#
（执行命令后将Oracle安装包用Xftp导入到/home/oracle目录下解压）

### （8）数据库安装用户的profile文件的设置

**使用oracle用户进行如下操作**

<p>假设数据库（实例）名为powerdes。编辑/home/oracle/.bash_profile（./bash_profile文件是解压包解压后生成），插入以下内容（数据库实例名可以自己设置，但是要记住。.bash_profile文件是主要配置环境变量的文件，如果出现执行文件没有找到的情况，很有可能就是里边的path写错了）。</p>    

```
export ORACLE_BASE=/home/oracle/app/oracle  #这个路径为（5）中的你创建的目录
export ORACLE_HOME=/home/oracle/app/oracle/product/11.2.0/dbhome_1
export ORACLE_SID=powerdes   #数据库实例名。实例名必须记住，创数据库时会用到
export PATH=$ORACLE_HOME/bin:$PATH  #非常重要！涉及的所有的可执行的文件都在PATH下边
export ORACLE_TERM=xterm
export TNS_ADMIN=$ORACLE_HOME/network/admin
export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:$ORACLE_HOME/oracm/lib:$ORACLE_HOME/lib
export CLASSPATH=$CLASSPATH:$ORACLE_HOME/rdbms/jlib:$ORACLE_HOME/jlib:$ORACLE_HOME/network/lib
export LANG=en_US.gbk
export NLS_LANG=american_america.ZHS16GBK
export EDITOR=vi
```


### （9）准备VNC远程连接linux桌面
参考链接：[http://www.linuxidc.com/Linux/2015-01/112326p2.html](http://www.linuxidc.com/Linux/2015-01/112326p2.html)

#### Ⅰ.在Windows上安装VNC

#### Ⅱ.在linux上安装VNC
先检查一下服务器是否已经安装了VNC服务。检查服务器是否安装VNC的命令如下:  

    [root@localhost ~]# rpm -qa | grep vnc
    [root@localhost ~]#
如果没有安装vnc可以使用下面命令进行安装：  

> yum install -y tigervnc tigervnc-server  

顺利安装完，检查一下

    [root@localhost ~]# rpm -qa|grep vnc
    tigervnc-1.1.0-16.el6.CentOS.x86_64
    tigervnc-server-1.1.0-16.el6.centos.x86_64
    libvncserver-0.9.7-4.el6.x86_64
    [root@localhost ~]#

#### Ⅲ.配置VNC
编辑/etc/sysconfig/vncservers配置文件（这是设置VNC用户的文件），在该文件里编辑以下内容： 
 
	vim /etc/sysconfig/vncservers
	VNCSERVERS="1:root" #配置远程桌面登录的用户名，如果两个用户，则使用VNCSERVERS="1:user1 2:user2"(VNCSERVERS="usernumber:myusername")  
	VNCSERVERARGS[1]="-geometry 800x600" #设置分辨率,连接上服务器后也可以更改

#### Ⅳ.设置VNC密码
使用下面的命令为VNC设置密码（需要使用su命令切换到需要设置VNC密码的用户上，例如：
su - oracle，其中oracle就是在上面配置文件内写的帐号）

    [root@localhost ~]# vncpasswd
    Password:
    Password must be at least 6 characters - try again
    Password:
    Verify:
    [root@localhost ~]# 

**注意：密码至少为6个字符**

#### Ⅴ.建立VNC链接
配置VNC完成后，需要执行命令service vncserver restart重新启动VNC。然后打开安装在Windows中的VNC，输入要连接的服务器IP。注意冒号后边对应的是你设置的VNC用户对应的用户名编号。如下图所示

![](http://i.imgur.com/CLfU10O.png)

**注意：直接连接会发现连接超时。**如下图所示：

![](http://i.imgur.com/PvqeyV3.png)

解决方法：开启防火墙VNCServer端口  
首先编辑/etc/sysconfig/iptables文件

    #vim /etc/sysconfig/iptables

在“-A INPUT -m state --state NEW -m tcp -p tcp --dport 22 -j ACCEPT”下面添加一行“-A INPUT -m state --state NEW -m tcp -p tcp --dport 5901 -j ACCEPT”,然后重启iptables服务。
    
    #/etc/init.d/iptables restart

连接成功后会显示出虚拟机的桌面。然后点击system下的preferences下的display





![](http://i.imgur.com/P35jLbj.png)

点击display后会弹出如下图所示的窗口，根据自己的需求更改分辨率

![](http://i.imgur.com/gKN2jT6.png)
   
然后新打开一个终端，在命令行中执行
export DISPLAY=localhost:1.0（要连接服务器IP：vnc用户对应的编号）命令与xhost +命令 

    [root@powerlong4 rlwrap-0.37]#  xhost + 
    access control disabled, clients can connect from any host
    [root@powerlong4 rlwrap-0.37]# 

表示linux下视窗环境已经准备OK，可以进行oracle安装。如下图

![](http://i.imgur.com/N7wtCwY.png)

## 4.安装过程

### FAQ问题--用oracle用户安装报错
1.用oracle用户安装报错

```
Checking monitor: must be configured to display at least 256 colors    Failed <<<<

    >>> Could not execute auto check for display colors using command /usr/X11R6/bin/xdpyinfo. Check if the DISPLAY variable is set.
```
解决方法：

```
1、root 下先执行#xhost +

2、su oracle

3、export DISPLAY=:0.0
```

### （1）开始安装oracle
切换oracle用户然后cd到你解压完安装包的database下，执行./runInstaller。
然后vnc就会弹出安装界面窗口。过程如下各图：  
不建议填写email，下边的创建密码选项可以不选，点击next(填写有可能卡死在这个地方)

![](http://i.imgur.com/om5Uk6y.png)

选择i want to remain........，点击continue

![](http://i.imgur.com/y0hrA5e.png)

选择第二个仅安装数据库软件，点击next

![](http://i.imgur.com/z7nf3gL.png)

选择第一个，点击next

![](http://i.imgur.com/eCuyKkp.png)

选择如下图所示的languages，点击next

![](http://i.imgur.com/R4NW230.png)

选择第一个，点击next

![](http://i.imgur.com/Lg5qdlq.png)

默认点击next

![](http://i.imgur.com/gA0DH7t.png)

默认点击next

![](http://i.imgur.com/YBAR9sW.png)

按下图选择组名，点击next

![](http://i.imgur.com/1oG3Yca.png)

由于CentOS版本较高，所以Oracle11g在check的时候不识别高版本lib包，选择lgnore All，点击next

![](http://i.imgur.com/xSBhgxX.png)

点击finish开始安装

![](http://i.imgur.com/6pUsTjx.png)

安装快结束时，会弹出一个窗口通知要执行两个脚本，点击OK，再点击close结束安装

![](http://i.imgur.com/BigEi8o.png)

执行之前提示的两个脚本，分别cd进入相应的路径下，执行sh orainstRoot.sh和sh root.sh如下图

![](http://i.imgur.com/SjYi6pz.png)

![](http://i.imgur.com/mOcWjAo.png)

至此oracle安装已经完成

###（2）数据库建库
**注意：以下命令都是在oracle用户下执行的。**
在执行以下所需的命令时，如果不能弹出相应的设置窗口，可以执行命令service vncserver restart将VNC重新启动一下，然后再将视图环境重新设置一遍：执行

> export DISPLAY=localhost:1.0  
> xhost +   

显示如下图： 

![](http://i.imgur.com/VkL3QA5.png)

然后再执行相应的执行命令。

在建库前首先创建监听器，切换用户oracle，cd到安装时配置环境变量中的export ORACLE_HOME下的bin中，执行./netca，会在vnc上弹出添加监听器的窗口。需要注意的是在选定监听程序的协议的时候，选择TPC协议，选择标准端口1521（可以参考下列各图）  

选择Listener configuration，点击next

![](http://i.imgur.com/aLwyOFb.png)

默认选择Add，点击next

![](http://i.imgur.com/y2uN7zV.png)

设置监听器名称，点击next

![](http://i.imgur.com/rMK5EkX.png)

默认选择TCP，点击next

![](http://i.imgur.com/cEC3AOv.png)

默认选择Use the standard port number of 1521,点击next

![](http://i.imgur.com/ZaOkvID.png)

默认选择No，点击next

![](http://i.imgur.com/JBa73Nb.png)

点击next

![](http://i.imgur.com/sV3VqNH.png)

点击next后点击finish完成监听器创建。创建完监听器后执行./dbca文件创建数据库。  

点击Next

![](http://i.imgur.com/qL3Rx04.png)

选择Create a Database，点击Next

![](http://i.imgur.com/RNqz7e7.png)

选择Custom Database，点击Next

![](http://i.imgur.com/PR5d4Zl.png)

Global Database Name框：输入前面设定的数据库名  
SID框：自动出现和数据库名相同的内容作为数据库实例名，单实例情况下不作改动  
点击Next
<p>注意：下图的global database name必须跟之前.bash_profile里边设的ORACLE_SID的名字相同。</p>

![](http://i.imgur.com/2PzCE3F.png)

默认所选，点击Next

![](http://i.imgur.com/oJx6vu3.png)

Use Different Administrative Passwords表格的Password和Confirm Password列中分别为User Name列SYS、SYSTEM、DBSNMP和SYSMAN用户输入口令并重复一次输入（如密码设置过于简单，下一步前会有弹出窗口提示确认接受安全风险）sys和system密码设置可以在数据库建立后修改。  
这里选择使用相同密码。点击Next

![](http://i.imgur.com/U7pGgzi.png)

提示设置的密码过于简单存在安全风险是否继续

![](http://i.imgur.com/enz5G6o.png)

按照图示选择，点击Next

![](http://i.imgur.com/GKSs0PS.png)

默认所选，点击Next

![](http://i.imgur.com/MNloHy6.png)

默认所选，点击Next

![](http://i.imgur.com/DCXY2QT.png)

默认所选，点击Character Sets

![](http://i.imgur.com/y1oDAJV.png)

按照图示选择兼容utf8的选项，点击Next

![](http://i.imgur.com/DSz2Oba.png)

在File Location Variables里可以看到一些基础参数信息。点击Next

![](http://i.imgur.com/fXkIU4G.png)

选择Generate Database Creation Scripts，默认为/home/oracle/app/oracle/admin/powerdes/scripts，点击Finish

![](http://i.imgur.com/hU30qQz.png)

点击OK

![](http://i.imgur.com/gAkhmho.png)

点击OK

![](http://i.imgur.com/7J8tK13.png)

等待创建

![](http://i.imgur.com/BW8Wi4x.png)

点击Exit完成创建

![](http://i.imgur.com/U8BelDC.png)

创建完数据库后启动数据库  
如果在切换用户进入数据库时，执行lsnrctl start或是执行sqlplus “/ as sysdba ”报出如下错误

> bash:lsnrctl:command not found.  
> bash:sqlplus:command not found.

可能是因为在切换oracle用户时没有加“-”，如#su - oracle。   

注：su 和 su - 的区别  
su是不更改环境变量的，而su -是要更改环境变量的。也就是说su只是获得了oracle的权限，su -是切换到oracle并获得oracle的环境变量及执行权限。

### (3)Linux下操作数据库的步骤：
 Ⅰ.以oracle用户登录  
 Ⅱ.执行：lsnrctl start （启动监听）  
 Ⅲ.执行：sqlplus /nolog   
 Ⅳ.执行：sql>conn /as sysdba   
 Ⅴ.执行：sql>startup  
 Ⅵ.进行数据库操作  
 Ⅶ.执行：sql>shutdown  
 Ⅷ.执行：sql>quit  
 Ⅸ.执行：lsnrctl stop（关闭监听）  

**注意：如果执行sql>conn /as sysdba时出现Connected to an idle instance.那么先执行sql>startup，后执行sql>conn /as sysdba** 

### (4)创建Oracle的启动脚本

复制以上脚本内容，在/etc/init.d目录下创建一个名为oracle的文件，然后黏贴进去，保存退出，然后chmod +x 给它执行权限，别忘了chkconfig添加开机启动以及防火墙规则的添加，到此数据库就安装完成了。

chkconfig参考:[http://www.cnblogs.com/panjun-Donet/archive/2010/08/10/1796873.html](http://www.cnblogs.com/panjun-Donet/archive/2010/08/10/1796873.html)

```
#!/bin/bash
# chkconfig: 2345 90 10
export ORACLE_BASE=/home/oracle/app/oracle
export ORACLE_HOME=/home/oracle/app/oracle/product/11.2.0/dbhome_1
export ORACLE_SID=powerdes
export PATH=$PATH:$ORACLE_HOME/bin
ORCL_OWN="oracle"
# if the executables do not exist -- display error
if [ ! -f $ORACLE_HOME/bin/dbstart -o ! -d $ORACLE_HOME ]
then
   echo "Oracle startup: cannot start"
   exit 1
fi
# depending on parameter -- start, stop, restart
# of the instance and listener or usage display
case "$1" in
start)
# Oracle listener and instance startup
echo -n "Starting Oracle: "
su - $ORCL_OWN -c "$ORACLE_HOME/bin/dbstart"
touch /var/lock/subsys/oradb
su - $ORCL_OWN -c "$ORACLE_HOME/bin/emctl start dbconsole"
echo "OK"
;;
stop)
# Oracle listener and instance shutdown
echo -n "Shutdown Oracle: "
su - $ORCL_OWN -c "$ORACLE_HOME/bin/emctl stop dbconsole"
su - $ORCL_OWN -c "$ORACLE_HOME/bin/dbshut"
rm -f /var/lock/subsys/oradb
echo "OK"
;;
reload|restart)
$0 stop
$1 start
;;
*)
echo "Usage: 'basename $0' start|stop|restart|reload"
exit 1
esac
exit 0
```



