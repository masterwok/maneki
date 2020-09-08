package com.masterwok.shrimplesearch.common.constants

import kotlinx.serialization.Serializable

@Serializable
enum class Theme(val id: Int) {
    Light(0),
    Oled(1);
}
