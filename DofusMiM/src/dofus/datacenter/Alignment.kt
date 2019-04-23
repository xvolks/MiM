package dofus.datacenter

class Alignment(var index: Int, var value: Int) {

    var isFallenAngelDemon: Boolean = false

    override fun toString(): String {
        var v = ""
        when (index) {
            0 -> v = "Neutre"
            1 -> v = "Ange"
            2 -> v = "Démon"
            6 -> v = "Alignement caché 6"
            else -> v = "Alignement inconnu $index"
        }
        return v + " : " + value + if (isFallenAngelDemon) " déchu" else ""
    }

}
