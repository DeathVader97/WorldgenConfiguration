package de.felixperko.worldgenconfig.Communication;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;

import de.felixperko.worldgenconfig.MainMisc.Main;
import de.felixperko.worldgenconfig.PropertyEditor.EditorMisc.EditorData;

public class SocketServer extends Thread{
	
	ServerSocket serviceSocket;
	Socket socket;
	BufferedReader input;
	PrintWriter output;
	
	Thread readThread = new Thread(new Runnable() {
		@Override
		public void run() {
			while (!Thread.interrupted()){
				String line;
				try {
					line = input.readLine();
					System.out.println("server read line: "+line);
					if (line.equals("updatedProperty")){
						File testFile = new File("testfile.yml");
						if (testFile.exists()){
							try {
								Main.main.stage.setGenerationComponent(((EditorData)Main.main.yaml.load(new FileInputStream(testFile))).getEndComponent());
								Gdx.app.postRunnable(new Runnable() {
								@Override
									public void run() {
										Main.main.stage.redrawTexture();
									}
								});
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				} catch (IOException e) {
					System.err.println("closing read thread because of error:");
					e.printStackTrace();
					break;
				}
			}
		}
	});
	
	private ArrayList<String> write = new ArrayList<>();
	
	@Override
	public void run(){
		try {
			
			serviceSocket = new ServerSocket(5239);
			socket = serviceSocket.accept();
			input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			output = new PrintWriter(socket.getOutputStream());
			readThread.start();
			
			while (!Thread.interrupted()){
				
				synchronized (this) {
					if (!write.isEmpty()){
						for (String s : write){
							System.out.println("write Line: "+s);
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
			serviceSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public synchronized void addOutput(String out){
		write.add(out);
	}
}
