package iie.tools;
import java.util.Map;
/**
 * @author xczhao
 * @date 2020/10/31 - 12:32
 */
public class NmsDepartmentNode {
	private String id;
	private String pId;
	private String name;
	private String desc;
	private Map<String, String> font;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getpId() {
		return pId;
	}

	public void setpId(String pId) {
		this.pId = pId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Map<String, String> getFont() {
		return font;
	}

	public void setFont(Map<String, String> font) {
		this.font = font;
	}

}
