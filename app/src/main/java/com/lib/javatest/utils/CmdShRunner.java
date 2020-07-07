package com.lib.javatest.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

public class CmdShRunner {
    public static String exeCmd(String[] commands){
        List<String> lCommand=Arrays.asList(commands);
        ProcessBuilder processBuilder=new ProcessBuilder(lCommand);
        processBuilder.redirectErrorStream(true);
        InputStream is=null;
        BufferedReader bs=null;
        String result="";
        try {
            Process p=processBuilder.start();
            is=p.getInputStream();
            bs=new BufferedReader(new InputStreamReader(is));
            p.waitFor();
            if (p.exitValue() != 0){
                //错误处理
            }

            String line=null;
            while ((line=bs.readLine()) != null){
                line+="\n";
                result+=line;
            }
            return result;
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
        catch (IOException  e) {
            e.printStackTrace();
        }finally {
            try {
                if (bs != null){
                    bs.close();
                }
                is.close();
                return "err";
            }catch (Exception e){
                e.printStackTrace();
                return "err";
            }

        }

    }
}
