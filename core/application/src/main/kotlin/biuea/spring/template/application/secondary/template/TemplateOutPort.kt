package biuea.spring.template.application.secondary.template

import biuea.spring.template.domain.Template
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface TemplateOutPort {
    fun getTemplate(id: Long): Template
    fun getAllTemplates(ids: Set<Long>): List<Template>
    fun getTemplateMono(id: Long): Mono<Template>
    fun getAllTemplatesFlux(ids: Set<Long>): Flux<Template>
}