/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package recallsapi2.ingester;

/**
 *
 * @author ferru001
 */
public class IngesterException extends Exception {

    public IngesterException(String msg, Exception cause) {
        super(msg, cause);
    }
}
