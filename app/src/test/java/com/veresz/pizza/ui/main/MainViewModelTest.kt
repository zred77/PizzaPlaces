package com.veresz.pizza.ui.main

import com.veresz.pizza.InstantTaskExecutorExtension
import com.veresz.pizza.model.Place
import com.veresz.pizza.repository.place.PlaceRepository
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.*
import org.junit.jupiter.api.TestInstance.*
import org.junit.jupiter.api.extension.*

@ExperimentalCoroutinesApi
@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(MockKExtension::class, InstantTaskExecutorExtension::class)
class MainViewModelTest {

    @MockK
    lateinit var repository: PlaceRepository

    lateinit var subject: MainViewModel

    private val testDispatcher = TestCoroutineDispatcher()

    @BeforeAll
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun `GIVEN no previous selected point WHEN setSelectedPoint() THEN proper pair setted`() = runBlocking {
        val point: Place = mockk()
        subject = MainViewModel(repository)

        subject.setSelectedPoint(point)
        assertEquals(subject.lastAndCurrentSelectedPlace.value, Pair(null, point))
    }

    @Test
    fun `GIVEN has previous selected point WHEN setSelectedPoint() THEN proper pair setted`() = runBlocking {
        val pointOld: Place = mockk()
        val pointNew: Place = mockk()
        subject = MainViewModel(repository)
        subject.lastAndCurrentSelectedPlace.value = Pair(null, pointOld)

        subject.setSelectedPoint(pointNew)
        assertEquals(subject.lastAndCurrentSelectedPlace.value, Pair(pointOld, pointNew))
    }
}
