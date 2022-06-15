package iie.service;

import iie.pojo.NmsAssetType;
import iie.pojo.NmsDepartment;
import iie.pojo.NmsRole;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Service;

@Service("NmsAssetTypeService")
public class NmsAssetTypeService {
	@Autowired
	HibernateTemplate hibernateTemplate;
	
	@SuppressWarnings("unchecked")
	public List<NmsAssetType> getAll() {
		String hsql = "from NmsAssetType";
		return (List<NmsAssetType>) hibernateTemplate.find(hsql);
	}
	
	@SuppressWarnings("unchecked")
	public NmsAssetType findById(int id) {
		String hsql = "from NmsAssetType na where na.id = ?";
		List<NmsAssetType> list = (List<NmsAssetType>) hibernateTemplate.find(hsql,id);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return new NmsAssetType();
	}
	
}
