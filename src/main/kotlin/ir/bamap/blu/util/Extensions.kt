package ir.bamap.blu.util

import java.util.function.BiFunction

fun <T> MutableCollection<T>.removeAndReturnIf(filter: (T) -> Boolean): List<T> {
    val removedEntries = mutableListOf<T>()
    val iterator = this.iterator()
    while (iterator.hasNext()) {
        val item = iterator.next()
        if (filter(item)) {
            iterator.remove()
            removedEntries.add(item)
        }
    }

    return removedEntries
}

fun <T> MutableCollection<T>.reset(element: T) {
    this.clear()
    this.add(element)
}

fun <T> MutableCollection<T>.reset(elements: Collection<T>) {
    this.clear()
    this.addAll(elements)
}

fun <T: MutableMap<K, V>, K, V : Any> T.merge(otherMap: Map<K, V>, remappingFunction: BiFunction<in V, in V, out V?>) {
    otherMap.forEach { (key, value) ->
        this.merge(key, value, remappingFunction)
    }
}
