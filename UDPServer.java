package UDP;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;

/**
 * @author alfre
 *
 */
public class UDPServer {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		DatagramSocket ds = new DatagramSocket(69);
		DatagramPacket dp;// = new DatagramPacket(b1,b1.length);
		byte[] b1;// = new byte[1024];
		byte[] b2 = new byte [4];
		InetAddress ia = InetAddress.getLocalHost();
		DatagramPacket dp1; // = new DatagramPacket(b2,b2.length,ia,dp.getPort());
		int i=0;
		while(i<11) {
			b1 = new byte[100];
			dp = new DatagramPacket(b1,b1.length);
			ds.receive(dp);
			//put it into the acceptable format
			b1 = shrinkByte(b1);
			//print out in byte and string format
			printThings(b1,0);
			//check Validity
			if(!isValid(b1)) {
				System.out.println("The input is not vallid!!");
				System.exit(1);
			}
			if(isWrite(b1)) {
				b2[0]=0;
				b2[1]=4;
				b2[2]=0;
				b2[3]=0;
			}else {
				b2[0]=0;
				b2[1]=3;
				b2[2]=0;
				b2[3]=1;
			}
			
			dp1 = new DatagramPacket(b2,b2.length,ia,dp.getPort());
			printThings(b2,1);
			ds.send(dp1);
			i++;
		}
		ds.close();
	}
	
	/**
	 * 
	 * @param input the byte array being checked
	 * @return true if the input has the write format, false otherwise
	 */
	static boolean isWrite(byte input[]) {
		if(input[1]==1) {
			return true;
		}else {
			return false;
		}
	}
	
	/**
	 * A long series of if statements that basically goes through 
	 * the format the byte should be in
	 * @param input the byte we are testing validity on
	 * @return boolean true if it's valid, and false otherwise
	 */
	static boolean isValid(byte input[]) {
		int i=0;
		if(input[i]==0) {
			i++;
			if(input[i]==1 ||input[i]==2) {
				//the read and write format has been confirmed
				i++;
				if(input[i]!=0) {
					for(;i<input.length;i++) {
						try {	//This is here in case I reach the end of the input array without finding what I'm looking for
							if((input[i]==0)&&(input[i+1]!=0)) {
								//the first set of strings should end with a zero, and another set of strings
								break;
							}
						}catch (Exception e) {
							//if we reach the end of the array within the first string, then it doesn't meet the required format
							System.out.println("Not Valid");
							return false;
						}
					}
				}else {
					return false;
				}
				i++;
				if(i<input.length) {
					for(;i<input.length;i++) {
						try {	//This is here in case I reach the end of the input array without finding what I'm looking for
							if((input[i]==0)&&(input[i+1]!=0)) {
								//the second set of strings should end with a zero, and nothing more
								break;
							}
						}catch (Exception e) {
							System.out.println("Reached the end of the array");
							//if we reach the end of the array within the second string, then it's acceptable
							//as long as the last value in the byte is zero
							if(input[i]==0) {
								return true;
							}else {
								return false;
							}
						}
					}
					return true;
				}else {
					return false;
				}
					
				}else {
					return false;
				}
			}else {
				return false;
			}
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
