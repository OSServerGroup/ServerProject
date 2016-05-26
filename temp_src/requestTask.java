/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package multithreadedserver;

import java.util.concurrent.Callable;
import java.lang.Thread.*;

/**
 *
 * @author Richard
 */
public class requestTask implements Callable<String>{

    protected long arrivalTime;
    protected long startTime;
    protected long finishTime;
    
    protected long turnAroundTime;
    protected long responseTime;
    
    int priority;
    //WorkerThread taskThread;
    printStuff taskThread;
    String name;
    
    
    protected requestTask(long arrivalTime,String name,printStuff t,int priority){
        this.arrivalTime = arrivalTime;
        this.name = name;
        this.priority = priority;
        this.taskThread = t;
    }
    
    
    @Override
    public String call() throws Exception {
        State s = this.taskThread.getState();
        System.out.println("Thread state: "+s);
        if(s.toString().equals("NEW")){
            this.taskThread.start();
            this.startTime = System.nanoTime();
            this.responseTime = this.startTime - this.arrivalTime;
        }
        else if(s.toString().equals("WAITING")){
            this.taskThread.notify();
        }
        //this.finishTime = System.nanoTime();
        //this.turnAroundTime = this.finishTime - this.arrivalTime;
        //System.out.println("Response Time: "+this.responseTime);
        //System.out.println("Turnaround Time: "+this.turnAroundTime);
        return "Thread finished before timeout!";
    }
    
    
}
