package cvds.todo.backend.exceptions;

/**
 * TaskException es una clase de excepción personalizada que extiende AppException.
 * Sirve como clase base para todas las excepciones relacionadas con tareas, permitiendo
 * un manejo específico de errores en situaciones como "no encontrado", valores inválidos o conflictos.
 */
public class TaskException extends AppException {

    /**
     * Constructor de TaskException.
     *
     * @param message    El mensaje de error.
     * @param statusCode El código de estado HTTP asociado al error.
     */
    public TaskException(String message, Integer statusCode) {
        super(message, statusCode);
    }

    /**
     * TaskNotFoundException se lanza cuando no se puede encontrar una tarea en la base de datos.
     */
    public static class TaskNotFoundException extends TaskException {

        /**
         * Constructor de TaskNotFoundException.
         *
         * @param task La tarea que no se encontró.
         */
        public TaskNotFoundException(String task) {
            super("Task: " + task + ", not found in the database.", 404);
        }
    }

    /**
     * TaskInvalidValueException se lanza cuando se encuentra un valor inválido en una tarea.
     */
    public static class TaskInvalidValueException extends TaskException {

        /**
         * Constructor de TaskInvalidValueException.
         *
         * @param value El valor inválido encontrado.
         */
        public TaskInvalidValueException(String value) {
            super("Invalid value for: " + value, 400);
        }
    }

    /**
     * TaskConflictException se lanza cuando hay un conflicto, como una tarea duplicada.
     */
    public static class TaskConflictException extends TaskException {

        /**
         * Constructor de TaskConflictException.
         *
         * @param task La tarea que causó el conflicto.
         */
        public TaskConflictException(String task) {
            super("Task: " + task + ", already exists in the database.", 409);
        }
    }
}
