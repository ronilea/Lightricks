package imageProcessing;

import CMDUtility.InvalidArgException;

import java.util.ArrayList;

public class Pixel {
    public final int x;
    public final int y;
    public double color;
    Pixel(int x, int y, double color) throws InvalidArgException {
        if (x >= 0 && y >= 0){
            this.x = x;
            this.y = y;
        }
        else{
            throw new InvalidArgException("x or y aren't valid");
        }
        this.color = color;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }



}
