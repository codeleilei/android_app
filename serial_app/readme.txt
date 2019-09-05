1）
	jni为c库文件 android在linux系统中有自己的驱动  所以 这里的c库 实际给予android官方串口驱动而编写的应用
2）
	1、java中serial_util.java、serialport_JNI.java为串口实际相关  
	2、serial_util.java是serialport_JNI.java的封装
	3、serialport_JNI.java为jni的java部分 用来加载c库

3）
	libs文档就是生成的各个架构的.so文件

 
主要参考文章：
	1）https://blog.csdn.net/u010312949/article/details/80199018
	2）https://www.jb51.net/article/138350.htm

