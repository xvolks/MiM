package dofus.aks.processor.nullImpl

import java.io.IOException

import dofus.aks.processor.listeners.AccountProcessorListener

open class NullAccountProcessor : AccountProcessorListener {

    @Throws(IOException::class)
    override fun logon(login: String, password: String) {
        // TODO Auto-generated method stub

    }

    @Throws(IOException::class)
    override fun onCharacterAdd(b: Boolean, substring: String) {
        // TODO Auto-generated method stub

    }

    @Throws(IOException::class)
    override fun onCharacterDelete(b: Boolean, substring: String) {
        // TODO Auto-generated method stub

    }

    @Throws(IOException::class)
    override fun onCharacterNameGenerated(b: Boolean, substring: String) {
        // TODO Auto-generated method stub

    }

    @Throws(IOException::class)
    override fun onCharacterSelected(b: Boolean, substring: String) {
        // TODO Auto-generated method stub

    }

    @Throws(IOException::class)
    override fun onCharactersList(b: Boolean, substring: String, c: Boolean) {
        // TODO Auto-generated method stub

    }

    @Throws(IOException::class)
    override fun onCharactersList(b: Boolean, substring: String) {
        // TODO Auto-generated method stub

    }

    @Throws(IOException::class)
    override fun onCharactersMigrationAskConfirm(substring: String) {
        // TODO Auto-generated method stub

    }

    @Throws(IOException::class)
    override fun onCommunity(substring: String) {
        // TODO Auto-generated method stub

    }

    @Throws(IOException::class)
    override fun onDofusPseudo(substring: String) {
        // TODO Auto-generated method stub

    }

    @Throws(IOException::class)
    override fun onFriendServerList(substring: String) {
        // TODO Auto-generated method stub

    }

    @Throws(IOException::class)
    override fun onGiftStored(b: Boolean) {
        // TODO Auto-generated method stub

    }

    @Throws(IOException::class)
    override fun onGiftsList(substring: String) {
        // TODO Auto-generated method stub

    }

    @Throws(IOException::class)
    override fun onHosts(substring: String) {
        // TODO Auto-generated method stub

    }

    @Throws(IOException::class)
    override fun onKey(substring: String) {
        // TODO Auto-generated method stub

    }

    @Throws(IOException::class)
    override fun onLogin(b: Boolean, substring: String) {
        // TODO Auto-generated method stub

    }

    @Throws(IOException::class)
    override fun onMiniClipInfo() {
        // TODO Auto-generated method stub

    }

    @Throws(IOException::class)
    override fun onNewLevel(substring: String) {
        // TODO Auto-generated method stub

    }

    @Throws(IOException::class)
    override fun onNewQueue(substring: String) {
        // TODO Auto-generated method stub

    }

    @Throws(IOException::class)
    override fun onQueue(substring: String) {
        // TODO Auto-generated method stub

    }

    @Throws(IOException::class)
    override fun onRegionalVersion(substring: String) {
        // TODO Auto-generated method stub

    }

    @Throws(IOException::class)
    override fun onRescue(b: Boolean) {
        // TODO Auto-generated method stub

    }

    @Throws(IOException::class)
    override fun onRestrictions(substring: String) {
        // TODO Auto-generated method stub

    }

    @Throws(IOException::class)
    override fun onSecretQuestion(substring: String) {
        // TODO Auto-generated method stub

    }

    @Throws(IOException::class)
    override fun onSelectServer(b: Boolean, c: Boolean, substring: String) {
        // TODO Auto-generated method stub

    }

    @Throws(IOException::class)
    override fun onServersList(b: Boolean, substring: String) {
        // TODO Auto-generated method stub

    }

    @Throws(IOException::class)
    override fun onStats(substring: String) {
        // TODO Auto-generated method stub

    }

    @Throws(IOException::class)
    override fun onTicketResponse(b: Boolean, substring: String) {
        // TODO Auto-generated method stub

    }

}
