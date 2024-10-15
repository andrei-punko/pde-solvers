package by.andd3dfx.math.pde.border;

/**
 * Params for border condition type 3
 */
public class BorderConditionType3 implements BorderCondition {

    /**
     * TODO add description
     * @param t time
     * @return ? value in asked time moment on this border
     */
    public double gTheta(double t) {
        return 0;
    }

    /**
     * TODO add description
     * @return
     */
    public double gH() {
        return 0;
    }
}
