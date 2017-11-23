package com.tikal.antsmasher

import akka.actor.{ActorSystem, Props}

object DevTeamApp {
  def main(args: Array[String]): Unit = {
    val system = ActorSystem("DevTeam")
//    system.actorOf(Props(classOf[StatsSampleClient], "/user/statsService"), "client")
  }
}
