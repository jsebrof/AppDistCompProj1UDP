import java.io.*;
import java.math.BigInteger;
import java.net.*;

class UDPClient
{
	public static void main(String args[]) throws Exception
	{
		System.out.println("Client Start");
		BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
		DatagramSocket clientSocket = new DatagramSocket();
		InetAddress IPAddress = InetAddress.getByName("127.0.0.1"); // n01
		byte[] sendData = new byte[1024];
		byte[] receiveData = new byte[1024];
		System.out.println("Enter something to send to the server");
		String sentence = inFromUser.readLine();
		sendData = sentence.getBytes();
		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9833);
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