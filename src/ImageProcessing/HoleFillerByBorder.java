package ImageProcessing;

import CMDUtility.InvalidArgException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class HoleFillerByBorder extends HoleFiller {
    public static final int EMPTY = -1;
    public final WeightFunc weightFunc;
    public final Connectivity connectivity;
    private final Set<Pixel> borderPixels;
    private final ArrayList<Pixel> holePixels;
    private Image image;
    private ImageNavigator imageNavigator;

    /**
     * in order to allow use the default weight func with its default parameters.
     * @param connectivity connectivity type
     */
    public HoleFillerByBorder(Connectivity connectivity) {

        this.weightFunc = new DefaultWeightFunc();
        this.borderPixels = new HashSet<>();
        this.holePixels = new ArrayList<>();
        this.connectivity = connectivity;


    }

    /**
     * constructor when we receive a different weight function( not the default one) from the user
     * @param connectivity connectivity type
     * @param weightFunc weight function (that used to fill the holes)
     */
    public HoleFillerByBorder(Connectivity connectivity, WeightFunc weightFunc) {
        this.connectivity = connectivity;
        this.weightFunc = weightFunc;
        this.borderPixels = new HashSet<>();
        this.holePixels = new ArrayList<>();
    }


    private void initBorderAndHole() throws InvalidArgException {

        ArrayList<Pixel> currNeighboursArr = new ArrayList<>();
        for (var row : image.data) {
            for (Pixel pixel : row) {
                if (pixel.color == EMPTY) {
                    holePixels.add(pixel);
                    currNeighboursArr = imageNavigator.defineNeighborsForPixel(pixel, currNeighboursArr);
                    for (Pixel neighbor : currNeighboursArr) {
                        if (image.getPixelColor(neighbor) != EMPTY) {
                            borderPixels.add(neighbor);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void fillPixelsInHole(Image image) throws InvalidArgException {
        this.image = image;
        this.imageNavigator = new ImageNavigator(image, connectivity);
        initBorderAndHole();
        setColorToHolePixels();
    }

    private void setColorToHolePixels() {
        for (Pixel hole : holePixels ){
            double newColor = getColorByNeighbors(borderPixels, hole, weightFunc);
            image.setPixelColor(hole, newColor);
        }
    }

    static public double getColorByNeighbors(Collection<Pixel> borderPixels, Pixel hole, WeightFunc weightFunc){
        double weightColorSum = 0;
        double weightSum = 0;

        for (Pixel neighbor: borderPixels) {
            double weight = weightFunc.getWeight(hole,neighbor);
            weightColorSum += (weight * neighbor.color);
            weightSum += weight;
        }
        return weightColorSum / weightSum;
    }
}
