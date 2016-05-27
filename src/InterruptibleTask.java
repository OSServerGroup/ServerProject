/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package multithreadedserver;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Richard
 */
public class InterruptibleTask implements Runnable{
    public int index;
    public final int max = 100;
    public Thread t;
    
    InterruptibleTask(){
        this.index = 0;
    }
    
    @Override
    public void run(){

        
        while(this.index<=this.max){
            System.out.println(index);
            this.index++;
        
            if(Thread.currentThread().isInterrupted()){
                System.out.println("interrupted");
                return;
            }
        }
        System.out.println("Finished");
    }
    public void start(){
        this.t = new Thread(this);
        this.t.start();
    }
    public int getIndex(){
        return this.index;
    }

    
//    
//    public static void main(String[] args) throws InterruptedException{
//        InterruptibleTask task =  new InterruptibleTask();
//        task.start();
//        Thread.sleep(1);
//        task.t.interrupt();
//        System.out.println("Index at: "+task.getIndex());
//        System.out.println(task.t.getState().toString());
//        Thread.sleep(2000);
//        task.start();
//        //task.start();
//        
//    }
    
}
