package org.pandas.bambooclub.domain.chat.repository

import org.pandas.bambooclub.domain.chat.model.Chat
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface ChatRepository : MongoRepository<Chat, String>
