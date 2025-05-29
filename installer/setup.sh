#!/bin/bash

echo "Configurando el backend de Easy Storage"

read -p "Usuario de la base de datos: " db_user
read -s -p "Contraseña de la base de datos: " db_pass
echo
read -p "Nombre de usuario administrador inicial: " admin_user
read -s -p "Contraseña del administrador inicial: " admin_pass
echo

echo "Generando archivo application.properties..."

# Sustituye los valores en la plantilla
sed "s/{{DB_USER}}/$db_user/g; s/{{DB_PASS}}/$db_pass/g; s/{{ADMIN_USER}}/$admin_user/g; s/{{ADMIN_PASS}}/$admin_pass/g" \
    application.properties.template > application.properties

echo "Archivo application.properties generado."

echo "Iniciando aplicación..."
java -jar easy-storage.jar --spring.config.location=application.properties

