package biuea.spring.template.mysql

import biuea.spring.template.application.secondary.template.TemplateOutPort
import biuea.spring.template.domain.Template
import biuea.spring.template.mysql.jpa.TemplateJpaRepository
import biuea.spring.template.mysql.r2dbc.TemplateR2dbcRepository
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Component
class TemplateRepository(
    private val templateR2dbcRepository: TemplateR2dbcRepository,
    private val templateJpaRepository: TemplateJpaRepository
): TemplateOutPort {
    override fun getTemplate(id: Long): Template {
        return this.templateJpaRepository.findByIdIs(id)?.toDomain()
            ?: throw IllegalArgumentException("Template not found")
    }

    override fun getAllTemplates(ids: Set<Long>): List<Template> {
        return this.templateJpaRepository.findAllByIdIn(ids).map { it.toDomain() }
    }

    override fun getTemplateMono(id: Long): Mono<Template> {
        return this.templateR2dbcRepository.findByIdIs(id).map { it.toDomain() }
    }

    override fun getAllTemplatesFlux(ids: Set<Long>): Flux<Template> {
        return this.templateR2dbcRepository.findAllByIdIn(ids).map { it.toDomain() }
    }
}