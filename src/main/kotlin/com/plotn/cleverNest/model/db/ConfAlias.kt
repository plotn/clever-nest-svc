package com.plotn.cleverNest.model.db

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import org.springframework.validation.annotation.Validated
import javax.persistence.*

@ApiModel(description = "Configuration aliases")
@Validated
@Entity
@Table(name = "CN_CONF_ALIAS")
data class ConfAlias (
        @ApiModelProperty(value = "Conf alias unique identifier. Sequence-based")
        @JsonProperty("aId")
        @Id
        @GeneratedValue(generator = "CNEST_CONF_ALIASES_SEQ")
        @SequenceGenerator(name = "CNEST_CONF_ALIASES_SEQ", sequenceName = "CNEST_CONF_ALIASES_SEQ", initialValue = 2000, allocationSize = 1)
        @Column(name = "A_ID")
        val aId: Long,

        @ApiModelProperty(value = "Conf alias name. Must be unique within conf")
        @JsonProperty("aName")
        @Column(name = "A_NAME")
        val aName: String,

        @ApiModelProperty(value = "Conf alias value")
        @JsonProperty("aValue")
        @Column(name = "A_VALUE")
        val aValue: String,

        @ApiModelProperty(value = "Alias link to conf. -1 equals to 'every'")
        @JsonProperty("aLinkConf")
        @Column(name = "A_LINK_CONF")
        val aLinkConf: Long
)