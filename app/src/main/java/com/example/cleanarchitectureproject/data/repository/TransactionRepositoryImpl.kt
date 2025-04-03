package com.example.cleanarchitectureproject.data.repository

import com.example.cleanarchitectureproject.data.local.portfolio.PortfolioDao
import com.example.cleanarchitectureproject.data.local.portfolio.PortfolioEntity
import com.example.cleanarchitectureproject.data.local.transactions.TransactionsDao
import com.example.cleanarchitectureproject.data.local.transactions.TransactionsEntity
import com.example.cleanarchitectureproject.domain.model.PortfolioCoin
import com.example.cleanarchitectureproject.domain.model.TransactionData
import com.example.cleanarchitectureproject.domain.repository.PortfolioRepository
import com.example.cleanarchitectureproject.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(
    private val dao: TransactionsDao
) : TransactionRepository {

    override fun getAllTransactions(): Flow<List<TransactionData>> {
        return dao.getAllTransactions().map { list ->
            list.map { entity ->
                TransactionData(
                    id = entity.id,
                    coinName = entity.coinName,
                    quantity = entity.quantity,
                    usd = entity.usd,
                    transaction = entity.transaction,
                    date = entity.date,
                    image = entity.image
                )
            }
        }
    }

    override suspend fun insertTransaction(transaction: TransactionData) {
        val entity = TransactionsEntity(
            id = transaction.id,
            coinName = transaction.coinName,
            quantity = transaction.quantity,
            usd = transaction.usd,
            transaction = transaction.transaction,
            date = transaction.date,
            image = transaction.image
        )
        dao.insertTransaction(entity)
    }

    override suspend fun clearAllTransactions() {
        dao.clearAllTransactions()
    }

    override suspend fun deleteTransaction(id: Int) {
        dao.deleteTransaction(id)
    }
}