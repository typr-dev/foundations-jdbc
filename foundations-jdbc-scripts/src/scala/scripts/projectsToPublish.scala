package scripts

import bleep.model

object projectsToPublish {
  def include(crossName: model.CrossProjectName): Boolean =
    crossName.name.value match {
      case "foundations-jdbc"        => true
      case "foundations-jdbc-hikari" => true
      case "foundations-jdbc-otel"   => true
      case "foundations-jdbc-spring" => true
      case "foundations-jdbc-kotlin" => true
      case "foundations-jdbc-scala"  => true
      case _                         => false
    }
}
