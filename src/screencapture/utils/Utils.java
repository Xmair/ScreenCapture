/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package screencapture.utils;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.Random;

/**
 *
 * @author Xmair
 */
public class Utils {
    public static void handleError(Exception e) {
        StackTraceElement[] elements = e.getStackTrace();
        
        System.out.println("An exception has occured");
        System.out.println("=========================");
        System.out.println(String.format("Exception: %s", e.toString()));
        
        for(int iterator = 1; iterator <= elements.length; iterator ++) {
            System.out.println(String.format("Class name: %s. Method name: %s. Line number: %d.", elements[iterator - 1].getClassName(), elements[iterator - 1].getMethodName(), elements[iterator - 1].getLineNumber()));
        }
        
        System.out.println("=========================");
    }
    
    public static String generateScreenshotName() {
        Date date = Date.from(Instant.now());
        return new SimpleDateFormat("MDhhmmss").format(date).concat(String.valueOf(new Random().nextInt())).concat(".png");
    }
}
