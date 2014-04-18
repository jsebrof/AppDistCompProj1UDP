import java.net.*;

class UDPClient
{
	public static void main(String args[]) throws Exception
	{
		System.out.println("Client Start");
		String serverIP = args[0];
		int port = Integer.parseInt(args[1]);
		String sentence = args[2];
		System.out.println("The IP is: " + serverIP + " port: " + port + " msg " + sentence);
		DatagramSocket clientSocket = new DatagramSocket();
		InetAddress IPAddress = InetAddress.getByName(serverIP); // n01
		byte[] sendData = new byte[1024];
		byte[] receiveData = new byte[1024];
		sendData = sentence.getBytes();
		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
		clientSocket.send(sendPacket);
		DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
		System.out.println("Sending message to the server");
		clientSocket.receive(receivePacket);
		String modifiedSentence = new String(receivePacket.getData());
		System.out.println("FROM SERVER:" + modifiedSentence);
		clientSocket.close();
		System.out.println("Client Close");
	}
}
