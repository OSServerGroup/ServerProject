/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package multithreadedserver;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author Richard
 */
public class WorkerThread extends Thread {
        protected Socket socket = null;
        
        String FILEPATH;
        BufferedReader in;
        DataOutputStream out;
        String inString;
        String time;
        Lock lock;

        public WorkerThread(Socket connectionSocket,String filePath) throws Exception {
            
            this.FILEPATH = filePath;
            this.lock = new ReentrantLock();
            this.socket = connectionSocket;
            this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            this.out = new DataOutputStream(this.socket.getOutputStream());

            this.inString = this.in.readLine();
            System.out.print("Request are :" + this.inString );
            Calendar cal = Calendar.getInstance();
            cal.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            time = "[" + sdf.format(cal.getTime()) + "] ";
            System.out.print(time + this.socket.getInetAddress().toString() + " " + this.inString);
        }

        public void run() {
            try{
            if(this.inString != null){

                //content of response()
                long startTime = System.nanoTime();
                String method = inString.substring(0, inString.indexOf("/")-1);
                //String version = inString.substring(inString.indexOf("/")+2, inString.lastIndexOf("/")+4);
                String file = inString.substring(inString.indexOf("/")+1, inString.lastIndexOf("/")-5);
                String mime = file.substring(file.indexOf(".")+1);
                if(method.equals("GET")) {
                    try {
                            byte[] fileBytes = null;

                            System.out.println("file path is: "+FILEPATH+file);
                            File fileObject = new File (FILEPATH+file);
                            long checkDoneTime = System.nanoTime();
                            InputStream is = new FileInputStream(FILEPATH+file);
                            fileBytes = new byte[is.available()];

                            is.read(fileBytes);

                            long readDoneTime = System.nanoTime();
                            //display("200", mime, fileBytes.length, out,time);

                            //out.write(fileBytes);

                            for(Byte b:fileBytes){
                                if(isInterrupted()){
                                    //lock
                                    System.out.println("\nInterrupted\n");
                                    synchronized (this.lock){
                                        this.lock.wait();
                                        System.out.println("lock obtained");
                                    }
                                }
                                out.write(b);
                            }

                            long writeDoneTime = System.nanoTime();
                            System.out.println(file);

                            long totalTime = writeDoneTime - startTime;
                            long checkTime = checkDoneTime - startTime;
                            long readTime = readDoneTime - checkDoneTime;
                            long writeTime = writeDoneTime - readDoneTime;
                            long checkPercent = checkTime*100/totalTime;
                            long readPercent = readTime*100/totalTime;
                            long writePercent = writeTime*100/totalTime;

                            System.out.println("Check: "+checkPercent+"%");
                            System.out.println("Read: "+readPercent+"%");
                            System.out.println("Write: "+writePercent+"%");



                    } catch(Exception e) {
                        e.printStackTrace();
                        //System.out.println("File not found");

                    } 

            }

                    System.out.println("connectionSocket");

                    this.out.flush();
                    this.out.close();
                    this.in.close();
            }
                } catch (Exception e) {
                        System.out.println("Error flushing and closing");
                }
        }
}
