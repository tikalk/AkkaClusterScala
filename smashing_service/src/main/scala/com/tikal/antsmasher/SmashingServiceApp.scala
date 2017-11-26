package com.tikal.antsmasher

import akka.actor.{ActorRef, ActorSystem, Props}
import com.tikal.antsmasher.actors.Game.StartGame
import com.tikal.antsmasher.actors.Manager

object SmashingServiceApp {

  val SMASH_TOPIC = "ANTS_SMASH"

  def loadGames(manager : ActorRef) = {
    val gameId = 1
    manager ! StartGame(1)
    //val antsGeneratorActor = system.actorOf(Props(new Game(gameId)), "Game" + gameId)
  }

  def main(args: Array[String]): Unit = {
    val system = ActorSystem("SmashingService")
    val manager = system.actorOf(Props(classOf[Manager]), "GameManager")
    loadGames(manager)

    Thread.sleep(1000)
    val selection = system.actorSelection("/user/GameManager/Game1")
//    selection ! Ant(1,2,3,HitType.Hit)
//    selection ! Ant(1,2,4,HitType.Miss)
  }
}
