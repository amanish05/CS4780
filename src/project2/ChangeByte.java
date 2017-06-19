package project2;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;
import java.util.Scanner;

public class ChangeByte {
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		System.out.print(".signed file to temper\n");
		String fileName = sc.next();
		
		
		File tampFile = new File(fileName);
		byte[] tamperBinary = byteFetcher(tampFile);
		
		System.out.print("Total bytes (0-" + (tamperBinary.length-1) + ") to tamper: ");
		String dumpInput = sc.next();
		while(!dumpInput.matches("\\d+") || (Integer.parseInt(dumpInput) < 0 || Integer.parseInt(dumpInput) >= tamperBinary.length)){
			System.out.print("Enter an number between 0 and " + (tamperBinary.length-1) + "to temper bytes: ");
			dumpInput = sc.next();
		}
		
		int bytePosition = Integer.parseInt(dumpInput);
		
		isError(tampFile,bytePosition,tamperBinary);
		
		sc.close();
		System.exit(0);
	}

	public static byte[] byteFetcher(File file) {
						
		byte[] binaryFile = new byte[(int)file.length()];		
		
		try{			
			BufferedInputStream fileStream = new BufferedInputStream(new FileInputStream(file));
			fileStream.read(binaryFile); 
			fileStream.close();			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return binaryFile;
	}
	
	public static void isError(File tamfile, int bytePos, byte[] binarray){
		byte[] randbyte = new byte[1];
		new Random().nextBytes(randbyte);
		
		while(randbyte[0] == binarray[bytePos])
			new Random().nextBytes(randbyte);
					
		final int SIDERANGE = 2; 
		int min = 0;
		if(binarray.length > SIDERANGE * 2 + 1){
			min = bytePos - (SIDERANGE);
			while(min < 0) min++;
			while(min + (SIDERANGE * 2) > binarray.length) min--;
		}
			
		binarray[bytePos] = randbyte[0];
								
		try {
			OutputStream fileOut = null;
			try {
				fileOut = new BufferedOutputStream(new FileOutputStream(tamfile));
				fileOut.write(binarray);
			} finally {
				fileOut.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("\n The file done tempring with byte index " + bytePos);
	}
}
