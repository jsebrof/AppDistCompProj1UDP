import java.net.*;
import java.util.HashMap;

class UDPServer
{
	public static void main(String args[]) throws Exception
	{
		long timestart = System.currentTimeMillis();
		HashMap<String, String> store = new HashMap<String, String>(); // Map for storing key/value pairs
		int port = Integer.parseInt(args[0]); // Port # to listen for messages at
		System.out.println("Server Start at " + (System.currentTimeMillis()-timestart) + " milliseconds");
		DatagramSocket serverSocket = new DatagramSocket(port); // just kill the server to release the socket

		while(true)
		{
			while(true)
			{
				System.out.println("\nWaiting to receive message at " + (System.currentTimeMillis()-timestart) + " milliseconds");
				
				// Setup bytes for sending and receiving packets
				byte[] receiveOperationData = new byte[1024];
				byte[] receiveKeyData = new byte[1024];
				byte[] receiveValueData = new byte[1024];
				byte[] sendData = new byte[1024];
				
				// Setup packets for receiving
				DatagramPacket receiveOperationPacket = new DatagramPacket(receiveOperationData, receiveOperationData.length);
				DatagramPacket receiveKeyPacket = new DatagramPacket(receiveKeyData, receiveKeyData.length);
				DatagramPacket receiveValuePacket = new DatagramPacket(receiveValueData, receiveValueData.length);

				// Receive operation and key packets
				serverSocket.setSoTimeout(0); // Don't timeout on waiting for the first packet
				serverSocket.receive(receiveOperationPacket);
				// get operation from operation packet
				String operation = new String(receiveOperationPacket.getData());
				operation = operation.trim().toLowerCase();
				serverSocket.setSoTimeout(1000); // Set a 1 second timeout for the Key and Value packets to arrive
				try
				{
					serverSocket.receive(receiveKeyPacket); // receive key packet
				}
				catch (SocketTimeoutException e)
				{
					System.out.println("Timeout on partial message received: " + operation + " at " + (System.currentTimeMillis()-timestart) + " milliseconds");
					break; // Reset the innermost while loop, start back at the beginning
				}

				// get key from key packet
				String key = new String(receiveKeyPacket.getData());
				key = key.trim();
				if (operation.matches("put")) // put operation requires a third "value" packet
				{
					try
					{
						serverSocket.receive(receiveValuePacket); // receive value packet
					}
					catch (SocketTimeoutException e)
					{
						System.out.println("Timeout on partial message received: " + operation + " " + key + " at " + (System.currentTimeMillis()-timestart) + " milliseconds");
						break; // Reset the innermost while loop, start back at the beginning
					}
				}

				// get value from value packet
				// if no value packet was received, value string will be empty and not obstruct the code to follow
				String value = new String(receiveValuePacket.getData());
				value = value.trim();

				// get IP address and port of client
				InetAddress IPAddress = receiveOperationPacket.getAddress();
				int clientPort = receiveOperationPacket.getPort();
				System.out.println("Request from: " + IPAddress + ":" + clientPort + " for " + operation + " " + key + " " + value + " at " + (System.currentTimeMillis()-timestart) + " milliseconds");


				switch (operation) // depending on the requested operation
				{
				case "put":
					store.put(key, value); // place key/value into the Map
					System.out.println("Key \"" + key + "\" Value \"" + store.get(key) + "\" stored at " + (System.currentTimeMillis()-timestart) + " milliseconds");
					break;
				case "get":
					String returnMessage;
					if (store.containsKey(key))
					{
						returnMessage = store.get(key);
						System.out.println("Value \"" + store.get(key) + "\" for Key \"" + key + "\" returned to " + IPAddress + ":" + clientPort + " at " + (System.currentTimeMillis()-timestart) + " milliseconds");
					}
					else
					{
						returnMessage = "Requested value not found in store for given key";
						System.out.println("No value found in store for Key \"" + key + "\" at " + (System.currentTimeMillis()-timestart) + " milliseconds");
					}
					sendData = returnMessage.getBytes();
					DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, clientPort);
					serverSocket.send(sendPacket); // return requested value to the client
					break;
				case "delete":
					if (store.containsKey(key))
					{
						System.out.println("Key \"" + key + "\" Value \"" + store.get(key) + "\" deleted at " + (System.currentTimeMillis()-timestart) + " milliseconds");
						store.remove(key); // delete key/value from the Map
					}
					else
					{
						System.out.println("No value found in store for Key \"" + key + "\" at " + (System.currentTimeMillis()-timestart) + " milliseconds");
					}
					break;
				default:
					System.out.println("Unknown command from client: \"" + operation + " " + key + " " + value + "\" at " + (System.currentTimeMillis()-timestart) + " milliseconds");
					break;
				}
			}
		}
	}
}