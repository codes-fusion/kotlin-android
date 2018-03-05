package com.example.rssgrabber.retrofit

import com.example.rssgrabber.retrofit.entities.FeedChannel
import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "rss", strict = false)
class FeedData {
    @field:Element(name = "channel")
    var channel: FeedChannel? = null
    var network: Boolean = false
}