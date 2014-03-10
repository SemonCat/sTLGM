package serializable;

import java.io.Serializable;

public class StudentHp implements Serializable {

    private String sid;
    private int deathTimes;
    private long ttl;

    private int interval;

    public StudentHp(String sid, long ttl, int deathTimes, int interval) {
        this.sid = sid;
        this.ttl = ttl;
        this.deathTimes = deathTimes;
        this.interval = interval;
    }

    public String getSid() {
        return this.sid;
    }

    public int getHpPercent() {

        int intervalSeconds = interval * 60; // 區間秒數
        long hpEnd = ttl;
        long now = System.currentTimeMillis();

        int offsetSeconds = (int) ((hpEnd - now) / 1000); // 還剩幾秒鐘

        if (offsetSeconds < 0) {
            return 0;
        } else {
            int percent = (int) (offsetSeconds * 100 / intervalSeconds);
            return percent;
        }

    }

    public int getDeathTimes() {
        return deathTimes;
    }
}
