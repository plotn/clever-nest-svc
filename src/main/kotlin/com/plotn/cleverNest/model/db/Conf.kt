package com.plotn.cleverNest.model.db

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.validation.annotation.Validated
import javax.persistence.*

@Schema(description = "Configurations")
@Validated
@Entity
@Table(name = "CN_CONF")
data class Conf (
    @Schema(description = "Conf unique identifier. Sequence-based. -1 for 'common' conf")
    @JsonProperty("cId")
    @Id
    @GeneratedValue(generator = "CNEST_CONF_SEQ")
    @SequenceGenerator(name = "CNEST_CONF_SEQ", sequenceName = "CNEST_CONF_SEQ", initialValue = 2000, allocationSize = 1)
    @Column(name = "C_ID")
    val cId: Long,

    @Schema(description = "Conf name. Must be unique")
    @JsonProperty("cName")
    @Column(name = "C_NAME")
    val cName: String,

    @Schema(description = "Conf description")
    @JsonProperty("cDesc")
    @Column(name = "C_DESC")
    val cDesc: String? = null,

    @Schema(description = "Check hrefs")
    @JsonProperty("cCheckHrefs")
    @Column(name = "C_CHECK_HREFS")
    val cCheckHrefs: String? = null,

    @Schema(description = "Conf is default - for device refreshing")
    @JsonProperty("cDefault")
    @Column(name = "C_DEFAULT")
    val cDefault: Int

)
