package by.andd3dfx.math.pde.border;

/**
 * Params for border condition type 1 (definite mode): U(X,t) = U(t)
 */
public class BorderConditionType1 implements BorderCondition {

    /**
     * U(t) value in asked time moment on this border
     *
     * @param t time
     * @return U value
     */
    public double gU(double t) {
        return 0;
    }
}
