/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package multithreadedserver;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Richard
 */
public class printStuff extends Thread{

    public Lock lock;
    //public boolean ready;
    
    printStuff(){
        this.lock = new ReentrantLock();
        //this.ready = true;
    }
    
    public void run() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        System.out.println();
        for(int i=0;i<500;i++){
            synchronized (this){
                try{
                    if(Thread.currentThread().isInterrupted()){

                    }

                    System.out.print(" "+i+" ");
                    
                }
                catch(Exception e){                
                    e.printStackTrace();
                    //System.out.println("waiting");
                    
                    
                }
            }
        }
    }
    
    

}
