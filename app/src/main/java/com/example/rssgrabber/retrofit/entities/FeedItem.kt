package com.example.rssgrabber.retrofit.entities

import com.example.rssgrabber.application.adapters.ViewAdapterItem
import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "item", strict = false)
class FeedItem(
    @field:Element(name = "pubDate")
    var pubDate: String? = null,

    @field:Element(name = "title")
    var title: String? = null,

    @field:Element(name = "link")
    var link: String? = null,

    @field:Element(name = "description")
    var description: String? = null,

    var id: Long = 0,
    var hasMetaInfo: Int = 0,
    var date: String? = null,
    var views: String? = null,
    var salary: String? = null,
    var location: String? = null,
    var readyToRemote: String? = null,
    var companyName: String? = null,
    var companyAbout: String? = null,
    var companyLogo: String? = null,
    var skils: String? = null,
    var companyUrl: String? = null,
    var vacancyDescription: String? = null,
    var vacancyTitle: String? = null
): ViewAdapterItem()
