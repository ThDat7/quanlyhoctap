package com.thanhdat.quanlyhoctap.config;

import com.thanhdat.quanlyhoctap.entity.UserRole;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.spec.SecretKeySpec;

@Configuration
@EnableWebSecurity
public class JWTSecurityConfig {
    @Value("${jwt.signerKey}")
    private String signerKey;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.oauth2ResourceServer(oauth2 ->
                oauth2.jwt(jwtConfigurer ->
                        jwtConfigurer.decoder(jwtDecoder())
                        .jwtAuthenticationConverter(jwtAuthenticationConverter())
                ).authenticationEntryPoint(new JwtAuthenticationEntryPoint()));

        http.authorizeHttpRequests(request ->
                request
                        .requestMatchers("/api/auth/token",
                                "/api/auth/introspect",

                                "/api/education-programs/search",
                                "/api/education-programs/view/*",

                                "/api/news/view",
                                "/api/news/view/*",

//                                development only
                                "/data"
                                )
                        .permitAll()


                        .requestMatchers("/api/course-classes/semester/*/course/*",
                                "/api/course-classes/semester/*/course/*",
                                "/api/course-classes/semester/*/course/*",
                                "/api/course-classes/semester/*/course-with-courseclass-count",
                                "/api/course-classes/*",
                                "/api/course-classes/course/*/semester/*/select-options-available-teacher",
                                "/api/course-classes/course/*/semester/*/select-options-available-student-class",
                                "/api/course-classes",

                                "/api/courses",
                                "/api/courses/*",
                                "/api/courses/semester/*/from-course-class",
                                "/api/courses/types",
                                "/api/courses/select-options",
                                "/api/courses/semester/*/from-course-class",

                                "/api/course-outlines",
                                "/api/course-outlines/*",

                                "/api/education-programs",
                                "/api/education-programs/clone-batching/fromYear/*/toYear/*",
                                "/api/education-programs/*",

                                "/api/exam-schedules/semester/*/course-with-final-exam-schedule-status",
                                "/api/exam-schedules/semester/*/course/*/final-exam",
                                "/api/exam-schedules/semester/*/course-with-final-exam-schedule-status",
                                "/api/exam-schedules/semester/*/course/*/final-exam",
                                "/api/exam-schedules/course-class/*/available-time-final-exam",
                                "/api/exam-schedules/course-class/*/available-room-final-exam",
                                "/api/exam-schedules/course-class/*/final-exam",

                                "/api/faculties",
                                "/api/faculties/*",
                                "/api/faculties/select-options",

                                "/api/majors",
                                "/api/majors/*",
                                "/api/majors/select-options",

                                "/api/news",
                                "/api/news/*",

                                "/api/scores/course-class/*/final-exam",
                                "/api/scores/update/final-exam",
                                "/api/scores/course-class/*/final-exam",
                                "/api/scores/update/final-exam",

                                "/api/semesters",
                                "/api/semesters/select-options",

                                "/api/settings",
                                "/api/settings/*",

                                "/api/staffs",
                                "/api/staffs/select-options",

                                "/api/timetables/semester/*/student-classes-with-status",
                                "/api/timetables/semester/*/student-class/*/course-classes-with-status",
                                "/api/timetables/semester/*/student-class/*/schedules",
                                "/api/timetables/course-class/*",
                                "/api/timetables/course-class/*/available-classrooms",
                                "/api/timetables/schedule-study/*")
                        .hasAuthority(UserRole.ADMIN.name())


                        .requestMatchers("/api/course-classes/semester/*/current-teacher-teaching",

                                "/api/course-outlines/current-teacher",
                                "/api/course-outlines/*/current-teacher",

                                "/api/exam-schedules/semester/*/current-teacher",
                                "/api/exam-schedules/course-class/*/available-date-midterm-exam",
                                "/api/exam-schedules/course-class/*/midterm-exam/current-teacher",

                                "/api/scores/course-class/*/current-teacher",
                                "/api/scores/update/current-teacher",

                                "/api/semesters/none-locked",
                                "/api/semesters/current-teacher",

                                "/api/timetables/semester/*/current-teacher")
                        .hasAuthority(UserRole.TEACHER.name())


                        .requestMatchers("/api/course-registers/by-current-education-program",
                                "/api/course-registers/register-course/*",
                                "/api/course-registers/unregister-course/*",

                                "/api/exam-schedules/semester/*/current-student",

                                "/api/invoices/semester/*/current-student",

                                "/api/semesters/current-student",

                                "/api/student-status/current-student",

                                "/api/study-results/current-student",

                                "/api/timetables/semester/*/current-student")
                        .hasAuthority(UserRole.STUDENT.name())

        );
        http.csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }

    @Bean
    public PasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    JwtDecoder jwtDecoder(){
        SecretKeySpec secretKeySpec = new SecretKeySpec(signerKey.getBytes(), MacAlgorithm.HS512.name());
        return NimbusJwtDecoder
                .withSecretKey(secretKeySpec)
                .macAlgorithm(MacAlgorithm.HS512)
                .build();
    }

    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter(){
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);

        return jwtAuthenticationConverter;
    }

}
