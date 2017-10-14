package com.example.kenju.shiningliveperformancecalculater

class Calucurater(val bromides_: MutableList<BromideData>, val songAttribute: Attribute) {
    fun getTotalValue(): Int {
        var total = 0
        val unit = bromides_.take(6)
        for (bromide in unit) {
            var score = (bromide.dance + bromide.vocal + bromide.act).toDouble()
            if (bromide.attribute == songAttribute) {
                score *= 1.3
            }
            total += score.toInt()
        }
        return total
    }
}