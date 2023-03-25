package org.jukeboxmc.util

import java.util.Optional
import java.util.function.Function
import java.util.function.IntFunction
import java.util.function.Predicate

/**
 * @author KCodeYT
 * @version 1.0
 */
class NonStream private constructor() {
    init {
        throw UnsupportedOperationException()
    }

    interface CollectionFactory<C : MutableCollection<R>?, R> {
        fun create(): C
    }

    interface MapFactory<K, V> {
        fun create(): MutableMap<K, V>
    }

    companion object {
        fun <E> noneMatch(iterable: Iterable<E>, predicate: Predicate<E>): Boolean {
            for (e in iterable) if (predicate.test(e)) return false
            return true
        }

        fun <E> singleMatch(iterable: Iterable<E>, predicate: Predicate<E>): Boolean {
            for (e in iterable) if (predicate.test(e)) return true
            return false
        }

        fun <E> allMatch(iterable: Iterable<E>, predicate: Predicate<E>): Boolean {
            for (e in iterable) if (!predicate.test(e)) return false
            return true
        }

        fun <E : Any> filterOptional(iterable: Iterable<E>, predicate: Predicate<E>): Optional<E> {
            for (e in iterable) if (predicate.test(e)) return Optional.of(e)
            return Optional.empty()
        }

        fun <E> filter(iterable: Iterable<E>, predicate: Predicate<E>): E? {
            for (e in iterable) if (predicate.test(e)) return e
            return null
        }

        fun <E> filterList(iterable: Iterable<E>, predicate: Predicate<E>): List<E> {
            val list: MutableList<E> = mutableListOf()
            for (e in iterable) if (predicate.test(e)) list.add(e)
            return list
        }

        fun <E, C : MutableCollection<R>, R> filterAndMap(
            array: Array<E>,
            predicate: Predicate<E>,
            mapper: Function<E, R>,
            factory: CollectionFactory<C, R>,
        ): C {
            val collection = factory.create()
            for (e in array) if (predicate.test(e)) collection.add(mapper.apply(e))
            return collection
        }

        fun <E : Any> findFirst(iterable: Iterable<E>): Optional<E> {
            for (e in iterable) return Optional.of(e)
            return Optional.empty()
        }

        fun <E> sum(array: Array<E>, mapper: Function<E, Int>): Int {
            var sum = 0
            for (e in array) sum += mapper.apply(e)
            return sum
        }

        fun <E, C : MutableCollection<R>, R> map(
            array: Array<E>,
            mapper: Function<E, R>,
            factory: CollectionFactory<C, R>,
        ): C {
            val collection = factory.create()
            for (e in array) collection.add(mapper.apply(e))
            return collection
        }

        fun <E, C : MutableCollection<R>, R> map(
            iterable: Iterable<E>,
            mapper: Function<E, R>,
            factory: CollectionFactory<C, R>,
        ): C {
            val collection = factory.create()
            for (e in iterable) collection.add(mapper.apply(e))
            return collection
        }

        fun <E, C : MutableCollection<R>, UC : Collection<R>?, R> map(
            iterable: Iterable<E>,
            mapper: Function<E, R>,
            factory: CollectionFactory<C, R>,
            unmodifiableFunc: Function<C, UC>,
        ): UC {
            val collection = factory.create()
            for (e in iterable) collection.add(mapper.apply(e))
            return unmodifiableFunc.apply(collection)
        }

        fun <E, R> mapArray(iterable: Iterable<E>, mapper: Function<E, R>, function: IntFunction<Array<R>>): Array<R> {
            var counter = 0
            for (e in iterable) counter++
            val array = function.apply(counter)
            var i = 0
            for (e in iterable) array[i++] = mapper.apply(e)
            return array
        }

        fun <E, K, V> toMap(
            iterable: Iterable<E>,
            keyMapper: Function<E, K>,
            valueMapper: Function<E, V>,
            mapFactory: MapFactory<K, V>,
        ): Map<K, V> {
            val map = mapFactory.create()
            for (e in iterable) map[keyMapper.apply(e)] = valueMapper.apply(e)
            return map
        }
    }
}
