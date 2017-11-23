package com.tikal.antsmasher

import akka.actor.{ActorSystem, Props}

object SmashingServiceApp {
  def main(args: Array[String]): Unit = {
    val system = ActorSystem("SmashingService")
//    system.actorOf(Props(classOf[StatsSampleClient], "/user/statsService"), "client")
  }
}
