package com.example.androidkotlinassignment.models


object Model {

    data class Result(val title:String, val rows:List<Row>);

    class Row(val title: String?,val description:String?,val imageHref:String?){

    }
}