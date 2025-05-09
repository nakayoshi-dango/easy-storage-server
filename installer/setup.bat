@echo off
setlocal enabledelayedexpansion

echo.
echo =====================================
echo     Easy Storage - Instalador
echo =====================================
echo.

:: PEDIR DATOS DEL USUARIO DE BASE DE DATOS
set /p DB_USER= Usuario de la base de datos: 
set /p DB_PASS= Contraseña del usuario de la base de datos: 

:: PEDIR DATOS DEL USUARIO ADMINISTRADOR
set /p ADMIN_USER= Usuario administrador inicial: 
set /p ADMIN_PASS= Contraseña del administrador: 

:: CREAR ARCHIVO application.properties
(
    echo spring.datasource.username=!DB_USER!
    echo spring.datasource.password=!DB_PASS!
    echo app.admin.username=!ADMIN_USER!
    echo app.admin.password=!ADMIN_PASS!
    echo spring.application.name=easy-storage
    echo spring.datasource.url=jdbc:mysql://localhost:3306/easy_storage?useSSL=false&serverTimezone=UTC
    echo spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
    echo spring.jpa.show-sql=true
    echo spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect
    echo spring.jpa.hibernate.ddl-auto=update
) > application.properties

:: MENSAJE
echo.
echo  Archivo application.properties generado con exito.
echo  Lanzando aplicacion para crear el usuario administrador...

:: EJECUTAR EL JAR CON LA CONFIG GENERADA
java -jar easy-storage.jar --spring.config.location=application.properties

echo.
pause

