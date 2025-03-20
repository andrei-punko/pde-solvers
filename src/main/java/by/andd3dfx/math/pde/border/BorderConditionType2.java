package by.andd3dfx.math.pde.border;

/**
 * Represents a Neumann boundary condition (Type 2) for partial differential equations.
 * This condition specifies the value of the spatial derivative at the boundary:
 * <p>
 * ∂U/∂x(x,t) = ∂U/∂x(t) at x = x₁ or x = x₂
 * <p>
 * where ∂U/∂x(t) is a prescribed function of time. This type of boundary condition
 * is commonly used when the flux or gradient at the boundary is known or controlled.
 *
 * @see BorderCondition
 */
public class BorderConditionType2 implements BorderCondition {

    /**
     * Returns the prescribed value of the spatial derivative at the boundary at time t.
     * This method should be overridden to provide the actual derivative value function.
     *
     * @param t time coordinate
     * @return prescribed value of ∂U/∂x(t) at the boundary
     */
    public double gdU_dx(double t) {
        return 0;
    }
}
