物理连接：
	两块板子对接：
	两块板子上的can h+ 对接H+
			L- 对接L-

app原理：（前提是ip这个命令 我们有权限使用）
	开启can：  命令： ip link set can名字  up
	关闭can：  命令： ip link set can名字 down
	设置can波特率  命令： ip link  set can名字  type can bitrate 波特率
主要是这几条命令的使用



1、打开app之前需要先附一下ip命令权限

root@android:/ # mount -o remount,rw /system /system
EXT4-fs (mmcblk0p5): re-mounted. Opts: (null)
root@android:/ # chmod 4777 /system/bin/ip
root@android:/ # ls -l /system/bin/ip
-rwsrwxrwx root     shell      149096 2014-05-19 09:02 ip
root@android:/ #


主要目的： 开启ip命令的权限


