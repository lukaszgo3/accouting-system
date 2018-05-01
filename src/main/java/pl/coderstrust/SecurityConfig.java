package pl.coderstrust;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class SecurityConfig {

  @Bean
  public UserDetailsService userDetailsService() throws Exception {
    InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
    manager.createUser(
        User.withUsername("admin").password("admin").roles("USER").build());
    return manager;
  }

}
