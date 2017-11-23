package com.tikal.antsmasher

import akka.actor.{ActorSystem, Props}

object AntsPublisherApp {
  def main(args: Array[String]): Unit = {
    val system = ActorSystem("AntsPublisher")
//    system.actorOf(Props(classOf[StatsSampleClient], "/user/statsService"), "client")
  }
}
