package io.github.andreipunko.math.pde.equation;

import io.github.andreipunko.math.pde.border.BorderCondition;
import io.github.andreipunko.math.pde.solver.EquationSolver;
import lombok.Getter;

/**
 * Abstract base class representing a second-order partial differential equation.
 * The equation has the general form:
 * <p>
 * M(x,t,U)*∂²U/∂t² + L(x,t,U)*∂U/∂t = ∂U( K(x,t,U)*∂U/∂x )/∂x + V(x,t,U)*∂U/∂x + F(x,t,U)
 * <p>
 * where U = U(x,t) is the unknown function.
 * <p>
 * The equation is defined on a space-time domain [x1,x2]&times;[0,t2] with boundary conditions
 * specified on the left and right boundaries. This class provides default implementations
 * for the coefficient functions that can be overridden by specific equation types.
 *
 * @see BorderCondition
 * @see EquationSolver
 */
@Getter
public abstract class Equation {

    private final double x1;
    private final double x2;
    private final double t2;
    private final BorderCondition leftBorderCondition;
    private final BorderCondition rightBorderCondition;

    /**
     * Creates a new partial differential equation with specified domain and boundary conditions.
     *
     * @param x1                   left boundary of the spatial domain
     * @param x2                   right boundary of the spatial domain
     * @param t2                   right boundary of the temporal domain
     * @param leftBorderCondition  boundary condition at x = x1
     * @param rightBorderCondition boundary condition at x = x2
     * @throws IllegalArgumentException if x1 &gt;= x2 or t2 &lt;= 0
     */
    public Equation(double x1, double x2, double t2,
                    BorderCondition leftBorderCondition,
                    BorderCondition rightBorderCondition) {
        this.x1 = x1;
        this.x2 = x2;
        this.t2 = t2;
        this.leftBorderCondition = leftBorderCondition;
        this.rightBorderCondition = rightBorderCondition;
    }

    /**
     * Returns the initial condition U(x) at time t = 0.
     * This method should be overridden by specific equation types to provide
     * the actual initial condition.
     *
     * @param x spatial coordinate
     * @return initial value U(x,0)
     */
    public double gU0(double x) {
        return 0;
    }

    /**
     * Returns the coefficient M(x,t,U) of the second-order time derivative term.
     * This coefficient represents the mass or inertia term in the equation.
     * The default implementation returns 0, which should be overridden for equations
     * containing second-order time derivatives.
     *
     * @param x spatial coordinate
     * @param t time coordinate
     * @param U value of the solution at (x,t)
     * @return coefficient M(x,t,U)
     */
    public double gM(double x, double t, double U) {
        return 0;
    }

    /**
     * Returns the coefficient L(x,t,U) of the first-order time derivative term.
     * This coefficient represents damping or dissipation in the equation.
     * The default implementation returns 0, which should be overridden for equations
     * containing first-order time derivatives.
     *
     * @param x spatial coordinate
     * @param t time coordinate
     * @param U value of the solution at (x,t)
     * @return coefficient L(x,t,U)
     */
    public double gL(double x, double t, double U) {
        return 0;
    }

    /**
     * Returns the coefficient K(x,t,U) of the second-order space derivative term.
     * This coefficient represents diffusion or conductivity in the equation.
     * The default implementation returns 1, which should be overridden for equations
     * with variable diffusion coefficients.
     *
     * @param x spatial coordinate
     * @param t time coordinate
     * @param U value of the solution at (x,t)
     * @return coefficient K(x,t,U)
     */
    public double gK(double x, double t, double U) {
        return 1;
    }

    /**
     * Returns the coefficient V(x,t,U) of the first-order space derivative term.
     * This coefficient represents convection or advection in the equation.
     * The default implementation returns 0, which should be overridden for equations
     * containing convective terms.
     *
     * @param x spatial coordinate
     * @param t time coordinate
     * @param U value of the solution at (x,t)
     * @return coefficient V(x,t,U)
     */
    public double gV(double x, double t, double U) {
        return 0;
    }

    /**
     * Returns the source term F(x,t,U) of the equation.
     * This term represents external forces or sources in the equation.
     * The default implementation returns 0, which should be overridden for equations
     * containing source terms.
     *
     * @param x spatial coordinate
     * @param t time coordinate
     * @param U value of the solution at (x,t)
     * @return source term F(x,t,U)
     */
    public double gF(double x, double t, double U) {
        return 0;
    }
}
