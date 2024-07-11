package com.example.business.account

import com.example.plugins.City
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import java.sql.Connection
import java.sql.Statement


@Serializable
data class Account(
    val name: String,
    val password: String
)

class AccountService(private val connection: Connection) {

    companion object {
        private const val CREATE_TABLE_ACCOUTNS =
            "CREATE TABLE accounts (ID SERIAL PRIMARY KEY, NAME VARCHAR(255), PASSWORD VARCHAR(255));"
        private const val INSERT_ACCOUNT =
            "INSERT INTO  (name, password) VALUES (?, ?)"
        private const val SELECT_ACCOUNT_BY_NAME =
            "SELECT * FROM accounts WHERE NAME=?;"
        private const val UPDATE_ACCOUNT =
            "UPDATE accounts SET name = ?, password = ? WHERE id = ?"
        private const val DELETE_ACCOUNT =
            "DELETE FROM accounts WHERE id = ?"
    }

    init {
        val statment = connection.createStatement()
        statment.executeUpdate(CREATE_TABLE_ACCOUTNS)
    }

    suspend fun create(account: Account): Int = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(INSERT_ACCOUNT, Statement.RETURN_GENERATED_KEYS)
        statement.setString(1, account.name)
        statement.setString(2, account.password)
        statement.executeUpdate()
        val generatedKeys = statement.generatedKeys
        if (generatedKeys.next()) {
            return@withContext generatedKeys.getInt(1)
        } else {
            throw Exception("Unable to retrieve the id of the newly inserted account")
        }
    }

    suspend fun read(name: String): Account? = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(SELECT_ACCOUNT_BY_NAME)
        statement.setString(1, name)
        val resultSet = statement.executeQuery()

        if (resultSet.next()) {
            return@withContext Account(
                resultSet.getString("name"),
                resultSet.getString("password")
            )
        } else {
            return@withContext null
        }
    }

    suspend fun update(id: Int, account: Account) = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(UPDATE_ACCOUNT)
        statement.setString(1, account.name)
        statement.setString(2, account.password)
        statement.setInt(3, id)
        statement.executeUpdate()
    }

    suspend fun delete(id: Int) = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(DELETE_ACCOUNT)
        statement.setInt(1, id)
        statement.executeUpdate()
    }
}

