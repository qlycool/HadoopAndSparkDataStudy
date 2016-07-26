
# 附录一：POC实施前准备


##第一章 OS & Linux

1、检查操作系统及其版本

查看方法：
```
cat etc/issue
```

2、检查hostname,FQDN,DNS

查看方法：
```
hostname

hostname-f

cat /etc/resolv.conf
```

更改方法：


```
vim /etc/sysconfig/network

vim /etc/hosts

□ hostname只能是以字母和数字的组合(中间允许’-‘)，不能有“,” / ”.” / “_”等特殊字符

vim /etc/resolv.conf

```

3、检查机器硬件配置

查看方法：

```
磁盘：df -h
内存：free -g (free -m)
网络：ethtool eth0 \ ifconfig
CPU:cat /proc/cpuinfo
```

4、检查机器时间

查看方法：date

更改方法：date -s '2016-3-3 9:00:00'

5、需要了解的Linux命令：

- 文件／文件夹操作类：

    **cd、mkdir 、rm 、cp 、mv 、touch 、du -h --max-depth=1**

- 查看文本：
    **cat、less、tail、vim、vi**

- 查找类：
    **grep、find**

- 压缩解压缩：**tar、gzip、zip、unzip**

- 帮助类：**man**
- 时间类：**date**
- IO类：***iostat -mx 3**
- 权限相关类：**sudo -u、chown、chmod、chgrp**
- 端口连接类：**netstat －nlap、ping IP、telnet IP端口**
- 查看文件占用：**lsof**
- 启停服务：**etc/init.d/mysqld [start|stop|restart]**
- 网页类：**elinks http://192.168.1.210:60010**
- 挂载：**mount、unmount**




    

