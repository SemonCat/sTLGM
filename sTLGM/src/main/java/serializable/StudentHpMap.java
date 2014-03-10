package serializable;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class StudentHpMap implements Serializable {

    private Map<String, StudentHp> hpMap = new HashMap<String, StudentHp>();

    public StudentHpMap() {

    }

    public void putHp(String sid, StudentHp studentHp) {
        hpMap.put(sid, studentHp);
    }

    public Map<String, StudentHp> getHpMap() {
        return this.hpMap;
    }

}
