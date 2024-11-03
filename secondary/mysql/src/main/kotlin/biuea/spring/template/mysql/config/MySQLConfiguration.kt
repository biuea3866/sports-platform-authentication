package biuea.spring.template.mysql.config

import com.zaxxer.hikari.HikariDataSource
import jakarta.persistence.EntityManagerFactory
import org.hibernate.boot.model.naming.PhysicalNamingStrategy
import org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl
import org.springframework.beans.factory.ObjectProvider
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.autoconfigure.transaction.TransactionManagerCustomizers
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.DependsOn
import org.springframework.context.annotation.Primary
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.annotation.EnableTransactionManagement
import javax.sql.DataSource

@Configuration
@EnableJpaRepositories(
    entityManagerFactoryRef = "entityManagerFactory",
    basePackages = ["biuea.spring.template.mysql.jpa"],
)
@EnableTransactionManagement
class MySQLConfiguration {
    /**
     * write replica 접근을 위한 DataSource
     */
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.primary")
    fun writeDateSource(): DataSource = DataSourceBuilder.create().type(HikariDataSource::class.java).build()

    /**
     * read replica 접근을 위한 DataSource
     */
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.secondary")
    fun readDataSource(): DataSource = DataSourceBuilder.create().type(HikariDataSource::class.java).build()

    @Bean
    @DependsOn(value = ["writeDateSource", "readDataSource"])
    fun routingDataSource(): DataSource {
        return MySQLRoutingDataSource().apply {
            val dataSourceMap = mapOf<Any, Any>(
                MySQLRoutingKey.WRITE to writeDateSource(),
                MySQLRoutingKey.READ to readDataSource()
            )
            this.setTargetDataSources(dataSourceMap)
            this.setDefaultTargetDataSource(writeDateSource())
        }
    }

    /**
     * 실제 커넥션이 필요한 경우가 아니라면 데이터베이스 풀에서 커넥션을 점유하지 않고, 필요한 시점에 커넥션을 점유한다.
     */
    @Bean
    @Primary
    @DependsOn(value = ["routingDataSource"])
    fun dataSource(): DataSource = LazyConnectionDataSourceProxy(routingDataSource())

    /**
     * entity를 관리하는 EntityManager를 생성하는 factory
     */
//    @Bean(value = ["entityManagerFactory"])
//    @DependsOn(value = ["dataSource"])
//    fun entityManagerFactory(
//        entityManagerFactoryBuilder: EntityManagerFactoryBuilder,
//        dataSource: DataSource
//    ): LocalContainerEntityManagerFactoryBean = entityManagerFactoryBuilder
//        .dataSource(dataSource)
//        .properties(jpaProperties())
//        .packages("biuea.spring.template.mysql.jpa")
//        .persistenceUnit("legacy")
//        .build()

    @Bean
    @ConditionalOnBean(EntityManagerFactory::class)
    fun transactionManager(
        entityManagerFactory: EntityManagerFactory,
        transactionManagerCustomizers: ObjectProvider<TransactionManagerCustomizers>
    ): PlatformTransactionManager = JpaTransactionManager(entityManagerFactory).apply {
        transactionManagerCustomizers.ifAvailable { it.customize(this) }
    }
}