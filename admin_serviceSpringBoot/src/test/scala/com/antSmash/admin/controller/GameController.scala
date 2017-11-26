package com.antSmash.admin.controller

import org.slf4j.{Logger, LoggerFactory}
import org.springframework.web.bind.annotation._
import org.springframework.beans.factory.annotation.Autowired
//import com.tikal.weather.dao.RealTimeDataDao
//import com.tikal.weather.model.RealTimeData
import javax.annotation.PostConstruct
import org.springframework.stereotype.Controller
import java.lang.Iterable
import scala.collection.JavaConversions.asScalaBuffer
import com.antSmash.admin.service.RealTimeTemperature
import com.antSmash.admin.service.RealTimeTemperature
import com.antSmash.admin.model.RealTimeData

/**
  * Created by Evyatar on 1/7/2016.
  */
@RestController
class GameController {
  private val logger: Logger = LoggerFactory.getLogger(classOf[GameController])

  

  @Autowired
  val rtTemperatureService : RealTimeTemperature = null ;
  

  
  @PostConstruct
  def init() = {
  }

  def getDataFromIms(): scala.List[RealTimeData] = List(
    new RealTimeData("148","10:00:00","27.0", 1, 8, 2017),
    new RealTimeData("148","10:10:00","27.1", 1, 8, 2017),
    new RealTimeData("148","10:20:00","27.2", 1, 8, 2017),
    new RealTimeData("148","10:30:00","27.3", 1, 8, 2017),
    new RealTimeData("148","10:40:00","27.4", 1, 8, 2017),
    new RealTimeData("148","10:50:00","27.5", 1, 8, 2017)) ;

  @RequestMapping(value = Array("/test"), method = Array(RequestMethod.GET))
  def test() : String = {
      logger.warn(s"test")
      val all : List[RealTimeData] = getDataFromIms()
      logger.info("size="+all.size);
      val x : RealTimeData = all(0)
      s"${x.stationId} ${x.day}-${x.month}-${x.year} ${x.time} ${x.temperature}"
  }
  

}
