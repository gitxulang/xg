package iie.service;

import iie.pojo.NmsFunction;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Service;

@Service("NmsFunctionService")
public class NmsFunctionService {

	@Autowired
	HibernateTemplate hibernateTemplate;

	@SuppressWarnings("unchecked")
	public List<NmsFunction> getAll() {
		String hsql = "from NmsFunction";
		return (List<NmsFunction>) hibernateTemplate.find(hsql);
	}

	public boolean saveFunction(NmsFunction nf) {
		if (findByFunctionDesc(nf.getFunctionDesc()) == null) {
			try {
				nf.setId((int) 0);
				hibernateTemplate.save(nf);
			} catch (Exception e) {
				// TODO: handle exception
				return false;
			}
			return true;
		} else {
			System.out.println("存在重复记录！");
			return false;
		}

	}

	public NmsFunction findById(int id) {
		String hsql = "from NmsFunction na where na.id = ?";
		@SuppressWarnings("unchecked")
		List<NmsFunction> list = (List<NmsFunction>) hibernateTemplate.find(
				hsql, id);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return new NmsFunction();
	}

	public NmsFunction findByFunctionDesc(String functionDesc) {
		String hsql = "from NmsFunction na where na.functionDesc = ?";
		@SuppressWarnings("unchecked")
		List<NmsFunction> list = (List<NmsFunction>) hibernateTemplate.find(
				hsql, functionDesc);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return new NmsFunction();
	}
	

}
