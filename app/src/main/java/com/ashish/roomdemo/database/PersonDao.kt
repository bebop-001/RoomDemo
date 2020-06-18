package com.ashish.roomdemo.database

import androidx.room.*
import com.ashish.roomdemo.model.Person

@Dao
interface PersonDao {
    @Query("SELECT * FROM PERSON ORDER BY ID")
    fun loadAllPersons(): List<Person>

    @Insert
    fun insertPerson(person: Person)

    @Update
    fun updatePerson(person: Person)

    @Delete
    fun delete(person: Person)

    @Query("SELECT * FROM PERSON WHERE id = :id")
    fun loadPersonById(id: Int): Person
}