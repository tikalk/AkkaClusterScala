package com.tikal.antsmasher

import akka.actor.{ActorSystem, Props}

object TeamScoreApp {
  def main(args: Array[String]): Unit = {
    val system = ActorSystem("TeamScore")
//    system.actorOf(Props(classOf[StatsSampleClient], "/user/statsService"), "client")
  }
}
