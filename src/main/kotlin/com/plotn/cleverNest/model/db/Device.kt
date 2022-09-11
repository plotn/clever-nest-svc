package com.plotn.cleverNest.model.db

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.validation.annotation.Validated
import java.time.OffsetDateTime
import javax.persistence.*

@Schema(description = "Devices")
@Validated
@Entity
@Table(name = "CN_DEVICE")
data class Device (
        @Schema(description = "Device unique identifier. Sequence-based")
        @JsonProperty("dId")
        @Id
        @GeneratedValue(generator = "CNEST_DEVICES_SEQ")
        @SequenceGenerator(name = "CNEST_DEVICES_SEQ", sequenceName = "CNEST_DEVICES_SEQ", initialValue = 2000, allocationSize = 1)
        @Column(name = "D_ID")
        val dId: Long,

        @Schema(description = "Device. Must be unique within conf")
        @JsonProperty("dName")
        @Column(name = "d_NAME")
        val dName: String,

        @Schema(description = "Device description")
        @JsonProperty("dDesc")
        @Column(name = "D_DESC")
        val dValue: String,

        @Schema(description = "Device type")
        @JsonProperty("dtype")
        @Column(name = "D_TYPE")
        var dType: Int,

        @Schema(description = "Command 'On'")
        @JsonProperty("dCommandOn")
        @Column(name = "D_COMMAND_ON")
        var dCommandOn: String? = null,

        @Schema(description = "Command 'Off'")
        @JsonProperty("dCommandOff")
        @Column(name = "D_COMMAND_OFF")
        var dCommandOff: String? = null,

        @Schema(description = "Command 'Status'")
        @JsonProperty("dCommandStatus")
        @Column(name = "D_COMMAND_STATUS")
        var dCommandStatus: String? = null,

        @Schema(description = "Device link to loc")
        @JsonProperty("dLinkLoc")
        @Column(name = "D_LINK_LOC")
        val dLinkLoc: Long,

        @Schema(description = "Last device state")
        @JsonProperty("dLastState")
        @Column(name = "D_LAST_STATE")
        var dLastState: String? = null,

        @Schema(description = "Last device state set by ... ")
        @JsonProperty("dLastStateBy")
        @Column(name = "D_LAST_STATE_BY")
        var dLastStateBy: String? = null,

        @Schema(description = "Last device state timestamp")
        @JsonProperty("dLastStateWhen")
        @Column(name = "D_LAST_STATE_WHEN")
        var dLastStateWhen: OffsetDateTime? = null,

        @Schema(description = "Is device enabled")
        @JsonProperty("dEnabled")
        @Column(name = "D_ENABLED")
        val dEnabled: Int,

        @Schema(description = "Check status interval - sec")
        @JsonProperty("dCheckStatusInterval")
        @Column(name = "D_CHECK_STATUS_INTERVAL")
        val dCheckStatusInterval: Int,

        @Schema(description = "Last device status check timestamp")
        @JsonProperty("dLastStatusCheck")
        @Column(name = "D_LAST_STATUS_CHECK")
        var dLastStatusCheck: OffsetDateTime? = null

)