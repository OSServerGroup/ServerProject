package multithreadedserver;
import java.lang.*;
import java.io.*;
import java.util.LinkedList;
import java.util.concurrent.*;

public class requestManager implements Runnable{

    public LinkedList<Thread> taskQueue;
    public Thread managerThread;
    public Thread currentTask;

    public requestManager(){
        this.taskQueue = new LinkedList<Thread>();
    }
    public void insertTask(Thread task) throws Exception{
        try{
            this.taskQueue.add(task);
            System.out.println("New Task Added");
        }catch(Exception e){
            e.printStackTrace();
        }

    }
    public void start(){
        if(this.managerThread==null){
            this.managerThread = new Thread(this);
            this.managerThread.start();
        }else{
            this.managerThread.start();
        }
        System.out.println("Manager Thread Started");
    }
    public void run(){
        //FIFO Scheduling
        System.out.println("Run method called");
        while(true){
            try{
               //FIFO(taskQueue);
               RoundRobbin(this.taskQueue);
            }catch(Exception e){
                e.printStackTrace();
            }
        }

    }
    protected void FIFO(LinkedList<Thread> taskQueue) throws InterruptedException{
        System.out.print("");
        if(this.taskQueue.size()!=0){
            System.out.println("Polling Queue");
            this.currentTask = taskQueue.poll();
            System.out.println("Thread: "+this.currentTask.getId()+" started");
            this.currentTask.start();
            long start = System.nanoTime();

            this.currentTask.join();
            System.out.println("Task: "+this.currentTask.getId()+" finished");
            System.out.println("Took: "+(System.nanoTime()-start)+" ns");
        }
    }
    protected void RoundRobbin(LinkedList<Thread> taskQueue) throws InterruptedException{
        System.out.print("");
        if(this.taskQueue.size()!=0){
            
            this.currentTask = taskQueue.poll();
            this.currentTask.start();
            
            this.currentTask.join();
            
            ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
            //Runnable r = new Runnable(){};
            
            try{
                executor.schedule(new Runnable(){

                    public void run(){
                        int sum = 0;
                        for(int i=0;i<20;i++){
                            for(int j=0;j<20;j++){
                                sum += i*j;
                            }
                        }
                        System.out.println("this task is completed"+" sum = "+sum);
                    }
                }, 1, TimeUnit.NANOSECONDS);
            }catch(Exception e){
                e.printStackTrace();
                System.out.println("Times out!");
            }
        
        }
    }
    
    class RRTask implements Callable<Integer> {
        
        public Thread currentTask;
        
        public RRTask(Thread currentTask){
            this.currentTask = currentTask;
        }
        
        @Override
        public Integer call() throws Exception {
            this.currentTask.start();
            return 0;
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        
    }
    
}
