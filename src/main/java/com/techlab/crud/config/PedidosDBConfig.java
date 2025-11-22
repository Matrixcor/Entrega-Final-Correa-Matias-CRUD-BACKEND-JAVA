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
    basePackages = "com.techlab.crud.repository.Pedido",
    entityManagerFactoryRef = "pedidosEntityManagerFactory",
    transactionManagerRef = "pedidosTransactionManager"
)
@Profile("!test")
public class PedidosDBConfig {

    @Bean(name = "pedidosDataSource")
    public DataSource pedidosDataSource(
        @Value("${datasource.pedidos.jdbc-url}") String url,
        @Value("${datasource.pedidos.username}") String username,
        @Value("${datasource.pedidos.password}") String password,
        @Value("${datasource.pedidos.driver-class-name}") String driver
    ) {
        HikariConfig cfg = new HikariConfig();
        cfg.setJdbcUrl(url);
        cfg.setUsername(username);
        cfg.setPassword(password);
        cfg.setDriverClassName(driver);
        return new HikariDataSource(cfg);
    }

    @Bean(name = "pedidosEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean pedidosEntityManagerFactory(
        @Qualifier("pedidosDataSource") DataSource pedidosDataSource
    ) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(pedidosDataSource);
        em.setPackagesToScan("com.techlab.crud.model");
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        em.setPersistenceUnitName("pedidosPU");
        em.getJpaPropertyMap().put("hibernate.hbm2ddl.auto", System.getProperty("spring.jpa.hibernate.ddl-auto", "update"));
        em.getJpaPropertyMap().put("hibernate.dialect", System.getProperty("spring.jpa.properties.hibernate.dialect", "org.hibernate.dialect.MySQLDialect"));
        return em;
    }

    @Bean(name = "pedidosTransactionManager")
    public PlatformTransactionManager pedidosTransactionManager(
        @Qualifier("pedidosEntityManagerFactory") LocalContainerEntityManagerFactoryBean pedidosEMF
    ) {
        EntityManagerFactory emf = Objects.requireNonNull(pedidosEMF.getObject(), "pedidosEntityManagerFactory returned null");
        return new JpaTransactionManager(emf);
    }
}
