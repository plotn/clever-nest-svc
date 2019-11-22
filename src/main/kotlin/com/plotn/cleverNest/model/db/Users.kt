package com.plotn.cleverNest.model.db

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import org.springframework.validation.annotation.Validated
import java.time.OffsetDateTime
import javax.persistence.*

@ApiModel(description = "User definition")
@Validated
@Entity
@Table(name = "CNEST_USERS")
@JsonIgnoreProperties(value = ["userPassw"])
data class Users (

    @ApiModelProperty(value = "Users unique identifier. Sequence-based. -1 is internal")
    @JsonProperty("userId")
    @Id
    @GeneratedValue(generator = "CNEST_USERS_SEQ")
    @SequenceGenerator(name = "CNEST_USERS_SEQ", sequenceName = "CNEST_USERS_SEQ", initialValue = 2000, allocationSize = 1)
    @Column(name = "USER_ID")
    val userId: Long,

    @ApiModelProperty(value = "User login. Must be unique")
    @JsonProperty("userLogin")
    @Column(name = "USER_LOGIN")
    val userLogin: String,

    @ApiModelProperty(value = "user's password hash")
    @JsonProperty("userPassw")
    @Column(name = "USER_PASSW")
    val userPassw: String,

    @ApiModelProperty(value = "User registration ts")
    @JsonProperty("userRegisterTs")
    @Column(name = "USER_REGISTER_TS")
    var userRegisterDate: OffsetDateTime,

    @ApiModelProperty(value = "User last login ts")
    @JsonProperty("userLastLoginTs")
    @Column(name = "USER_LAST_LOGIN_TS")
    var userLastLoginTs: OffsetDateTime? = null,

    @ApiModelProperty(value = "user's last login device")
    @JsonProperty("userLastLoginDevice")
    @Column(name = "USER_LAST_LOGIN_DEVICE")
    var userLastLoginDevice: String? = null,

    @ApiModelProperty(value = "User name")
    @JsonProperty("userName")
    @Column(name = "USER_NAME")
    val userName: String,

    @ApiModelProperty(value = "User email")
    @JsonProperty("userEmail")
    @Column(name = "USER_EMAIL")
    var userEmail: String? = null,

    @ApiModelProperty(value = "User status")
    @JsonProperty("userStatus")
    @Column(name = "USER_STATUS")
    var userStatus: Int? = null,

    @ApiModelProperty(value = "User role - ADMIN, USER")
    @JsonProperty("userRole")
    @Column(name = "USER_ROLE")
    var userRole: String? = null

)
