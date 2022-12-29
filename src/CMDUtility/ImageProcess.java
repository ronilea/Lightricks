package CMDUtility;

import imageProcessing.HoleFiller;
import imageProcessing.Image;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

/**
 * Enables functions of loading or writing an image through openCV.
 */
public class ImageProcess {
    public static final double BLACK_HOLE_THRESHOLD = 0.5;
    private static final double NORMALIZE_FACTOR = 255;

    public static Image loadImage(String filePath) throws InvalidArgException {
        if ((!filePath.endsWith(".png") && !filePath.endsWith(".jpg"))) {
            throw new InvalidArgException("Image must be from type png of jpg");
        }
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        Mat rgb = Imgcodecs.imread(filePath);
        Mat imageMatrix = new Mat();
        Imgproc.cvtColor(rgb, imageMatrix, Imgproc.COLOR_RGB2GRAY);
        Image image = new Image(imageMatrix.width(), imageMatrix.height());
        for (int x = 0; x < imageMatrix.width(); x++) {
            for (int y = 0; y < imageMatrix.height(); y++) {
                double grayscale = imageMatrix.get(y, x)[0];
                image.setPixelColor(x, y, normalizeColorToGS(grayscale, 'r'));
            }
        }
        return image;
    }

    public static Image mergeImages(Image originalImage, Image maskImage) throws InvalidArgException {
        validateImages(originalImage, maskImage);
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();
        Image mergedImage = new Image(width, height);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                // if the pixel is considered black
                if (maskImage.getPixel(x, y).color < BLACK_HOLE_THRESHOLD) {
                    mergedImage.setPixelColor(x, y, HoleFiller.EMPTY);
                }
                // the pixel doesn't belong to the hole
                else {
                    mergedImage.setPixelColor(x, y, originalImage.getPixel(x, y).color);
                }
            }
        }
        return mergedImage;
    }
    private static void validateImages(Image originalImage, Image mask) throws InvalidArgException {
        if (originalImage.getWidth() != mask.getWidth() || originalImage.getHeight() != mask.getHeight()) {
            throw new InvalidArgException("Mask and Image must be the same size.");
        }
    }

    public static void writeImageToFile(Image outputImage, String outputPath) throws InvalidArgException{
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        Mat matOutput = new Mat(outputImage.getHeight(), outputImage.getWidth(), CvType.CV_8UC1);

        for (int y = 0; y < outputImage.getHeight(); y++) {
            for (int x = 0; x < outputImage.getWidth(); x++) {
                matOutput.put(y, x, normalizeColorToGS(outputImage.getPixel(x, y).color, 'w'));
            }
        }
        Imgcodecs.imwrite(outputPath, matOutput);
    }

    /**
     * normalize colors to be in the range of [0,1] (gray scale) in both writing to an image ('w') or else reading it.
     *
     * @param color  color to normalize
     * @param action a char that represents which action we would like to normalize according to.
     * @return normalized color (double)
     */
    private static double normalizeColorToGS(double color, char action) {
        if (action == 'w') {
            return color * NORMALIZE_FACTOR;
        } else {
            return color / NORMALIZE_FACTOR;
        }

    }
}