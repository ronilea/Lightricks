package imageProcessing;

public class DefaultWeightFunc extends WeightFunc {
    static final double Z = 1.0;
    static final double EPS = 0.03;
    public final double epsilon;
    public final double z;
    public DefaultWeightFunc(){
        this.epsilon = EPS;
        this.z = Z;
    }
    public DefaultWeightFunc(double epsilon, double z){
        this.epsilon = epsilon;
        this.z = z;
    }

    @Override
    public double getWeight(Pixel u, Pixel v) {

            double retVal = 1;
            var norm = Math.pow(Math.pow(Math.abs(u.getX()-v.getX()), this.z) +
                    Math.pow(Math.abs(u.getY()-v.getY()), this.z), 1.0/ this.z);
            retVal /= (this.epsilon +norm);
            return retVal;
           }
}
