# Preparación
Antes de configurar el servidor, hay que crear la base de datos y un usuario con permisos para manipular el esquema.

En Linux:
sudo mysql -u root -p

En Windows:
mysql -u root -p

(Enter y poner contraseña)
CREATE USER 'nombre_usuario'@'localhost' IDENTIFIED BY 'contraseña';
CREATE DATABASE easy_storage;
GRANT ALL PRIVILEGES ON easy_storage.* TO 'nombre_usuario'@'localhost';
FLUSH PRIVILEGES;

# Configuración inicial
Al ejecutar el setup, se solicitará un nombre de usuario y contraseña para la base de datos. También se solicitará para el usuario administrador con el que crear otros usuarios.

Para configurar el servidor en Linux:
    1. Ejecutar setup.sh
    2. Ejecutar start.sh
Para configurar el servidor en Windows:
    1. Ejecutar setup.bat
    2. Ejecutar start.bat

Una vez configurado, ejecutar start.bat o start.sh

# Ejecución
Siempre comprobar que el servicio mysql está iniciado y después ejecutar start.bat o start.sh.
Si se desea usar los datos de prueba, hay que establecer el ddl-auto en create: 
spring.jpa.hibernate.ddl-auto=create
