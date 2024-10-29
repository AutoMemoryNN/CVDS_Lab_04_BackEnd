package cvds.todo.backend;


import cvds.todo.backend.services.AuthorizationService;
import cvds.todo.backend.services.TaskService;
import cvds.todo.backend.services.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Clase de configuración que gestiona la creación de beans para el servicio de tareas
 * y su repositorio.
 */
@Configuration
public class Config implements WebMvcConfigurer {

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

    @Bean
    public UserService userService() {
        return new UserService();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthorizationService authorizationService() {
        return new AuthorizationService();
    }
}