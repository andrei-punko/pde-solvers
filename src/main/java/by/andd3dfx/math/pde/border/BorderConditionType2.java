package by.andd3dfx.math.pde.border;

/**
 * Params for border condition type 2 (definite force): ∂U/∂x(X,t) = ∂U/∂x(t)
 */
public class BorderConditionType2 implements BorderCondition {

    /**
     * ∂U/∂x(X,t) in asked time moment on this border
     *
     * @param t time
     * @return ∂U/∂x value
     */
    public double gdU_dx(double t) {
        return 0;
    }
}
