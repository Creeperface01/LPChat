package com.creeperface.nukkit.lpchat

/**
 * @author CreeperFace
 */

fun String.replaceMap(data: Map<String, String>): String {
    var s = this

    data.forEach { key, value -> s = s.replace(key, value) }

    return s
}