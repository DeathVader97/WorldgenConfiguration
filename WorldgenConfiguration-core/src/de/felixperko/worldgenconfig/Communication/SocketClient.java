package de.felixperko.worldgenconfig.Communication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class SocketClient extends Thread{
	Socket socket;
	BufferedReader input;
	PrintWriter output;
	
	private ArrayList<String> write = new ArrayList<>();
	
	Thread readThread = new Thread(new Runnable() {
		@Override
		public void run() {
			while (!Thread.interrupted()){
				String line;
				try {
					line = input.readLine();
					System.out.println("client read line: "+line);
					if (line.equals("/close")){
						System.exit(0);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	});
	
	@Override
	public void run(){
		try {
			
			System.out.println("client connecting");
			socket = new Socket("localhost", 5239);
			input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			output = new PrintWriter(socket.getOutputStream());
			readThread.start();
			
			while (!Thread.interrupted()){
				
				synchronized (this) {
					if (!write.isEmpty()){
						for (String s : write){
//							System.out.println("client write Line: "+s);
							output.println(s);
						}
						write.clear();
						output.flush();
					}
				}
				try {
					Thread.sleep(5);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			input.close();
			output.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public synchronized void addOutput(String out){
		write.add(out);
	}
}
