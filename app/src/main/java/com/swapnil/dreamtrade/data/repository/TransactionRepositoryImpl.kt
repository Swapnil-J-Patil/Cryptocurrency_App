package com.swapnil.dreamtrade.data.repository

import com.swapnil.dreamtrade.data.local.transactions.TransactionsDao
import com.swapnil.dreamtrade.data.local.transactions.TransactionsEntity
import com.swapnil.dreamtrade.domain.model.TransactionData
import com.swapnil.dreamtrade.domain.repository.TransactionRepository
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
            coinName = transaction.coinName,
            quantity = transaction.quantity,
            usd = transaction.usd?:0.0,
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