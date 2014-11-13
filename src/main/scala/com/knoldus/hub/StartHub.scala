package com.knoldus.hub

import com.knoldus.util.ActorHelper
import akka.actor.Props
import akka.io.IO
import spray.can.Http
import akka.actor.ActorContext
import akka.actor.Actor
import reactive.socket.BootWebSocketService

object StartHub extends App with ActorHelper with BootWebSocketService {

  implicit lazy val system = hubActorSystem
  // create and start Email services
  val service = system.actorOf(Props[HubServices], "hubServices")
  // start a new HTTP server on port 8080 with our service actor as the handler
  IO(Http) ! Http.Bind(service, interface = "localhost", port = 8080)
}

class HubServices extends Actor with HubRoutes {
  def actorRefFactory: ActorContext = context
  def receive: Receive = runRoute(hubRoutes)
}

object Configuration {
  import com.typesafe.config.ConfigFactory

  private val config = ConfigFactory.load
  config.checkValid(ConfigFactory.defaultReference)
  val portWs = config.getInt("ports.ws")
}