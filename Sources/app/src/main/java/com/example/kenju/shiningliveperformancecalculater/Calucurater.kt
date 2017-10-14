package com.example.kenju.shiningliveperformancecalculater

val skillMagnification = 0.6

class Calucurater(val bromides_: MutableList<BromideData>, val songAttribute: Attribute, val eventBonus: Ability, val skillAttribute: Attribute, val skillAbility: Ability) {
    fun getTotalValue(): Int {
        var total = 0
        val unit = bromides_.take(6)
        for (bromide in unit) {
            val base = (bromide.dance + bromide.vocal + bromide.act)
            val songBonus = if (bromide.attribute == songAttribute) (base * 0.3).toInt() else 0
            val skillBonus =
                    if (bromide.attribute == skillAttribute)
                        when (skillAbility) {
                            Ability.Dance -> (bromide.dance.toDouble() * skillMagnification).toInt()
                            Ability.Vocal -> (bromide.vocal.toDouble() * skillMagnification).toInt()
                            Ability.Act -> (bromide.act.toDouble() * skillMagnification).toInt()
                            Ability.None -> 0
                        }
                    else 0
            val eventBonus = when (eventBonus) {
                Ability.Dance -> bromide.dance * 2
                Ability.Vocal -> bromide.vocal * 2
                Ability.Act -> bromide.act * 2
                Ability.None -> 0
            }
            total += base + songBonus + eventBonus + skillBonus
        }
        return total
    }
}