package by.andd3dfx.math.pde.border;

/**
 * Params for border condition type 3 (elastic fixing): dU_dx(X,t) = h*( U(X,t) - Theta(t) )
 */
public class BorderConditionType3 implements BorderCondition {

    /**
     * Theta(t) value in asked time moment on this border
     *
     * @param t time
     * @return Theta value
     */
    public double gTheta(double t) {
        return 0;
    }

    /**
     * h coefficient value on this border
     *
     * @return h value
     */
    public double gH() {
        return 0;
    }
}
