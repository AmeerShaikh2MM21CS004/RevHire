package com.revhire.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashUtil {

    private static final Logger logger = LogManager.getLogger(HashUtil.class);

    public static String hash(String input) {
        logger.debug("Hashing input | length={}", input != null ? input.length() : 0);

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(input.getBytes());

            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }

            String hashedString = sb.toString();
            logger.debug("Hashing successful | hash={}", hashedString);
            return hashedString;

        } catch (NoSuchAlgorithmException e) {
            logger.error("Hashing failed: algorithm not found", e);
            throw new RuntimeException("Hashing algorithm not found", e);
        }
    }
}
