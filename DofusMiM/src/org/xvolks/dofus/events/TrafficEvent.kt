package org.xvolks.dofus.events

import java.util.ArrayList

class TrafficEvent(val source: Source, val timeStamp: Long, val data: String) {
    val reponseEvent: List<TrafficEvent> = ArrayList()

    enum class Source {
        FROM_SERVER,
        FROM_CLIENT
    }
}
