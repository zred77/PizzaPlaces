package com.veresz.pizza.repository

import com.veresz.pizza.api.PizzaService
import com.veresz.pizza.model.PizzaPlacesList
import com.veresz.pizza.model.PizzaPlacesResponse
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class PizzaRepositoryImplTest {

    @MockK
    lateinit var mockService: PizzaService

    lateinit var subject: PizzaRepository

    @Test
    fun `WHEN getPizzaPlaces() then api called`() = runBlocking {
        coEvery { mockService.getPizzaPlaces() } returns PizzaPlacesResponse(PizzaPlacesList(listOf()))
        subject = PizzaRepositoryImpl(mockService)

        subject.getPizzaPlaces()
        coVerify(exactly = 1) { mockService.getPizzaPlaces() }
    }
}
