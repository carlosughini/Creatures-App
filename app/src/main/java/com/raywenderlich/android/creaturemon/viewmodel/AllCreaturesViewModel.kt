package com.raywenderlich.android.creaturemon.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.raywenderlich.android.creaturemon.model.Creature
import com.raywenderlich.android.creaturemon.model.CreatureRepository
import com.raywenderlich.android.creaturemon.model.room.RoomRepository

class AllCreaturesViewModel(private val repository: CreatureRepository = RoomRepository())
    : ViewModel() {

    /**
     * Cria uma lista com todas criaturas a partir de uma busca no repositório.
     */
    private val allCreatures = repository.getAllCreatures()

    /**
     * Função para a View buscar todas as criaturas.
     */
    fun getAllCreaturesLiveData() = allCreatures

    /**
     * Função que limpa todas crituras do banco de dados.
     */
    fun clearAllCreatures() = repository.clearAllCreatures()

}