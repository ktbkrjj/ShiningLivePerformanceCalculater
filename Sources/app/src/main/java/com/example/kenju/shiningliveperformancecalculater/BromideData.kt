package com.example.kenju.shiningliveperformancecalculater

enum class Attribute {
    Star, Shine, Dream
}

data class BromideData(val name: String, val attribute: Attribute, val star: Int, val shine: Int,val dream: Int) {
    val imageId = when (attribute) {
        Attribute.Star  -> R.drawable.star
        Attribute.Shine -> R.drawable.shine
        Attribute.Dream -> R.drawable.dream
    }
}