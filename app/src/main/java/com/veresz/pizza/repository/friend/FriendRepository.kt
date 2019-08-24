package com.veresz.pizza.repository.friend

import com.veresz.pizza.model.Friend

interface FriendRepository {

    suspend fun getFriends(refresh: Boolean = false): List<Friend>
}
