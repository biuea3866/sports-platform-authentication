package biuea.spring.template.application.primary.usecase

import biuea.spring.template.domain.Template

interface TemplateInPort {
    fun getTemplate(id: Long): Template
    suspend fun getTemplateSuspend(id: Long): Template
}