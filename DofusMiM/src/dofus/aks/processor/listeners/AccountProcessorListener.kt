package dofus.aks.processor.listeners

import java.io.IOException

interface AccountProcessorListener {

    @Throws(IOException::class)
    fun onCharacterAdd(b: Boolean, substring: String)

    @Throws(IOException::class)
    fun onCharacterDelete(b: Boolean, substring: String)

    @Throws(IOException::class)
    fun onCharacterNameGenerated(b: Boolean, substring: String)

    @Throws(IOException::class)
    fun onCharacterSelected(success: Boolean, extra: String)

    @Throws(IOException::class)
    fun onCharactersList(b: Boolean, substring: String, c: Boolean)

    @Throws(IOException::class)
    fun onCharactersList(b: Boolean, substring: String)

    @Throws(IOException::class)
    fun onCharactersMigrationAskConfirm(substring: String)

    @Throws(IOException::class)
    fun onCommunity(substring: String)

    @Throws(IOException::class)
    fun onDofusPseudo(substring: String)

    @Throws(IOException::class)
    fun onFriendServerList(substring: String)

    @Throws(IOException::class)
    fun onGiftsList(substring: String)

    @Throws(IOException::class)
    fun onGiftStored(b: Boolean)

    @Throws(IOException::class)
    fun onHosts(substring: String)

    @Throws(IOException::class)
    fun onKey(substring: String)

    @Throws(IOException::class)
    fun onLogin(b: Boolean, substring: String)

    @Throws(IOException::class)
    fun onMiniClipInfo()

    @Throws(IOException::class)
    fun onNewLevel(substring: String)

    @Throws(IOException::class)
    fun onNewQueue(substring: String)

    @Throws(IOException::class)
    fun onQueue(substring: String)

    @Throws(IOException::class)
    fun onRegionalVersion(substring: String)

    @Throws(IOException::class)
    fun onRescue(b: Boolean)

    @Throws(IOException::class)
    fun onRestrictions(substring: String)

    @Throws(IOException::class)
    fun onSecretQuestion(substring: String)

    @Throws(IOException::class)
    fun onSelectServer(b: Boolean, c: Boolean, substring: String)

    @Throws(IOException::class)
    fun onServersList(b: Boolean, substring: String)

    @Throws(IOException::class)
    fun onStats(substring: String)

    @Throws(IOException::class)
    fun onTicketResponse(b: Boolean, substring: String)

    @Throws(IOException::class)
    fun logon(login: String, password: String)

}
