package io.github.andreipunko.web.controller;

import io.github.andreipunko.web.dto.EquationRequest;
import io.github.andreipunko.web.dto.SolutionResponse;
import io.github.andreipunko.web.service.SolverService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST контроллер для решения дифференциальных уравнений в частных производных.
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class SolverController {

    private final SolverService solverService;

    public SolverController(SolverService solverService) {
        this.solverService = solverService;
    }

    /**
     * Решает уравнение на основе переданных параметров.
     *
     * @param request запрос с параметрами уравнения
     * @return результат решения
     */
    @PostMapping("/solve")
    public ResponseEntity<?> solve(@Valid @RequestBody EquationRequest request) {
        try {
            SolutionResponse response = solverService.solve(request);
            
            // Логирование для отладки
            System.out.println("Response created: timeSteps=" + response.getTimeSteps() + 
                    ", spacePoints=" + response.getSpacePoints() +
                    ", xCoordinates length=" + (response.getXCoordinates() != null ? response.getXCoordinates().length : "null") +
                    ", tCoordinates length=" + (response.getTCoordinates() != null ? response.getTCoordinates().length : "null") +
                    ", solution dimensions=" + (response.getSolution() != null ? response.getSolution().length + "x" + (response.getSolution().length > 0 ? response.getSolution()[0].length : 0) : "null"));
            
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse("Ошибка валидации: " + e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Ошибка при решении уравнения: " + e.getMessage()));
        }
    }

    /**
     * Проверка работоспособности сервиса.
     *
     * @return статус сервиса
     */
    @GetMapping("/health")
    public ResponseEntity<HealthResponse> health() {
        return ResponseEntity.ok(new HealthResponse("OK", "PDE Solver API is running"));
    }

    /**
     * Класс для ответа об ошибке.
     */
    public static class ErrorResponse {
        private String error;

        public ErrorResponse(String error) {
            this.error = error;
        }

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }
    }

    /**
     * Класс для ответа о состоянии сервиса.
     */
    public static class HealthResponse {
        private String status;
        private String message;

        public HealthResponse(String status, String message) {
            this.status = status;
            this.message = message;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}

