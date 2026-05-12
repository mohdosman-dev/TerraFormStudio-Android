package com.otaku.terraformstudio.features.auth.data

import com.otaku.terraformstudio.features.auth.domain.User

fun UserDto.toUser(): User {
    return User(
        id = id,
        email = email,
        roles = roles
    )
}
