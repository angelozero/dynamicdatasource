package com.angelozero.dynamicdatasource.config;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "writerEntityManagerFactory",
        transactionManagerRef = "writerTransactionManager",
        basePackages = {"com.angelozero.dynamicdatasource.repository.writer"}
)
public class WriterDataSourceConfig {

    @Bean(name = "writerDataSource")
    @ConfigurationProperties("spring.datasource.writer")
    public DataSource writerDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "writerEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder builder,
                                                                       @Qualifier("writerDataSource") DataSource dataSource) {
        return builder
                .dataSource(dataSource)
                .packages("com.angelozero.dynamicdatasource")
                .persistenceUnit("second")
                .build();
    }

    @Bean(name = "writerTransactionManager")
    public PlatformTransactionManager transactionManager(@Qualifier("writerEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

}
