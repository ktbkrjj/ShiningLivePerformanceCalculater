package com.example.kenju.shiningliveperformancecalculater

enum class Attribute {
    None, Star, Shine, Dream
}

enum class Ability {
    None, Dance, Vocal, Act
}

data class BromideData(val name: String, val attribute: Attribute, val dance: Int, val vocal: Int,val act: Int) {
    val imageId = when (attribute) {
        Attribute.Star  -> R.drawable.star
        Attribute.Shine -> R.drawable.shine
        Attribute.Dream -> R.drawable.dream
        Attribute.None  -> R.drawable.star
    }
}