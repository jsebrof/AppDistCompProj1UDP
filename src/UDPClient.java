import java.net.*;

class UDPClient
{
	public static void main(String args[]) throws Exception
	{
		System.out.println("Client Start");
		
		String serverIP = args[0];
		int port = Integer.parseInt(args[1]);
		String operation = args[2];
		String key = args[3];
		String value = "";
		if (operation.toLowerCase().matches("put"))
		{
			value = args[4];
		}
		
		DatagramSocket clientSocket = new DatagramSocket();
		InetAddress IPAddress = InetAddress.getByName(serverIP); // n01
		
		byte[] sendData = new byte[1024];
		sendData = operation.getBytes();
		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
		System.out.println("Sending packets to the server");
		clientSocket.send(sendPacket);
		sendData = key.getBytes();
		sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
		clientSocket.send(sendPacket);
		if (operation.toLowerCase().matches("put"))
		{
			sendData = value.getBytes();
			sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
			clientSocket.send(sendPacket);
		}
		
		if (operation.toLowerCase().matches("get"))
		{
			byte[] receiveData = new byte[1024];
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			clientSocket.receive(receivePacket);
			String modifiedSentence = new String(receivePacket.getData());
			System.out.println("FROM SERVER:" + modifiedSentence.trim());
		}
						
		clientSocket.close();
		System.out.println("Client Close");
	}
}
