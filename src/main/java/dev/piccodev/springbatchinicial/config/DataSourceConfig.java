package dev.piccodev.springbatchinicial.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    @Primary //Indica que esse será o banco de dados padrão. Quando não informarmos um qualificador, utilizaremos esse banco de dados.
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource") //As configurações que estão no "spring.datasource" serão utilizadas para criar esse banco.
    public DataSource springDataSource(){

        return DataSourceBuilder.create().build();
    }

    @Bean
    @ConfigurationProperties(prefix = "app.datasource")
    public DataSource springBatchDataSource(){

        return DataSourceBuilder.create().build();
    }
}
