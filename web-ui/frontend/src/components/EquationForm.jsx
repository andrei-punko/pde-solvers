import React, { useState, useEffect } from 'react';

const EquationForm = ({ onSubmit, isLoading }) => {
  // Функция для получения начальных значений
  const getInitialFormData = () => ({
    // Основные параметры
    // Типичные параметры для уравнения диффузии (диффузия в пластине толщиной 1 мм)
    equationType: 'parabolic',
    x1: 0,                    // Левая граница (м)
    x2: 0.001,                // Правая граница: 1 мм = 0.001 м
    t2: 30,                   // Время моделирования: 30 секунд (достаточно для наблюдения диффузии)
    h: 0.00001,               // Пространственный шаг: 0.01 мм = 0.00001 м (100 точек на 1 мм)
    tau: 0.01,                // Временной шаг: 0.01 сек (100 шагов на 1 сек)
    
    // Граничные условия
    // Dirichlet: нулевая концентрация на границах (типично для задачи диффузии)
    leftBorderConditionType: 'dirichlet',
    rightBorderConditionType: 'dirichlet',
    leftDirichletValue: 0,     // Нулевая концентрация на левой границе
    rightDirichletValue: 0,   // Нулевая концентрация на правой границе
    leftNeumannValue: 0,
    rightNeumannValue: 0,
    leftRobinH: 1,
    leftRobinTheta: 0,
    rightRobinH: 1,
    rightRobinTheta: 0,
    
    // Коэффициенты уравнения диффузии: Ut = D*Uxx
    // D - коэффициент диффузии (для воды при 25°C: ~1e-9 м²/с)
    coefficientK: 1e-9,        // Коэффициент диффузии D = 1e-9 м²/с
    coefficientV: 0,          // Нет конвекции
    coefficientF: 0,          // Нет источника
    coefficientL: 1,          // Стандартное значение для параболического уравнения
    coefficientM: 1,
    
    // Начальные условия
    // Гауссова функция: начальная концентрация с максимумом в центре области
    // Распределение: U(x,0) = A * exp(-((x - центр)/sigma)^2)
    initialConditionType: 'gaussian',
    initialConditionParam1: 100,  // Амплитуда: максимальная концентрация в центре
    initialConditionParam2: 0.15, // Ширина гауссова распределения (sigma) - оптимально для визуализации
    initialDerivative: 0,
  });

  const [formData, setFormData] = useState(getInitialFormData());

  // Сброс формы при монтировании компонента
  useEffect(() => {
    setFormData(getInitialFormData());
  }, []);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: name.includes('Type') || name === 'equationType' 
        ? value 
        : parseFloat(value) || 0
    }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    onSubmit(formData);
  };

  return (
    <form onSubmit={handleSubmit} className="card" autoComplete="off">
      <h2>Equation Parameters</h2>
      
      <div style={{ 
        backgroundColor: '#e3f2fd', 
        padding: '10px', 
        borderRadius: '4px', 
        marginBottom: '15px',
        fontSize: '14px',
        color: '#1976d2'
      }}>
        <strong>Typical solution:</strong> Diffusion equation with Gaussian concentration distribution in the center of the domain.
        Initial concentration has a maximum in the center and gradually spreads (diffuses) to the boundaries.
      </div>
      
      {/* Equation type */}
      <div className="form-group">
        <label>Equation type:</label>
        <select 
          name="equationType" 
          value={formData.equationType}
          onChange={handleChange}
          autoComplete="off"
        >
          <option value="parabolic">Parabolic (diffusion)</option>
          <option value="hyperbolic">Hyperbolic (waves)</option>
        </select>
      </div>

      {/* Domain boundaries */}
      <div className="form-row">
        <div className="form-group">
          <label>Left boundary (x1), m:</label>
          <input
            type="number"
            autoComplete="off"
            name="x1"
            value={formData.x1}
            onChange={handleChange}
            step="0.0001"
            autoComplete="off"
            required
          />
        </div>
        <div className="form-group">
          <label>Right boundary (x2), m:</label>
          <input
            type="number"
            autoComplete="off"
            name="x2"
            value={formData.x2}
            onChange={handleChange}
            step="0.0001"
            required
          />
        </div>
      </div>

      <div className="form-row">
        <div className="form-group">
          <label>Time boundary (t2), sec:</label>
          <input
            type="number"
            autoComplete="off"
            name="t2"
            value={formData.t2}
            onChange={handleChange}
            step="0.001"
            min="0.001"
            required
          />
        </div>
        <div className="form-group">
          <label>Spatial step (h), m:</label>
          <input
            type="number"
            autoComplete="off"
            name="h"
            value={formData.h}
            onChange={handleChange}
            step="0.000001"
            min="0.000001"
            required
          />
        </div>
      </div>

      <div className="form-group">
        <label>Time step (tau), sec:</label>
        <input
          type="number"
          name="tau"
          value={formData.tau}
          onChange={handleChange}
          step="0.0001"
          min="0.0001"
          required
        />
      </div>

      {/* Boundary conditions */}
      <h3>Boundary Conditions</h3>
      
      <div className="form-row">
        <div className="form-group">
          <label>Left boundary:</label>
          <select 
            name="leftBorderConditionType" 
            value={formData.leftBorderConditionType}
            onChange={handleChange}
            autoComplete="off"
          >
            <option value="dirichlet">Dirichlet</option>
            <option value="neumann">Neumann</option>
            <option value="robin">Robin</option>
          </select>
          {formData.leftBorderConditionType === 'dirichlet' && (
            <input
              type="number"
            autoComplete="off"
              name="leftDirichletValue"
              value={formData.leftDirichletValue}
              onChange={handleChange}
              step="0.001"
              placeholder="U value"
            />
          )}
          {formData.leftBorderConditionType === 'neumann' && (
            <input
              type="number"
            autoComplete="off"
              name="leftNeumannValue"
              value={formData.leftNeumannValue}
              onChange={handleChange}
              step="0.001"
              placeholder="dU/dx value"
            />
          )}
          {formData.leftBorderConditionType === 'robin' && (
            <>
              <input
                type="number"
            autoComplete="off"
                name="leftRobinH"
                value={formData.leftRobinH}
                onChange={handleChange}
                step="0.001"
                placeholder="Coefficient h"
              />
              <input
                type="number"
            autoComplete="off"
                name="leftRobinTheta"
                value={formData.leftRobinTheta}
                onChange={handleChange}
                step="0.001"
                placeholder="Theta"
              />
            </>
          )}
        </div>

        <div className="form-group">
          <label>Right boundary:</label>
          <select 
            name="rightBorderConditionType" 
            value={formData.rightBorderConditionType}
            onChange={handleChange}
            autoComplete="off"
          >
            <option value="dirichlet">Dirichlet</option>
            <option value="neumann">Neumann</option>
            <option value="robin">Robin</option>
          </select>
          {formData.rightBorderConditionType === 'dirichlet' && (
            <input
              type="number"
            autoComplete="off"
              name="rightDirichletValue"
              value={formData.rightDirichletValue}
              onChange={handleChange}
              step="0.001"
              placeholder="U value"
            />
          )}
          {formData.rightBorderConditionType === 'neumann' && (
            <input
              type="number"
            autoComplete="off"
              name="rightNeumannValue"
              value={formData.rightNeumannValue}
              onChange={handleChange}
              step="0.001"
              placeholder="dU/dx value"
            />
          )}
          {formData.rightBorderConditionType === 'robin' && (
            <>
              <input
                type="number"
            autoComplete="off"
                name="rightRobinH"
                value={formData.rightRobinH}
                onChange={handleChange}
                step="0.001"
                placeholder="Coefficient h"
              />
              <input
                type="number"
            autoComplete="off"
                name="rightRobinTheta"
                value={formData.rightRobinTheta}
                onChange={handleChange}
                step="0.001"
                placeholder="Theta"
              />
            </>
          )}
        </div>
      </div>

      {/* Coefficients */}
      <h3>Equation Coefficients</h3>
      <div className="form-row">
        <div className="form-group">
          <label>K (diffusion coefficient D), m²/s:</label>
          <input
            type="number"
            autoComplete="off"
            name="coefficientK"
            value={formData.coefficientK}
            onChange={handleChange}
            step="any"
            min="0"
          />
          <small style={{display: 'block', color: '#666', marginTop: '5px'}}>
            Typical values: water ~1e-9, gases ~1e-5 m²/s
          </small>
        </div>
        <div className="form-group">
          <label>V (convection):</label>
          <input
            type="number"
            autoComplete="off"
            name="coefficientV"
            value={formData.coefficientV}
            onChange={handleChange}
            step="0.001"
          />
        </div>
      </div>
      <div className="form-row">
        <div className="form-group">
          <label>F (source):</label>
          <input
            type="number"
            autoComplete="off"
            name="coefficientF"
            value={formData.coefficientF}
            onChange={handleChange}
            step="0.001"
          />
        </div>
        <div className="form-group">
          <label>L (damping):</label>
          <input
            type="number"
            autoComplete="off"
            name="coefficientL"
            value={formData.coefficientL}
            onChange={handleChange}
            step="0.001"
          />
        </div>
      </div>
      {formData.equationType === 'hyperbolic' && (
        <div className="form-group">
          <label>M (mass):</label>
          <input
            type="number"
            autoComplete="off"
            name="coefficientM"
            value={formData.coefficientM}
            onChange={handleChange}
            step="0.001"
          />
        </div>
      )}

      {/* Initial conditions */}
      <h3>Initial Conditions</h3>
      <div className="form-group">
        <label>Initial condition type:</label>
        <select 
          name="initialConditionType" 
          value={formData.initialConditionType}
          onChange={handleChange}
          autoComplete="off"
        >
          <option value="constant">Constant</option>
          <option value="linear">Linear function</option>
          <option value="sinusoidal">Sinusoidal</option>
          <option value="gaussian">Gaussian (centered)</option>
        </select>
        {formData.initialConditionType === 'gaussian' && (
          <small style={{display: 'block', color: '#666', marginTop: '5px'}}>
            Gaussian distribution centered in the middle of the domain: U(x,0) = A × exp(-((x-center)/σ)²)
          </small>
        )}
      </div>
      <div className="form-row">
        <div className="form-group">
          <label>
            Parameter 1 {formData.initialConditionType === 'gaussian' ? '(amplitude A)' : 
                       formData.initialConditionType === 'constant' ? '(constant)' :
                       formData.initialConditionType === 'linear' ? '(intercept)' :
                       '(amplitude)'}:
          </label>
          <input
            type="number"
            autoComplete="off"
            name="initialConditionParam1"
            value={formData.initialConditionParam1}
            onChange={handleChange}
            step="1"
          />
          {formData.initialConditionType === 'gaussian' && (
            <small style={{display: 'block', color: '#666', marginTop: '5px'}}>
              Maximum concentration in the center of the domain
            </small>
          )}
        </div>
        <div className="form-group">
          <label>
            Parameter 2 {formData.initialConditionType === 'gaussian' ? '(width σ)' : 
                       formData.initialConditionType === 'linear' ? '(slope)' :
                       formData.initialConditionType === 'sinusoidal' ? '(frequency)' :
                       '(parameter)'}:
          </label>
          <input
            type="number"
            autoComplete="off"
            name="initialConditionParam2"
            value={formData.initialConditionParam2}
            onChange={handleChange}
            step="0.01"
            min="0.01"
          />
          {formData.initialConditionType === 'gaussian' && (
            <small style={{display: 'block', color: '#666', marginTop: '5px'}}>
              Distribution width (smaller = narrower peak)
            </small>
          )}
        </div>
      </div>
      {formData.equationType === 'hyperbolic' && (
        <div className="form-group">
          <label>Initial derivative (dU/dt):</label>
          <input
            type="number"
            autoComplete="off"
            name="initialDerivative"
            value={formData.initialDerivative}
            onChange={handleChange}
            step="0.001"
          />
        </div>
      )}

      <button type="submit" className="btn btn-primary" disabled={isLoading}>
        {isLoading ? 'Solving...' : 'Solve Equation'}
      </button>
    </form>
  );
};

export default EquationForm;

