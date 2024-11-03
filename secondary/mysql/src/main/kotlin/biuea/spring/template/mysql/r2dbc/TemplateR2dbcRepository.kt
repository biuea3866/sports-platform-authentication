package biuea.spring.template.mysql.r2dbc

import biuea.spring.template.mysql.entity.TemplateEntity
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface TemplateR2dbcRepository: ReactiveCrudRepository<TemplateEntity, Long> {
    fun findByIdIs(id: Long): Mono<TemplateEntity>
    fun findAllByIdIn(ids: Set<Long>): Flux<TemplateEntity>
}