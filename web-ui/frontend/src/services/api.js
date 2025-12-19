import axios from 'axios';

const API_BASE_URL = '/api';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

/**
 * Solves the equation based on provided parameters.
 * 
 * @param {Object} equationData - Equation data
 * @returns {Promise} Solution result
 */
export const solveEquation = async (equationData) => {
  try {
    const response = await api.post('/solve', equationData);
    return response.data;
  } catch (error) {
    if (error.response) {
      // Server returned an error
      throw new Error(error.response.data.error || 'Error solving the equation');
    } else if (error.request) {
      // Request was sent but no response received
      throw new Error('Failed to connect to server');
    } else {
      // Error setting up the request
      throw new Error('Error sending request');
    }
  }
};

/**
 * Checks API health status.
 * 
 * @returns {Promise} API status
 */
export const checkHealth = async () => {
  try {
    const response = await api.get('/health');
    return response.data;
  } catch (error) {
    throw new Error('API unavailable');
  }
};

export default api;

