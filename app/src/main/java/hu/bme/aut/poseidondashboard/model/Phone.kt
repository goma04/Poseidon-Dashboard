package hu.bme.aut.poseidondashboard.model

import java.io.Serializable

@SuppressWarnings("serial")
class Phone(
    val name: String,
    val smsList: MutableList<Sms> = mutableListOf()
) : Serializable {

}