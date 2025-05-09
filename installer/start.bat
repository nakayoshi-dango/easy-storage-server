@echo off
echo.
echo =====================================
echo     Easy Storage - Lanzador
echo =====================================
echo.

:: Verificar que existe application.properties
if not exist application.properties (
    echo ERROR: No se encuentra application.properties.
    echo Por favor ejecuta primero setup.bat
    pause
    exit /b
)

:: Ejecutar la aplicación
echo Iniciando aplicacion...
java -jar easy-storage.jar --spring.config.location=application.properties

pause

