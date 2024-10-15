package by.andd3dfx.math.pde.border;

/**
 * Params for border condition type 2
 */
public class BorderConditionType2 implements BorderCondition {

    /**
     * Border condition dU_dx(t)
     *
     * @param t time
     * @return dU_dt value in asked time moment on this border
     */
    public double gdU_dx(double t) {
        return 0;
    }
}
