package imageProcessing;

import CMDUtility.InvalidArgException;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class LinearHoleFiller extends GeneralFiller {
    private static final int ENOUGH_NEIGHBORS = 4;
    private final WeightFunc weightFunc;
    private final Connectivity connectivity;
    private Image image;

    /**
     * Constructor, defines connectivity and the weight function of the filler
     * @param connectivity Connectivity type
     * @param weightFunc weight Function
     */
    public LinearHoleFiller( Connectivity connectivity,WeightFunc weightFunc){
    this.weightFunc = weightFunc;
    this.connectivity = connectivity;
}
    @Override
    public void fillPixelsInHole(Image image) throws InvalidArgException {
        this.image = image;
        int first_row_index = getFirstHoleRow();
        int last_row_index = getLastHoleRow();
        ArrayList<Pixel> first_row = image.data.get(first_row_index);
        Set<Pixel> visitedNotDefined =  new HashSet<>();

        do {
            // check first row
            defineColorOfRowPixels(first_row, visitedNotDefined);
            first_row_index += 1;
            // check last row

            first_row = image.data.get(first_row_index);
//            last_row = image.data.get(last_row_index);
        } while (first_row_index <= last_row_index || !visitedNotDefined.isEmpty());

    }

    private void defineColorOfRowPixels(ArrayList<Pixel> row, Set<Pixel> visitedNotDefined) throws InvalidArgException {
        for (Pixel pixel: row){
            if (pixel.color == Image.EMPTY){
                ArrayList<Pixel> neighbors = image.getNoneEmptyNeighbors(pixel,connectivity);
                if (neighbors.size() >= ENOUGH_NEIGHBORS) {
                    pixel.color = HoleFiller.getColorByNeighbors(neighbors, pixel, weightFunc);
                    visitedNotDefined.remove(pixel);
                }
                else {
                    visitedNotDefined.add(pixel);
                }
            }
        }
    }

    private int getLastHoleRow() {
//        ArrayList<Pixel> last_row = null;
        int index = -1;
        for (int i = (image.data.size() - 1); i >= 0; i--){
            for (Pixel pixel : image.data.get(i)) {
                if (pixel.color == Image.EMPTY) {
                    index = i;
                    break;
                }
            }
            if (index != -1) {
                break;
            }
        }
        return index;
    }

    private int getFirstHoleRow() {
        int index = -1;
        for (int i = 0; i < image.data.size(); i++){
            for (Pixel pixel : image.data.get(i)) {
                if (pixel.color == Image.EMPTY) {
                    index = i;
                    break;
                }
            }
            if (index != -1) {
                break;
            }
        }
        return index;
    }
}
