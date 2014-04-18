import java.net.*;
import java.util.HashMap;

class UDPServer
{
	public static void main(String args[]) throws Exception
	{
		long timestart = System.currentTimeMillis();
		HashMap<String, String> store = new HashMap<String, String>();
		int port = Integer.parseInt(args[0]);
		System.out.println("Server Start at " + (System.currentTimeMillis()-timestart));
		DatagramSocket serverSocket = new DatagramSocket(port); // just kill the server to release the socket
		
		byte[] receiveOperationData = new byte[1024];
		byte[] receiveKeyData = new byte[1024];
		byte[] receiveValueData = new byte[1024];
		byte[] sendData = new byte[1024];
		
		while(true)
		{
			System.out.println("Waiting to receive message at " + (System.currentTimeMillis()-timestart));
			DatagramPacket receiveOperationPacket = new DatagramPacket(receiveOperationData, receiveOperationData.length);
			DatagramPacket receiveKeyPacket = new DatagramPacket(receiveKeyData, receiveKeyData.length);
			DatagramPacket receiveValuePacket = new DatagramPacket(receiveValueData, receiveValueData.length);
			
			serverSocket.receive(receiveOperationPacket);
			serverSocket.receive(receiveKeyPacket);
			
			String operation = new String(receiveOperationPacket.getData());
			operation = operation.trim().toLowerCase();
			
			if (operation.matches("put"))
			{
				serverSocket.receive(receiveValuePacket);
			}
			
			String key = new String(receiveKeyPacket.getData());
			key = key.trim();
			String value = new String(receiveValuePacket.getData());
			value = value.trim();
			InetAddress IPAddress = receiveOperationPacket.getAddress();
			int clientPort = receiveOperationPacket.getPort();
			String returnMessage = "";
			System.out.println("Request from: " + IPAddress + ":" + clientPort + " for " + operation + " " + key + " " + value + " at " + (System.currentTimeMillis()-timestart));
			
			
			switch (operation)
			{
			case "put":
				store.put(key, value);
				System.out.println("Key \"" + key + "\" Value \"" + store.get(key) + "\" stored at " + (System.currentTimeMillis()-timestart));
				break;
			case "get":
				System.out.println("Value \"" + store.get(key) + "\" for Key \"" + key + "\" returned to " + IPAddress + ":" + clientPort + " at " + (System.currentTimeMillis()-timestart));
				returnMessage = store.get(key);
				break;
			case "delete":
				store.remove(key);
				System.out.println("Key \"" + key + "\" Value \"" + store.get(key) + "\" deleted at " + (System.currentTimeMillis()-timestart));
				break;
			}

			if (operation.matches("get"))
			{
				sendData = returnMessage.getBytes();
				DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, clientPort);
				serverSocket.send(sendPacket);
			}
		}
	}
}