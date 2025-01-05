package org.pandas.bambooclub.domain.user.repository

import org.pandas.bambooclub.domain.user.model.User
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : MongoRepository<User, String> {
    fun findByUserId(userId: String): User?
}
