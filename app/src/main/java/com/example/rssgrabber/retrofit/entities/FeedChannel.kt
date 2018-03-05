package com.example.rssgrabber.retrofit.entities

import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name = "channel", strict = false)
class FeedChannel {
    var id: Long = 0

    @field:Element(name = "title")
    var title: String? = null

    @field:Element(name = "description")
    var description: String? = null

    @field:ElementList(inline = true, name = "item")
    var feedItems: MutableList<FeedItem>? = null

    var siteTitle: String? = null
    var image: String? = null
    var divisions: String? = null
}