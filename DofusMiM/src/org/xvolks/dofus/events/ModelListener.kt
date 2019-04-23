package org.xvolks.dofus.events

import org.xvolks.dofus.mvc.Model

interface ModelListener<V> where V : Model<*> {

    val model: V

    fun modelUpdated()
}
