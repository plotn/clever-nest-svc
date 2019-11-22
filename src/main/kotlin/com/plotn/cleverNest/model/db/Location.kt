package com.plotn.cleverNest.model.db

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import org.springframework.validation.annotation.Validated
import javax.persistence.*

@ApiModel(description = "Locations")
@Validated
@Entity
@Table(name = "CN_LOCATION")
data class Location (
        @ApiModelProperty(value = "Location unique identifier. Sequence-based")
        @JsonProperty("lId")
        @Id
        @GeneratedValue(generator = "CNEST_LOCATIONS_SEQ")
        @SequenceGenerator(name = "CNEST_LOCATIONS_SEQ", sequenceName = "CNEST_LOCATIONS_SEQ", initialValue = 2000, allocationSize = 1)
        @Column(name = "L_ID")
        val lId: Long,

        @ApiModelProperty(value = "Location name. Must be unique within conf")
        @JsonProperty("lName")
        @Column(name = "L_NAME")
        val lName: String,

        @ApiModelProperty(value = "Location description")
        @JsonProperty("lDesc")
        @Column(name = "L_DESC")
        val lValue: String,

        @ApiModelProperty(value = "Location link to conf")
        @JsonProperty("lLinkConf")
        @Column(name = "L_LINK_CONF")
        val lLinkConf: Long
)