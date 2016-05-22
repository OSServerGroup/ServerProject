package multithreadedserver;
import java.lang.*;
import java.lang.Thread.*;
import java.io.*;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.concurrent.*;

public class requestManager implements Runnable{

    public PriorityQueue<requestTask> taskQueue;
    public Thread managerThread;
    public requestTask currentTask;
    private final long TIMEQUANTUM = 1;

    public requestManager(){
        this.taskQueue = new PriorityQueue<requestTask>();
    }
    public void insertTask(requestTask task) throws Exception{
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
    protected void FIFO(PriorityQueue<requestTask> taskQueue) throws InterruptedException{
        System.out.print("");
        if(this.taskQueue.size()!=0){
            System.out.println("Polling Queue");
            this.currentTask = taskQueue.poll();
            System.out.println("Thread: "+this.currentTask.taskThread.getId()+" started");
            this.currentTask.taskThread.start();
            long start = System.nanoTime();

            this.currentTask.taskThread.join();
            System.out.println("Task: "+this.currentTask.taskThread.getId()+" finished");
            System.out.println("Took: "+(System.nanoTime()-start)+" ns");
        }
    }
    protected void RoundRobbin(PriorityQueue<requestTask> taskQueue) throws InterruptedException{
        System.out.print("");
        if(this.taskQueue.size()!=0){
            try{
                this.currentTask = taskQueue.poll();

                ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
                //Runnable r = new Runnable(){};

            
                Future<String> future = executor.submit(this.currentTask);
                System.out.println("sleeping task begins");
                System.out.println(future.get(TIMEQUANTUM*100000, TimeUnit.NANOSECONDS));
                
            }catch(Exception e){
                e.printStackTrace();
                System.out.println("Times out!");
                this.currentTask.wait();
                this.taskQueue.add(this.currentTask);
                System.out.println("queue size: "+this.taskQueue.size());
            }
        
        }
    }
    
    /*class RRTask implements Callable<Integer> {
        
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
    
    class Task implements Callable<String> {
    @Override
    public String call() throws Exception {
        Thread.sleep(4000); // Just to demo a long running task of 4 seconds.
        return "Ready!";
        }
    }
    class SleepTask implements Runnable{
        
        @Override
        public void run(){
            try{
                this.wait(5000);
            }catch(Exception e){
                e.printStackTrace();
            }
            

            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

    }*/
    
}
