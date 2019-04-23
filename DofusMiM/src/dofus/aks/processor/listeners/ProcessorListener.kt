package dofus.aks.processor.listeners

import java.io.IOException

import dofus.aks.network.IO


interface ProcessorListener {

    val basics: BasicsProcessorListener
    val account: AccountProcessorListener
    val game: GameProcessorListener
    val gameActions: GameActionsProcessorListener
    val chat: ChatProcessorListener
    val dialog: DialogProcessorListener
    val infos: InfosProcessorListener
    val items: ItemsProcessorListener
    val spells: SpellsProcessorListener
    val friends: FriendsProcessorListener
    val enemies: EnemiesProcessorListener
    val keys: KeysProcessorListener
    val job: JobProcessorListener
    val exchange: ExchangeProcessorListener
    val houses: HousesProcessorListener
    val storage: StoragesProcessorListener
    val emotes: EmotesProcessorListener
    val documents: DocumentsProcessorListener
    val guild: GuildProcessorListener
    val waypoints: WaypointsProcessorListener
    val subway: SubwayProcessorListener
    val subareas: SubareasProcessorListener
    val conquest: ConquestProcessorListener
    val specialization: SpecializationProcessorListener
    val fight: FightsProcessorListener
    val tutorial: TutorialProcessorListener
    val quests: QuestsProcessorListener
    val party: PartyProcessorListener
    val mount: MountProcessorListener

    val io: IO?

    @Throws(IOException::class)
    fun onHelloConnectionServer(substring: String)

    @Throws(IOException::class)
    fun onHelloGameServer(substring: String)

    @Throws(IOException::class)
    fun disconnect(b: Boolean, c: Boolean)

    @Throws(IOException::class)
    fun onPong()

    /**
     * This method should send **"rpong" + string**
     * <br></br>The default implementation does nothing since Dofus will answer to the rpong request.
     * <br></br>If a pluging eats the rpong request it is its responsibility to answer the rpong request
     * @param string
     */
    fun onResponsePong(string: String)

    @Throws(IOException::class)
    fun onQuickPong()

    @Throws(IOException::class)
    fun onServerMessage(substring: String)

    @Throws(IOException::class)
    fun onServerWillDisconnect()

}
