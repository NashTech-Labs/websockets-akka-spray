package reactive.socket

import com.knoldus.hub.Configuration
import com.knoldus.util.ActorHelper
import akka.actor.Props
import com.knoldus.hub.StartHub

trait BootWebSocketService {self:StartHub.type with ActorHelper =>
  private val webSocketService = new ReactiveServer(Configuration.portWs)
  val dashboardActor = system.actorOf(Props[DashBoardActor], "dashboard")
  webSocketService.forResource("/dashboard/ws", Some(dashboardActor))
  webSocketService.start
}
