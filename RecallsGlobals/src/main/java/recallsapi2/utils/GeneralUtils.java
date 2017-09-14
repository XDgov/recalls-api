/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package recallsapi2.utils;

import java.util.List;

/**
 *
 * @author ferru001
 */
public class GeneralUtils {
     /**
     * Given a string, a token and a repetition factor, take that string, append
     * to it the provided token and do so the provided number of times.
     *
     * @param str string to multiply
     * @param token token to place between each instance of the copied string.
     * @param times number of times to do this.
     * @return
     */
    public static String multiplyStringWithToken(String str, String token, int times) {
        StringBuilder strBuilder = new StringBuilder();
        for (int x = 0; x < times; x++) {
            strBuilder.append(str).append(token);
        }
        if (token != null && token.trim().length() > 0) {
            strBuilder.delete(strBuilder.length() - token.length(), strBuilder.length());
        }
        return strBuilder.toString();
    }
    
    public static String concat(List<String> toks, String concatenator) {
        StringBuilder strBuilder = new StringBuilder();
        for (String tok : toks) {
            strBuilder.append(tok).append(concatenator);
        }
        strBuilder.delete(strBuilder.length() - concatenator.length(), strBuilder.length());
        return strBuilder.toString();
    }
}
