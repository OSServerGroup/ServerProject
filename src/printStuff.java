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
    
    printStuff(Lock lock){
        this.lock = new ReentrantLock();
    }
    
    public void run() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        System.out.println();
        for(int i=0;i<1000;i++){
            try{
                if(this.isInterrupted()){
                    System.out.println("interrupted");
                    synchronized (this.lock){
                        this.lock.wait();
                    }
                    
                    System.out.println("unlocked");
                    continue;
                }

                System.out.print(" "+i+" ");
            }
            catch(InterruptedException e){
                System.out.println("waiting");
            }
        }
    }

}
