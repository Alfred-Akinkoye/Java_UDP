package UDP;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;

/**
 * @author alfre
 *
 */
public class UDPClient {
	public static String filename = "Test.txt";
	public static byte[] filename_Byte = filename.getBytes();
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		int i=0;
		int request;
		DatagramSocket ds = new DatagramSocket();
		InetAddress ia = InetAddress.getLocalHost();
		byte[] send_byte;
		DatagramPacket dp; //= new DatagramPacket(send_byte,send_byte.length,ia,23);
		
		byte[] b1;
		DatagramPacket dp1; // = new DatagramPacket(b1,b1.length,ia,dp.getPort());
		
		//Using the while loop to ensure this happens 11 times
		while(i<11) {
			//using this to alternate between read and write requests
			if(i%2==0) {
				request=0;
			}else {
				request=1;
			}
			send_byte = sortByte(request);
			printThings(send_byte,1);
			
			//send info
			dp = new DatagramPacket(send_byte,send_byte.length,ia,23);
			ds.send(dp);
			
			//receive info
			b1 = new byte[4];
			dp1 = new DatagramPacket(b1,b1.length,ia,dp.getPort());
			ds.receive(dp1);
			
			//print output
			printThings(dp1.getData(),0);
			i++;
		}
		// TODO Auto-generated method stub
		
		ds.close();
	}
	/**
	 * 
	 * @param type this is an integer to differentiate whether I'm making a write or read array
	 * @return trial which is the combination of all the bytes in the appropriate order
	 * and the right length.
	 */
	static byte[] sortByte(int type) {
		byte[] start = new byte[2];
		byte[] mode = "netascii".getBytes();
		byte[] zero = {0};
		int sum = 0;
		start[0] = 0;
		if(type==0) {
			start[1] = 1;
		}else {
			start[1] = 2;
		}
		byte[] finalByte = new byte[1024];
		//using arraycopy to move everything into the same byte array
		System.arraycopy(start, 0, finalByte, sum, start.length);
		sum+=start.length;
		System.arraycopy(filename_Byte, 0, finalByte, sum, filename_Byte.length);
		sum+=filename_Byte.length;
		System.arraycopy(zero, 0, finalByte, sum, zero.length);
		sum+=zero.length;
		System.arraycopy(mode, 0, finalByte, sum, mode.length);
		sum+=mode.length;
		System.arraycopy(zero, 0, finalByte, sum, zero.length);
		sum+=zero.length;
		//using trial as a way to trim finalByte to ensure only what is needed is what is sent
		byte[] trial = new byte[sum];
		System.arraycopy(finalByte, 0, trial, 0, sum);
		return trial;
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

