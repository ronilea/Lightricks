package ImageProcessing;

import CMDUtility.InvalidArgException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class LinearHoleFiller extends HoleFiller {
    private static final int ENOUGH_NEIGHBORS = 3;
    private final WeightFunc weightFunc;
    private final Connectivity connectivity;
    private Image image;
    private ImageNavigator imageNavigator;

    /**
     * Constructor, defines connectivity and the weight function of the filler
     *
     * @param connectivity Connectivity type
     * @param weightFunc   weight Function
     */
    public LinearHoleFiller(Connectivity connectivity, WeightFunc weightFunc) {
        this.weightFunc = weightFunc;
        this.connectivity = connectivity;
    }

    @Override
    public void fillPixelsInHole(Image image) throws InvalidArgException {
        this.image = image;
        this.imageNavigator = new ImageNavigator(image, connectivity);
        setColorToPixelsByPrevRow();


    }

    /**
     * This algorithm sets the colors to the pixels by the order of the first row of the hole till the last row of it.
     * The color is set by the neighbors of the hole pixel, if there are not enough neighbors with color, the algo will
     * leave them to the last iterations where their neighbors will be set by then.
     */
    private void setColorToPixelsByPrevRow() throws InvalidArgException {
        int first_row_index = getFirstHoleRowIndex();
        int last_row_index = getLastHoleRowIndex();
        ArrayList<Pixel> first_row = image.data.get(first_row_index);
        Set<Pixel> visitedNotDefined = new HashSet<>();

        do {
            // check first row
            defineColorOfRowPixels(first_row, visitedNotDefined);
            first_row_index += 1;
            // check last row
            first_row = image.data.get(first_row_index);
        } while (first_row_index <= last_row_index || !visitedNotDefined.isEmpty());
    }

    /**
     * @param row               row in the image data.
     * @param visitedNotDefined a set of hole pixels that were visited but were still empty because there were not
     *                          enough neighbors in order to fill them.
     */
    private void defineColorOfRowPixels(ArrayList<Pixel> row, Set<Pixel> visitedNotDefined) throws InvalidArgException {
        for (Pixel pixel : row) {
            if (pixel.color == Image.EMPTY) {
                ArrayList<Pixel> neighbors = imageNavigator.getNoneEmptyNeighbors(pixel);
                if (neighbors.size() >= ENOUGH_NEIGHBORS) {
                    pixel.color = HoleFillerByBorder.getColorByNeighbors(neighbors, pixel, weightFunc);
                    visitedNotDefined.remove(pixel);
                } else {
                    visitedNotDefined.add(pixel);
                }
            }
        }
    }

    private int getLastHoleRowIndex() {
        int index = -1;
        for (int i = (image.data.size() - 1); i >= 0; i--) {
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

    private int getFirstHoleRowIndex() {
        int index = -1;
        for (int i = 0; i < image.data.size(); i++) {
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
