package com.raywenderlich.android.creaturemon.viewmodel

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.raywenderlich.android.creaturemon.model.Creature
import com.raywenderlich.android.creaturemon.model.CreatureAttributes
import com.raywenderlich.android.creaturemon.model.CreatureGenerator
import com.raywenderlich.android.creaturemon.model.CreatureRepository
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class CreatureViewModelTest {

    // Instancia viewModel que vai ser testada
    private lateinit var creatureViewModel: CreatureViewModel

    /**
     * Como usamos LiveData, precisamos de uma regra para alterar a background thread
     * normalmente usada por uma synchronous thread executor.
     */
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    // Cria um objeto simulado de creatura com o CreatureGenerator.
    @Mock
    lateinit var mockGenerator: CreatureGenerator

    @Mock
    lateinit var repository: CreatureRepository

    // Inicializa o objeto simulado criado no @Mock
    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        creatureViewModel = CreatureViewModel(mockGenerator, repository)
    }

    @Test
    fun testSetupCreature() {
        val attributes = CreatureAttributes(10, 3, 7)
        val stubCreature = Creature(attributes, 87, "Test Creature")
        `when` (mockGenerator.generateCreature(attributes)).thenReturn(stubCreature)

        creatureViewModel.inteliggence = 10
        creatureViewModel.strength = 3
        creatureViewModel.endurance = 7

        creatureViewModel.updateCreature()

        assertEquals(stubCreature, creatureViewModel.creature)
    }

    @Test
    fun testCantSaveCreaturesWithBlankName() {
        creatureViewModel.inteliggence = 10
        creatureViewModel.strength = 3
        creatureViewModel.endurance = 7
        creatureViewModel.drawable = 1
        creatureViewModel.name = ""
        val canSaveCriature = creatureViewModel.canSaveCreature()
        assertEquals(false, canSaveCriature)
    }

    @Test
    fun testCantSaveCreaturesWithoutStrength() {
        creatureViewModel.inteliggence = 10
        creatureViewModel.strength = 0
        creatureViewModel.endurance = 7
        creatureViewModel.drawable = 1
        creatureViewModel.name = "Testing"
        val canSaveCriature = creatureViewModel.canSaveCreature()
        assertEquals(false, canSaveCriature)

    }

}