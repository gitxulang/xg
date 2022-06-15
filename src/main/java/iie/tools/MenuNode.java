package iie.tools;

public class MenuNode {

    private String typeName;

    private Integer count;

    public MenuNode(String typeName,Integer count){
        this.typeName=typeName;
        this.count = count;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
