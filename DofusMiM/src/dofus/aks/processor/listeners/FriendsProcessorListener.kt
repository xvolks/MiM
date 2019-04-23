package dofus.aks.processor.listeners

interface FriendsProcessorListener {

    fun onAddFriend(b: Boolean, substring: String)

    fun onFriendsList(substring: String)

    fun onNotifyChange(substring: String)

    fun onRemoveFriend(b: Boolean, substring: String)

    fun onSpouse(substring: String)

}
