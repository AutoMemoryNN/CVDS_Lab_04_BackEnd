package cvds.todo.backend;

import cvds.todo.backend.services.AuthorizationService;
import cvds.todo.backend.services.TaskService;
import cvds.todo.backend.services.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Clase de configuración para la aplicación que gestiona la creación de beans y la configuración CORS.
 * Define los beans para los servicios de tareas, usuarios y autorización, así como para el cifrado de contraseñas.
 */
@Configuration
public class Config implements WebMvcConfigurer {

    /**
     * Configura las reglas CORS para la aplicación, permitiendo acceso a todas las rutas
     * y especificando métodos HTTP permitidos.
     *
     * @param registry El registro CORS utilizado para definir las políticas.
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH")
                .allowedHeaders("*")
                .allowCredentials(false);
    }

    /**
     * Crea y configura el bean de TaskService, que gestiona la lógica de las tareas.
     *
     * @return Una nueva instancia de TaskService.
     */
    @Bean
    public TaskService taskService() {
        return new TaskService();
    }

    /**
     * Crea y configura el bean de UserService, que gestiona la lógica de los usuarios.
     *
     * @return Una nueva instancia de UserService.
     */
    @Bean
    public UserService userService() {
        return new UserService();
    }

    /**
     * Crea y configura el bean de PasswordEncoder para cifrar las contraseñas de los usuarios.
     *
     * @return Una instancia de BCryptPasswordEncoder.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Crea y configura el bean de AuthorizationService, que gestiona la lógica de autorización.
     *
     * @return Una nueva instancia de AuthorizationService.
     */
    @Bean
    public AuthorizationService authorizationService() {
        return new AuthorizationService();
    }
}
