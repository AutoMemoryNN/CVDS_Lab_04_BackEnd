# Laboratorio 4 - BackEnd

**Juan Pablo Camargo**  
**Tomás Panqueva**  
**Sebastian Buitrago**

## API REST para Gestión de Tareas

Este proyecto consiste en una API REST para gestionar tareas, con funcionalidades de autenticación y control de acceso mediante usuarios y roles. Se utilizan los servicios `TaskService`, `UserService`, `SessionService` y `AuthorizationService` para implementar las diferentes operaciones.

**Desplegado en:** [todo-d2bccdgyhqbchwcb.canadacentral-01.azurewebsites.net/tasks/health](https://todo-d2bccdgyhqbchwcb.canadacentral-01.azurewebsites.net/tasks/health)

## Endpoints

### Endpoints para Usuarios

1. **Crear un nuevo usuario**
    - **Método:** POST
    - **URL:** `/users`
    - **Descripción:** Permite la creación de un usuario común.
    - **Lógica:** Llama a `createUserAsUser(user)` en el servicio `UserService` para registrar un nuevo usuario.
    - **Respuesta:** Retorna un mensaje de confirmación con el nombre de usuario creado.

2. **Crear un nuevo usuario como administrador**
    - **Método:** POST
    - **URL:** `/users/admin`
    - **Descripción:** Permite la creación de un usuario con roles de administrador.
    - **Lógica:** Llama a `createUserAsAdmin(user, roles)` en el servicio `UserService` para registrar un nuevo usuario con permisos de administrador.
    - **Autorización:** Requiere un token de autenticación con privilegios de administrador.
    - **Respuesta:** Retorna un mensaje de confirmación con el nombre de usuario creado.

### Endpoints para Tareas

1. **Obtener todas las tareas**
    - **Método:** GET
    - **URL:** `/tasks`
    - **Descripción:** Obtiene la lista de todas las tareas asociadas al usuario autenticado.
    - **Autorización:** Requiere un token de sesión válido.
    - **Respuesta:** Lista de tareas del usuario autenticado.

2. **Obtener una tarea por ID**
    - **Método:** GET
    - **URL:** `/tasks/{id}`
    - **Descripción:** Obtiene una tarea específica por su ID.
    - **Autorización:** Requiere un token de sesión válido.
    - **Respuesta:** La tarea correspondiente al ID proporcionado.

3. **Crear una nueva tarea**
    - **Método:** POST
    - **URL:** `/tasks`
    - **Descripción:** Crea una nueva tarea para el usuario autenticado.
    - **Autorización:** Requiere un token de sesión válido.
    - **Respuesta:** La tarea creada con el estado `201 Created`.

4. **Actualizar una tarea existente**
    - **Método:** PATCH
    - **URL:** `/tasks/{id}`
    - **Descripción:** Actualiza los detalles de una tarea específica por su ID.
    - **Autorización:** Requiere un token de sesión válido.
    - **Respuesta:** La tarea actualizada.

5. **Eliminar una tarea por ID**
    - **Método:** DELETE
    - **URL:** `/tasks/{id}`
    - **Descripción:** Elimina una tarea específica por su ID.
    - **Autorización:** Requiere un token de sesión válido.
    - **Respuesta:** La tarea eliminada o un mensaje de error si no existe.

6. **Eliminar todas las tareas**
    - **Método:** DELETE
    - **URL:** `/tasks/all`
    - **Descripción:** Elimina todas las tareas asociadas al usuario autenticado.
    - **Autorización:** Requiere un token de sesión válido.
    - **Respuesta:** Mensaje indicando la cantidad de tareas eliminadas.

7. **Generar tareas de ejemplo**
    - **Método:** POST
    - **URL:** `/tasks/gen`
    - **Descripción:** Genera un conjunto de tareas de ejemplo para el usuario autenticado.
    - **Autorización:** Requiere un token de sesión válido y permisos de administrador.
    - **Respuesta:** Mensaje indicando la cantidad de tareas generadas.

8. **Verificar el estado del servicio**
    - **Método:** GET
    - **URL:** `/tasks/health`
    - **Descripción:** Verifica que el servicio está en funcionamiento.
    - **Respuesta:** Objeto con el estado del servidor.

## Servicios

### TaskService
Este servicio implementa la lógica de negocio para la gestión de tareas y se encarga de realizar operaciones CRUD en el repositorio de tareas.

- `getAllTasks(user)`: Obtiene todas las tareas del usuario.
- `getTaskById(id, user)`: Obtiene una tarea específica por su ID, validando que pertenezca al usuario autenticado.
- `createTask(task, user)`: Crea una nueva tarea para el usuario.
- `updateTask(id, task, user)`: Actualiza una tarea existente si pertenece al usuario.
- `deleteTask(id, user)`: Elimina una tarea específica por su ID.
- `deleteAllTasks(user)`: Elimina todas las tareas del usuario.
- `generateExamples(user)`: Genera tareas de ejemplo para el usuario autenticado.

### UserService
El servicio `UserService` maneja la lógica de creación y autenticación de usuarios.

- `createUserAsUser(user)`: Crea un usuario sin roles de administrador.
- `createUserAsAdmin(user, roles)`: Crea un usuario con roles de administrador, validando los privilegios de quien realiza la solicitud.

### SessionService
Gestiona la autenticación de usuarios y las sesiones activas.

- `isSessionActive(token)`: Verifica si el token de sesión proporcionado es válido y está activo.
- `getUserFromSession(token)`: Obtiene el usuario asociado al token de sesión.

### AuthorizationService
Maneja la autorización de usuarios para acceder a recursos específicos en función de sus roles.

- `adminResource(token)`: Verifica que el usuario autenticado tenga privilegios de administrador antes de acceder a ciertos recursos.
