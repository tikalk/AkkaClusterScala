package com.antSmash.admin.model

case class State(str: String) {
    val PAUSED     : String = "PAUSED"
    val RESUMED    : String = "RESUMED"
    val STARTED    : String = "STARTED"
    val NOTSTARTED : String = "NOT_STARTED"
    val STOPPED    : String = "STOPPED"

}
