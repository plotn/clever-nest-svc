package com.plotn.cleverNest.model.db

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import org.springframework.validation.annotation.Validated
import javax.persistence.*

@ApiModel(description = "Configurations")
@Validated
@Entity
@Table(name = "CN_CONF")
data class Conf (
    @ApiModelProperty(value = "Conf unique identifier. Sequence-based. -1 for 'common' conf")
    @JsonProperty("cId")
    @Id
    @GeneratedValue(generator = "CNEST_CONF_SEQ")
    @SequenceGenerator(name = "CNEST_CONF_SEQ", sequenceName = "CNEST_CONF_SEQ", initialValue = 2000, allocationSize = 1)
    @Column(name = "C_ID")
    val cId: Long,

    @ApiModelProperty(value = "Conf name. Must be unique")
    @JsonProperty("cName")
    @Column(name = "C_NAME")
    val cName: String,

    @ApiModelProperty(value = "Conf description")
    @JsonProperty("cDesc")
    @Column(name = "C_DESC")
    val cDesc: String? = null,

    @ApiModelProperty(value = "Check hrefs")
    @JsonProperty("cCheckHrefs")
    @Column(name = "C_CHECK_HREFS")
    val cCheckHrefs: String? = null,

    @ApiModelProperty(value = "Conf is default - for device refreshing")
    @JsonProperty("cDefault")
    @Column(name = "C_DEFAULT")
    val cDefault: Int

)
