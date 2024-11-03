package biuea.spring.template.mysql.config

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource
import org.springframework.transaction.support.TransactionSynchronizationManager

/**
 * MySQLRoutingDataSource
 * write, read 라우팅을 위한 DataSource
 */
class MySQLRoutingDataSource: AbstractRoutingDataSource() {
    override fun determineCurrentLookupKey(): Any {
        return when (TransactionSynchronizationManager.isCurrentTransactionReadOnly()) {
            true -> MySQLRoutingKey.READ
            false -> MySQLRoutingKey.WRITE
        }
    }
}