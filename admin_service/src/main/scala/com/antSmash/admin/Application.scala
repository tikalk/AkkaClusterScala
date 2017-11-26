package com.antSmash.admin

import javax.annotation.{PostConstruct, PreDestroy}

import com.antSmash.admin.model.Game
import org.slf4j.LoggerFactory
import org.springframework.boot._
import org.springframework.boot.autoconfigure._
import org.springframework.context.annotation._


@Configuration
@ComponentScan
@SpringBootApplication
@EnableAutoConfiguration
class Application {

  val log  = LoggerFactory.getLogger(this.getClass.getSimpleName)
  val Game = new Game()

  @PostConstruct
  def init(): Unit = {
    log.info("admin service started")

  }

  @PreDestroy
  def shutdown(): Unit = {
    log.info("server shutdown")
  }

}

object Application {
  def main(args: Array[String]) {
//    val configuration: Array[Object] = Array(classOf[Application])
//    val springApplication = new SpringApplication(configuration:_*)
//    springApplication.addListeners(new ApplicationPidFileWriter())
//    springApplication.run(args:_*)
    SpringApplication.run(classOf[Application])
  }
}

