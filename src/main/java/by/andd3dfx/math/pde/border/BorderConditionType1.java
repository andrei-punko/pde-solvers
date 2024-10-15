package by.andd3dfx.math.pde.border;

/**
 * Params for border condition type 1
 */
public class BorderConditionType1 implements BorderCondition {

    /**
     * Border condition U(t)
     *
     * @param t time
     * @return U value in asked time moment on this border
     */
    public double gU(double t) {
        return 0;
    }
}
