package com.example.springSecurityWebApp.serurity;

import com.example.springSecurityWebApp.entity.User;
import com.example.springSecurityWebApp.enums.Role;
import com.example.springSecurityWebApp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
      private final UserRepository userRepository;

      @Bean
      public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
      }
      @Bean
      public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
            return authenticationConfiguration.getAuthenticationManager();
      }

      @Bean
      public AuthenticationProvider authenticationProvider(UserDetailServiceImpl userDetailsService) {
            DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
            authenticationProvider.setUserDetailsService(userDetailsService);
            authenticationProvider.setPasswordEncoder(passwordEncoder());
            return authenticationProvider;

      }

      @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
          http.authorizeHttpRequests(aut->aut
                  .requestMatchers("/login").permitAll()
                  .requestMatchers("/registration").permitAll()
                  .requestMatchers(HttpMethod.POST,"/register").permitAll()

                  .requestMatchers("/menu").authenticated()
                  .requestMatchers("/accounts").hasRole("ADMIN")
                  .anyRequest().authenticated()
          );

          http.cors(AbstractHttpConfigurer::disable);
          http.headers(AbstractHttpConfigurer::disable);
          http.csrf(AbstractHttpConfigurer::disable);

          http.httpBasic(Customizer.withDefaults());


          http.formLogin(login -> login
                  .loginPage("/login")
                  .defaultSuccessUrl("/menu")
                  .permitAll()
                  .failureUrl("/login?error")

                  );

          return http.build();
      }

      @Bean
      public CommandLineRunner createAdmin(){
            return args -> {
                User userName = userRepository.findByUsername("Admin").orElse(null) ;
                 if(userName == null) {
                       User admin = new User();
                       admin.setUsername("Admin");
                       admin.setPassword(passwordEncoder().encode("Admin"));
                       admin.setRole(Role.ADMIN);
                       userRepository.save(admin);
                 }
            };
      }
}
