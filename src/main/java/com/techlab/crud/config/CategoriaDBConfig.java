package com.techlab.crud.config;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
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
        basePackages = "com.techlab.crud.repository.Categoria",
        entityManagerFactoryRef = "categoriaEntityManagerFactory",
        transactionManagerRef = "categoriaTransactionManager"
)
@Profile("!test")
public class CategoriaDBConfig {

    @Bean(name = "categoriaDataSource")
    @ConfigurationProperties(prefix = "datasource.categoria")
    public DataSource categoriaDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "categoriaEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean categoriaEntityManagerFactory(
            @Qualifier("categoriaDataSource") DataSource categoriaDataSource
    ) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(categoriaDataSource);
        em.setPackagesToScan("com.techlab.crud.model");
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        em.setPersistenceUnitName("categoriaPU");
        return em;
    }

    @Bean(name = "categoriaTransactionManager")
    public PlatformTransactionManager categoriaTransactionManager(
            @Qualifier("categoriaEntityManagerFactory") LocalContainerEntityManagerFactoryBean categoriaEMF
    ) {
        return new JpaTransactionManager(categoriaEMF.getObject());
    }
}