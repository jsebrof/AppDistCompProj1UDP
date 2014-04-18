import java.io.*;
import java.net.*;

class UDPServer
{
	public static void main(String args[]) throws Exception
	{
		System.out.println("Server Start");
		System.out.println("Enter any input and press enter to stop server:");
		DatagramSocket serverSocket = new DatagramSocket(9833);
		byte[] receiveData = new byte[1024];
		byte[] sendData = new byte[1024];
		BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
		@SuppressWarnings("unused")
		String userInput;
		while((userInput = stdIn.readLine()) == null)
		{
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			serverSocket.receive(receivePacket);
			String sentence = new String( receivePacket.getData());
			System.out.println("RECEIVED: " + sentence);
			InetAddress IPAddress = receivePacket.getAddress();
			int port = receivePacket.getPort();
			String capitalizedSentence = sentence.toUpperCase();
			sendData = capitalizedSentence.getBytes();
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
			serverSocket.send(sendPacket);
		}
		System.out.println("Server Stop");
		serverSocket.close();
	}
}