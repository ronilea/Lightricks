package ImageProcessing;

public class DefaultWeightFunc extends WeightFunc {
    static final double Z = 1.0;
    static final double EPS = 0.03;
    public final double epsilon;
    public final double z;

    /**
     * Constructor that uses the default pre-defined zed and epsilon values.
     */
    public DefaultWeightFunc(){
        this.epsilon = EPS;
        this.z = Z;
    }

    /**
     * Constructor that gets the epsilon and zed values by the user.
     */
    public DefaultWeightFunc(double epsilon, double z){
        this.epsilon = epsilon;
        this.z = z;
    }

    @Override
    public double getWeight(Pixel u, Pixel v) {

            double retVal = 1;
            var norm = Math.pow(Math.pow(Math.abs(u.x-v.x), this.z) +
                    Math.pow(Math.abs(u.y-v.y), this.z), 1.0/ this.z);
            retVal /= (this.epsilon +norm);
            return retVal;
           }
}
