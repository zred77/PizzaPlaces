package com.veresz.pizza.repository.friend

import com.veresz.pizza.api.PizzaService
import com.veresz.pizza.db.dao.FriendDao
import com.veresz.pizza.model.Friend
import javax.inject.Inject

class FriendRepositoryImpl @Inject constructor(
    private val service: PizzaService,
    private val friendDao: FriendDao
) : FriendRepository {

    override suspend fun getFriends(refresh: Boolean): List<Friend> {
        var friends = friendDao.getAll()
        if (refresh || friends.isEmpty()) {
            friends = service.getFriends()
            friendDao.deleteAll()
            friendDao.insert(*friends.toTypedArray())
        }
        return friends
    }
}
