package com.veresz.pizza.repository.place

import com.veresz.pizza.api.PizzaService
import com.veresz.pizza.db.dao.PlaceDao
import com.veresz.pizza.model.PizzaPlacesList
import com.veresz.pizza.model.PizzaPlacesResponse
import com.veresz.pizza.model.Place
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
class PlaceRepositoryImplTest {

    @MockK
    lateinit var mockService: PizzaService

    @MockK
    lateinit var placeDao: PlaceDao

    private lateinit var subject: PlaceRepository

    private val placeList: List<Place> = listOf(mockk())

    @BeforeEach
    fun beforeEach() {
        clearAllMocks()
        coEvery { placeDao.deleteAll() } returns Unit
    }

    @Test
    fun `GIVEN empty database WHEN getPizzaPlaces() THEN api called`() = runBlocking {
        val reponse = PizzaPlacesResponse(PizzaPlacesList(listOf()))

        coEvery { mockService.getPizzaPlaces() } returns reponse
        coEvery { placeDao.insert() } returns Unit
        coEvery { placeDao.getAll() } returns listOf()

        subject = PlaceRepositoryImpl(mockService, placeDao)

        subject.getPizzaPlaces(false)
        coVerify(exactly = 1) { mockService.getPizzaPlaces() }
    }

    @Test
    fun `GIVEN not empty database and forced refresh WHEN getPizzaPlaces() THEN api called`() = runBlocking {
        val reponse = PizzaPlacesResponse(PizzaPlacesList(listOf()))

        coEvery { mockService.getPizzaPlaces() } returns reponse
        coEvery { placeDao.insert() } returns Unit
        coEvery { placeDao.getAll() } returns placeList

        subject = PlaceRepositoryImpl(mockService, placeDao)

        subject.getPizzaPlaces(true)
        coVerify(exactly = 1) { mockService.getPizzaPlaces() }
    }

    @Test
    fun `GIVEN not empty database WHEN getPizzaPlaces() THEN api not called`() = runBlocking {
        val reponse = PizzaPlacesResponse(PizzaPlacesList(listOf()))

        coEvery { mockService.getPizzaPlaces() } returns reponse
        coEvery { placeDao.insert(*placeList.toTypedArray()) } returns Unit
        coEvery { placeDao.getAll() } returns placeList

        subject = PlaceRepositoryImpl(mockService, placeDao)

        subject.getPizzaPlaces(false)
        coVerify(exactly = 0) { mockService.getPizzaPlaces() }
    }

    @Test
    fun `GIVEN empty database WHEN getPizzaPlaces() THEN api response stored`() = runBlocking {
        val reponse = PizzaPlacesResponse(PizzaPlacesList(listOf()))

        coEvery { mockService.getPizzaPlaces() } returns reponse
        coEvery { placeDao.insert(*reponse.list.places.toTypedArray()) } returns Unit
        coEvery { placeDao.getAll() } returns listOf()

        subject = PlaceRepositoryImpl(mockService, placeDao)

        subject.getPizzaPlaces(false)
        coVerify(exactly = 1) { placeDao.deleteAll() }
        coVerify(exactly = 1) { placeDao.insert(*reponse.list.places.toTypedArray()) }
    }
}
