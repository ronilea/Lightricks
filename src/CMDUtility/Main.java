package CMDUtility;

import imageProcessing.*;

public class Main {
    private static final int VALID_NUM_ARGS = 5;
    private static final int IMAGE_ARG_INDEX = 0;
    private static final int MASK_ARG_INDEX = 1;
    private static final int CONNECTIVITY_ARG_INDEX = 2;
    private static final int EPS_ARG_INDEX = 3;
    private static final int ZED_ARG_INDEX = 4;

    public static void main(String[] args) {

        try {
            if (args.length != VALID_NUM_ARGS) {
                throw new InvalidArgException("Invalid number of arguments.");
            }
            var givenImagePath = args[IMAGE_ARG_INDEX];
            var maskImagePath = args[MASK_ARG_INDEX];
            if (givenImagePath == null || maskImagePath == null) {
                throw new InvalidArgException("File name doesn't exist");
            }
            var connectivity_val = args[CONNECTIVITY_ARG_INDEX];
            Connectivity connectivity = Connectivity.getConnectivityType(connectivity_val);
            var epsilon = Float.parseFloat(args[EPS_ARG_INDEX]);
            var zed = Integer.parseInt(args[ZED_ARG_INDEX]);

            // create the holed image
            Image originalImage = ImageProcess.loadImage(givenImagePath);
            Image mask = ImageProcess.loadImage(maskImagePath);
            Image image = ImageProcess.mergeImages(originalImage, mask);

            // initiate the weight func to the default one
            WeightFunc weightFunc = new DefaultWeightFunc(epsilon, zed);

            // fill the holes in the image
            GeneralFiller fillingAlgo = new HoleFiller(connectivity, image, weightFunc);
            fillingAlgo.fillPixelsInHole(image);

            ImageProcess.writeImageToFile(image, givenImagePath + "_o_" + zed + "_" + epsilon + "_" +
                    connectivity_val + "_.png");
            System.out.println("Done with Image hole filling.");

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}