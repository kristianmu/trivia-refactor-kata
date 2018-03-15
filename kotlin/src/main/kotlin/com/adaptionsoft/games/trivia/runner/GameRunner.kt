package com.adaptionsoft.games.trivia.runner

import com.adaptionsoft.games.uglytrivia.Game
import java.util.*

object GameRunner {
    var notAWinner: Boolean = false

    fun run(): Game {
        val aGame = Game()

        listOf("Chet", "Pat", "Sue").forEach { aGame.add(it) }

        do {
            aGame.roll(Die.roll())

            if (HawkingsOracle.isAnswerCorrect()) {
                GameRunner.notAWinner = aGame.wrongAnswer()
            } else {
                GameRunner.notAWinner = aGame.wasCorrectlyAnswered()
            }
        } while (GameRunner.notAWinner)

        return aGame
    }
}

object HawkingsOracle {
    val rand = Random()
    var fixedResponse: Boolean? = null

    fun forceAlternatingAnswer() {
        fixedResponse = true
    }

    fun isAnswerCorrect(): Boolean {
        if (fixedResponse != null) {
            fixedResponse = !fixedResponse!!

            return fixedResponse!!
        }

        return rand.nextInt(9) == 7
    }
}

object Printer {
    var messages = mutableListOf<String>()

    fun println(message: Any) {
        messages.add(message as String)
        kotlin.io.println(message)
    }
}

object Die {
    val rand = Random()
    var fixedRoll: Int? = null

    fun roll(): Int {
        if (fixedRoll != null) {
            if (fixedRoll == 4) {
                fixedRoll = 1
            } else {
                fixedRoll = 4
            }
        }

        return fixedRoll ?: rand.nextInt(5)+1
    }

    fun fixAt(i: Int) {
        fixedRoll = i
    }
}

fun main(args: Array<String>) {
    GameRunner.run()
}
