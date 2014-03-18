package serializable;

import java.io.Serializable;

public class StudentInfo implements Serializable{

    private String sid, fb, name, dep, group, imgUrl;

    private int coin;

    public StudentInfo(String id, String fb, String name, String dep,
                       String group, String imgUrl,int coin) {
        this.sid = id;
        this.fb = fb;
        this.name = name;
        this.dep = dep;
        this.group = group;
        this.imgUrl = imgUrl;
        this.coin = coin;
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

    public int getCoin() {
        return coin;
    }

    public void setCoin(int coin) {
        this.coin = coin;
    }


}
