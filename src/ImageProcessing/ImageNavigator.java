package ImageProcessing;

import CMDUtility.InvalidArgException;

import java.util.ArrayList;

/**
 * this class allows to navigate through the image according to an algorithm that uses connectivity.
 */
public class ImageNavigator {
    /**
     * the directions are used as the directions for a pixel's neighbors according to the connectivity type.
     */
    public static final int[][] directions_four = {{-1,0}, {1,0}, {0,-1}, {0,1}};
    public static final int[][] directions_eight = {{-1,0}, {1,0}, {0,-1}, {0,1}, {1,1},{1,-1}, {-1,1}, {-1,-1}} ;
    private final Image image;
    private final Connectivity connectivity;


    ImageNavigator(Image image, Connectivity connectivity) {
        this.image = image;
        this.connectivity = connectivity;
    }
    public ArrayList<Pixel> getNoneEmptyNeighbors(Pixel pixel) throws InvalidArgException {
        ArrayList<Pixel> neighbors = new ArrayList<>();
        Pixel curr_neighbor;
        int[][] directions = getDirections(connectivity);

        for (var direction : directions) {
            curr_neighbor = image.getPixel(pixel.x + direction[0], pixel.y + direction[1]);
            if (curr_neighbor.color != Image.EMPTY) {
                neighbors.add(curr_neighbor);
            }
        }
        return neighbors;


    }

    private static int[][] getDirections(Connectivity connectivity) {
        int[][] directions;
        if (connectivity == Connectivity.Four) {
            directions = directions_four;
        } else {
            directions = directions_eight;
        }
        return directions;
    }


    public ArrayList<Pixel> defineNeighborsForPixel(Pixel pixel, ArrayList<Pixel> currNeighborsArr)
            throws InvalidArgException {
        currNeighborsArr.clear();
        int[][] directions = getDirections(connectivity);
        for (int[] direction : directions) {
            currNeighborsArr.add(image.getPixel(pixel.x + direction[0], pixel.y + direction[1]));
        }
        return currNeighborsArr;
    }

}


