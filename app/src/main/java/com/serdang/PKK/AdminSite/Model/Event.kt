package com.serdang.PKK.AdminSite.Model

class Event(
    var id : String,
    var name : String,
    var place : String,
    var date : String,
    var time : String,
    var uniform : String
) {
    constructor() : this("","","","","","") {

    }
}