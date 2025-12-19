import React, { useState, useMemo } from 'react';
import { LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer } from 'recharts';

const SolutionVisualization = ({ solutionData }) => {
  const [selectedTimeIndex, setSelectedTimeIndex] = useState(0);
  const [autoScaleY, setAutoScaleY] = useState(true);

  // Convert data for chart
  const chartData = useMemo(() => {
    if (!solutionData) {
      console.log('No solutionData');
      return [];
    }

    if (!solutionData.solution) {
      console.log('No solutionData.solution');
      return [];
    }

    if (!solutionData.xCoordinates) {
      console.log('No solutionData.xCoordinates');
      return [];
    }

    if (!Array.isArray(solutionData.solution)) {
      console.log('solutionData.solution is not an array:', typeof solutionData.solution);
      return [];
    }

    const timeIndex = Math.min(selectedTimeIndex, solutionData.timeSteps - 1);
    const data = [];

    // Check that time layer exists
    if (!solutionData.solution[timeIndex]) {
      console.log('No data for time layer:', timeIndex, 'of', solutionData.timeSteps);
      return [];
    }

    if (!Array.isArray(solutionData.solution[timeIndex])) {
      console.log('Time layer is not an array:', typeof solutionData.solution[timeIndex]);
      return [];
    }

    const spacePoints = solutionData.spacePoints || solutionData.xCoordinates.length;
    
    for (let i = 0; i < spacePoints; i++) {
      const x = solutionData.xCoordinates[i];
      const u = solutionData.solution[timeIndex][i];
      
      if (x === undefined || x === null || isNaN(x)) {
        console.warn(`Invalid x coordinate [${i}]:`, x);
        continue;
      }
      
      if (u === undefined || u === null || isNaN(u)) {
        console.warn(`Invalid u value [${i}]:`, u);
        continue;
      }
      
      data.push({
        x: Number(x),
        u: Number(u),
      });
    }

    console.log('Chart data:', {
      timeIndex,
      spacePoints,
      dataLength: data.length,
      firstPoint: data[0],
      lastPoint: data[data.length - 1],
      sample: data.slice(0, 5),
      solutionStructure: {
        solutionIsArray: Array.isArray(solutionData.solution),
        solutionLength: solutionData.solution.length,
        firstTimeSliceIsArray: Array.isArray(solutionData.solution[0]),
        firstTimeSliceLength: solutionData.solution[0]?.length
      }
    });

    return data;
  }, [solutionData, selectedTimeIndex]);

  if (!solutionData) {
    return (
      <div className="card">
        <p>Нет данных для отображения</p>
      </div>
    );
  }

  // Debug information
  console.log('SolutionVisualization received data:', {
    hasSolution: !!solutionData.solution,
    hasXCoordinates: !!solutionData.xCoordinates,
    hasTCoordinates: !!solutionData.tCoordinates,
    timeSteps: solutionData.timeSteps,
    spacePoints: solutionData.spacePoints,
    minValue: solutionData.minValue,
    maxValue: solutionData.maxValue,
    solutionType: typeof solutionData.solution,
    solutionIsArray: Array.isArray(solutionData.solution),
    firstTimeSlice: solutionData.solution?.[0]?.slice(0, 5)
  });

  const currentTime = solutionData.tCoordinates 
    ? solutionData.tCoordinates[selectedTimeIndex] 
    : 0;

  const handleTimeChange = (e) => {
    const newIndex = parseInt(e.target.value);
    setSelectedTimeIndex(newIndex);
  };

  return (
    <div className="card">
      <h2>Solution Visualization</h2>
      
      {/* Time selection slider */}
      <div className="form-group" style={{ marginBottom: '20px' }}>
        <label>
          Time: {currentTime.toFixed(4)} 
          (layer {selectedTimeIndex + 1} of {solutionData.timeSteps})
        </label>
        <input
          type="range"
          min="0"
          max={solutionData.timeSteps - 1}
          value={selectedTimeIndex}
          onChange={handleTimeChange}
          style={{ width: '100%' }}
        />
      </div>

      {/* Auto-scaling toggle */}
      <div className="form-group" style={{ marginBottom: '20px', display: 'flex', alignItems: 'center', gap: '10px' }}>
        <input
          type="checkbox"
          id="autoScaleY"
          checked={autoScaleY}
          onChange={(e) => setAutoScaleY(e.target.checked)}
          style={{ width: '18px', height: '18px', cursor: 'pointer' }}
        />
        <label htmlFor="autoScaleY" style={{ cursor: 'pointer', userSelect: 'none' }}>
          Auto-scale Y axis (if disabled, uses the range of the entire solution)
        </label>
      </div>

      {/* Solution information */}
      <div style={{ marginBottom: '20px', fontSize: '14px', color: '#666', padding: '10px', backgroundColor: '#f5f5f5', borderRadius: '4px' }}>
        <p><strong>Spatial points:</strong> {solutionData.spacePoints} (from x={solutionData.xCoordinates?.[0]?.toFixed(6)} to x={solutionData.xCoordinates?.[solutionData.xCoordinates.length - 1]?.toFixed(6)})</p>
        <p><strong>Time layers:</strong> {solutionData.timeSteps} (from t={solutionData.tCoordinates?.[0]?.toFixed(4)} to t={solutionData.tCoordinates?.[solutionData.tCoordinates.length - 1]?.toFixed(4)})</p>
        <p style={{ fontSize: '12px', color: '#999', fontStyle: 'italic', marginTop: '5px' }}>
          Different number of points is normal: spatial grid (X) and temporal grid (T) are independent and depend on steps h and tau
        </p>
        <p><strong>Minimum value:</strong> {solutionData.minValue !== undefined && solutionData.minValue !== null ? solutionData.minValue.toFixed(6) : 'N/A'}</p>
        <p><strong>Maximum value:</strong> {solutionData.maxValue !== undefined && solutionData.maxValue !== null ? solutionData.maxValue.toFixed(6) : 'N/A'}</p>
        <p><strong>Chart data points:</strong> {chartData.length} points (time layer {selectedTimeIndex + 1})</p>
        {chartData.length > 0 && (
          <>
            <p><strong>X range:</strong> {chartData[0]?.x?.toFixed(6)} ... {chartData[chartData.length - 1]?.x?.toFixed(6)}</p>
            <p><strong>U range:</strong> {Math.min(...chartData.map(d => d.u)).toFixed(6)} ... {Math.max(...chartData.map(d => d.u)).toFixed(6)}</p>
          </>
        )}
      </div>

      {/* Chart */}
      {chartData.length === 0 ? (
        <div style={{ padding: '20px', textAlign: 'center', color: '#f44336' }}>
          <p>No data to display chart</p>
          <p style={{ fontSize: '12px', color: '#666' }}>
            Check browser console for debug information
          </p>
        </div>
      ) : (
        <div>
          <ResponsiveContainer width="100%" height={400}>
            <LineChart 
              data={chartData} 
              margin={{ top: 5, right: 30, left: 20, bottom: 5 }}
            >
              <CartesianGrid strokeDasharray="3 3" />
              <XAxis 
                dataKey="x" 
                type="number"
                scale="linear"
                domain={['dataMin', 'dataMax']}
                label={{ value: 'Spatial coordinate x', position: 'insideBottom', offset: -5 }}
              />
              <YAxis 
                type="number"
                scale="linear"
                domain={
                  autoScaleY 
                    ? ['auto', 'auto'] 
                    : [
                        solutionData.minValue !== undefined && solutionData.minValue !== null 
                          ? solutionData.minValue 
                          : 'auto',
                        solutionData.maxValue !== undefined && solutionData.maxValue !== null 
                          ? solutionData.maxValue 
                          : 'auto'
                      ]
                }
                label={{ value: 'U(x,t)', angle: -90, position: 'insideLeft' }}
              />
              <Tooltip 
                formatter={(value) => [typeof value === 'number' ? value.toFixed(6) : value, 'U(x,t)']}
                labelFormatter={(label) => `x = ${typeof label === 'number' ? label.toFixed(6) : label}`}
              />
              <Legend />
              <Line 
                type="monotone" 
                dataKey="u" 
                stroke="#4CAF50" 
                strokeWidth={2}
                dot={false}
                name="U(x,t)"
                isAnimationActive={false}
                connectNulls={false}
              />
            </LineChart>
          </ResponsiveContainer>
          
          {/* Table with first values for debugging */}
          {chartData.length > 0 && (
            <details style={{ marginTop: '20px', fontSize: '12px' }}>
              <summary style={{ cursor: 'pointer', color: '#666' }}>Show first 10 data points</summary>
              <table style={{ width: '100%', marginTop: '10px', borderCollapse: 'collapse' }}>
                <thead>
                  <tr style={{ backgroundColor: '#f5f5f5' }}>
                    <th style={{ padding: '5px', border: '1px solid #ddd' }}>Index</th>
                    <th style={{ padding: '5px', border: '1px solid #ddd' }}>X</th>
                    <th style={{ padding: '5px', border: '1px solid #ddd' }}>U(x,t)</th>
                  </tr>
                </thead>
                <tbody>
                  {chartData.slice(0, 10).map((point, idx) => (
                    <tr key={idx}>
                      <td style={{ padding: '5px', border: '1px solid #ddd', textAlign: 'center' }}>{idx}</td>
                      <td style={{ padding: '5px', border: '1px solid #ddd', textAlign: 'right' }}>{point.x.toFixed(6)}</td>
                      <td style={{ padding: '5px', border: '1px solid #ddd', textAlign: 'right' }}>{point.u.toFixed(6)}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </details>
          )}
        </div>
      )}

      {/* Quick time selection */}
      <div style={{ marginTop: '20px', display: 'flex', gap: '10px', flexWrap: 'wrap' }}>
        <button 
          onClick={() => setSelectedTimeIndex(0)}
          className="btn"
          style={{ backgroundColor: '#2196F3', color: 'white', padding: '5px 15px', fontSize: '14px' }}
        >
          Start (t=0)
        </button>
        <button 
          onClick={() => setSelectedTimeIndex(Math.floor(solutionData.timeSteps / 2))}
          className="btn"
          style={{ backgroundColor: '#2196F3', color: 'white', padding: '5px 15px', fontSize: '14px' }}
        >
          Middle
        </button>
        <button 
          onClick={() => setSelectedTimeIndex(solutionData.timeSteps - 1)}
          className="btn"
          style={{ backgroundColor: '#2196F3', color: 'white', padding: '5px 15px', fontSize: '14px' }}
        >
          End (t={solutionData.tCoordinates?.[solutionData.timeSteps - 1]?.toFixed(4)})
        </button>
      </div>
    </div>
  );
};

export default SolutionVisualization;

