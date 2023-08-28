package ru.workwear.server.auth.configurations.database;

import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static io.r2dbc.spi.ConnectionFactoryOptions.*;

/********************************************************************************
 *       Вариант настройки через application.properties или application.yml     *
 *       spring.r2dbc.url=r2dbc:h2:file:///./src/main/resources/db/<name_db>    *
 *       spring.r2dbc.username=<USERNAME>                                       *
 *       spring.r2dbc.password=<PASSWORD>                                       *
 ********************************************************************************/

@Configuration
public class DataBaseConfiguration {

    @Value("${db.path}")
    private String path;
    @Value("${db.username}")
    private String username;
    @Value("${db.password}")
    private String password;

    @Bean
    public ConnectionFactory connectionFactory(){
        return ConnectionFactories.get(
                ConnectionFactoryOptions.builder()
                        .option(DRIVER, "h2")
                        .option(PROTOCOL,"file")
                        .option(USER, username)
                        .option(PASSWORD, password)
                        .option(DATABASE, path)
                        .build()
        );
    }
}
