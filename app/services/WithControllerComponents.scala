package services

import play.api.mvc.ControllerComponents

import scala.concurrent.ExecutionContext

trait WithControllerComponents {
  def controllerComponents: ControllerComponents
  implicit def executionContext: ExecutionContext
}
