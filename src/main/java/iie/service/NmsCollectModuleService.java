package iie.service;

import iie.pojo.NmsCollectModule;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Service;

@Service("NmsCollectModuleServcie")
public class NmsCollectModuleService {
	@Autowired
	HibernateTemplate hibernateTemplate;
	
	@SuppressWarnings("unchecked")
	public List<NmsCollectModule> getAll() {
		String hsql = "from NmsCollectModule";
		return (List<NmsCollectModule>) hibernateTemplate.find(hsql);
	}
}
