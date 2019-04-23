package dofus.aks.processor.listeners


interface BasicsProcessorListener {

    fun onAuthorizedCommand(b: Boolean, substring: String)

    fun onAuthorizedLine(substring: String)

    fun onAuthorizedCommandPrompt(substring: String)

    fun onAuthorizedCommand(b: Boolean)

    fun onAuthorizedCommandClear()

    fun onAuthorizedInterfaceClose(substring: String)

    fun onAuthorizedInterfaceOpen(substring: String)

    fun onAveragePing(substring: String)

    fun onDate(substring: String)

    fun onFileCheck(substring: String)

    fun onReferenceTime(substring: String)

    fun onSubscriberRestriction(substring: String)

    fun onWhoIs(b: Boolean, substring: String)

}
