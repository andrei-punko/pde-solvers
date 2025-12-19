package io.github.andreipunko.web.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

/**
 * DTO для запроса на решение дифференциального уравнения в частных производных.
 * Содержит все параметры, необходимые для создания уравнения и его решения.
 */
@Data
public class EquationRequest {
    
    /**
     * Тип уравнения: "parabolic" или "hyperbolic"
     */
    @NotNull(message = "Тип уравнения обязателен")
    private String equationType;
    
    /**
     * Левая граница пространственной области
     */
    @NotNull(message = "x1 обязателен")
    private Double x1;
    
    /**
     * Правая граница пространственной области
     */
    @NotNull(message = "x2 обязателен")
    private Double x2;
    
    /**
     * Правая граница временной области
     */
    @NotNull(message = "t2 обязателен")
    @Positive(message = "t2 должен быть положительным")
    private Double t2;
    
    /**
     * Шаг пространственной сетки
     */
    @NotNull(message = "h обязателен")
    @Positive(message = "h должен быть положительным")
    private Double h;
    
    /**
     * Шаг временной сетки
     */
    @NotNull(message = "tau обязателен")
    @Positive(message = "tau должен быть положительным")
    private Double tau;
    
    /**
     * Тип граничного условия на левой границе: "dirichlet", "neumann", "robin"
     */
    @NotNull(message = "Тип левого граничного условия обязателен")
    private String leftBorderConditionType;
    
    /**
     * Тип граничного условия на правой границе: "dirichlet", "neumann", "robin"
     */
    @NotNull(message = "Тип правого граничного условия обязателен")
    private String rightBorderConditionType;
    
    // Параметры граничных условий
    
    /**
     * Значение для Dirichlet на левой границе (функция от t, пока константа)
     */
    private Double leftDirichletValue;
    
    /**
     * Значение для Dirichlet на правой границе (функция от t, пока константа)
     */
    private Double rightDirichletValue;
    
    /**
     * Значение производной для Neumann на левой границе (функция от t, пока константа)
     */
    private Double leftNeumannValue;
    
    /**
     * Значение производной для Neumann на правой границе (функция от t, пока константа)
     */
    private Double rightNeumannValue;
    
    /**
     * Коэффициент h для Robin на левой границе
     */
    private Double leftRobinH;
    
    /**
     * Значение Theta для Robin на левой границе (функция от t, пока константа)
     */
    private Double leftRobinTheta;
    
    /**
     * Коэффициент h для Robin на правой границе
     */
    private Double rightRobinH;
    
    /**
     * Значение Theta для Robin на правой границе (функция от t, пока константа)
     */
    private Double rightRobinTheta;
    
    // Коэффициенты уравнения
    
    /**
     * Коэффициент K(x,t,U) - коэффициент диффузии/проводимости (пока константа)
     */
    private Double coefficientK;
    
    /**
     * Коэффициент V(x,t,U) - коэффициент конвекции (пока константа)
     */
    private Double coefficientV;
    
    /**
     * Источник F(x,t,U) (пока константа)
     */
    private Double coefficientF;
    
    /**
     * Коэффициент L(x,t,U) - коэффициент затухания (для параболических, пока константа)
     */
    private Double coefficientL;
    
    /**
     * Коэффициент M(x,t,U) - коэффициент массы (для гиперболических, пока константа)
     */
    private Double coefficientM;
    
    // Начальные условия
    
    /**
     * Начальное условие U(x,0) - тип: "constant", "linear", "sinusoidal", "custom"
     * Для упрощения пока поддерживаем константу или линейную функцию
     */
    private String initialConditionType;
    
    /**
     * Параметры начального условия (зависят от типа)
     */
    private Double initialConditionParam1;
    private Double initialConditionParam2;
    
    /**
     * Начальная производная по времени dU/dt(x,0) для гиперболических уравнений
     */
    private Double initialDerivative;
}

