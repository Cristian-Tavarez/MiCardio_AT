package com.example.micardioat.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.micardioat.data.dao.UsuarioDao
import com.example.micardioat.data.entity.UsuarioEntity

@Database(entities = [UsuarioEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun usuarioDao(): UsuarioDao
}