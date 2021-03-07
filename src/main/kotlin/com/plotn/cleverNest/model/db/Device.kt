package com.plotn.cleverNest.model.db

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import org.springframework.validation.annotation.Validated
import java.time.OffsetDateTime
import javax.persistence.*

@ApiModel(description = "Devices")
@Validated
@Entity
@Table(name = "CN_DEVICE")
data class Device (
        @ApiModelProperty(value = "Device unique identifier. Sequence-based")
        @JsonProperty("dId")
        @Id
        @GeneratedValue(generator = "CNEST_DEVICES_SEQ")
        @SequenceGenerator(name = "CNEST_DEVICES_SEQ", sequenceName = "CNEST_DEVICES_SEQ", initialValue = 2000, allocationSize = 1)
        @Column(name = "D_ID")
        val dId: Long,

        @ApiModelProperty(value = "Device. Must be unique within conf")
        @JsonProperty("dName")
        @Column(name = "d_NAME")
        val dName: String,

        @ApiModelProperty(value = "Device description")
        @JsonProperty("dDesc")
        @Column(name = "D_DESC")
        val dValue: String,

        @ApiModelProperty(value = "Device type")
        @JsonProperty("dtype")
        @Column(name = "D_TYPE")
        var dType: Int,

        @ApiModelProperty(value = "Command 'On'")
        @JsonProperty("dCommandOn")
        @Column(name = "D_COMMAND_ON")
        var dCommandOn: String? = null,

        @ApiModelProperty(value = "Command 'Off'")
        @JsonProperty("dCommandOff")
        @Column(name = "D_COMMAND_OFF")
        var dCommandOff: String? = null,

        @ApiModelProperty(value = "Command 'Status'")
        @JsonProperty("dCommandStatus")
        @Column(name = "D_COMMAND_STATUS")
        var dCommandStatus: String? = null,

        @ApiModelProperty(value = "Device link to loc")
        @JsonProperty("dLinkLoc")
        @Column(name = "D_LINK_LOC")
        val dLinkLoc: Long,

        @ApiModelProperty(value = "Last device state")
        @JsonProperty("dLastState")
        @Column(name = "D_LAST_STATE")
        var dLastState: String? = null,

        @ApiModelProperty(value = "Last device state set by ... ")
        @JsonProperty("dLastStateBy")
        @Column(name = "D_LAST_STATE_BY")
        var dLastStateBy: String? = null,

        @ApiModelProperty(value = "Last device state timestamp")
        @JsonProperty("dLastStateWhen")
        @Column(name = "D_LAST_STATE_WHEN")
        var dLastStateWhen: OffsetDateTime? = null,

        @ApiModelProperty(value = "Is device enabled")
        @JsonProperty("dEnabled")
        @Column(name = "D_ENABLED")
        val dEnabled: Int,

        @ApiModelProperty(value = "Check status interval - sec")
        @JsonProperty("dCheckStatusInterval")
        @Column(name = "D_CHECK_STATUS_INTERVAL")
        val dCheckStatusInterval: Int,

        @ApiModelProperty(value = "Last device status check timestamp")
        @JsonProperty("dLastStatusCheck")
        @Column(name = "D_LAST_STATUS_CHECK")
        var dLastStatusCheck: OffsetDateTime? = null

)