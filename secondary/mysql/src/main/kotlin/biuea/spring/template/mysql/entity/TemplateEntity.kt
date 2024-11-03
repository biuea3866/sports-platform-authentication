package biuea.spring.template.mysql.entity

import biuea.spring.template.domain.Template
import jakarta.persistence.*

@Table(name = "template")
@Entity
class TemplateEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    @Column(
        name = "name",
        nullable = false,
    )
    val name: String
) {
    fun toDomain(): Template {
        return Template(
            id = this.id,
            name = this.name
        )
    }

    companion object {
        fun fromDomain(template: Template): TemplateEntity {
            return TemplateEntity(
                id = template.id,
                name = template.name
            )
        }
    }
}