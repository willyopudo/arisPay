package org.arispay.utils;

import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class UniquePrefixGenerator {
    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final  Random random = new SecureRandom();
    private static final Set<String> generatedCombinations = new HashSet<>();

    /**
     * Generate a unique 3-letter combination
     * @return Unique 3-letter string
     */
    public static String generateUniquePrefix(int length) {
        String prefix;
        do {
            prefix = generateRandomPrefix(length);
        } while (!generatedCombinations.add(prefix));
        return prefix;
    }

    /**
     * Generate a random 3-letter combination
     * @return Random 3-letter combination
     */
    private static String generateRandomPrefix(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i <length; i++) {
            sb.append(ALPHABET.charAt(random.nextInt(ALPHABET.length())));
        }
        return sb.toString();
    }

    /**
     * Calculate total possible combinations
     * @return Total number of possible unique 3-letter combinations
     */
    public static long getTotalPossibleCombinations(int length) {
        return (long) Math.pow(ALPHABET.length(), length);
    }
}
