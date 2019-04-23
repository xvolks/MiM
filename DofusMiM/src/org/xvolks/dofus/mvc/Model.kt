package org.xvolks.dofus.mvc

interface Model<T> {

    /**
     * Adds a listener to this model
     * @param listener
     */
    fun addModelListener(listener: T)

    /**
     * Remove a listener from this model
     * @param listener
     * @return true if the listener was removed
     */
    fun removeModelListener(listener: T): Boolean

    /**
     * Should be called when the model is changed
     */
    fun fireModelUpdated()

}
