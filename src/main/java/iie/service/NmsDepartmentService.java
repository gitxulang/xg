package iie.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import iie.pojo.NmsDepartment;
import iie.tools.NmsDepartmentNode;
import iie.tools.excel.UuidUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Service;


@Service("NmsDepartmentService")
public class NmsDepartmentService {
    @Autowired
    HibernateTemplate hibernateTemplate;

    @SuppressWarnings("unchecked")
    public List<NmsDepartment> getAll() {
        String hsql = "from NmsDepartment na where na.deled = 0";
        return (List<NmsDepartment>) hibernateTemplate.find(hsql);
    }

    public boolean saveDepartment(NmsDepartment nd) {
        try {
            String id = UuidUtil.get32UUID();
            nd.setId(id);
            hibernateTemplate.save(nd);
        } catch (Exception e) {
            // TODO: handle exception
            return false;
        }
        return true;
    }

    public NmsDepartment findById(String id) {
        String hsql = "from NmsDepartment nd where nd.id = ?";
        @SuppressWarnings("unchecked")
        List<NmsDepartment> list = (List<NmsDepartment>) hibernateTemplate.find(hsql, id);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return new NmsDepartment();
    }

    @SuppressWarnings("unchecked")
    public List<NmsDepartmentNode> getTree() {
        List<NmsDepartment> departments = this.getAll();
        List<NmsDepartmentNode> nodeList = new ArrayList<>();
        for (NmsDepartment department : departments) {
            NmsDepartmentNode node = new NmsDepartmentNode();
            node.setId(department.getId());
            node.setpId(department.getParentId());
            node.setName(department.getDName());
            node.setDesc(department.getDDesc());
            Map<String,String> map = new HashMap<String,String>();
            map.put("font-style", "italic");
            map.put("font-weight", "bold");
            node.setFont(map);

            nodeList.add(node);
        }
        return nodeList;
    }

    public boolean updateDepartment(NmsDepartment nd) {
        try {
            hibernateTemplate.saveOrUpdate(nd);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public NmsDepartment findByIdForUpdate(String id) {
        String hsql = "from NmsDepartment nd where nd.id = ? and nd.deled = 0";
        @SuppressWarnings("unchecked")
        List<NmsDepartment> list = (List<NmsDepartment>) hibernateTemplate.find(hsql, id);
        if (list != null && list.size() > 0) {
            NmsDepartment obj = list.get(0);
            return obj;
        }
        return new NmsDepartment();
    }

    public boolean deleteDepartment(String id) {
        NmsDepartment department = findById(id);
        department.setDeled(1);
        return updateDepartment(department);
    }
}
