package UDP;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

/**
 * @author alfre
 *
 */
public class UDPHost {
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		DatagramSocket ds = new DatagramSocket(23);
		DatagramSocket ds_toClient = new DatagramSocket();
		DatagramSocket ds_Server = new DatagramSocket();
		ds_Server.connect(InetAddress.getLocalHost(),69);
		
		byte[] b1;// = new byte[1024];
		DatagramPacket dp;// = new DatagramPacket(b1,b1.length);
		
		DatagramPacket dp_toServer;
		byte[] byteToServer;
		
		byte[] bytefromServer;
		DatagramPacket dp_fromServer;// = new DatagramPacket(bytefromServer,bytefromServer.length);
		
		byte[] bytetoClient;// = new byte[1024];
		DatagramPacket dp_toClient;// = new DatagramPacket(bytetoClient,bytetoClient.length,InetAddress.getLocalHost(),dp.getPort());
		
		//Using the while loop to ensure this happens 11 times
		int i=0;
		while(i<11) {
			b1 = new byte[100];
			dp = new DatagramPacket(b1,b1.length);
			//waiting for input from the client
			try {
				ds.receive(dp);
			}catch (IOException e) {
		         e.printStackTrace();
		         System.exit(1);
			}
			b1 = shrinkByte(b1);
			//prints received bytes
			printThings(b1,0);
			//sends said data to Server
			byteToServer = b1;
			dp_toServer = new DatagramPacket(byteToServer,byteToServer.length);
			printThings(byteToServer,1);
			ds_Server.send(dp_toServer);
			
			//Waits on the Server
			bytefromServer = new byte[4];
			dp_fromServer = new DatagramPacket(bytefromServer,bytefromServer.length);
			try {
				ds_Server.receive(dp_fromServer);
			}catch (IOException e) {
		         e.printStackTrace();
		         System.exit(1);
			}
			
			//Print what is received from the server
			printThings(bytefromServer,0);
			//form a packet to send back to the Client
			bytetoClient = bytefromServer;
			dp_toClient = new DatagramPacket(bytetoClient,bytetoClient.length,InetAddress.getLocalHost(),dp.getPort());
			//prints and sends
			printThings(bytetoClient,1);
			ds_toClient.send(dp_toClient);
			i++;
		}
		ds_toClient.close();
		ds_Server.close();
		ds.close();
	}
	
	/**
	 * I created this method to make is easier to print later on,
	 * and send the right sized byte.
	 * Because the byte coming in is put into a byte array of size 100, but not all of that will be used
	 * @param input the array to be shrunken
	 * @return the shrunken array
	 */
	static byte[] shrinkByte(byte input[]) {
		int i;
		//find the "end" of the byte array
		for(i=0;i<input.length;i++) {
			if((input[i]==0)&&(input[i+1]==0)) {
				break;
			}
		}
		i++;
		//use arraycopy to move the input into a new byte array with the size i we already found
		byte[] finalByte = new byte[i];
		System.arraycopy(input, 0, finalByte, 0, i);
		return finalByte;
	}
	
	/**
	 * 
	 * @param input an array of byte to be printed
	 * @param a the variable to differentiate if it's a recieved or a sent file
	 */
	static void printThings(byte input[], int a) {
		//Byte Side
		if(a==0) {
			System.out.println("The Received file in Byte format: ");
		}else {
			System.out.println("The Sent file in Byte format: ");
		}
		System.out.print("[");
		int i =0;
		//prints out the bytes using a for loop through the whole byte array
		System.out.print(input[i]);
		for(i=1;i<input.length;i++) {
			System.out.print(", "+input[i]);
		}
		System.out.println("]");
		//String Side
		if(a==0) {
			System.out.println("The Received file in String format: ");
		}else {
			System.out.println("The Sent file in String format: ");
		}
		//uses the StandardCharsets.UFT_8 to convert the bytes to String
		//I attempted to use input.toString, but that didn't achieve the desired results
		System.out.print("[");
		String s  = new String(input,StandardCharsets.UTF_8);
		System.out.print(s);
		System.out.println("]");
	}                      
}
