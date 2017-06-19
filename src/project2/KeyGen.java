package project2;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.security.SecureRandom;

public class KeyGen {
	
	public static final boolean flag = true;

	public static void main(String[] args) {
		BigInteger[] keys = getKeys(1024);
		writeKeys(keys);
	}
	
	public static BigInteger[] getKeys(int totalBytes) {		
						
		BigInteger p = null;
		BigInteger q = null;
		BigInteger n = null;
		
		boolean cont = true;
		
		while (cont) {
			
			p = new BigInteger(totalBytes / 2, Integer.MAX_VALUE, new SecureRandom());
			q = new BigInteger(totalBytes / 2, Integer.MAX_VALUE, new SecureRandom());
			n = p.multiply(q);
			
			BigInteger nConstraint = BigInteger.TEN.pow(50 * (int) Math.floor(((0.0f + totalBytes) / 1024)));
			BigInteger pMinusQ = p.subtract(q).abs();
			cont = nConstraint.compareTo(pMinusQ) > 0;
			
			//System.out.println(nConstraint);
			//System.out.println(pMinusQ);
		}
		BigInteger totientN = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
		
		BigInteger e;
		do {
			e = new BigInteger(totalBytes, new SecureRandom());
		} while ((!totientN.gcd(e).equals(BigInteger.ONE)) || e.equals(BigInteger.ZERO));
		BigInteger d = e.modInverse(totientN);
		if (flag) {
			System.out.printf("Value of p: %d%nValue of q: %d%nValue of n: %d%n e: %d%n d: %d%n e * d mod totient(n): %d%ngcd(e, totient(n)): %d%n",
					p, q, n, e, d, e.multiply(d).mod(totientN), totientN.gcd(e));
		}
		return new BigInteger[] { n, e, d };
	}

	public static void writeKeys(BigInteger[] keys) {
		
		File publicKeyFile = new File("pubkey.rsa");
		File privateKeyFile = new File("privkey.rsa");
		ObjectOutputStream publicKeyOut = null;
		ObjectOutputStream privateKeyOut = null;
		try {
			publicKeyOut = new ObjectOutputStream(new FileOutputStream(publicKeyFile));
			privateKeyOut = new ObjectOutputStream(new FileOutputStream(privateKeyFile));
			publicKeyOut.writeObject(keys[0]);
			privateKeyOut.writeObject(keys[0]);
			publicKeyOut.writeObject(keys[1]);
			privateKeyOut.writeObject(keys[2]);
			if (flag) {
				System.out.println("Keys written");
			}
		} catch (IOException e) {
			if (flag) {
				System.out.println("Failed to write keys");
			}
		} catch (Exception e) {
			throw e;
		} finally {
			if (publicKeyOut != null) {
				try {
					publicKeyOut.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (privateKeyOut != null) {
				try {
					privateKeyOut.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
