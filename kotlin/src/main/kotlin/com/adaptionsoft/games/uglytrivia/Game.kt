package com.adaptionsoft.games.uglytrivia

import com.adaptionsoft.games.trivia.runner.Printer
import java.util.*

enum class Question(val type: String, val places: List<Int>) {
    Pop("Pop", listOf(0,4,8)),
    Sport("Sports", listOf(2,6,10)),
    Science( "Science", listOf(1,5,9)),
    Rock("Rock", listOf(3,7,11))
}

const val boardSize = 12

class Game {
    var players = ArrayList<String>()
    var places = IntArray(6)
    var purses = IntArray(6)
    var inPenaltyBox = BooleanArray(6)

    var popQuestions = LinkedList<Any>()
    var scienceQuestions = LinkedList<Any>()
    var sportsQuestions = LinkedList<Any>()
    var rockQuestions = LinkedList<Any>()

    var currentPlayer = 0
    var isGettingOutOfPenaltyBox: Boolean = false
    
    init {
        for (i in 0..49) {
            popQuestions.addLast(createPopQuestions(i) )
            scienceQuestions.addLast(createScienceQuestions(i) )
            sportsQuestions.addLast(createSportQuestions(i) )
            rockQuestions.addLast(createRockQuestions(i))
        }
    }

    private fun createRockQuestions(questionNumber: Int) = createQuestion( Question.Rock.type + " Question ", questionNumber)
    private fun createSportQuestions(questionNumber: Int) = createQuestion(Question.Sport.type + " Question ", questionNumber)
    private fun createScienceQuestions(questionNumber: Int) = createQuestion(Question.Science.type + " Question ", questionNumber)
    private fun createPopQuestions(questionNumber: Int) = createQuestion(Question.Pop.type + " Question ", questionNumber)

    fun createQuestion(message: String, questionNumber: Int): String {
        return message + questionNumber
    }

    fun isPlayable(): Boolean {
        return numberOfPlayers() >= 2
    }

    fun add(playerName: String): Boolean {
        players.add(playerName)
        places[numberOfPlayers()] = 0
        purses[numberOfPlayers()] = 0
        inPenaltyBox[numberOfPlayers()] = false

        Printer.println("$playerName was added")
        Printer.println("They are player number " + players.size)
        return true
    }

    fun numberOfPlayers(): Int {
        return players.size
    }

    fun roll(roll: Int) {
        Printer.println(currentPlayerName() + " is the current player")
        Printer.println("They have rolled a $roll")

        if (inPenaltyBox[currentPlayer]) {
            if (roll % 2 != 0) {
                isGettingOutOfPenaltyBox = true

                Printer.println(currentPlayerName() + " is getting out of the penalty box")
                movePlayerToBox(currentPlayerBox() + roll)
                if (currentPlayerBox() == boardSize) movePlayerToBox(0)

                Printer.println(currentPlayerName()
                        + "'s new location is "
                        + currentPlayerBox())
                Printer.println("The category is " + currentCategory())
                askQuestion()
            } else {
                Printer.println(currentPlayerName() + " is not getting out of the penalty box")
                isGettingOutOfPenaltyBox = false
            }

        } else {

            movePlayerToBox(currentPlayerBox() + roll)
            if (currentPlayerBox() == boardSize) movePlayerToBox(0)

            Printer.println(currentPlayerName()
                    + "'s new location is "
                    + currentPlayerBox())
            Printer.println("The category is " + currentCategory())
            askQuestion()
        }

    }

    private fun movePlayerToBox(boxNumber: Int) {
        places[currentPlayer] = boxNumber
    }

    private fun askQuestion() {
        if (currentCategory() === Question.Pop.type)
            Printer.println(popQuestions.removeFirst())
        if (currentCategory() === Question.Science.type)
            Printer.println(scienceQuestions.removeFirst())
        if (currentCategory() === Question.Sport.type)
            Printer.println(sportsQuestions.removeFirst())
        if (currentCategory() === Question.Rock.type)
            Printer.println(rockQuestions.removeFirst())
    }

    private fun currentCategory(): String {
        if(isPopPlace(currentPlayerBox())){
            return Question.Pop.type
        }
        if(isSciencePlace(currentPlayerBox())){
            return Question.Science.type
        }
        if(isSportPlace(currentPlayerBox())){
            return Question.Sport.type
        }

        return Question.Rock.type
    }

    private fun currentPlayerBox() = places[currentPlayer]

    private fun isPopPlace(place: Int) = Question.Pop.places.contains(place)
    private fun isSciencePlace(place: Int) = Question.Science.places.contains(place)
    private fun isSportPlace(place: Int) = Question.Sport.places.contains(place)

    fun wasCorrectlyAnswered(): Boolean {
        if (inPenaltyBox[currentPlayer]) {
            if (isGettingOutOfPenaltyBox) {
                Printer.println("Answer was correct!!!!")
                purses[currentPlayer]++
                Printer.println(currentPlayerName()
                        + " now has "
                        + purses[currentPlayer]
                        + " Gold Coins.")

                val winner = didPlayerWin()
                currentPlayer++
                if (currentPlayer == players.size) currentPlayer = 0

                return winner
            } else {
                currentPlayer++
                if (currentPlayer == players.size) currentPlayer = 0
                return true
            }


        } else {

            Printer.println("Answer was correct!!!!")
            purses[currentPlayer]++
            Printer.println(currentPlayerName()
                    + " now has "
                    + purses[currentPlayer]
                    + " Gold Coins.")

            val winner = didPlayerWin()
            currentPlayer++
            if (currentPlayer == players.size) currentPlayer = 0

            return winner
        }
    }

    fun wrongAnswer(): Boolean {
        Printer.println("Question was incorrectly answered")
        Printer.println(currentPlayerName() + " was sent to the penalty box")
        inPenaltyBox[currentPlayer] = true

        currentPlayer++
        if (currentPlayer == players.size) currentPlayer = 0
        return true
    }

    private fun currentPlayerName() = players[currentPlayer]

    private fun didPlayerWin(): Boolean {
        return purses[currentPlayer] != 6
    }
}
