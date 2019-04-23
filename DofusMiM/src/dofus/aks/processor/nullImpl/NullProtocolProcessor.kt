package dofus.aks.processor.nullImpl

import java.io.IOException

import dofus.aks.processor.listeners.AccountProcessorListener
import dofus.aks.processor.listeners.BasicsProcessorListener
import dofus.aks.processor.listeners.ChatProcessorListener
import dofus.aks.processor.listeners.ConquestProcessorListener
import dofus.aks.processor.listeners.DialogProcessorListener
import dofus.aks.processor.listeners.DocumentsProcessorListener
import dofus.aks.processor.listeners.EmotesProcessorListener
import dofus.aks.processor.listeners.EnemiesProcessorListener
import dofus.aks.processor.listeners.ExchangeProcessorListener
import dofus.aks.processor.listeners.FightsProcessorListener
import dofus.aks.processor.listeners.FriendsProcessorListener
import dofus.aks.processor.listeners.GameActionsProcessorListener
import dofus.aks.processor.listeners.GameProcessorListener
import dofus.aks.processor.listeners.GuildProcessorListener
import dofus.aks.processor.listeners.HousesProcessorListener
import dofus.aks.processor.listeners.InfosProcessorListener
import dofus.aks.processor.listeners.ItemsProcessorListener
import dofus.aks.processor.listeners.JobProcessorListener
import dofus.aks.processor.listeners.KeysProcessorListener
import dofus.aks.processor.listeners.MountProcessorListener
import dofus.aks.processor.listeners.PartyProcessorListener
import dofus.aks.processor.listeners.ProcessorListener
import dofus.aks.processor.listeners.QuestsProcessorListener
import dofus.aks.processor.listeners.SpecializationProcessorListener
import dofus.aks.processor.listeners.SpellsProcessorListener
import dofus.aks.processor.listeners.StoragesProcessorListener
import dofus.aks.processor.listeners.SubareasProcessorListener
import dofus.aks.processor.listeners.SubwayProcessorListener
import dofus.aks.processor.listeners.TutorialProcessorListener
import dofus.aks.processor.listeners.WaypointsProcessorListener

abstract class NullProtocolProcessor : ProcessorListener {

    override var account: AccountProcessorListener = NullAccountProcessor()
    override var basics: BasicsProcessorListener = NullBasicsProcessor()
    override var chat: ChatProcessorListener = NullChatProcessor()
    override var conquest: ConquestProcessorListener = NullConquestProcessor()
    override var dialog: DialogProcessorListener = NullDialogProcessor()
    override var documents: DocumentsProcessorListener = NullDocumentsProcessor()
    override var emotes: EmotesProcessorListener = NullEmotesProcessor()
    override var enemies: EnemiesProcessorListener = NullEnemiesProcessor()
    override var exchange: ExchangeProcessorListener = NullExchangeProcessor()
    override var fight: FightsProcessorListener = NullFightsProcessor()
    override var friends: FriendsProcessorListener = NullFriendsProcessor()
    override var game: GameProcessorListener = NullGameProcessor()
    override var gameActions: GameActionsProcessorListener = NullGameActionsProcessor()
    override var guild: GuildProcessorListener = NullGuildProcessor()
    override var houses: HousesProcessorListener = NullHousesProcessor()
    override var infos: InfosProcessorListener = NullInfosProcessor()
    override var items: ItemsProcessorListener = NullItemsProcessor()
    override var job: JobProcessorListener = NullJobProcessor()
    override var keys: KeysProcessorListener = NullKeysProcessor()
    override var mount: MountProcessorListener = NullMountProcessor()
    override var party: PartyProcessorListener = NullPartyProcessor()
    override var quests: QuestsProcessorListener = NullQuestProcessor()
    override var specialization: SpecializationProcessorListener = NullSpecializationProcessor()
    override var spells: SpellsProcessorListener = NullSpellsProcessor()
    override var storage: StoragesProcessorListener = NullStoragesProcessor()
    override var subareas: SubareasProcessorListener = NullSubareasProcessor()
    override var subway: SubwayProcessorListener = NullSubwayProcessor()
    override var tutorial: TutorialProcessorListener = NullTutorialProcessor()
    override var waypoints: WaypointsProcessorListener = NullWaypointsProcessor()

    @Throws(IOException::class)
    override fun onPong() {
        // Empty by design
    }

    @Throws(IOException::class)
    override fun onQuickPong() {
        // Empty by design
    }

    override fun onResponsePong(string: String) {
        // Empty by design
    }

}
