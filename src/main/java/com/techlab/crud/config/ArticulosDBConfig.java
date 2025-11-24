package com.techlab.crud.config;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.context.annotation.*;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.*;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Objects;
import jakarta.persistence.EntityManagerFactory;

@Configuration
@EnableTransactionManagement

@EnableJpaRepositories(
    basePackages ={ "com.techlab.crud.repository.Articulo", "com.techlab.crud.repository.Categoria"},
    entityManagerFactoryRef = "articuloEntityManagerFactory",
    transactionManagerRef = "articuloTransactionManager"
)
@Profile("!test")
public class ArticulosDBConfig {

    @Bean(name = "articuloDataSource")
    public DataSource articuloDataSource(
        @Value("${datasource.articulos.jdbc-url}") String url,
        @Value("${datasource.articulos.username}") String username,
        @Value("${datasource.articulos.password}") String password,
        @Value("${datasource.articulos.driver-class-name}") String driver
    ) {
        HikariConfig cfg = new HikariConfig();
        cfg.setJdbcUrl(url);
        cfg.setUsername(username);
        cfg.setPassword(password);
        cfg.setDriverClassName(driver);
        return new HikariDataSource(cfg);
    }

    @Bean(name = "articuloEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean articuloEntityManagerFactory(
        @Qualifier("articuloDataSource") DataSource articuloDataSource,
        @Value("${spring.jpa.hibernate.ddl-auto:update}") String ddlAuto,
        @Value("${spring.jpa.properties.hibernate.dialect:org.hibernate.dialect.MySQLDialect}") String dialect
    ) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(articuloDataSource);
        //em.setPackagesToScan("com.techlab.crud.model");
        em.setPackagesToScan("com.techlab.crud.model.Articulo", "com.techlab.crud.model.Categoria");
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        em.setPersistenceUnitName("articulosPU");
        em.getJpaPropertyMap().put("hibernate.hbm2ddl.auto", ddlAuto);
        em.getJpaPropertyMap().put("hibernate.dialect", dialect);
        return em;
    }

    @Bean(name = "articuloTransactionManager")
    public PlatformTransactionManager articuloTransactionManager(
        @Qualifier("articuloEntityManagerFactory") LocalContainerEntityManagerFactoryBean articuloEMF
    ) {
        EntityManagerFactory emf = Objects.requireNonNull(articuloEMF.getObject(), "articuloEntityManagerFactory returned null");
        return new JpaTransactionManager(emf);
    }
}