package imageProcessing;

public abstract class GeneralFiller {
    public static final int[][] directions_four = {{-1,0}, {1,0}, {0,-1}, {0,1}};
    public static final int[][] directions_eight = {{-1,0}, {1,0}, {0,-1}, {0,1}, {1,1},{1,-1}, {-1,1}, {-1,-1}} ;


    public abstract void fillPixelsInHole(Image image);

}
