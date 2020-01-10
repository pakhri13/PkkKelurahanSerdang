package com.serdang.PKK.Auth.Model

class RegisterModel (
    var uid: String,
    var name: String,
    var email: String,
    var jabatan : String
) {
    constructor() : this ("","","", ""){

    }
}