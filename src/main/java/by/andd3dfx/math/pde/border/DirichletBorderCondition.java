package by.andd3dfx.math.pde.border;

/**
 * Represents a Dirichlet boundary condition for partial differential equations.
 * This condition specifies the value of the solution at the boundary:
 * <p>
 * U(x,t) = U(t) at x = x₁ or x = x₂
 * <p>
 * where U(t) is a prescribed function of time. This is the simplest type of boundary
 * condition, commonly used when the boundary value is known or controlled.
 *
 * @see BorderCondition
 */
public class DirichletBorderCondition implements BorderCondition {

    /**
     * Returns the prescribed value of the solution at the boundary at time t.
     * This method should be overridden to provide the actual boundary value function.
     *
     * @param t time coordinate
     * @return prescribed value U(t) at the boundary
     */
    public double gU(double t) {
        return 0;
    }
}
