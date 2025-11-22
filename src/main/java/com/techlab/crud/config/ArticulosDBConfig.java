package com.techlab.crud.config;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.*;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.*;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@EnableTransactionManagement

@EnableJpaRepositories(
    basePackages = "com.techlab.crud.repository.Articulo",
    entityManagerFactoryRef = "articuloEntityManagerFactory",
    transactionManagerRef = "articuloTransactionManager"
)
@Profile("!test")
public class ArticulosDBConfig {

    @Bean(name = "articuloDataSource")
    @ConfigurationProperties(prefix = "datasource.articulos")
    public DataSource articuloDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "articuloEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean articuloEntityManagerFactory(
        @Qualifier("articuloDataSource") DataSource articuloDataSource
    ) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(articuloDataSource);
        em.setPackagesToScan("com.techlab.crud.model");
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        em.setPersistenceUnitName("articulosPU");
        return em;
    }

    @Bean(name = "articuloTransactionManager")
    public PlatformTransactionManager articuloTransactionManager(
        @Qualifier("articuloEntityManagerFactory") LocalContainerEntityManagerFactoryBean articuloEMF
    ) {
        return new JpaTransactionManager(articuloEMF.getObject());
    }
}