package com.example.kenju.shiningliveperformancecalculater

class Calucurater(val bromides_: MutableList<BromideData>) {
    fun getTotalValue() : Int {
        var total = 0
        for(index in 1..6) {
            if (bromides_.count() > index) {
                total += bromides_[index].star + bromides_[index].shine + bromides_[index].dream
            }
        }
        return total
    }
}