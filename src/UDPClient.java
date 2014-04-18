import java.net.*;

class UDPClient
{
	public static void main(String args[]) throws Exception
	{
		long timestart = System.currentTimeMillis();
		DatagramSocket clientSocket = new DatagramSocket();
		while (true)
		{
			System.out.println("Client Start at " + (System.currentTimeMillis()-timestart) + " milliseconds");

			String serverIP = "";
			int port = 0;
			String operation = "";
			String key = "";

			if (args.length >= 4) // If there are the minimum # of command line arguments
			{
				serverIP = args[0];
				port = Integer.parseInt(args[1]);
				operation = args[2];
				key = args[3];
			}
			else
			{
				System.out.println("Insufficient command line arguments at " + (System.currentTimeMillis()-timestart) + " milliseconds");
				break;
			}

			String value = "";
			if (args.length > 4) // A fifth command line argument in case of a put operation
			{
				value = args[4];
			}

			InetAddress IPAddress = InetAddress.getByName(serverIP);
			byte[] sendData = new byte[1024];
			
			sendData = operation.getBytes();
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
			System.out.println("Sending packets to the server at " + (System.currentTimeMillis()-timestart) + " milliseconds");
			clientSocket.send(sendPacket); // Sending operation to server
			
			sendData = key.getBytes();
			sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
			clientSocket.send(sendPacket); // Sending key to server
			
			if (operation.toLowerCase().matches("put"))
			{
				sendData = value.getBytes();
				sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
				clientSocket.send(sendPacket); // Sending value to server
			}

			if (operation.toLowerCase().matches("get"))
			{
				byte[] receiveData = new byte[1024];
				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
				clientSocket.setSoTimeout(1000); // Set a 1 second timeout for the Value packet to arrive
				try
				{
					clientSocket.receive(receivePacket); // receive value packet from server
				}
				catch (SocketTimeoutException e)
				{
					System.out.println("Timeout on server at " + (System.currentTimeMillis()-timestart) + " milliseconds");
					break; // kill the sequence, as there is no returned packet to work with
				}
				String returnedValue = new String(receivePacket.getData());
				System.out.println("Received from server: \"" + returnedValue.trim() + "\" at " + (System.currentTimeMillis()-timestart) + " milliseconds");
			}
			break; // end the client sequence
		}
		clientSocket.close();
		System.out.println("Client Close at " + (System.currentTimeMillis()-timestart) + " milliseconds");
	}
}
