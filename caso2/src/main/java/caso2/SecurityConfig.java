package caso2;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                // Recursos públicos
                .requestMatchers("/css/**", "/js/**", "/images/**", "/webjars/**").permitAll()
                .requestMatchers("/", "/Home", "/login").permitAll()
                
                // Rutas de administración solo para ADMIN
                .requestMatchers("/usuarios/nuevo", "/usuarios/modificar/**", "/usuarios/guardar", "/usuarios/eliminar").hasRole("ADMIN")
                .requestMatchers("/roles/**").hasRole("ADMIN")
                
                // Rutas de visualización para ADMIN y PROFESOR
                .requestMatchers("/usuarios").hasAnyRole("ADMIN", "PROFESOR")
                .requestMatchers("/reportes/**").hasAnyRole("ADMIN", "PROFESOR")
                .requestMatchers("/consultas/**").hasAnyRole("ADMIN", "PROFESOR")
                .requestMatchers("/usuarios/perfil").hasAnyRole("ADMIN", "PROFESOR", "ESTUDIANTE")
                .requestMatchers("/acceso-denegado").permitAll()
                .requestMatchers("/dashboard").authenticated()
                
                // Cualquier otra ruta requiere autenticación
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/dashboard", true)
                .failureUrl("/login?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/Home?logout")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            )
            .exceptionHandling(ex -> ex
                .accessDeniedPage("/acceso-denegado")
            );

        return http.build();
    }

    @Bean
    @SuppressWarnings("deprecation")
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}