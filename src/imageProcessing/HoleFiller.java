package imageProcessing;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class HoleFiller extends GeneralFiller {
    public static final int EMPTY = -1;
    public final WeightFunc weightFunc;
    public final Connectivity connectivity;
    private final Set<Pixel> borderPixels;
    private final ArrayList<Pixel> holePixels;
    private final Image image;

    /**
     * in order to allow use the default weight func with its default parameters.
     * @param connectivity connectivity type
     * @param image image object to fill
     */
    public HoleFiller(Connectivity connectivity, Image image) {

        this.weightFunc = new DefaultWeightFunc();
        this.borderPixels = new HashSet<>();
        this.holePixels = new ArrayList<>();
        this.connectivity = connectivity;
        this.image = image;


    }

    /**
     * constructor when we receive a different weight function( not the default one) from the user
     * @param connectivity connectivity type
     * @param image image object to fill
     * @param weightFunc weight function (that used to fill the holes)
     */
    public HoleFiller(Connectivity connectivity, Image image, WeightFunc weightFunc) {
        this.connectivity = connectivity;
        this.weightFunc = weightFunc;
        this.borderPixels = new HashSet<>();
        this.holePixels = new ArrayList<>();
        this.image = image;
        initBorderAndHole();
    }

    private void defineNeighborsForPixel(Pixel pixel, ArrayList<Pixel> currNeighborsArr) {
        currNeighborsArr.clear();
        currNeighborsArr.add(image.getPixel(pixel.getX() - 1, pixel.getY()));
        currNeighborsArr.add(image.getPixel(pixel.getX() + 1, pixel.getY()));
        currNeighborsArr.add(image.getPixel(pixel.getX(), pixel.getY() - 1));
        currNeighborsArr.add(image.getPixel(pixel.getX(), pixel.getY() + 1));
        if (connectivity == Connectivity.Eight) {
            currNeighborsArr.add(image.getPixel(pixel.getX() - 1, pixel.getY() - 1));
            currNeighborsArr.add(image.getPixel(pixel.getX() + 1, pixel.getY() + 1));
            currNeighborsArr.add(image.getPixel(pixel.getX() + 1, pixel.getY() - 1));
            currNeighborsArr.add(image.getPixel(pixel.getX() - 1, pixel.getY() + 1));
        }
    }

    private void initBorderAndHole() {
        ArrayList<Pixel> currNeighboursArr = new ArrayList<>();
        for (var row : image.data) {
            for (Pixel pixel : row) {
                if (pixel.color == EMPTY) {
                    holePixels.add(pixel);
                    defineNeighborsForPixel(pixel, currNeighboursArr);
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
    public void fillPixelsInHole(Image image) {
        for (Pixel hole : holePixels ){
            double WeightColorSum = 0;
            double WeightSum = 0;

            for (Pixel neighbor: borderPixels){
                double weight = weightFunc.getWeight(hole,neighbor);
                WeightColorSum += weight * image.getPixelColor(neighbor);
                WeightSum += weight;

            }
            image.setPixelColor(hole, WeightColorSum / WeightSum);
        }
    }

}
//todo: delete this
//                FillHoleAlgorithm algo = FillHoleAlgorithm();
//                algo.apply(image);
//                System.out.println("neighbor");
//                System.out.println("X: " +neighbor.getX());
//                System.out.println("Y: "+neighbor.getY());
//                System.out.println("WEIGHT: "+weight);
//                System.out.println("COLOR: " +neighbor.getColor());

//            System.out.println("COLOR final: "+ WeightColorSum/WeightSum);
//            hole.setColor(0.3);
//        System.out.println(borderPixels.size());
