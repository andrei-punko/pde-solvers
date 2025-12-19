@echo off
rem Navigate to frontend directory
cd web-ui\frontend

rem Install dependencies (first time only)
if not exist node_modules (
    echo Installing dependencies...
    call npm install
)

rem Start dev server
echo Starting frontend dev server...
call npm run dev
