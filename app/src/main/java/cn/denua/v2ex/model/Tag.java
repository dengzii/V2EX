package cn.denua.v2ex.model;

/*
 *
 *
 * @author denua
 * @date 2018/10/21
 */
public class Tag implements Cloneable {

    private String name;

    public Tag(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
