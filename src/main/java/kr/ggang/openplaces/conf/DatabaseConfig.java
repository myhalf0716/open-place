package kr.ggang.openplaces.conf;

import java.sql.SQLException;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariDataSource;

import org.h2.tools.Server;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableJpaRepositories(basePackages= {"kr.ggang.openplaces.dao"})
@EnableTransactionManagement
public class DatabaseConfig {
    @Value("com.dxvk.dxvkta.repository.password")
    String tcpPassword;

    @Bean
    @ConfigurationProperties("spring.datasource.hikari")
    public DataSource dataSource() throws SQLException {
        
        Server server = runPersistanceServer(8083, tcpPassword, "open-places", "~/dev/h2/data/open-places");
        if(server.isRunning(true)){
            log.info("server run success");
        }
        log.info("h2 server url = {}", server.getURL());

        return DataSourceBuilder.create()
        .type(HikariDataSource.class)
        .build();
    }

    private Server runPersistanceServer(int port, String tcpPassword, String externalDbName, String dbname) throws SQLException {
        return Server.createTcpServer(
                "-tcp",
                "-tcpAllowOthers",
                "-ifNotExists",
                //"-tcpPassword", tcpPassword,
                "-tcpPort", port+"", "-key", externalDbName, dbname).start();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() throws SQLException {

      HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
      vendorAdapter.setGenerateDdl(false);

      LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
      factory.setJpaVendorAdapter(vendorAdapter);
      factory.setPackagesToScan("kr.ggang.openplaces.dao.entity");
      factory.setDataSource(dataSource());
      return factory;
    }

    @Bean
    public PlatformTransactionManager transactionManager(@Qualifier("entityManagerFactory") final EntityManagerFactory entityManagerFactory) {

      JpaTransactionManager txManager = new JpaTransactionManager(entityManagerFactory);
//      txManager.setEntityManagerFactory(entityManagerFactory());
      return txManager;
    }
}
