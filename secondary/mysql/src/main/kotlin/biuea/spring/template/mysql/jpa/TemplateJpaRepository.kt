package biuea.spring.template.mysql.jpa

import biuea.spring.template.mysql.entity.TemplateEntity
import org.springframework.data.jpa.repository.JpaRepository

interface TemplateJpaRepository: JpaRepository<TemplateEntity, Long> {
    fun findByIdIs(id: Long): TemplateEntity?
    fun findAllByIdIn(ids: Set<Long>): List<TemplateEntity>
}