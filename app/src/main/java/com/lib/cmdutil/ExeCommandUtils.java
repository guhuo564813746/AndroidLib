package com.lib.cmdutil;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ExeCommandUtils {
//    shell进程
    private Process process;
//    对应进程3个流
    private BufferedReader successResult;
    private BufferedReader errorResult;
    private DataOutputStream os;
//    是否同步
    private boolean bSynchronous;
//    表示shell是否还在运行
    private boolean bRunning=false;
//    同步锁
    ReadWriteLock lock=new ReentrantReadWriteLock();
//    保存执行结果
    private StringBuffer result=new StringBuffer();

    public ExeCommandUtils(boolean synchronous){
        bSynchronous=synchronous;
    }

    public ExeCommandUtils(){
        bSynchronous=true;
    }

    public boolean isRunning(){
        return bRunning;
    }

    public String getResult(){
        Lock readLock=lock.readLock();
        readLock.lock();
        try {
            String res=new String(result);
            Log.i("getResult",res);
            return res;
        }finally {
            readLock.unlock();
        }
    }

    /*
    * 执行命令
    * */
    public ExeCommandUtils run(String command, final int maxTime){
        Log.i("Run--","command--"+command+"maxTime--"+maxTime);
        if (command ==null || command.length() ==0){
            return this;
        }
        try {
            process=Runtime.getRuntime().exec("sh");
        }catch (Exception e){
            return this;
        }
        bRunning=true;
        successResult=new BufferedReader(new InputStreamReader(process.getInputStream()));
        errorResult=new BufferedReader(new InputStreamReader(process.getErrorStream()));
        os=new DataOutputStream(process.getOutputStream());
        try {
            os.write(command.getBytes());
            os.writeBytes("\n");
            os.flush();
            os.writeBytes("exit\n");
            os.flush();
            os.close();
            //开启超时关闭
            if (maxTime >0){
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(maxTime);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        try {
                            int ret=process.exitValue();
                        }catch (IllegalThreadStateException e){
                            process.destroy();
                        }
                    }
                }).start();
            }
            //开一个线程来处理input流
            final Thread inputThread=new Thread(new Runnable() {
                @Override
                public void run() {
                    String line;
                    Lock writeLock=lock.writeLock();
                    try {
                        while((line=successResult.readLine()) != null){
                            line+="\n";
                            writeLock.lock();
                            result.append(line);
                            writeLock.unlock();
                        }
                    }catch (Exception e){
                        Log.i("inputThread","read inputstream exception"+e.toString());
                    }finally {
                        try {
                            successResult.close();
                            Log.i("inputThread","read inputstream over");
                        }catch (Exception e){
                            Log.i("inputThread","close inputstream exception"+e.toString());
                        }
                    }
                }
            });
            inputThread.start();

            //开一个线程处理error流
            final Thread errorInputThread=new Thread(new Runnable() {
                @Override
                public void run() {
                    String line;
                    Lock writeLock=lock.writeLock();
                    try {
                        while ((line=errorResult.readLine()) != null){
                            line +="\n";
                            writeLock.lock();
                            result.append(line);
                            writeLock.unlock();
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                        Log.i("errorInputThread","read errorInputStream exception"+e.toString());
                    }finally {
                        try {
                            errorResult.close();
                            Log.i("errorInputThread","read errorInputStream over");
                        }catch (Exception e){
                            Log.i("errorInputThread","read errorInputStream exception"+e.toString());
                        }
                    }
                }
            });
            errorInputThread.start();
            Thread runThread=new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        // 等待执行完毕
                        inputThread.join();
                        errorInputThread.join();
                        process.waitFor();
                    }catch (Exception e){

                    }finally {
                        bRunning=false;
                        Log.i("runThread","runCommand end");
                    }
                }
            });
            runThread.start();
            if (bSynchronous){
                runThread.join();
                Log.i("run--","end");
            }

        }catch (Exception e){
            e.printStackTrace();
            Log.i("run--","run command exception"+e.toString());
        }
        return this;
    }

}
