package com.thu.stlgm.util;

/**
 * Created by SemonCat on 2014/3/25.
 */
public class StringUtils {

    public static String join(String join,String[] strAry){
        StringBuffer sb=new StringBuffer();
        for(int i=0;i<strAry.length;i++){
            if(i==(strAry.length-1)){
                sb.append(strAry[i]);
            }else{
                sb.append(strAry[i]).append(join);
            }
        }

        return new String(sb);
    }
}
