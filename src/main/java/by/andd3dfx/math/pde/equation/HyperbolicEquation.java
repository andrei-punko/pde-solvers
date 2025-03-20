package by.andd3dfx.math.pde.equation;

import by.andd3dfx.math.pde.border.BorderCondition;
import by.andd3dfx.math.pde.solver.HyperbolicEquationSolver;

/**
 * Represents a hyperbolic partial differential equation, which typically describes
 * wave propagation and oscillation processes. The equation has the form:
 * <p>
 * M(x,t,U)*∂²U/∂t² + L(x,t,U)*∂U/∂t = ∂U( K(x,t,U)*∂U/∂x )/∂x + V(x,t,U)*∂U/∂x + F(x,t,U)
 * <p>
 * where U = U(x,t) is the unknown function (displacement, pressure, etc.).
 * <p>
 * This is a special case of the general second-order PDE where the coefficient
 * of the second-order time derivative (M) is one, representing the standard form
 * of wave equations.
 *
 * @see HyperbolicEquationSolver
 * @see Equation
 */
public class HyperbolicEquation extends Equation {

    /**
     * Creates a new hyperbolic partial differential equation with specified domain
     * and boundary conditions.
     *
     * @param x1                   left boundary of the spatial domain
     * @param x2                   right boundary of the spatial domain
     * @param t2                   right boundary of the temporal domain
     * @param leftBorderCondition  boundary condition at x = x1
     * @param rightBorderCondition boundary condition at x = x2
     * @throws IllegalArgumentException if x1 &gt;= x2 or t2 &lt;= 0
     */
    public HyperbolicEquation(double x1, double x2, double t2,
                              BorderCondition leftBorderCondition,
                              BorderCondition rightBorderCondition) {
        super(x1, x2, t2, leftBorderCondition, rightBorderCondition);
    }

    /**
     * Returns the coefficient M(x,t,U) of the second-order time derivative term.
     * For hyperbolic equations, this coefficient is always 1, representing the
     * standard form of wave equations.
     *
     * @param x spatial coordinate
     * @param t time coordinate
     * @param U value of the solution at (x,t)
     * @return coefficient M(x,t,U) = 1
     */
    @Override
    public double gM(double x, double t, double U) {
        return 1;
    }

    /**
     * Returns the initial condition for the time derivative ∂U/∂t at time t = 0.
     * This method should be overridden by specific hyperbolic equation types to provide
     * the actual initial velocity or rate of change.
     *
     * @param x spatial coordinate
     * @return initial value of ∂U/∂t(x,0)
     */
    public double gdU_dt0(double x) {
        return 0;
    }
}
