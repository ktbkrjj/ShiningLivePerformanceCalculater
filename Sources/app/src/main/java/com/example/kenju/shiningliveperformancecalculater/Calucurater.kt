package com.example.kenju.shiningliveperformancecalculater

class Calucurater(val bromides_: MutableList<BromideData>, val songAttribute: Attribute, val eventBonus: Ability) {
    fun getTotalValue(): Int {
        var total = 0
        val unit = bromides_.take(6)
        for (bromide in unit) {
            val base = (bromide.dance + bromide.vocal + bromide.act)
            val songBonus = if (bromide.attribute == songAttribute) (base * 0.3).toInt() else 0
            val eventBonus = when (eventBonus) {
                Ability.Dance -> bromide.dance * 2
                Ability.Vocal -> bromide.vocal * 2
                Ability.Act   -> bromide.act * 2
                Ability.None -> 0
            }
            total += base + songBonus + eventBonus
        }
        return total
    }
}