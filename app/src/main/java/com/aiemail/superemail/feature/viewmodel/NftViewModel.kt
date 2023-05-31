/*
 * Created by Inuwa Ibrahim on 17/03/2022, 7:43 PM
 *     https://linktr.ee/Ibrajix
 *     Copyright (c) 2022.
 *     All rights reserved.
 */

package com.aiemail.superemail.feature.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ibrajix.nftapp.data.NftData
import com.ibrajix.nftapp.network.Resource
import com.ibrajix.nftapp.utilis.Constants.FEATURED_IMAGE
import com.ibrajix.nftapp.utilis.Constants.FEATURED_IMAGE_TITLE
import com.aiemail.superemail.data.NftRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

//@HiltViewModel
class NftViewModel @Inject constructor(private val nftRepository: NftRepository) : ViewModel() {

    private val _nft = MutableStateFlow<Resource<List<NftData>>>(Resource.Loading)
    val nft: StateFlow<Resource<List<NftData>>> get() = _nft

    init {
        getNft()
    }

    private fun getNft() = viewModelScope.launch {

        _nft.emit(Resource.Loading)

        val topNftDeferred = async { nftRepository.getTopNft() }
        val trendingNftDeferred = async { nftRepository.getTrendingNft() }

        val topNft = topNftDeferred.await()
        val trendingNft = trendingNftDeferred.await()

        val nftList = mutableListOf<NftData>()

        if(topNft is Resource.Success && trendingNft is Resource.Success){
          //  nftList.add(NftData.Title(1, "Featured", ""))
           // nftList.add(NftData.Featured(FEATURED_IMAGE, FEATURED_IMAGE_TITLE))
            nftList.add(NftData.Title(2, "Personal", "View all"))
            nftList.addAll(topNft.value)
            nftList.add(NftData.Title(2, "Notification", "Show all"))
            nftList.addAll(trendingNft.value)
            _nft.emit(Resource.Success(nftList))
        }else{
            Resource.Failure(false, null, null)
        }

    }

}