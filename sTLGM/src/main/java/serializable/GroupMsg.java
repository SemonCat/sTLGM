package serializable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by SemonCat on 2014/2/20.
 */
public class GroupMsg implements Serializable {

    private Map<String, ArrayList<String>> gMsg = new HashMap<String, ArrayList<String>>();

    public GroupMsg(Map<String, ArrayList<String>> _msg) {
        this.gMsg = _msg;
    }

    public Map<String, ArrayList<String>> getMsg() {
        return this.gMsg;
    }
}

