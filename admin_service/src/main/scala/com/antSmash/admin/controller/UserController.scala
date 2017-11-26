package com.antSmash.admin.controller

import com.antSmash.admin.dao.UserRepository
import com.antSmash.admin.model.User
import org.springframework.web.bind.annotation.{RequestMapping, RequestMethod, RequestParam}
import org.springframework.stereotype.Controller
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
import org.springframework.ui.Model

@Controller
@RequestMapping(Array("/users"))
abstract class UserController   {

  @Autowired
  var userRepository : UserRepository

  @Autowired
  val env : Environment = null


  @RequestMapping(value = Array("all"), method = Array(RequestMethod.GET))
  def getUser(@RequestParam id  :Long ) : String = {
    val user: User = userRepository.findOne(id)
    //userRepository.save(user)
    user.fistName
  }

    @RequestMapping(value = Array("all"), method = Array(RequestMethod.GET))
  def getAllUsers(model: Model) = {

    val users = userRepository.findAll()
    model.addAttribute("users", users)

    "users/list"
  }

  @RequestMapping(value = Array("users"), method = Array(RequestMethod.PUT))
  def updateUser( @RequestParam id  :Long , name : String ):  String = {
    val user : User = userRepository.findOne(id)
    userRepository.save(user)
    user.fistName

  }
  @RequestMapping(value = Array("users"), method = Array(RequestMethod.POST))
  def createUser(model : Model):  String = {
    // add value of "name" to Model ("$name" is used in the template)

    val user : User = new User("yanai")

    user.lastName = "yanai"
    userRepository.save(user)
    model.addAttribute("name","--++-- " + env.getProperty("user.name", "unknown user") + " --++--")

    // return the name of the view:
    user.fistName
  }
}