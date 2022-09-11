package com.plotn.cleverNest.model.db

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.validation.annotation.Validated
import javax.persistence.*

@Schema(description = "Locations")
@Validated
@Entity
@Table(name = "CN_LOCATION")
data class Location (
        @Schema(description = "Location unique identifier. Sequence-based")
        @JsonProperty("lId")
        @Id
        @GeneratedValue(generator = "CNEST_LOCATIONS_SEQ")
        @SequenceGenerator(name = "CNEST_LOCATIONS_SEQ", sequenceName = "CNEST_LOCATIONS_SEQ", initialValue = 2000, allocationSize = 1)
        @Column(name = "L_ID")
        val lId: Long,

        @Schema(description = "Location name. Must be unique within conf")
        @JsonProperty("lName")
        @Column(name = "L_NAME")
        val lName: String,

        @Schema(description = "Location description")
        @JsonProperty("lDesc")
        @Column(name = "L_DESC")
        val lValue: String,

        @Schema(description = "Location link to conf")
        @JsonProperty("lLinkConf")
        @Column(name = "L_LINK_CONF")
        val lLinkConf: Long
)