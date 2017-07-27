package de.felixperko.worldgenconfig.Communication;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class NonBlockingClient extends Thread{
	
	int port = 5239;
	
	Selector s;
	
	List<String> writeList = Collections.synchronizedList(new ArrayList<String>());
	List<String> readList = Collections.synchronizedList(new ArrayList<String>());
	
	public synchronized List<String> getMessages(){
		ArrayList<String> res = new ArrayList<>();
		readList.forEach(e -> res.add(e.trim()));
		readList.clear();
		return res;
	}
	
	public void writeMessage(String msg){
		writeList.add(msg);
	}
	
	public NonBlockingClient() {
		System.out.println("[C] preparing socket...");
		try {
			
			InetAddress hostIPAddress = InetAddress.getByName("localhost");
			
			s = Selector.open();
			SocketChannel sc = SocketChannel.open();
			sc.configureBlocking(false);
			sc.connect(new InetSocketAddress(hostIPAddress, port));
			int operations = SelectionKey.OP_CONNECT | SelectionKey.OP_READ | SelectionKey.OP_WRITE;
			sc.register(s, operations);
			
//			ServerSocketChannel ssc;
//			ssc = ServerSocketChannel.open();
//			ssc.configureBlocking(false);
//			ssc.socket().bind(new InetSocketAddress(hostIPAddress, port));
//			ssc.register(s, SelectionKey.OP_ACCEPT);
			
			System.out.println("[C] prepared.");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void run(){
		while (!Thread.interrupted()){
			try {
				handleSelections();
				Thread.sleep(5);
			} catch (Exception e){
				e.printStackTrace();
			}
		}
	}

	private void handleSelections() throws Exception{
		if (s.select() > 0)
			processReadySet(s.selectedKeys());
	}

	private void processReadySet(Set<SelectionKey> selectedKeys) throws Exception{
		Iterator<SelectionKey> it = selectedKeys.iterator();
		while (it.hasNext()){
			SelectionKey key = it.next();
			it.remove();
			
			try {
				if (key.isConnectable()){
					boolean connected = processConnect(key);
					if (!connected)
						return;
				}
				if (key.isReadable()){
					String msg = processRead(key);
					if (msg.length() > 0){
						readList.add(msg);
					}
				}
				if (key.isWritable()){
					synchronized (writeList) {
						for (String msg : writeList)
							write(key, msg);
						writeList.clear();
					}
				}
			} catch (IOException e){
				key.cancel();
				return;
			}
		}
	}

	private boolean processConnect(SelectionKey key) throws Exception{
		SocketChannel sc = (SocketChannel) key.channel();
		while (sc.isConnectionPending()){
			sc.finishConnect();
		}
		System.out.println("[C] connected.");
		return true;
	}

	private String processRead(SelectionKey key) throws Exception{
		SocketChannel sChannel = (SocketChannel) key.channel();
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		int bytesCount = sChannel.read(buffer);
		if (bytesCount > 0){
			buffer.flip();
			return new String(buffer.array());
		}
		return "";
	}
	
	private void write(SelectionKey key, String msg) throws IOException{
		SocketChannel sc = (SocketChannel)key.channel();
		ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
		sc.write(buffer);
	}
}
