package imageProcessing;

import CMDUtility.InvalidArgException;

public abstract class GeneralFiller {

    /**
     * the directions are used as the directions for a pixel's neighbors according to the connectivity type.
     */
    public static final int[][] directions_four = {{-1,0}, {1,0}, {0,-1}, {0,1}};
    public static final int[][] directions_eight = {{-1,0}, {1,0}, {0,-1}, {0,1}, {1,1},{1,-1}, {-1,1}, {-1,-1}} ;


    public abstract void fillPixelsInHole(Image image) throws InvalidArgException;

}
