package serializable;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class StudentInfoMap implements Serializable {

    private Map<String, StudentInfo> stuMap = new HashMap<String, StudentInfo>();

    public StudentInfoMap() {

    }

    public void putStudent(String sid, StudentInfo student) {
        stuMap.put(sid, student);
    }

    public Map<String, StudentInfo> getStudentMap() {
        return this.stuMap;
    }

}
