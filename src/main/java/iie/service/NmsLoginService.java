package iie.service;

import iie.pojo.NmsAdmin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Service;

@Service("NmsLoginService")
public class NmsLoginService {
	
	@Autowired
	HibernateTemplate hibernateTemplate;
	
	public List<NmsAdmin> getCode() {
		String hsql = "from NmsAdmin na where na.deled = 0";
		return (List<NmsAdmin>) hibernateTemplate.find(hsql);
	}
	
}
