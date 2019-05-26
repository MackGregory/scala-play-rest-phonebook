package configs

import scala.concurrent.ExecutionContext

trait ExecutionContextComponents {
  implicit def executionContext: ExecutionContext
}
