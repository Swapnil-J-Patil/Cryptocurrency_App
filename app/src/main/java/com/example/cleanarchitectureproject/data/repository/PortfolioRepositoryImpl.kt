package com.example.cleanarchitectureproject.data.repository

import com.example.cleanarchitectureproject.data.local.portfolio.PortfolioDao
import com.example.cleanarchitectureproject.data.local.portfolio.PortfolioEntity
import com.example.cleanarchitectureproject.data.local.saved_coins.CryptoCoinEntity
import com.example.cleanarchitectureproject.data.local.saved_coins.CryptoDao
import com.example.cleanarchitectureproject.domain.model.CryptoCoin
import com.example.cleanarchitectureproject.domain.model.PortfolioCoin
import com.example.cleanarchitectureproject.domain.repository.CryptoRepository
import com.example.cleanarchitectureproject.domain.repository.PortfolioRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PortfolioRepositoryImpl @Inject constructor(
    private val dao: PortfolioDao
) : PortfolioRepository {

    override fun getAllCrypto(): Flow<List<PortfolioCoin>> {
        return dao.getAllCrypto().map { list ->
            list.map { entity ->
                PortfolioCoin(
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
                    badges = entity.badges ?: emptyList(),
                    quantity = entity.quantity
                )
            }
        }
    }

    override suspend fun insertCrypto(crypto: PortfolioCoin) {
        val entity = PortfolioEntity(
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
            badges = crypto.badges ?: emptyList(),
            quantity = crypto.quantity
        )
        dao.insertCrypto(entity)
    }

    override suspend fun clearCrypto() {
        dao.clearCrypto()
    }

    override suspend fun deleteCoin(coin: PortfolioCoin) {
        val entity = PortfolioEntity(
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
            badges = coin.badges ?: emptyList(),
            quantity = coin.quantity
        )
        dao.deleteCoin(entity)
    }

    override suspend fun isCoinSaved(coinId: String): Boolean {
        return dao.isCoinSaved(coinId)
    }

    override fun getCoin(coinId: String): Flow<PortfolioCoin> {
        return dao.getCoin(coinId).map { entity->
            PortfolioCoin(
                id = entity!!.id,
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
                badges = entity.badges ?: emptyList(),
                quantity = entity.quantity
            )
        }
    }
}