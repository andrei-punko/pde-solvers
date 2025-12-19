import React, { useState } from 'react';
import EquationForm from './components/EquationForm';
import SolutionVisualization from './components/SolutionVisualization';
import { solveEquation } from './services/api';
import './index.css';

function App() {
  const [solutionData, setSolutionData] = useState(null);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState(null);

  const handleSolve = async (formData) => {
    setIsLoading(true);
    setError(null);
    setSolutionData(null);

    try {
      // Convert form data to API format
      const requestData = {
        equationType: formData.equationType,
        x1: formData.x1,
        x2: formData.x2,
        t2: formData.t2,
        h: formData.h,
        tau: formData.tau,
        leftBorderConditionType: formData.leftBorderConditionType,
        rightBorderConditionType: formData.rightBorderConditionType,
        leftDirichletValue: formData.leftDirichletValue,
        rightDirichletValue: formData.rightDirichletValue,
        leftNeumannValue: formData.leftNeumannValue,
        rightNeumannValue: formData.rightNeumannValue,
        leftRobinH: formData.leftRobinH,
        leftRobinTheta: formData.leftRobinTheta,
        rightRobinH: formData.rightRobinH,
        rightRobinTheta: formData.rightRobinTheta,
        coefficientK: formData.coefficientK,
        coefficientV: formData.coefficientV,
        coefficientF: formData.coefficientF,
        coefficientL: formData.coefficientL,
        coefficientM: formData.coefficientM,
        initialConditionType: formData.initialConditionType,
        initialConditionParam1: formData.initialConditionParam1,
        initialConditionParam2: formData.initialConditionParam2,
        initialDerivative: formData.initialDerivative,
      };

      const result = await solveEquation(requestData);
      console.log('Received result from API:', {
        allKeys: Object.keys(result),
        hasSolution: !!result.solution,
        timeSteps: result.timeSteps,
        spacePoints: result.spacePoints,
        minValue: result.minValue,
        maxValue: result.maxValue,
        solutionType: typeof result.solution,
        solutionIsArray: Array.isArray(result.solution),
        xCoordinates: result.xCoordinates,
        xCoordinatesLength: result.xCoordinates?.length,
        tCoordinates: result.tCoordinates,
        tCoordinatesLength: result.tCoordinates?.length,
        fullResult: result
      });
      setSolutionData(result);
    } catch (err) {
      setError(err.message || 'An error occurred while solving the equation');
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="container">
      <h1>Partial Differential Equations Solver</h1>
      
      {error && (
        <div className="error">
          <strong>Error:</strong> {error}
        </div>
      )}

      <EquationForm onSubmit={handleSolve} isLoading={isLoading} />

      {isLoading && (
        <div className="loading">
          <p>Solving equation... Please wait.</p>
        </div>
      )}

      {solutionData && !isLoading && (
        <SolutionVisualization solutionData={solutionData} />
      )}
    </div>
  );
}

export default App;

