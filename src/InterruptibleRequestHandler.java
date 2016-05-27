/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package multithreadedserver;

import java.io.*;
import java.net.Socket;

/**
 *
 * @author Richard
 */
public class InterruptibleRequestHandler implements Runnable{
    
    public byte[] byteArray;
    public int index;
    public Socket socket;
    public DataOutputStream out;
    public Thread t;
    public boolean over;
    
    InterruptibleRequestHandler(byte[] byteArray,DataOutputStream out){
        System.out.println("New Handler Created");
        this.byteArray = byteArray;
        this.out = out;
        this.over = false;
    }
    
    public void start(){
        this.t = new Thread(this);
        this.t.start();
        System.out.println("Task Started");
    }
    
    public void run(){
        try{
            while(this.index<byteArray.length){
                if(this.t.isInterrupted()){
                    System.out.println("Task Interrupted");
                    return;
                }
                out.write(this.byteArray[index]);
                this.index++;
            }
            
            if(this.index>=byteArray.length){
                System.out.println("Response Complete");
                //this.socket.close();
                this.out.close();
                this.over = true;
            }

        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
