1）
	jni为c库文件 android在linux系统中有自己的驱动  所以 这里的c库 实际给予android官方串口驱动而编写的应用
2）
	1、java中serial_util.java、serialport_JNI.java为串口实际相关  
	2、serial_util.java是serialport_JNI.java的封装
	3、serialport_JNI.java为jni的java部分 用来加载c库

3）
	libs文档就是生成的各个架构的.so文件
	layout 是布局文件xml
 
主要参考文章：
	1）https://blog.csdn.net/u010312949/article/details/80199018
	2）https://www.jb51.net/article/138350.htm


版本说明：
V1 :  
	1)完成串口基本功能以及api的工作
      	主要完成了串口的JNI的c库 和java端的编写和java基本函数的封装
      
	功能： 实现了基本的 从android 通过串口发送数据到pc

V2 : 
	1) 完善在点击edit的时候自动弹出软键盘的功能

	功能：输入的时候可以自动弹出软键盘

V3 ：	1)完成数据接收显示功能
	
	功能：数据接收显示功能