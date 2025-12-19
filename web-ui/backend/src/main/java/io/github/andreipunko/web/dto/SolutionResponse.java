package io.github.andreipunko.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO для ответа с результатами решения уравнения.
 * Содержит матрицу решения и метаданные о сетке.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SolutionResponse {
    
    /**
     * Матрица решения U(x,t) в виде 2D массива.
     * solution[i][j] = U(x_j, t_i), где i - индекс времени, j - индекс пространства
     */
    @JsonProperty("solution")
    private double[][] solution;
    
    /**
     * Пространственные координаты сетки
     */
    @JsonProperty("xCoordinates")
    private double[] xCoordinates;
    
    /**
     * Временные координаты сетки
     */
    @JsonProperty("tCoordinates")
    private double[] tCoordinates;
    
    /**
     * Количество временных слоев
     */
    @JsonProperty("timeSteps")
    private int timeSteps;
    
    /**
     * Количество пространственных точек
     */
    @JsonProperty("spacePoints")
    private int spacePoints;
    
    /**
     * Минимальное значение решения
     */
    @JsonProperty("minValue")
    private double minValue;
    
    /**
     * Максимальное значение решения
     */
    @JsonProperty("maxValue")
    private double maxValue;
}

