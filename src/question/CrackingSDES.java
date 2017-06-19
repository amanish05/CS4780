package question;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class CrackingSDES {
	
	static List<String> possiblekey = new ArrayList<>();
	static byte[] plaintext;
	
	public static void main(String[] args) {		
		
		final String encodingText = "CRYPTOGRAPHY";
		final String rawKey = "0111001101";
		

		byte[] results = encryptwithKey(encodingText, rawKey);
		System.out.print("\nEncrypted text is :");
		String test= "";
		for (byte result : results) {
			System.out.print(result);
			test = test.concat(Byte.toString(result));			
		}
		
		results = decryptwithKey(test, rawKey);
		String casiString = CASCII.toString(results);
		System.out.println("\nThe word was: "+casiString);
		
		
		char set[] = {'0', '1'};        
		getAllPermutation(set, "", set.length, 10);
        sdesFileDecrypt();
        

	}
	
	public static void getAllPermutation(char set[], String prefix, int n, int k) {         
        if (k == 0) {
        	possiblekey.add(prefix);
        	return;
        }
        
        for (int i = 0; i < n; ++i) {             
            String newPrefix = prefix + set[i];           
            getAllPermutation(set, newPrefix, n, k-1); 
        }
    }
	
	public static byte[] encryptwithKey(String encoding, String rawKey) {

		byte[] casciiEncoded = CASCII.Convert(encoding);
		System.out.print("CASCII Endoded byte is :");
		for (byte each : casciiEncoded) {
			System.out.print(each);
		}
		byte[] results = new byte[casciiEncoded.length];
		for (int j = 0, i = 0; i < casciiEncoded.length;) {
			j = i + 8;
			byte[] temp = SDES.Encrypt(SDES.convertToByte(rawKey), Arrays.copyOfRange(casciiEncoded, i, j));
			for (int y = 0, x = i; y < temp.length; x++, y++) {
				results[x] = temp[y];
			}
			i += 8;
		}
		return results;
	}
	
	public static byte[] decryptwithKey(String encoding, String rawKey) {
		
		byte[] results = new byte[encoding.length()];
		for (int j = 0, i = 0; i < encoding.length();) {
			j = i + 8;			
			byte[] temp = SDES.Decrypt(SDES.convertToByte(rawKey), Arrays.copyOfRange(SDES.convertToByte(encoding), i, j));
			for (int y = 0, x = i; y < temp.length; x++, y++) {
				results[x] = temp[y];
			}
			i += 8;
		}						
		return results;
	}

	public static byte[] sdesFileDecrypt() {

		Path path = Paths.get("msg1.txt");
		
		try (Stream<String> lines = Files.lines(path)) {

			lines.forEach(s -> {
				
				possiblekey.forEach( key -> {
					
					byte[] results = decryptwithKey(s, key);					
					String casiString = CASCII.toString(results);					
					System.out.println("The message is: " + casiString +" And key is: " +key);
					if(casiString.contains("CRYPTOGRAPHY")){
						System.out.println("The message is: " + casiString +" And key is: " +key);
						return;
					}									
				});				
			});

		} catch (IOException ex) {
			System.out.println("Error while reading file: " + ex);
		}
		return null;
	}
}
