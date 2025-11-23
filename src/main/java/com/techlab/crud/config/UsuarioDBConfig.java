package com.techlab.crud.config;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.beans.factory.annotation.Value;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.*;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.*;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import java.util.Objects;
import jakarta.persistence.EntityManagerFactory;

@Configuration
@EnableTransactionManagement
// 1. ESCANEO DE REPOSITORIOS: Solo los del módulo Usuario
@EnableJpaRepositories(
        basePackages = {"com.techlab.crud.repository.Usuario", "com.techlab.crud.repository.Roles"}, // Incluir ambos repositorios de usuario/roles
        entityManagerFactoryRef = "usuarioEntityManagerFactory",
        transactionManagerRef = "usuarioTransactionManager"
)
@Profile("!test")
public class UsuarioDBConfig {

    // 2. DATA SOURCE: Define la conexión específica para el módulo de usuario
    @Bean(name = "usuarioDataSource")
    public DataSource usuarioDataSource(
            @Value("${datasource.usuario.jdbc-url}") String url,
            @Value("${datasource.usuario.username}") String username,
            @Value("${datasource.usuario.password}") String password,
            @Value("${datasource.usuario.driver-class-name}") String driver
    ) {
        HikariConfig cfg = new HikariConfig();
        cfg.setJdbcUrl(url);
        cfg.setUsername(username);
        cfg.setPassword(password);
        cfg.setDriverClassName(driver);
        return new HikariDataSource(cfg);
    }

    // 3. ENTITY MANAGER FACTORY: Define cómo Hibernate mapea las entidades
    @Bean(name = "usuarioEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean usuarioEntityManagerFactory(
            @Qualifier("usuarioDataSource") DataSource usuarioDataSource
    ) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(usuarioDataSource);
        
        // 💡 IMPORTANTE: Especifica el paquete de los modelos de USUARIO y ROLES.
        em.setPackagesToScan("com.techlab.crud.model.Usuario", "com.techlab.crud.model.Roles"); 
        
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        em.setPersistenceUnitName("usuarioPU");
        em.getJpaPropertyMap().put("hibernate.hbm2ddl.auto", System.getProperty("spring.jpa.hibernate.ddl-auto", "update"));
        em.getJpaPropertyMap().put("hibernate.dialect", System.getProperty("spring.jpa.properties.hibernate.dialect", "org.hibernate.dialect.MySQLDialect"));
        return em;
    }

    // 4. TRANSACTION MANAGER: Administra las transacciones para este módulo
    @Bean(name = "usuarioTransactionManager")
    public PlatformTransactionManager usuarioTransactionManager(
            @Qualifier("usuarioEntityManagerFactory") LocalContainerEntityManagerFactoryBean usuarioEMF
    ) {
        EntityManagerFactory emf = Objects.requireNonNull(usuarioEMF.getObject(), "usuarioEntityManagerFactory returned null");
        return new JpaTransactionManager(emf);
    }
}