package by.andd3dfx.math.pde.border;

/**
 * Represents a Robin boundary condition for partial differential equations.
 * This condition specifies a linear combination of the solution and its derivative
 * at the boundary:
 * <p>
 * ∂U/∂x(x,t) = h * (U(x,t) - Θ(t)) at x = x₁ or x = x₂
 * <p>
 * where h is a constant coefficient and Θ(t) is a prescribed function of time.
 * This type of boundary condition is commonly used to model convective heat transfer
 * or elastic support at the boundary.
 *
 * @see BorderCondition
 */
public class RobinBorderCondition implements BorderCondition {

    /**
     * Returns the coefficient h in the Robin boundary condition.
     * This coefficient represents the heat transfer coefficient in heat conduction
     * problems or the spring constant in mechanical problems.
     *
     * @return coefficient h in the boundary condition
     */
    public double gH() {
        return 0;
    }

    /**
     * Returns the prescribed reference value Θ(t) at time t.
     * This value represents the ambient temperature in heat conduction problems
     * or the equilibrium position in mechanical problems.
     *
     * @param t time coordinate
     * @return prescribed reference value Θ(t)
     */
    public double gTheta(double t) {
        return 0;
    }
}
