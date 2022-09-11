package com.plotn.cleverNest.model.db

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.validation.annotation.Validated
import javax.persistence.*

@Schema(description = "Configuration aliases")
@Validated
@Entity
@Table(name = "CN_CONF_ALIAS")
data class ConfAlias (
        @Schema(description = "Conf alias unique identifier. Sequence-based")
        @JsonProperty("aId")
        @Id
        @GeneratedValue(generator = "CNEST_CONF_ALIASES_SEQ")
        @SequenceGenerator(name = "CNEST_CONF_ALIASES_SEQ", sequenceName = "CNEST_CONF_ALIASES_SEQ", initialValue = 2000, allocationSize = 1)
        @Column(name = "A_ID")
        val aId: Long,

        @Schema(description = "Conf alias name. Must be unique within conf")
        @JsonProperty("aName")
        @Column(name = "A_NAME")
        val aName: String,

        @Schema(description = "Conf alias value")
        @JsonProperty("aValue")
        @Column(name = "A_VALUE")
        val aValue: String,

        @Schema(description = "Alias link to conf. -1 equals to 'every'")
        @JsonProperty("aLinkConf")
        @Column(name = "A_LINK_CONF")
        val aLinkConf: Long
)