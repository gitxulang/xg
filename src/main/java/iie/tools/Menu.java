package iie.tools;

import java.util.List;

public class Menu {

    private String dName;

    private List<MenuNode> nodeList;

    public Menu(String dName, List<MenuNode> nodeList) {
        this.dName = dName;
        this.nodeList = nodeList;
    }

    public String getdName() {
        return dName;
    }

    public void setdName(String dName) {
        this.dName = dName;
    }

    public List<MenuNode> getNodeList() {
        return nodeList;
    }

    public void setNodeList(List<MenuNode> nodeList) {
        this.nodeList = nodeList;
    }
}
