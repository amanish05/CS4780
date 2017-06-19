package question;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class CrackingTripleSDES {
	
	static List<String> possiblekey = new ArrayList<>();
	static byte[] plaintext;
	
	public static void main(String[] args) throws InterruptedException, IOException {				
		
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
	
	public static byte[] decryptwithKey(String encoding, String rawKey1, String rawKey2) {
		
		byte[] results = new byte[encoding.length()];
		for (int j = 0, i = 0; i < encoding.length();) {
			j = i + 8;			
			byte[] temp = TripleSDES.Decrypt(SDES.convertToByte(rawKey1), SDES.convertToByte(rawKey2),  Arrays.copyOfRange(SDES.convertToByte(encoding), i, j));
			for (int y = 0, x = i; y < temp.length; x++, y++) {
				results[x] = temp[y];
			}
			i += 8;
		}						
		return results;
	}

	public static byte[] sdesFileDecrypt() {

		Path path = Paths.get("msg2.txt");
		
		try (Stream<String> lines = Files.lines(path)) {

			lines.forEach(s -> {
				
				possiblekey.forEach( rawkey1 -> {
					System.out.println("Looking for key: " +rawkey1);
					
					possiblekey.forEach(rawkey2 -> {
						
						byte[] results = decryptwithKey(s, rawkey1, rawkey2);					
						String casiString = CASCII.toString(results);
																		
						if(casiString.contains("THERE ARE NO SECRETS")){
							System.out.println("The message is: " +casiString+" .First key is: " +rawkey1 + " and Second is: "+rawkey2);
							return;
						}
						
					});				
													
				});				
			});

		} catch (IOException ex) {
			System.out.println("Error while reading file: " + ex);
		}
		return null;
	}
}
