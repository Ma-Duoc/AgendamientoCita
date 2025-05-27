# Microservicio de Agendamiento de Citas

## Descripción

Este microservicio está desarrollado con Spring Boot y Maven para gestionar la creación, actualización, consulta y eliminación de agendamientos de citas médicas. Permite manejar la asociación entre pacientes, médicos y horarios disponibles, asegurando que un horario no pueda ser asignado a más de una cita.

El microservicio sigue una arquitectura en capas: Model, Repository, Service y Controller, exponiendo APIs REST para las operaciones sobre agendamientos.

## Tecnologías usadas

- Java 17  
- Spring Boot  
- Oracle Database  
- Maven  
- Lombok  
- Postman (para pruebas de endpoints)  

## Dependencias incluidas (`pom.xml`)

- `spring-boot-starter-web`  
- `spring-boot-starter-data-jpa`  
- `lombok`  
- `ojdbc8` (Oracle JDBC Driver)  
- `oraclepki`, `osdt_core`, `osdt_cert` (Oracle Wallet Security)  

## Estructura del proyecto

**Modelos:**  
Define la entidad principal `AgendamientoCita` que incluye las relaciones con `Cliente` (Paciente), `Medico` y `HorarioMedico`. El modelo está diseñado para evitar que un horario sea asignado a más de una cita mediante restricciones en la base de datos y en la lógica de negocio.

**Repository:**  
Extiende `JpaRepository` y provee métodos para buscar agendamientos por cliente, médico o horario, además de las operaciones CRUD estándar.

**Service:**  
Contiene la lógica de negocio, validando la disponibilidad del horario, existencia de entidades asociadas y evitando duplicados en la creación o actualización de citas.

**Controller:**  
Expone los endpoints REST para operaciones de listar, crear (individual y múltiple), actualizar, eliminar y buscar agendamientos. Implementa validaciones para asegurar que los campos obligatorios estén completos y que no haya conflictos en los horarios asignados. Responde con códigos HTTP adecuados según la situación (ejemplo: 201, 400, 404).

## Funcionalidades principales

- Crear, actualizar, eliminar y listar agendamientos de citas médicas.
- Validar que un horario no sea asignado a más de una cita.
- Permitir la creación de múltiples agendamientos en una sola solicitud.
- Búsqueda de agendamientos por ID, cliente, médico o horario.
- Validaciones para garantizar integridad y evitar conflictos en los datos.

## Configuración importante

- Configuración en `application.properties` para conexión a base de datos Oracle mediante Wallet.
- Hibernate configurado con `spring.jpa.hibernate.ddl-auto=update` para mantener el esquema sincronizado con los modelos.
- Restricción de unicidad (`UNIQUE`) en la columna `id_horario` de la tabla `agendamiento_cita` para evitar horarios duplicados asignados a distintas citas.
