package com.tikal.antsmasher

import akka.actor.{ActorSystem, Props}

object AntSmasherApp {
  def main(args: Array[String]): Unit = {
    val system = ActorSystem("ClusterSystem")
//    system.actorOf(Props(classOf[StatsSampleClient], "/user/statsService"), "client")
  }
}
