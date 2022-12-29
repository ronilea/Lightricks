package ImageProcessing;

public abstract class WeightFunc {

    /**
     * default function of get weight.
     * @param u one pixel
     * @param v second pixel
     * @return the weight value in float
     */

    public abstract double getWeight(Pixel u, Pixel v);}
