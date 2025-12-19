package io.github.andreipunko.web.service;

import io.github.andreipunko.math.matrix.Matrix2D;
import io.github.andreipunko.math.pde.equation.Equation;
import io.github.andreipunko.math.pde.equation.HyperbolicEquation;
import io.github.andreipunko.math.pde.equation.ParabolicEquation;
import io.github.andreipunko.math.pde.solver.HyperbolicEquationSolver;
import io.github.andreipunko.math.pde.solver.ParabolicEquationSolver;
import io.github.andreipunko.math.pde.solver.Solution;
import io.github.andreipunko.math.space.Area;
import io.github.andreipunko.web.dto.EquationRequest;
import io.github.andreipunko.web.dto.SolutionResponse;
import org.springframework.stereotype.Service;

/**
 * Сервис для решения уравнений и преобразования результатов.
 */
@Service
public class SolverService {

    private final EquationBuilderService equationBuilderService;

    public SolverService(EquationBuilderService equationBuilderService) {
        this.equationBuilderService = equationBuilderService;
    }

    /**
     * Решает уравнение на основе запроса и возвращает результат.
     *
     * @param request запрос с параметрами уравнения
     * @return результат решения
     */
    public SolutionResponse solve(EquationRequest request) {
        Equation equation = equationBuilderService.buildEquation(request);

        Solution<?> solution;
        if (equation instanceof HyperbolicEquation) {
            HyperbolicEquationSolver solver = new HyperbolicEquationSolver();
            solution = solver.solve((HyperbolicEquation) equation, request.getH(), request.getTau());
        } else if (equation instanceof ParabolicEquation) {
            ParabolicEquationSolver solver = new ParabolicEquationSolver();
            solution = solver.solve((ParabolicEquation) equation, request.getH(), request.getTau());
        } else {
            throw new IllegalArgumentException("Неподдерживаемый тип уравнения");
        }

        return convertToResponse(solution);
    }

    /**
     * Преобразует Solution в SolutionResponse.
     */
    private SolutionResponse convertToResponse(Solution<?> solution) {
        Area area = solution.area();
        Matrix2D solutionMatrix = solution.solution();

        int timeSteps = solutionMatrix.getM();
        int spacePoints = solutionMatrix.getN();

        // Преобразуем матрицу в 2D массив
        double[][] solutionArray = new double[timeSteps][spacePoints];
        for (int i = 0; i < timeSteps; i++) {
            for (int j = 0; j < spacePoints; j++) {
                solutionArray[i][j] = solutionMatrix.get(i, j);
            }
        }

        // Получаем координаты
        // Матрица имеет размеры (timeSteps x spacePoints), где timeSteps = area.tn() + 1, spacePoints = area.xn() + 1
        double[] xCoordinates = new double[spacePoints];
        for (int j = 0; j < spacePoints; j++) {
            xCoordinates[j] = area.xx(j);
        }

        double[] tCoordinates = new double[timeSteps];
        for (int i = 0; i < timeSteps; i++) {
            tCoordinates[i] = area.tx(i);
        }

        // Находим min и max значения
        double minValue = solutionMatrix.min();
        double maxValue = solutionMatrix.max();

        // Проверка и логирование
        System.out.println("Creating SolutionResponse: " +
                "timeSteps=" + timeSteps + 
                ", spacePoints=" + spacePoints +
                ", xCoordinates.length=" + xCoordinates.length +
                ", tCoordinates.length=" + tCoordinates.length +
                ", solutionArray.length=" + solutionArray.length +
                ", solutionArray[0].length=" + (solutionArray.length > 0 ? solutionArray[0].length : 0));

        SolutionResponse response = new SolutionResponse(
                solutionArray,
                xCoordinates,
                tCoordinates,
                timeSteps,
                spacePoints,
                minValue,
                maxValue
        );
        
        // Проверка после создания
        System.out.println("SolutionResponse created: " +
                "xCoordinates=" + (response.getXCoordinates() != null ? "not null, length=" + response.getXCoordinates().length : "null") +
                ", tCoordinates=" + (response.getTCoordinates() != null ? "not null, length=" + response.getTCoordinates().length : "null"));
        
        return response;
    }
}

