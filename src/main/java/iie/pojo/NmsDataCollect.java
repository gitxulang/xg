/**
 * 
 */
package iie.pojo;

/**
 * @author TianYu
 * 2019-4-1
 * 原始收集数据
 */
public class NmsDataCollect {
	private int type;
	private int index;
	private String ip;
	private String data;
	
	public NmsDataCollect(int type, int index, String ip, String data) {
		super();
		this.type = type;
		this.index = index;
		this.ip = ip;
		this.data = data;
	}
	
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}

}
