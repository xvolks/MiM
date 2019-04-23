package dofus.aks

data class GameServer(val ipAddress: String, val port: Int, val ticket: String? = null)
