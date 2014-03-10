package serializable;

import java.io.Serializable;

public class GroupCoinInfo implements Serializable {
    private int weekCoin = 0;
    private int totalCoin = 0;

    public GroupCoinInfo(int weekCoin, int totalCoin) {
        this.totalCoin = totalCoin;
    }

    public int getWeekCoin() {
        return this.weekCoin;
    }

    public int getTotalCoin() {
        return this.totalCoin;
    }
}
