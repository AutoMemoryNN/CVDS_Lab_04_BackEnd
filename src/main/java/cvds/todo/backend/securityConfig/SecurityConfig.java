package cvds.todo.backend.securityConfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.List;

/**
 * Configuración de seguridad para la aplicación.
 * Define los permisos de acceso a los recursos y configura la política de CORS.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Configura el filtro de seguridad HTTP para la autenticación y autorización de solicitudes.
     *
     * @param http Objeto HttpSecurity utilizado para configurar los permisos de acceso.
     * @return SecurityFilterChain Cadena de filtros de seguridad configurada.
     * @throws Exception En caso de error al construir la configuración de seguridad.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()  // Desactiva CSRF para simplificar la autenticación
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/tasks", "/tasks/**").permitAll() // Permite acceso sin autenticación a rutas de tareas
                        .requestMatchers("/auth").permitAll() // Permite acceso sin autenticación a la ruta de autenticación
                        .requestMatchers("/users").permitAll() // Permite acceso sin autenticación a la ruta de usuarios
                        .anyRequest().authenticated() // Requiere autenticación para cualquier otra solicitud
                )
                .httpBasic(); // Habilita autenticación básica HTTP

        return http.build();
    }

    /**
     * Configura el filtro de CORS para permitir solicitudes de dominios cruzados.
     *
     * @return CorsFilter Filtro CORS configurado.
     */
    @Bean
    public CorsFilter corsFilter() {
        return new CorsFilter(corsConfigurationSource());
    }

    /**
     * Configura las reglas de CORS, permitiendo acceso desde cualquier origen y especificando métodos HTTP permitidos.
     *
     * @return UrlBasedCorsConfigurationSource Fuente de configuración CORS.
     */
    private UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("*")); // Permite solicitudes desde cualquier origen
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")); // Métodos HTTP permitidos
        config.setAllowedHeaders(List.of("*")); // Permite todos los encabezados
        config.setAllowCredentials(false); // No permite el envío de credenciales en solicitudes de origen cruzado

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config); // Aplica la configuración CORS a todas las rutas
        return source;
    }
}
