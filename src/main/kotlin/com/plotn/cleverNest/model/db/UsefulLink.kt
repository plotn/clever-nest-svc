package com.plotn.cleverNest.model.db

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.validation.annotation.Validated
import javax.persistence.*

@Schema(description = "Useful links")
@Validated
@Entity
@Table(name = "CN_USEFUL_LINKS")
data class UsefulLink (
        @Schema(description = "Useful link unique identifier. Sequence-based")
        @JsonProperty("ulId")
        @Id
        @GeneratedValue(generator = "CNEST_USEFUL_LINKS_SEQ")
        @SequenceGenerator(name = "CNEST_USEFUL_LINKS_SEQ", sequenceName = "CNEST_USEFUL_LINKS_SEQ", initialValue = 2000, allocationSize = 1)
        @Column(name = "UL_ID")
        val ulId: Long,

        @Schema(description = "Useful link URL. Must be unique within conf")
        @JsonProperty("ulUrl")
        @Column(name = "UL_URL")
        val ulUrl: String,

        @Schema(description = "Useful link description")
        @JsonProperty("ulDesc")
        @Column(name = "UL_DESC")
        val ulDesc: String,

        @Schema(description = "Useful link link to conf")
        @JsonProperty("ulLinkConf")
        @Column(name = "UL_LINK_CONF")
        val ulLinkConf: Long
)