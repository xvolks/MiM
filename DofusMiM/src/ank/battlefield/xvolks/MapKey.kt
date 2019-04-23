package ank.battlefield.xvolks

class MapKey(val id: String?, val date: String?) : Comparable<MapKey> {

    override fun hashCode(): Int {
        val prime = 31
        var result = 1
        result = prime * result + (date?.hashCode() ?: 0)
        result = prime * result + (id?.hashCode() ?: 0)
        return result
    }

    override fun equals(obj: Any?): Boolean {
        if (this === obj)
            return true
        if (obj == null)
            return false
        if (javaClass != obj.javaClass)
            return false
        val other = obj as MapKey?
        if (date == null) {
            if (other!!.date != null)
                return false
        } else if (date != other!!.date)
            return false
        if (id == null) {
            if (other.id != null)
                return false
        } else if (id != other.id)
            return false
        return true
    }

    override fun compareTo(o: MapKey): Int {
        return (id!! + date!!).compareTo(o.id!! + o.date!!)
    }

}
