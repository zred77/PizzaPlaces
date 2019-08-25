package com.veresz.pizza.repository.friend

import com.veresz.pizza.api.PizzaService
import com.veresz.pizza.db.dao.FriendDao
import com.veresz.pizza.model.Friend
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.*
import org.junit.jupiter.api.extension.*

@ExtendWith(MockKExtension::class)
class FriendRepositoryImplTest {

    @MockK
    lateinit var mockService: PizzaService

    @MockK
    lateinit var friendDao: FriendDao

    private lateinit var subject: FriendRepository

    private val friendList: List<Friend> = listOf(mockk())

    @BeforeEach
    fun beforeEach() {
        clearAllMocks()
        coEvery { friendDao.deleteAll() } returns Unit
    }

    @Test
    fun `GIVEN empty database WHEN getFriends() THEN api called`() = runBlocking {
        coEvery { mockService.getFriends() } returns listOf()
        coEvery { friendDao.insert() } returns Unit
        coEvery { friendDao.getAll() } returns listOf()

        subject = FriendRepositoryImpl(mockService, friendDao)

        subject.getFriends(false)
        coVerify(exactly = 1) { mockService.getFriends() }
    }

    @Test
    fun `GIVEN not empty database and forced refresh WHEN getFriends() THEN api called`() = runBlocking {
        coEvery { mockService.getFriends() } returns listOf()
        coEvery { friendDao.insert() } returns Unit
        coEvery { friendDao.getAll() } returns friendList

        subject = FriendRepositoryImpl(mockService, friendDao)

        subject.getFriends(true)
        coVerify(exactly = 1) { mockService.getFriends() }
    }

    @Test
    fun `GIVEN not empty database WHEN getFriends() THEN api not called`() = runBlocking {
        coEvery { mockService.getFriends() } returns listOf()
        coEvery { friendDao.insert(*friendList.toTypedArray()) } returns Unit
        coEvery { friendDao.getAll() } returns friendList

        subject = FriendRepositoryImpl(mockService, friendDao)

        subject.getFriends(false)
        coVerify(exactly = 0) { mockService.getFriends() }
    }

    @Test
    fun `GIVEN empty database WHEN getFriends() THEN api response stored`() = runBlocking {
        coEvery { mockService.getFriends() } returns friendList
        coEvery { friendDao.insert(*friendList.toTypedArray()) } returns Unit
        coEvery { friendDao.getAll() } returns listOf()

        subject = FriendRepositoryImpl(mockService, friendDao)

        subject.getFriends(false)
        coVerify(exactly = 1) { friendDao.deleteAll() }
        coVerify(exactly = 1) { friendDao.insert(*friendList.toTypedArray()) }
    }
}
