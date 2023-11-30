package com.mysite.spring.security.config;

import com.mysite.spring.security.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserService userService;

    @Autowired
    public SecurityConfig(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests() // ко всему приложению пользователи имеют доступ
                .antMatchers("/authenticated/**").authenticated() // но если постучатся в authenticated, должны зайти под своей учеткой
                .antMatchers("/only_for_admins/**").hasRole("ADMIN") // пускать только тех, у кого есть роль ADMIN (ROLE_ADMIN)
                .antMatchers("/read_profile/**").hasAuthority("READ_PROFILE") // (без проверки наличия ROLE_). Role - роль, Authority - право доступа. Оба обычный текст, нет разделения
                .and()
                .formLogin() // предоставление стандартной формы
                .and()
                .logout().logoutSuccessUrl("/"); // после логаута попадут в корень
    }

    // хранение пользователей в памяти (In-Memory)
//    @Bean
//    public UserDetailsService users() {
//
//        // создаем юзера
//        UserDetails user = User.builder() // UserDetails - это минимальная ин-ция о пользователе
//                .username("user")
//                .password("{bcrypt}$2a$12$TP60axqj3ipV4Uo1hz9Nz.D7aAbMBrusiUusitqZD8FLyhKRe8zHe") // 100
//                .roles("USER")
//                .build();
//
//        UserDetails admin = User.builder() // UserDetails - это минимальная ин-ция о пользователе
//                .username("admin")
//                .password("{bcrypt}$2a$12$TP60axqj3ipV4Uo1hz9Nz.D7aAbMBrusiUusitqZD8FLyhKRe8zHe") // 100
//                .roles("ADMIN", "USER")
//                .build();
//
//        // создаем 2 пользователя в базе: никакой БД, ни SpringData
//        return new InMemoryUserDetailsManager(user, admin);
//    }

    // хранение пользователей в БД (jdbcAuthentication): должны быть созданы таблицы users, authorities по специальной схеме
//    @Bean
//    public JdbcUserDetailsManager users(DataSource dataSource) {
//
//        // создаем юзера
//        UserDetails user = User.builder() // UserDetails - это минимальная ин-ция о пользователе
//                .username("user")
//                .password("{bcrypt}$2a$12$TP60axqj3ipV4Uo1hz9Nz.D7aAbMBrusiUusitqZD8FLyhKRe8zHe") // 100
//                .roles("USER")
//                .build();
//
//        UserDetails admin = User.builder() // UserDetails - это минимальная ин-ция о пользователе
//                .username("admin")
//                .password("{bcrypt}$2a$12$TP60axqj3ipV4Uo1hz9Nz.D7aAbMBrusiUusitqZD8FLyhKRe8zHe") // 100
//                .roles("ADMIN", "USER")
//                .build();
//
//        // добавление пользователей в БД, либо можно изначально положить пользователей в БД
//        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);
//        if (jdbcUserDetailsManager.userExists(user.getUsername())) {
//            jdbcUserDetailsManager.deleteUser(user.getUsername());
//        }
//        if (jdbcUserDetailsManager.userExists(admin.getUsername())) {
//            jdbcUserDetailsManager.deleteUser(admin.getUsername());
//        }
//        jdbcUserDetailsManager.createUser(user);
//        jdbcUserDetailsManager.createUser(admin);
//
//
//        return jdbcUserDetailsManager;
//
//        // можно вместо всего выше обойтись только этим, если в БД уже есть пользователи и роли
//        return new JdbcUserDetailsManager(dataSource);
//    }

    // хранение настоящх пользователей в БД со своей структурой таблиц
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        authenticationProvider.setUserDetailsService(userService); // понять, существует пользователь или нет. Предоставляет пользователя по его имени
        return authenticationProvider;
    }

    // для преобразования паролей
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
