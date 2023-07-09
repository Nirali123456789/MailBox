/*
 * Created by Inuwa Ibrahim on 17/03/2022, 7:43 PM
 *     https://linktr.ee/Ibrajix
 *     Copyright (c) 2022.
 *     All rights reserved.
 */

package com.ibrajix.nftapp.utilis

object Constants {
    public fun <K, V> reverseOrderOfKeys(originalMap: HashMap<K, V>): LinkedHashMap<K, V> {
        val reversedMap = LinkedHashMap<K, V>()

        val keysIterator = originalMap.keys.iterator()
        while (keysIterator.hasNext()) {
            val key = keysIterator.next()
            reversedMap[key] = originalMap[key]!!
        }

        return reversedMap
    }

    const val SPLASH_SCREEN_TIME = 2000L
    const val FEATURED_IMAGE = "https://i.ibb.co/xCVtp2L/nft1.jpg"
    const val FEATURED_IMAGE_TITLE = "Bored Ape"
}