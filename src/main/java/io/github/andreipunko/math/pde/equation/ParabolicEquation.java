package io.github.andreipunko.math.pde.equation;

import io.github.andreipunko.math.pde.border.BorderCondition;
import io.github.andreipunko.math.pde.solver.ParabolicEquationSolver;

/**
 * Represents a parabolic partial differential equation, which typically describes
 * heat conduction or mass diffusion processes. The equation has the form:
 * <p>
 * L(x,t,U)*∂U/∂t = ∂U( K(x,t,U)*∂U/∂x )/∂x + V(x,t,U)*∂U/∂x + F(x,t,U)
 * <p>
 * where U = U(x,t) is the unknown function (temperature or concentration).
 * <p>
 * This is a special case of the general second-order PDE where the coefficient
 * of the second-order time derivative (M) is zero, and the coefficient of the
 * first-order time derivative (L) is one.
 *
 * @see ParabolicEquationSolver
 * @see Equation
 */
public class ParabolicEquation extends Equation {

    /**
     * Creates a new parabolic partial differential equation with specified domain
     * and boundary conditions.
     *
     * @param x1                   left boundary of the spatial domain
     * @param x2                   right boundary of the spatial domain
     * @param t2                   right boundary of the temporal domain
     * @param leftBorderCondition  boundary condition at x = x1
     * @param rightBorderCondition boundary condition at x = x2
     * @throws IllegalArgumentException if x1 &gt;= x2 or t2 &lt;= 0
     */
    public ParabolicEquation(double x1, double x2, double t2,
                             BorderCondition leftBorderCondition,
                             BorderCondition rightBorderCondition) {
        super(x1, x2, t2, leftBorderCondition, rightBorderCondition);
    }

    /**
     * Returns the coefficient L(x,t,U) of the first-order time derivative term.
     * For parabolic equations, this coefficient is always 1, representing the
     * standard form of heat/mass transfer equations.
     *
     * @param x spatial coordinate
     * @param t time coordinate
     * @param U value of the solution at (x,t)
     * @return coefficient L(x,t,U) = 1
     */
    @Override
    public double gL(double x, double t, double U) {
        return 1;
    }
}
