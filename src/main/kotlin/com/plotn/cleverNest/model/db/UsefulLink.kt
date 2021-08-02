package com.plotn.cleverNest.model.db

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import org.springframework.validation.annotation.Validated
import javax.persistence.*

@ApiModel(description = "Useful links")
@Validated
@Entity
@Table(name = "CN_USEFUL_LINKS")
data class UsefulLink (
        @ApiModelProperty(value = "Useful link unique identifier. Sequence-based")
        @JsonProperty("ulId")
        @Id
        @GeneratedValue(generator = "CNEST_USEFUL_LINKS_SEQ")
        @SequenceGenerator(name = "CNEST_USEFUL_LINKS_SEQ", sequenceName = "CNEST_USEFUL_LINKS_SEQ", initialValue = 2000, allocationSize = 1)
        @Column(name = "UL_ID")
        val ulId: Long,

        @ApiModelProperty(value = "Useful link URL. Must be unique within conf")
        @JsonProperty("ulUrl")
        @Column(name = "UL_URL")
        val ulUrl: String,

        @ApiModelProperty(value = "Useful link description")
        @JsonProperty("ulDesc")
        @Column(name = "UL_DESC")
        val ulDesc: String,

        @ApiModelProperty(value = "Useful link link to conf")
        @JsonProperty("ulLinkConf")
        @Column(name = "UL_LINK_CONF")
        val ulLinkConf: Long
)