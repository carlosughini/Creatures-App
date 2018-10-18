package com.raywenderlich.android.creaturemon.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.persistence.room.Room
import com.raywenderlich.android.creaturemon.model.*
import com.raywenderlich.android.creaturemon.model.room.RoomRepository
import org.w3c.dom.Attr
import java.text.FieldPosition
import kotlin.math.ceil

/**
 * Classe viewModel para a view de adicionar uma nova criatura.
 * É passada uma CreatureGenerator como parâmetro e um valor default.
 */
class CreatureViewModel(private val generator: CreatureGenerator = CreatureGenerator(),
                        private val repository: CreatureRepository
                        = RoomRepository()) : ViewModel() {

    /**
     * Cria uma criatura.
     * A ViewModel vai usar LiveData para enviar informações das criaturas geradas para a view layer.
     * MutableLiveData permite os métodos post e set.
     * Método POST é usado em uma background thread, mas é dada preferência para o SET.
     * SET é usado na main thread.
     * Usaremos o POST, pois enviaremos a criatura da ViewModel para a View.
     */
    private val creatureLiveData = MutableLiveData<Creature>()

    private val saveLiveData = MutableLiveData<Boolean>()

    /**
     * Função que permite buscar a criatura gerada.
     */
    fun getCreatureLiveData(): LiveData<Creature> = creatureLiveData

    fun getSaveLiveData(): LiveData<Boolean> = saveLiveData

    /**
     * A ViewModel precisa acompanhar as informações das criatruas que estão sendo criadas.
     */
    var name = ""
    var inteliggence = 0
    var strength = 0
    var endurance = 0
    var drawable = 0

    // Não inicializa no momento a criatura que está sendo criada
    lateinit var creature: Creature

    /**
     * Atualiza a criatura com os valores obtidos na View.
     */
    fun updateCreature() {
        val attributes = CreatureAttributes(inteliggence, strength, endurance)
        creature = generator.generateCreature(attributes, name, drawable)
        creatureLiveData.postValue(creature)
    }

    /**
     * Método que a ViewLayer vai chamar quando o usuário escolher um valor na lista
     * de atributos (Spinner).
     * Ele determina o valor pela posição que o usuário escolher na lista das opções.
     */
    fun attributeSelected(attributeType: AttributeType, position: Int) {
        when (attributeType) {
            AttributeType.INTELLIGENCE ->
                inteliggence = AttributeStore.INTELLIGENCE[position].value
            AttributeType.STRENGTH ->
                strength = AttributeStore.STRENGTH[position].value
            AttributeType.ENDURANCE ->
                endurance = AttributeStore.ENDURANCE[position].value
        }

        /**
         * Passa a nova criatura para a view com os atributos selecionads.
         */
        updateCreature()
    }

    // Função que atualiza o avatar da criatura quando ele for selecionado.
    fun drawableSelected(drawable: Int) {
        this.drawable = drawable
        updateCreature()
    }

    fun saveCreature() {
        return if (canSaveCreature()) {
            repository.saveCreature(creature)
            saveLiveData.postValue(true)
        } else {
            saveLiveData.postValue(false)
        }
    }

    // Função que verifica se existem valores que são 0 ou em branco
    fun canSaveCreature(): Boolean {
        return inteliggence != 0 && strength != 0 && endurance != 0 &&
                name.isNotEmpty() && drawable != 0
    }

}