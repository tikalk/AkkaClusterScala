package com.antSmash.admin.controller

import org.slf4j.{Logger, LoggerFactory}
import org.springframework.web.bind.annotation._
import org.springframework.ui.Model
import org.springframework.stereotype.Component
import org.springframework.stereotype.Controller
import org.springframework.beans.factory.annotation.Autowired
import com.antSmash.admin.service.RealTimeTemperature
import org.springframework.core.env.Environment


@Controller
@RequestMapping(value = Array("/ui"))
class UIController {
  private val logger: Logger = LoggerFactory.getLogger(classOf[UIController])

  @Autowired
  val rtTemperatureService : RealTimeTemperature = null ;





  @RequestMapping(value = Array("game/latest"), method = Array(RequestMethod.GET))
  def getLatestGame(model : Model):  String = {
    model.addAttribute("month", "June")
    model.addAttribute("year", "2016")
    model.addAttribute("data", rtTemperatureService.minMaxTemperature("178", "06", "2016"));
    "FullMonthTemperatureGraph"
  }

  @RequestMapping(value = Array("game/gameOverData"), method = Array(RequestMethod.GET))
  def geGameOverData(model : Model):  String = {
    model.addAttribute("month", "June")
    model.addAttribute("year", "2016")
    model.addAttribute("data", rtTemperatureService.minMaxTemperature("178", "06", "2016"));
    "FullMonthTemperatureGraph"
  }




  @RequestMapping(value = Array("teams"), method = Array(RequestMethod.GET))
  def getAllTeams(model : Model,
             @RequestParam day  :Int,
             @RequestParam month:Int,
             @RequestParam year :Int,
             @RequestParam(defaultValue = "178") station:String,
             @RequestParam(defaultValue = "TD" ) channel:String ):  String = {
    model.addAttribute("day", day.toString())
    model.addAttribute("month", month.toString)
    model.addAttribute("year", year.toString)
    model.addAttribute("station", station)
    val data = rtTemperatureService.temperatureForOneDay(station, day.toString, month.toString, year.toString, channel) ;
    model.addAttribute("data", data);
    "OneDayTemperatureGraph"
  }

  @RequestMapping(value = Array("teams/leader"), method = Array(RequestMethod.GET))
  def getTeamLeader(model : Model):  String = {
    model.addAttribute("month", "June")
    model.addAttribute("year", "2016")
    model.addAttribute("data", rtTemperatureService.minMaxTemperature("178", "06", "2016"));
    "FullMonthTemperatureGraph"
  }

  @RequestMapping(value = Array("players"), method = Array(RequestMethod.GET))
  def getAllPlayers(model : Model):  String = {
    model.addAttribute("month", "June")
    model.addAttribute("year", "2016")
    model.addAttribute("data", rtTemperatureService.minMaxTemperature("178", "06", "2016"));
    "FullMonthTemperatureGraph"
  }


  @RequestMapping(value = Array("players/leader"), method = Array(RequestMethod.GET))
  def getPlayerLeader(model : Model):  String = {
    model.addAttribute("month", "June")
    model.addAttribute("year", "2016")
    model.addAttribute("data", rtTemperatureService.minMaxTemperature("178", "06", "2016"));
    "FullMonthTemperatureGraph"
  }



  @Autowired
  val env : Environment = null


  @RequestMapping(value = Array("player"), method = Array(RequestMethod.POST))
  def createPlayer(model : Model):  String = {
    // add value of "name" to Model ("$name" is used in the template)
    model.addAttribute("name","--++-- " + env.getProperty("player.name", "unknown player") + " --++--")

    // return the name of the view:
    "Hello"
  }
}
