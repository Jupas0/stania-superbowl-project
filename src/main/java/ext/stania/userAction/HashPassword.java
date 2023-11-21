package ext.stania.userAction;

import java.util.Random;

import org.apache.commons.codec.digest.DigestUtils;

public class HashPassword {
	public static final Random r = new Random();
	public static final String alphabet =("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ&é\"'(-è_çà)=1234567890°+^$*ù!:;,¨£%µ?./§1~#{[|`\\^@]}¤");
	
	
	public String hash(String mdpAHasher){ 
		return DigestUtils.sha256Hex(mdpAHasher);
	}
	

	public String getSalt(int taille) {
		String s = ""; //On initialise une chaine vide.
		for (int i = 0; i < taille; i++) {
			s +=alphabet.charAt(r.nextInt(alphabet.length())); //On pioche un caractère au hasard dans la chaine "alphabet", et nous le concaténons à s.
		}
		
		return s; //On renvoie s
	}
	public String hashSalt(String toHash, String salt) {
		return DigestUtils.sha256Hex(toHash+salt);
	}
	public String slowHash(String s, int count) {
		for (int i=0; i <count; i++) {
			s=hash(s);
			
		}
		return s;
	}
	public String hashSpecialExercice(String s, int key, int iter) {
		String res=s;
		for(int i =0; i<iter;i++) {
			res= hashSalt(res, getSalt(key));
		}
		System.out.println(res);
		return res;
	}
	
}
