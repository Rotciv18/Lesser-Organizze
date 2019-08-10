package com.rotciv.organizze.activity.helper;

import java.text.SimpleDateFormat;

public class DateUtil {

    public static String dataAtual(){

        long date = System.currentTimeMillis();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String dataString = simpleDateFormat.format(date);
        return dataString;
    }

    public static String reduzData (String data){
        String retornoData[] = data.split("/");
        String novaData = (retornoData[1] + retornoData[2]);
        return novaData;
    }

}
