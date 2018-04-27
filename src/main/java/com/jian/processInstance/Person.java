package com.jian.processInstance;

import java.io.Serializable;

public class Person  implements Serializable{
    private static final long serialVersionUID = -6250211815378979592L;
    private String id;
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
