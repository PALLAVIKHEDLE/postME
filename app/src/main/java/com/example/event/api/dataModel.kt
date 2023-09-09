package com.example.event.api

    data class MyRequest(
        val collection: Collection
    )

    data class Collection(
        val info: Info,
        val item: List<Item>
    )

    data class Info(
        val name: String,
        val description: String,
        val schema: String
    )

    data class Item(
        val name: String,
        val event: List<Event>,
        val request: Request
    )

    data class Event(
        val listen: String,
        val script: Script
    )

    data class Script(
        val id: String,
        val exec: List<String>,
        val type: String
    )

    data class Request(
        val url: String,
        val method: String,
        val header: List<Header>
    )

    data class Header(
        val key: String,
        val value: String
    )





