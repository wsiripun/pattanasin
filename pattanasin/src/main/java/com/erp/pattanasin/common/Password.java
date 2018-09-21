package com.erp.pattanasin.common;


import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

public class Password {
	
    public static byte[] hashPassword( final char[] password, final byte[] salt, final int iterations, final int keyLength ) {

        try {
        	// PBKDF2 = Password Based Key Derivation Function 2
        	// HMAC = keyed-hash message authentication code or hash-based message authentication code
            SecretKeyFactory skf = SecretKeyFactory.getInstance( "PBKDF2WithHmacSHA512" );
            PBEKeySpec spec = new PBEKeySpec( password, salt, iterations, keyLength );
            SecretKey key = skf.generateSecret( spec );
            byte[] res = key.getEncoded( );
            return res;         
        } catch ( NoSuchAlgorithmException d ) {
            throw new RuntimeException( d );
        } catch (InvalidKeySpecException e) {
        	throw new RuntimeException( e );
        }
    }
    
    
    //public static byte[] getSalt() throws NoSuchAlgorithmException
    public static byte[] getSalt() 
    {
        //Always use a SecureRandom generator
        SecureRandom sr;
        byte[] salt = new byte[16];
		try {
			sr = SecureRandom.getInstance("SHA1PRNG");	        
	        //Get a random salt
	        sr.nextBytes(salt);

		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        //return salt
        return salt;

    }    

}
