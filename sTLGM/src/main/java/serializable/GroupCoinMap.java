package serializable;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class GroupCoinMap implements Serializable {

    private Map<String, GroupCoinInfo> coinMap = new HashMap<String, GroupCoinInfo>();

    public void putCoinMap(String gid, int weekCoin, int totalCoin) {
        GroupCoinInfo coinInfo = new GroupCoinInfo(weekCoin, totalCoin);
        coinMap.put(gid, coinInfo);
    }

    public Map<String, GroupCoinInfo> getCoinInfoMap() {
        return this.coinMap;
    }
}
