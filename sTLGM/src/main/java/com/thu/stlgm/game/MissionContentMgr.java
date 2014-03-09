package com.thu.stlgm.game;

import com.thu.stlgm.R;

/**
 * Created by SemonCat on 2014/3/7.
 */
public class MissionContentMgr {


    //private static int[] MissionContent = new int[]{R.drawable.m1_1_123,R.drawable.m1_2,R.drawable.m1_3,R.drawable.m1_4,R.drawable.m1_5};

    private static int[] MissionContent = new int[]{R.drawable.mis113,R.drawable.mis12,R.drawable.mis13,R.drawable.mis14,R.drawable.mis15};


    private static int normal = R.drawable.missionexplanation_2;

    public static int getMissionContent(int quizid){
        return MissionContent[quizid];
    }

    public static int getNormal(){
        return normal;
    }
}
