package iie.service;

import java.util.ArrayList;
import java.util.List;

import iie.pojo.NmsAsset;
import iie.pojo.NmsStaticInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

/**
 * @author TianYu
 * 2019-4-1
 * 数据采集接口服务
 */
@Service("NmsDataCollectServcie")
public class NmsDataCollectServcie {

	@Autowired
	HibernateTemplate hibernateTemplate;
	
	public Object classifyType(int type){
		switch(type){
		case 1:	//服务器类
		//	System.out.println(1);
			break;
		case 2:	//交换机类
			break;
		case 3:	//防火墙类
			break;
		case 4:	//IDS类
			break;
		case 5:	//网关类
			break;
		}
		return null;
	}
	
	public Object classfyIndex(int index, String ip, String data){
		NmsAssetService nas = new NmsAssetService();
		NmsAsset na = nas.findByIp(ip);
		//NmsAsset na = null;
		if(na != null){
			JsonParser parser=new JsonParser();
	        JsonObject object=(JsonObject) parser.parse(data);
	        int count = object.get("count").getAsInt();         
	        JsonArray value = object.get("value").getAsJsonArray();
	        
			switch(index){
			case 1:	//服务器静态信息
				List<NmsStaticInfo> list = new ArrayList<NmsStaticInfo>();
				for(int i=0;i<count;i++){
	                JsonObject subObject = value.get(i).getAsJsonObject();
	                NmsStaticInfo nsi = new Gson().fromJson(subObject, NmsStaticInfo.class);
	                nsi.setNmsAsset(na);	
	                list.add(nsi);
	            }
				break;
			case 2:	//服务器动态信息
				break;
			case 3:	//服务器CPU信息
				break;
			case 4:	//服务器内存信息
				break;
			case 5:	//文件系统信息
				break;
			case 6:	//磁盘IO
				break;
			case 7:	//网络接口信息
				break;
			case 8:	//进程信息
				break;
			case 9:	//服务器BMC信息
				break;
			}
		}
	
		
		return null;
	}
	
	
}
