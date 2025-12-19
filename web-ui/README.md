# Web UI for PDE Solver

Web application for solving partial differential equations.

## Structure

- `backend/` - Spring Boot REST API
- `frontend/` - React application

## Running Backend

```bash
# From the project root directory
./gradlew :web-ui:backend:bootRun
```

Backend will be available at `http://localhost:8080`

## Running Frontend

```bash
# Navigate to frontend directory
cd web-ui/frontend

# Install dependencies (first time)
npm install

# Start dev server
npm run dev
```

Frontend will be available at `http://localhost:3000`

## API Endpoints

- `POST /api/solve` - Solve equation
- `GET /api/health` - Health check

## Usage

1. Start the backend server
2. Start the frontend server
3. Open browser at `http://localhost:3000`
4. Fill in the equation parameters form
5. Click "Solve Equation"
6. Results will be displayed on the chart

