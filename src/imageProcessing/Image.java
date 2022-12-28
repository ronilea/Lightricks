package imageProcessing;

import CMDUtility.InvalidArgException;
import org.opencv.ml.EM;

import java.util.ArrayList;

public class Image implements Cloneable {
    public static int EMPTY = -1;
    public ArrayList<ArrayList<Pixel>> data;
    private final int width;
    private final int height;

    /**
     * in case we want to create an image with an already initialized data
     * @param imageMat image matrix (double array of pixels)
     */
    public Image(ArrayList<ArrayList<Pixel>> imageMat){
        data = imageMat;
        this.width = data.get(0).size();
        this.height = data.size();
    }

    /**
     * Created new empty Image sized width * height
     */
    public Image(int width, int height){
        this.width = width;
        this.height = height;
        try {
            initialize_data();
        }
        catch (Exception e){
            data = new ArrayList<>();
        }

    }

    /**
     * initializes the data double Array of Pixels with pixels defined as empty.
     */
    private void initialize_data() throws InvalidArgException {
        data = new ArrayList<>();
        for (int row = 0; row < height; row++){
            data.add(new ArrayList<>(width));
            for(int col=0; col < width; col++){
                Pixel newP = new Pixel(col, row, EMPTY); // set all as empty
                data.get(row).add(newP);
            }
        }
    }


    public Pixel getPixel(int x, int y){
        if (0 <= x && x < width && 0<=y && y < height ) return data.get(y).get(x);
        return null;
    }
    public double getPixelColor(Pixel pixel){
        int x = pixel.getX();
        int y = pixel.getY();
        return data.get(y).get(x).color;
    }
    public void setPixelColor(Pixel pixel, double color){
        setPixelColor(pixel.getX(), pixel.getY(),color);
    }
    public void setPixelColor(int x, int y, double color){
        Pixel pixel = data.get(y).get(x);
        pixel.color = color;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }


//todo:  add operator == to Pixel
    public ArrayList<Pixel> getNoneEmptyNeighbors(Pixel pixel, Connectivity connectivity) {
        ArrayList<Pixel> neighbors = new ArrayList<>();
        Pixel curr_neighbor;
        int[][] directions = getDirections(connectivity);

        for (var direction: directions){
            curr_neighbor = getPixel(pixel.getX() + direction[0], pixel.getY() + direction[1]);
            if (curr_neighbor.color != EMPTY){
                neighbors.add(curr_neighbor);
            }
        }
        return neighbors;




    }

    private static int[][] getDirections(Connectivity connectivity) {
        int[][] directions;
        if (connectivity ==Connectivity.Four){
            directions = GeneralFiller.directions_four;
        }
        else{
            directions = GeneralFiller.directions_eight;
        }
        return directions;
    }


    public ArrayList<Pixel> defineNeighborsForPixel(Pixel pixel, ArrayList<Pixel> currNeighborsArr, Connectivity connectivity) {
        currNeighborsArr.clear();
        int[][] directions = getDirections(connectivity);
        for (int[] direction: directions){
            currNeighborsArr.add(getPixel(pixel.getX() + direction[0], pixel.getY() + direction[1]));
        }
        //todo: delete it
//        currNeighborsArr.add(getPixel(pixel.getX() - 1, pixel.getY()));
//        currNeighborsArr.add(getPixel(pixel.getX() + 1, pixel.getY()));
//        currNeighborsArr.add(getPixel(pixel.getX(), pixel.getY() - 1));
//        currNeighborsArr.add(getPixel(pixel.getX(), pixel.getY() + 1));
//        if (connectivity == Connectivity.Eight) {
//            currNeighborsArr.add(getPixel(pixel.getX() - 1, pixel.getY() - 1));
//            currNeighborsArr.add(getPixel(pixel.getX() + 1, pixel.getY() + 1));
//            currNeighborsArr.add(getPixel(pixel.getX() + 1, pixel.getY() - 1));
//            currNeighborsArr.add(getPixel(pixel.getX() - 1, pixel.getY() + 1));
//        }
        return currNeighborsArr;
    }

    @Override
    public Image clone() {
        try {
            Image clone = (Image) super.clone();
            // TODO: copy mutable state here, so the clone can't change the internals of the original
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
