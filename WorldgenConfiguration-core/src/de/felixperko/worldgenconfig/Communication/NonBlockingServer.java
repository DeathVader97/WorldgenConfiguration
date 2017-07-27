package de.felixperko.worldgenconfig.Communication;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedSelectorException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.github.czyzby.kiwi.util.tuple.immutable.Pair;

public class NonBlockingServer extends Thread{
	
	int port = 5239;
	
	Selector s;
	
	Map<String, ArrayList<String>> writeList = Collections.synchronizedMap(new HashMap<>());
	List<Pair<String, String>> readList = Collections.synchronizedList(new ArrayList<>());
	
	ArrayList<String> connectedClients = new ArrayList<>();
	
	public synchronized List<Pair<String, String>> getMessages(){
		List<Pair<String, String>> res = new ArrayList<>();
		res.addAll(readList);
		readList.clear();
		return res;
	}
	
	public void writeMessage(String target, String msg){
		if (target == null)
			throw new NullPointerException("The target is null!");
		System.out.println("sending message to "+target+" - "+msg);
		ArrayList<String> list = writeList.get(target);
		if (list == null){
			list = new ArrayList<>();
			writeList.put(target, list);
		}
		list.add(msg);
	}
	
	public NonBlockingServer() {
		System.out.println("[S] preparing socket...");
		try {
			
			InetAddress hostIPAddress = InetAddress.getByName("localhost");
			
			s = Selector.open();
			ServerSocketChannel ssc;
			ssc = ServerSocketChannel.open();
			ssc.configureBlocking(false);
			ssc.socket().bind(new InetSocketAddress(hostIPAddress, port));
			ssc.register(s, SelectionKey.OP_ACCEPT);
			
			System.out.println("[S] prepared.");
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
			} catch (ClosedSelectorException e){
				System.out.println("[S] shut down...");
				break;
			} catch (Exception e){
				e.printStackTrace();
			}
		}
	}
	
	ArrayList<String> currentWrite = new ArrayList<>();
	
	private void handleSelections() throws Exception{
		if (s.select() > 0)
			processReadySet(s.selectedKeys());
	}

	private void processReadySet(Set<SelectionKey> selectedKeys) throws Exception{
		Iterator<SelectionKey> it = selectedKeys.iterator();
		while (it.hasNext()){
			SelectionKey key = it.next();
			it.remove();
			if (key.isAcceptable()){
				ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
				SocketChannel sc = ssc.accept();
				System.out.println("[S] accepted key.");
				connectedClients.add(sc.getRemoteAddress().toString());
				sc.configureBlocking(false);
				sc.register(key.selector(), SelectionKey.OP_READ | SelectionKey.OP_WRITE);
			}
			if (key.isReadable()){
				try {
					Pair<String,String> msg = processRead(key);
					if (msg != null){
						readList.add(msg);
					}
				} catch (IOException e){
					System.out.println("[S] lost connection to a client");
					connectedClients.remove(((SocketChannel)key.channel()).getRemoteAddress().toString());
					key.channel().close();
					return;
				}
			}
			if (key.isWritable()){
				synchronized (writeList) {
					ArrayList<String> messages = writeList.get(((SocketChannel)key.channel()).getRemoteAddress().toString());
					if (messages != null){
						for (String msg2 : messages)
							write(key, msg2);
						messages.clear();
					}
				}
			}
		}
	}

	private Pair<String, String> processRead(SelectionKey key) throws Exception{
		SocketChannel sChannel = (SocketChannel) key.channel();
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		int bytesCount = sChannel.read(buffer);
		if (bytesCount > 0){
			buffer.flip();
			return new Pair<String, String>(sChannel.getRemoteAddress().toString(), new String(buffer.array()).trim());
		}
		return null;
	}
	
	private void write(SelectionKey key, String msg) throws IOException{
		SocketChannel sc = (SocketChannel)key.channel();
		System.out.println("[S] write message: "+sc.getRemoteAddress().toString()+" - "+msg);
		ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
		sc.write(buffer);
	}
	
	public void close(){
		for (String target : connectedClients)
			writeMessage(target, "/close");
		while (!writeList.isEmpty() && !connectedClients.isEmpty()){}
		try {
			s.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
