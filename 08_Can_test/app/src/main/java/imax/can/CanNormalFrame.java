package imax.can;

import android.R.integer;

public class CanNormalFrame {
	private int StdId;	//标准帧
	private int ExtId;	//扩展帧
	private byte IDE;		//表示帧类别（指明是扩展帧还是数据帧）
	private byte  RTR;	//数据帧和远程帧(保留)
	private byte Can_DLC;
	private short[] mData = new short[8];
	public String printfCanNormalFrame()
	{
		String message = "stdId:"+Integer.toHexString(this.StdId)+","+
				"ExtId:"+Integer.toHexString(this.ExtId)+"\n"+"IDE:"+this.IDE+","+
				"RTR:"+this.RTR+","+"Can_DLC:"+this.Can_DLC+"\n";
		int z;
		 StringBuffer  tStringBuf=new StringBuffer ();
		for(z=0; z<this.Can_DLC-1; z++)
		{
			tStringBuf.append((char)this.mData[z]);
			tStringBuf.append(',');
		}
		tStringBuf.append((char)this.mData[z]);
		message += tStringBuf.toString()+"\n";
		return message;
	}
	public CanNormalFrame(){
	}
	public CanNormalFrame(int stdId, int extId, byte iDE, byte rTR,
			byte can_DLC, short[] mData) {
		super();
		StdId = stdId;
		ExtId = extId;
		IDE = iDE;
		RTR = rTR;
		Can_DLC = can_DLC;
		this.mData = mData;
	}
	public int getStdId() {
		return StdId;
	}
	public void setStdId(int stdId) {
		StdId = stdId;
	}
	public int getExtId() {
		return ExtId;
	}
	public void setExtId(int extId) {
		ExtId = extId;
	}
	public byte getIDE() {
		return IDE;
	}
	public void setIDE(byte iDE) {
		IDE = iDE;
	}
	public byte getRTR() {
		return RTR;
	}
	public void setRTR(byte rTR) {
		RTR = rTR;
	}
	public byte getCan_DLC() {
		return Can_DLC;
	}
	public void setCan_DLC(byte can_DLC) {
		Can_DLC = can_DLC;
	}
	public short[] getmData() {
		return mData;
	}
	public void setmData(short[] mData) {
		this.mData = mData;
	}
	
}
