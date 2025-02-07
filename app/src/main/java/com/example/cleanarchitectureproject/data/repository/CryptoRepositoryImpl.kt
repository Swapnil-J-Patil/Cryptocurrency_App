package com.example.cleanarchitectureproject.data.repository

import com.example.cleanarchitectureproject.data.local.CryptoDao
import com.example.cleanarchitectureproject.data.local.CryptoCoinEntity
import com.example.cleanarchitectureproject.domain.model.CryptoCoin
import com.example.cleanarchitectureproject.domain.repository.CryptoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CryptoRepositoryImpl @Inject constructor(
    private val dao: CryptoDao
) : CryptoRepository {

    override fun getAllCrypto(): Flow<List<CryptoCoin>> {
        return dao.getAllCrypto().map { list ->
            list.map { entity ->
                CryptoCoin(
                    id = entity.id,
                    name = entity.name,
                    symbol = entity.symbol,
                    slug = entity.slug,
                    tags = entity.tags,
                    cmcRank = entity.cmcRank,
                    marketPairCount = entity.marketPairCount,
                    circulatingSupply = entity.circulatingSupply,
                    selfReportedCirculatingSupply = entity.selfReportedCirculatingSupply,
                    totalSupply = entity.totalSupply,
                    maxSupply = entity.maxSupply,
                    isActive = entity.isActive,
                    lastUpdated = entity.lastUpdated,
                    dateAdded = entity.dateAdded,
                    quotes = entity.quotes,
                    isAudited = entity.isAudited,
                    badges = entity.badges ?: emptyList()
                )
            }
        }
    }

    override suspend fun insertCrypto(crypto: CryptoCoin) {
        val entity = CryptoCoinEntity(
            id = crypto.id,
            name = crypto.name,
            symbol = crypto.symbol,
            slug = crypto.slug,
            tags = crypto.tags,
            cmcRank = crypto.cmcRank,
            marketPairCount = crypto.marketPairCount,
            circulatingSupply = crypto.circulatingSupply,
            selfReportedCirculatingSupply = crypto.selfReportedCirculatingSupply,
            totalSupply = crypto.totalSupply,
            maxSupply = crypto.maxSupply,
            isActive = crypto.isActive,
            lastUpdated = crypto.lastUpdated,
            dateAdded = crypto.dateAdded,
            quotes = crypto.quotes,
            isAudited = crypto.isAudited,
            badges = crypto.badges ?: emptyList()
        )
        dao.insertCrypto(entity)
    }

    override suspend fun clearCrypto() {
        dao.clearCrypto()
    }

    override suspend fun deleteCoin(coin: CryptoCoin) {
        val entity = CryptoCoinEntity(
            id = coin.id,
            name = coin.name,
            symbol = coin.symbol,
            slug = coin.slug,
            tags = coin.tags,
            cmcRank = coin.cmcRank,
            marketPairCount = coin.marketPairCount,
            circulatingSupply = coin.circulatingSupply,
            selfReportedCirculatingSupply = coin.selfReportedCirculatingSupply,
            totalSupply = coin.totalSupply,
            maxSupply = coin.maxSupply,
            isActive = coin.isActive,
            lastUpdated = coin.lastUpdated,
            dateAdded = coin.dateAdded,
            quotes = coin.quotes,
            isAudited = coin.isAudited,
            badges = coin.badges ?: emptyList()
        )
        dao.deleteCoin(entity)
    }

    override fun isCoinSaved(coinId: String): Flow<Boolean> {
        return dao.isCoinSaved(coinId)
    }
}
