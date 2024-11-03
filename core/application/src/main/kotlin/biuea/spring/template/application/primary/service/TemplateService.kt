package biuea.spring.template.application.primary.service

import biuea.spring.template.application.primary.usecase.TemplateInPort
import biuea.spring.template.application.secondary.template.TemplateOutPort
import biuea.spring.template.domain.Template
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.stereotype.Service

@Service
class TemplateService(
    private val templateOutPort: TemplateOutPort
): TemplateInPort {
    override fun getTemplate(id: Long): Template {
        return templateOutPort.getTemplate(id)
    }

    override suspend fun getTemplateSuspend(id: Long): Template {
        return withContext(Dispatchers.IO) {
            templateOutPort.getTemplateMono(id).block()
        }!!
    }
}