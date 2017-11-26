package com.tikal.antsmasher.controller

import akka.actor.{ActorRef, Props}
import com.tikal.antsmasher.{AntsFirehoseApp, AntsGenerator, GameFirehose}

import scala.collection.mutable

case class Game(id : String, state : String)


object ChangeGameState {

  val games : mutable.Map[String, ActorRef] = mutable.Map.empty[String, ActorRef]

  def createGame(game : Game) = {
    println("createGame " + game.id)
    games(game.id) = AntsFirehoseApp.system.actorOf(Props(classOf[GameFirehose], game.id), "gameFirehose" + "-" + game.id)
    games(game.id) ! "create"
  }

  def startGame(game : Game) = {
//    if (games.contains(game.id)) {
//      createGame(game)
//    }
    games(game.id) ! "start"
  }

  def pauseGame(game : Game) = {
    games(game.id) ! "pause"
  }

  def resumeGame(game : Game) = {
    games(game.id) ! "resume"
  }

  def stopGame(game : Game) = {
    games(game.id) ! "stop"
  }


}
