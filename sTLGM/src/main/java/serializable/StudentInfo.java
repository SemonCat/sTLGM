package serializable;

import java.io.Serializable;

public class StudentInfo implements Serializable{

    private String sid, fb, name, dep, group, imgUrl;

    public StudentInfo(String id, String fb, String name, String dep,
                       String group, String imgUrl) {
        this.sid = id;
        this.fb = fb;
        this.name = name;
        this.dep = dep;
        this.group = group;
        this.imgUrl = imgUrl;
    }

    public String getGid() {
        return this.group;
    }

    public String getSid() {
        return this.sid;
    }

    public String getFb() {
        return this.fb;
    }

    public String getName() {
        return this.name;
    }

    public String getDep() {
        return this.dep;
    }

    public String getImgUrl() {
        return this.imgUrl;
    }
}
