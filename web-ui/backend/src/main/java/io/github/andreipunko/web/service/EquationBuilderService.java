package io.github.andreipunko.web.service;

import io.github.andreipunko.math.pde.border.*;
import io.github.andreipunko.math.pde.equation.Equation;
import io.github.andreipunko.math.pde.equation.HyperbolicEquation;
import io.github.andreipunko.math.pde.equation.ParabolicEquation;
import io.github.andreipunko.web.dto.EquationRequest;
import org.springframework.stereotype.Service;

/**
 * Сервис для создания объектов уравнений из DTO запросов.
 */
@Service
public class EquationBuilderService {

    /**
     * Создает уравнение на основе запроса.
     *
     * @param request запрос с параметрами уравнения
     * @return созданное уравнение
     */
    public Equation buildEquation(EquationRequest request) {
        BorderCondition leftBorder = createBorderCondition(
                request.getLeftBorderConditionType(),
                request.getLeftDirichletValue(),
                request.getLeftNeumannValue(),
                request.getLeftRobinH(),
                request.getLeftRobinTheta()
        );

        BorderCondition rightBorder = createBorderCondition(
                request.getRightBorderConditionType(),
                request.getRightDirichletValue(),
                request.getRightNeumannValue(),
                request.getRightRobinH(),
                request.getRightRobinTheta()
        );

        // Сохраняем параметры для использования в методах коэффициентов
        final double k = request.getCoefficientK() != null ? request.getCoefficientK() : 1.0;
        final double v = request.getCoefficientV() != null ? request.getCoefficientV() : 0.0;
        final double f = request.getCoefficientF() != null ? request.getCoefficientF() : 0.0;
        final double l = request.getCoefficientL() != null ? request.getCoefficientL() : 1.0;
        final double m = request.getCoefficientM() != null ? request.getCoefficientM() : 1.0;

        final String initialConditionType = request.getInitialConditionType() != null 
                ? request.getInitialConditionType() : "constant";
        final double initParam1 = request.getInitialConditionParam1() != null 
                ? request.getInitialConditionParam1() : 0.0;
        final double initParam2 = request.getInitialConditionParam2() != null 
                ? request.getInitialConditionParam2() : 0.0;
        final double initialDerivative = request.getInitialDerivative() != null 
                ? request.getInitialDerivative() : 0.0;

        if ("hyperbolic".equalsIgnoreCase(request.getEquationType())) {
            return new HyperbolicEquation(
                    request.getX1(),
                    request.getX2(),
                    request.getT2(),
                    leftBorder,
                    rightBorder
            ) {
                @Override
                public double gK(double x, double t, double U) {
                    return k;
                }

                @Override
                public double gV(double x, double t, double U) {
                    return v;
                }

                @Override
                public double gF(double x, double t, double U) {
                    return f;
                }

                @Override
                public double gL(double x, double t, double U) {
                    return l;
                }

                @Override
                public double gM(double x, double t, double U) {
                    return m;
                }

                @Override
                public double gU0(double x) {
                    return calculateInitialCondition(x, request.getX1(), request.getX2(), 
                            initialConditionType, initParam1, initParam2);
                }

                @Override
                public double gdU_dt0(double x) {
                    return initialDerivative;
                }
            };
        } else {
            // Параболическое уравнение (по умолчанию)
            return new ParabolicEquation(
                    request.getX1(),
                    request.getX2(),
                    request.getT2(),
                    leftBorder,
                    rightBorder
            ) {
                @Override
                public double gK(double x, double t, double U) {
                    return k;
                }

                @Override
                public double gV(double x, double t, double U) {
                    return v;
                }

                @Override
                public double gF(double x, double t, double U) {
                    return f;
                }

                @Override
                public double gL(double x, double t, double U) {
                    return l;
                }

                @Override
                public double gU0(double x) {
                    return calculateInitialCondition(x, request.getX1(), request.getX2(), 
                            initialConditionType, initParam1, initParam2);
                }
            };
        }
    }

    /**
     * Создает граничное условие на основе типа и параметров.
     */
    private BorderCondition createBorderCondition(String type, Double dirichletValue,
                                                   Double neumannValue, Double robinH, Double robinTheta) {
        if (type == null) {
            return new DirichletBorderCondition() {
                @Override
                public double gU(double t) {
                    return 0.0;
                }
            };
        }

        switch (type.toLowerCase()) {
            case "dirichlet":
                final double dirValue = dirichletValue != null ? dirichletValue : 0.0;
                return new DirichletBorderCondition() {
                    @Override
                    public double gU(double t) {
                        return dirValue;
                    }
                };

            case "neumann":
                final double neuValue = neumannValue != null ? neumannValue : 0.0;
                return new NeumannBorderCondition() {
                    @Override
                    public double gdU_dx(double t) {
                        return neuValue;
                    }
                };

            case "robin":
                final double h = robinH != null ? robinH : 0.0;
                final double theta = robinTheta != null ? robinTheta : 0.0;
                return new RobinBorderCondition() {
                    @Override
                    public double gH() {
                        return h;
                    }

                    @Override
                    public double gTheta(double t) {
                        return theta;
                    }
                };

            default:
                return new DirichletBorderCondition() {
                    @Override
                    public double gU(double t) {
                        return 0.0;
                    }
                };
        }
    }

    /**
     * Вычисляет начальное условие на основе типа и параметров.
     */
    private double calculateInitialCondition(double x, double x1, double x2,
                                             String type, double param1, double param2) {
        double normalizedX = (x - x1) / (x2 - x1); // Нормализация к [0, 1]

        switch (type.toLowerCase()) {
            case "constant":
                return param1;

            case "linear":
                // Линейная функция: param1 + param2 * normalizedX
                return param1 + param2 * normalizedX;

            case "sinusoidal":
                // Синусоида: param1 * sin(param2 * PI * normalizedX)
                return param1 * Math.sin(param2 * Math.PI * normalizedX);

            case "gaussian":
                // Гауссова функция: param1 * exp(-((normalizedX - 0.5) / param2)^2)
                double center = 0.5;
                double sigma = param2 > 0 ? param2 : 0.1;
                return param1 * Math.exp(-Math.pow((normalizedX - center) / sigma, 2));

            default:
                return 0.0;
        }
    }
}

