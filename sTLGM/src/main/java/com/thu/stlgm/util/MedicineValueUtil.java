package com.thu.stlgm.util;

import com.thu.stlgm.bean.Blood;

import java.util.Random;

/**
 * Created by SemonCat on 2014/3/2.
 */
public class MedicineValueUtil {
    public static final Blood getSmallMedicine(int currentBlood,int reward,int Interval,int MAX_BLOOD){
        int targetBlood = new Random().nextInt(41)+30;
        if (currentBlood+targetBlood>MAX_BLOOD){
            targetBlood = MAX_BLOOD-currentBlood;
        }
        int bloodMills = (int)(Interval*((targetBlood/(float)MAX_BLOOD)));
        return new Blood(targetBlood,bloodMills);
    }

    public static final Blood getBigMedicine(int currentBlood,int reward,int Interval,int MAX_BLOOD){
        int targetBlood = new Random().nextInt(41)+30;
        if (currentBlood+targetBlood>MAX_BLOOD){
            targetBlood = MAX_BLOOD-currentBlood;
        }
        int bloodMills = (int)(Interval*((targetBlood/(float)MAX_BLOOD)));
        return new Blood(targetBlood,bloodMills);
    }
}
