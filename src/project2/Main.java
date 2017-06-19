package project2;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Main {
	
	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		while (true) {
			
			System.out.printf("Menu:%n  0   Exit%n  1  \"Send\" files%n  2  \"Receive\" files%n");
			int menu = sc.nextInt();
			if (menu == 1) {
				System.out.println("Enter a filename:");
				String filename = sc.next();
				DigitalSignature.writeSigned(new File(filename), getKeys(true));
			} else if (menu == 2) {
				System.out.println("Filename ends in \".signed\":");
				String filename = sc.next();
				if (!filename.matches(".*" + Pattern.quote(".") + "signed")) {
					System.out.println("Not a .signed file. Invalid");
					continue;
				}
				
				boolean valid = DigitalSignature.verify(new File(filename), getKeys(false));
				System.out.printf("The file has %sbeen tampered with.%n", valid ? "not " : "");
				
			} else {
				break;
			}
		}
		// Fixes failure to cede resources after exit
		sc.close();
		System.exit(0);
	}

	public static BigInteger[] getKeys(boolean privateKey) {
		
		File keyFile = new File(privateKey ? "privkey.rsa" : "pubkey.rsa");
		ObjectInputStream keyIn = null;

		BigInteger n = null;
		BigInteger exponent = null;
		try {
			keyIn = new ObjectInputStream(new FileInputStream(keyFile));
			n = (BigInteger) keyIn.readObject();
			exponent = (BigInteger) keyIn.readObject();
			if (KeyGen.flag) {
				System.out.println("Read keys");
			}
		} catch (IOException e) {
			if (KeyGen.flag)
				System.out.println("Failed to read keys");
		} catch (ClassCastException e) {
			
		} catch (ClassNotFoundException e) {
			
		} finally {
			if (keyIn != null) {
				try {
					keyIn.close();
				} catch (IOException e) {
				}
			}
		}
		BigInteger[] keys = { n, exponent };
		return keys;
	}
}
