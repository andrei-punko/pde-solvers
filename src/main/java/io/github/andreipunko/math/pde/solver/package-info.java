/**
 * Numerical PDE solvers using finite differences and the Thomas algorithm.
 * <p>
 * <b>Grid steps {@code h} and {@code tau}:</b> solvers only require positive finite steps and build a tensor-product
 * grid. They do <em>not</em> enforce physical stability conditions (such as CFL bounds typical of explicit schemes).
 * The implemented schemes are <em>implicit</em>, which generally improves stability relative to explicit methods,
 * but <em>accuracy</em> still depends on resolving the relevant space–time scales: {@code h} and {@code tau} should be
 * chosen using the problem physics or standard discretization theory. Poor choices can yield inaccurate results or
 * poorly conditioned tridiagonal systems; the latter may trigger
 * {@link IllegalArgumentException} from {@link AbstractEquationSolver#solve3DiagonalEquationsSystem}.
 */
package io.github.andreipunko.math.pde.solver;
