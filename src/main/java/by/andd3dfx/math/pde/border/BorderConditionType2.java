package by.andd3dfx.math.pde.border;

/**
 * Params for border condition type 2 (заданная сила): dU_dx(X,t)
 */
public class BorderConditionType2 implements BorderCondition {

    /**
     * dU_dx(X,t) in asked time moment on this border
     *
     * @param t time
     * @return dU_dx value
     */
    public double gdU_dx(double t) {
        return 0;
    }
}
