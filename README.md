# Laboratorio 4 - BackEnd

- **Juan Pablo Camargo**
- **Tomás Panqueva**
- **Sebastian Buitrago**

# API REST para Gestión de Tareas

Este controlador maneja la lógica de la API REST para gestionar tareas utilizando el servicio `TaskService`.

Desplegado en: todo-d2bccdgyhqbchwcb.canadacentral-01.azurewebsites.net/tasks/health

## Endpoints

### 1. Obtener todas las tareas
- **Método**: `GET`
- **URL**: `/tasks`
- **Descripción**: Obtiene la lista de todas las tareas.
- **Lógica**: Llama a `getAllTasks()` en el servicio para obtener todas las tareas.
- **Respuesta**: Retorna la lista de tareas. Si hay algún error, responde con un mensaje o código de error adecuado.

### 2. Obtener una tarea por ID
- **Método**: `GET`
- **URL**: `/tasks/{id}`
- **Descripción**: Obtiene una tarea específica por su ID.
- **Lógica**: Llama a `getTaskById(id)` en el servicio para obtener una tarea por su identificador.
- **Respuesta**: Retorna la tarea correspondiente al ID proporcionado.

### 3. Crear una nueva tarea
- **Método**: `POST`
- **URL**: `/tasks`
- **Descripción**: Crea una nueva tarea.
- **Lógica**: Llama a `createTask(task)` en el servicio para crear una nueva tarea.
- **Respuesta**: Retorna la tarea creada con el estado `201 Created`.

### 4. Actualizar una tarea existente
- **Método**: `PATCH`
- **URL**: `/tasks/{id}`
- **Descripción**: Actualiza una tarea existente por su ID.
- **Lógica**: Llama a `updateTask(id, task)` para actualizar los datos de una tarea.
- **Respuesta**: Retorna la tarea actualizada.

### 5. Eliminar una tarea
- **Método**: `DELETE`
- **URL**: `/tasks/{id}`
- **Descripción**: Elimina una tarea por su ID.
- **Lógica**: Llama a `deleteTask(id)` en el servicio para eliminar una tarea.
- **Respuesta**: Retorna la tarea eliminada o un mensaje de error si no existe.

### 6. Eliminar todas las tareas
- **Método**: `DELETE`
- **URL**: `/tasks/all`
- **Descripción**: Elimina todas las tareas de la colección.
- **Lógica**: Llama a `deleteAllTasks()` en el servicio para eliminar todas las tareas.
- **Respuesta**: Retorna un mensaje indicando cuántas tareas fueron eliminadas exitosamente.

### 7. Generar tareas de ejemplo
- **Método**: `POST`
- **URL**: `/tasks/gen`
- **Descripción**: Genera un conjunto de tareas de ejemplo.
- **Lógica**: Llama a `generateExamples()` en el servicio para crear tareas de ejemplo.
- **Respuesta**: Retorna un mensaje indicando cuántas tareas fueron generadas exitosamente.

### 8. Verificar el estado del servicio
- **Método**: `GET`
- **URL**: `/tasks/health`
- **Descripción**: Verifica el estado del servicio.
- **Lógica**: Devuelve un mensaje que indica que el servidor está operativo.
- **Respuesta**: Retorna un objeto con el estado del servidor.

## Servicio `TaskService`

El servicio `TaskService` implementa la lógica de negocio y se encarga de gestionar las operaciones de CRUD comunicándose con el repositorio de tareas.

- **getAllTasks()**: Obtiene todas las tareas.
- **getTaskById(id)**: Obtiene una tarea por su ID.
- **createTask(task)**: Crea una nueva tarea.
- **updateTask(id, task)**: Actualiza una tarea existente.
- **deleteTask(id)**: Elimina una tarea por su ID.
- **deleteAllTasks()**: Elimina todas las tareas.
- **generateExamples()**: Genera tareas de ejemplo.
