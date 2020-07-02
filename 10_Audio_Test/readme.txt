1、本例子使用的是AudioTrack 来播放的例子 

2、主要的点
	1）初始化 at= AudioTrack（） 
	2）播放 at.play
	3）播放文件： at.write( )
	
	
3、处理的点
	在录音 或者 播放这种耗时操作的时候 使用线程