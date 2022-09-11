package com.plotn.cleverNest.model.db

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.validation.annotation.Validated
import java.time.OffsetDateTime
import javax.persistence.*

@Schema(description = "User definition")
@Validated
@Entity
@Table(name = "CNEST_USERS")
@JsonIgnoreProperties(value = ["userPassw"])
data class User (

    @Schema(description = "Users unique identifier. Sequence-based. -1 is internal")
    @JsonProperty("userId")
    @Id
    @GeneratedValue(generator = "CNEST_USERS_SEQ")
    @SequenceGenerator(name = "CNEST_USERS_SEQ", sequenceName = "CNEST_USERS_SEQ", initialValue = 2000, allocationSize = 1)
    @Column(name = "USER_ID")
    val userId: Long,

    @Schema(description = "User login. Must be unique")
    @JsonProperty("userLogin")
    @Column(name = "USER_LOGIN")
    val userLogin: String,

    @Schema(description = "user's password hash")
    @JsonProperty("userPassw")
    @Column(name = "USER_PASSW")
    val userPassw: String,

    @Schema(description = "User registration ts")
    @JsonProperty("userRegisterTs")
    @Column(name = "USER_REGISTER_TS")
    var userRegisterDate: OffsetDateTime,

    @Schema(description = "User last login ts")
    @JsonProperty("userLastLoginTs")
    @Column(name = "USER_LAST_LOGIN_TS")
    var userLastLoginTs: OffsetDateTime? = null,

    @Schema(description = "user's last login device")
    @JsonProperty("userLastLoginDevice")
    @Column(name = "USER_LAST_LOGIN_DEVICE")
    var userLastLoginDevice: String? = null,

    @Schema(description = "User name")
    @JsonProperty("userName")
    @Column(name = "USER_NAME")
    val userName: String,

    @Schema(description = "User email")
    @JsonProperty("userEmail")
    @Column(name = "USER_EMAIL")
    var userEmail: String? = null,

    @Schema(description = "User status")
    @JsonProperty("userStatus")
    @Column(name = "USER_STATUS")
    var userStatus: Int? = null,

    @Schema(description = "User role - ADMIN, USER")
    @JsonProperty("userRole")
    @Column(name = "USER_ROLE")
    var userRole: String? = null

)
