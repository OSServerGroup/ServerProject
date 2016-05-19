package multithreadedserver;
import java.io.*;
import java.net.*;
import java.util.*;

import javax.tools.FileObject;

import java.text.SimpleDateFormat;
import java.nio.file.*;

public final class MyWebServer {

	private final static String SERVERSTRING = "Server: AnkitaMukherjee";
	private static int PORT;
	private static String FILEPATH ;


	private static final Map<String, String> map = new HashMap<String, String>() {{
		put("html", "text/html");
		put("css", "text/css");
		put("png", "image/png");
		put("jpg", "image/jpg");
		put("jpeg", "image/jpeg");
		put("js", "application/js");
	}};

	private static void display(String code, String mime, int length, DataOutputStream out, String time) throws Exception {
		System.out.println(" (" + code + ") ");
		out.writeBytes("HTTP Code:" + code + " \r\n");
		out.writeBytes("Content-Type: " + map.get(mime) + "\r\n");
		out.writeBytes("Content-Length: " + length + "\r\n");
		out.writeBytes("Date: " + time + "\r\n");
		out.writeBytes(SERVERSTRING);
		out.writeBytes("\r\n\r\n");
	}

	private static void response(String inString, DataOutputStream out,String time) throws Exception {
		String method = inString.substring(0, inString.indexOf("/")-1);
		//String version = inString.substring(inString.indexOf("/")+2, inString.lastIndexOf("/")+4);
		String file = inString.substring(inString.indexOf("/")+1, inString.lastIndexOf("/")-5);
		if(file.equals(""))
			file = "index.html";
		String mime = file.substring(file.indexOf(".")+1);

		//Return if file contains potentialy bad string
		if(file. contains(";") || file.contains("*"))	{
			System.out.println(" (Drop Connection : Bad Request)");
			return;
		}
		// Return if trying to load file outside of web server root
		Path path = Paths.get(FILEPATH, file);
		if(!path.startsWith(FILEPATH)) {
			System.out.println(" Drop connection ");
			return;
		}

		if (method.equals("POST")) {
			String responseString = "400 Bad Request";
			display("400", "html", responseString.length(), out, time);
			out.write(responseString.getBytes());
			return;
		}

		if(method.equals("GET")) {
			try {
				if((!inString.contains("HTTP/1.0")) && (!inString.contains("HTTP/1.1"))) {
					System.out.println(" (Dropping connection)");
					 throw new IOException();
				}
				// Open file
				byte[] fileBytes = null;
				//if(file.endsWith("/"))
				//	file = file +"index.html";
                System.out.println("file path is: "+FILEPATH+file);
				File fileObject = new File (FILEPATH+file);

				if(fileObject.exists() &&(fileObject.canRead()== false)){
					String responseString = " 403 forbidden";
					display("403", "html", responseString.length(), out,time);
					out.write(responseString.getBytes());
					return;

				}
				InputStream is = new FileInputStream(FILEPATH+file);
				fileBytes = new byte[is.available()];
				is.read(fileBytes);
				display("200", mime, fileBytes.length, out,time);
				out.write(fileBytes);
				System.out.println(file);

			} catch(FileNotFoundException e) {
				// Try to use 404.html
				try {
					byte[] fileBytes = null;
					InputStream is = new FileInputStream(FILEPATH+"404.html");
					fileBytes = new byte[is.available()];
					is.read(fileBytes);
					display("404", "html", fileBytes.length, out,time);
					out.write(fileBytes);
				} catch(FileNotFoundException e2) {
					String responseString = "404 File Not Found";
                                    
					display("404", "html", responseString.length(), out,time);
					out.write(responseString.getBytes());
                                        out.write(inString.getBytes());
				}
			} catch(IOException e3) {
				String responseString = " 400 Bad Request";
				display("400", "html", responseString.length(), out,time);
				out.write(responseString.getBytes());
			}

		}

	}

	private static class WorkerRunnable implements Runnable {

		protected Socket socket = null;

		BufferedReader in;
		DataOutputStream out;
		String inString;
		String time;

		public WorkerRunnable(Socket connectionSocket) throws Exception {

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
				if(this.inString != null)
					response(this.inString, this.out,this.time);
				System.out.println("connectionSocket");

				this.out.flush();
				this.out.close();
				this.in.close();

			} catch (Exception e) {
				System.out.println("Error flushing and closing");
			}
		}
	}

	public static void main(String args[]) throws Exception {

		int i=0;
		for (i=0; i<args.length; i++) {
			System.out.println(" args :" + args[i]);

		}
		PORT = Integer.valueOf(args[3]);
		FILEPATH = args[1];
		ServerSocket serverSocket = new ServerSocket(PORT);

		System.out.println("Listening to connection..");

                requestManager manager = new requestManager();
                manager.start();
		for(;;) {
                    Socket connectionSocket = serverSocket.accept();
                    Thread t = new Thread(new WorkerRunnable(connectionSocket));
                    //t.start();
                    System.out.println("This step is reached");
                    System.out.println("Current thread ID: "+t.getId());
                    manager.insertTask(t);

		}
	}
}
