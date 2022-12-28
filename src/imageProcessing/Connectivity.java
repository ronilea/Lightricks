package imageProcessing;

import CMDUtility.InvalidArgException;

import java.util.ArrayList;

/**
 * Defines types of connectivity.
 */
public enum Connectivity {
    Four, Eight;
    public static Connectivity getConnectivityType(String value) throws InvalidArgException{
        switch (value){
            case "4":
                return Four;
            case "8":
                return Eight;
        }
        throw new InvalidArgException("Invalid connectivity value:" + value);


    }

}
