package com.thu.stlgm.game;

import com.thu.stlgm.R;

/**
 * Created by SemonCat on 2014/3/7.
 */
public class MissionContentMgr {


    //private static int[] MissionContent = new int[]{R.drawable.m1_1_123,R.drawable.m1_2,R.drawable.m1_3,R.drawable.m1_4,R.drawable.m1_5};


    private static int normal = R.drawable.missionexplanation_2;

    public static int getMissionContent(int quizid){
        if (quizid==21){
            return R.drawable.top_m21;
        }else if(quizid==22){
            return R.drawable.top_m22;
        }else if(quizid==23){
            return R.drawable.top_m23;
        }else if(quizid==41){
            return R.drawable.top_m41;
        }else if(quizid==43){
            return R.drawable.top_m43;
        }else if(quizid==51){
            return R.drawable.top_m51;
        }else if(quizid==52){
            return R.drawable.top_m52;
        }else if(quizid==53){
            return R.drawable.top_m53;
        }else if(quizid==54){
            return R.drawable.top_m54;
        }else if(quizid==55){
            return R.drawable.top_m55;
        }else if(quizid==61){
            return R.drawable.top_m61;
        }else if(quizid==62){
            return R.drawable.top_m62;
        }else if(quizid==63){
            return R.drawable.top_m63;
        }
        return R.drawable.top_m23;
    }

    public static int getNormal(){
        return normal;
    }
}
