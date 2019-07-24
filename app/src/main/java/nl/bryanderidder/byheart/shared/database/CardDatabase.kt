package nl.bryanderidder.byheart.shared.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import nl.bryanderidder.byheart.card.Card
import nl.bryanderidder.byheart.card.CardDao
import nl.bryanderidder.byheart.pile.Pile
import nl.bryanderidder.byheart.pile.PileDao
import nl.bryanderidder.byheart.shared.Preferences
import nl.bryanderidder.byheart.shared.Preferences.KEY_NOT_FIRST_START
import nl.bryanderidder.byheart.shared.Preferences.KEY_REHEARSAL_MEMORY

/**
 * Room database class.
 * @author Bryan de Ridder
 */
@Database(entities = [Card::class, Pile::class], version = 2, exportSchema = true)
abstract class CardDatabase : RoomDatabase() {
    abstract fun cardDao(): CardDao
    abstract fun pileDao(): PileDao

    companion object {
        @Volatile
        private var INSTANCE: CardDatabase? = null

        //Singleton
        fun getDatabase(context: Context, scope: CoroutineScope): CardDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room
                    .databaseBuilder(context.applicationContext, CardDatabase::class.java, "card")
                    .addCallback(CardDatabaseCallback(scope))
                    .addMigrations(DatabaseMigrations.MIGRATION_1_2)
                    .build()
                INSTANCE = instance
                return instance
            }
        }

        private class CardDatabaseCallback(private val scope: CoroutineScope) : RoomDatabase.Callback() {
            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                if (!isFirstStart()) return
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateDatabase(database.cardDao(), database.pileDao())
                    }
                }
            }
        }

        // Check first start
        private fun isFirstStart(): Boolean {
            if (!Preferences.NOT_FIRST_START) {
                Preferences.write(KEY_NOT_FIRST_START, true)
                Preferences.write(KEY_REHEARSAL_MEMORY, true)
                return true
            }
            return false
        }

        fun populateDatabase(cardDao: CardDao, pileDao: PileDao) {
            cardDao.deleteAll()
            pileDao.deleteAll()

            val english = "en-GB"

            //French
            val pile = Pile("French")
            pile.id = 1
            pile.languageCardFront = english
            pile.languageCardBack = "fr-FR"
            pileDao.insert(pile)
            cardDao.insert(Card( "Hello", "Bonjour", 1))
            cardDao.insert(Card( "Have", "Avoir", 1))
            cardDao.insert(Card( "Fun", "Amusement", 1))
            cardDao.insert(Card( "Using", "Utiliser", 1))
            cardDao.insert(Card( "This", "Cette", 1))
            cardDao.insert(Card( "App", "Application", 1))

            //Capitals
            val pile2 = Pile("Capitals")
            pile2.id = 2
            pile2.languageCardFront = english
            pile2.languageCardBack = english
            pileDao.insert(pile2)
            cardDao.insert(Card( "France", "Paris", 2))
            cardDao.insert(Card( "Germany", "Berlin", 2))
            cardDao.insert(Card( "Russia", "Moscow", 2))
            cardDao.insert(Card( "Turkye", "Istanbul", 2))
            cardDao.insert(Card( "United Kingdom", "London", 2))
            cardDao.insert(Card( "Brazil", "Brasilia", 2))
            cardDao.insert(Card( "Indonesia", "Jakarta", 2))
            cardDao.insert(Card( "Japan", "Tokyo", 2))
            cardDao.insert(Card( "Libya", "Tripoli", 2))

            //Planets
            val pile3 = Pile("Planets")
            pile3.id = 3
            pile3.languageCardFront = english
            pile3.languageCardBack = english
            pileDao.insert(pile3)
            cardDao.insert(Card( "1st Planet", "Mercury", 3))
            cardDao.insert(Card( "2nd Planet", "Venus", 3))
            cardDao.insert(Card( "3rd Planet", "Earth", 3))
            cardDao.insert(Card( "4th Planet", "Mars", 3))
            cardDao.insert(Card( "5th Planet", "Jupiter", 3))
            cardDao.insert(Card( "6th Planet", "Saturn", 3))
            cardDao.insert(Card( "7th Planet", "Uranus", 3))
            cardDao.insert(Card( "8th Planet", "Neptune", 3))


            //Planets
            val pile4 = Pile("Periodic Table")
            pile4.id = 4
            pile4.languageCardFront = english
            pile4.languageCardBack = english
            pileDao.insert(pile4)
            cardDao.insert(Card( "H", "Hydrogen", 4))
            cardDao.insert(Card( "He", "Helium", 4))
            cardDao.insert(Card( "Li", "Lythium", 4))
            cardDao.insert(Card( "Be", "Beryllium", 4))
            cardDao.insert(Card( "B", "Boron", 4))
            cardDao.insert(Card( "C", "Carbon", 4))
            cardDao.insert(Card( "N", "Nitrogen", 4))
            cardDao.insert(Card( "O", "Oxygen", 4))
            cardDao.insert(Card( "F", "Fluorine", 4))
            cardDao.insert(Card( "Ne", "Neon", 4))
            cardDao.insert(Card( "Na", "Sodium", 4))
            cardDao.insert(Card( "Mg", "Magnesium", 4))
            cardDao.insert(Card( "Al", "Aluminium", 4))
            cardDao.insert(Card( "Si", "Silicon", 4))
            cardDao.insert(Card( "P", "Phosphorus", 4))
            cardDao.insert(Card( "S", "Sulfur", 4))
            cardDao.insert(Card( "Ar", "Argon", 4))
            cardDao.insert(Card( "K", "Potassium", 4))
            cardDao.insert(Card( "Ca", "Calcium", 4))
            cardDao.insert(Card( "Sc", "Scandium", 4))
            cardDao.insert(Card( "Ti", "Titanium", 4))
            cardDao.insert(Card( "V", "Vanadium", 4))
            cardDao.insert(Card( "Cr", "Chromium", 4))
            cardDao.insert(Card( "Mn", "Manganese", 4))
            cardDao.insert(Card( "Fe", "Iron", 4))
            cardDao.insert(Card( "Co", "Cobalt", 4))
            cardDao.insert(Card( "Ni", "Nickel", 4))
            cardDao.insert(Card( "Cu", "Copper", 4))
            cardDao.insert(Card( "Zn", "Zinc", 4))
            cardDao.insert(Card( "Ga", "Gallium", 4))
            cardDao.insert(Card( "Ge", "Germanium", 4))
            cardDao.insert(Card( "As", "Arsenic", 4))
            cardDao.insert(Card( "Se", "Selenium", 4))
            cardDao.insert(Card( "Br", "Bromine", 4))
            cardDao.insert(Card( "Kr", "Krypton", 4))
            cardDao.insert(Card( "Rb", "Rubidium", 4))
            cardDao.insert(Card( "Sr", "Strontium", 4))
            cardDao.insert(Card( "Y", "Yttrium", 4))
            cardDao.insert(Card( "Zr", "Zirconium", 4))
            cardDao.insert(Card( "Nb", "Niobium", 4))
            cardDao.insert(Card( "Mo", "Molybdenum", 4))
            cardDao.insert(Card( "Tc", "Technetium", 4))
            cardDao.insert(Card( "Ru", "Ruthenium", 4))
            cardDao.insert(Card( "Rh", "Rhodium", 4))
            cardDao.insert(Card( "Pd", "Palladium", 4))
            cardDao.insert(Card( "Ag", "Silver", 4))
            cardDao.insert(Card( "Cd", "Cadmium", 4))
            cardDao.insert(Card( "In", "Indium", 4))
            cardDao.insert(Card( "Sn", "Tin", 4))
            cardDao.insert(Card( "Te", "Tellurium", 4))
            cardDao.insert(Card( "I", "Iodine", 4))
            cardDao.insert(Card( "Xe", "Xenon", 4))
            cardDao.insert(Card( "Cs", "Caesium", 4))
            cardDao.insert(Card( "Ba", "Barium", 4))
            cardDao.insert(Card( "Lu", "Lutetium", 4))
            cardDao.insert(Card( "Hf", "Hafnium", 4))
            cardDao.insert(Card( "Ta", "Tantalum", 4))
            cardDao.insert(Card( "W", "Tungsten", 4))
            cardDao.insert(Card( "Re", "Rhenium", 4))
            cardDao.insert(Card( "Os", "Osmium", 4))
            cardDao.insert(Card( "Ir", "Iridium", 4))
            cardDao.insert(Card( "Pt", "Platinum", 4))
            cardDao.insert(Card( "Au", "Gold", 4))
            cardDao.insert(Card( "Hg", "Mercury", 4))
            cardDao.insert(Card( "Tl", "Thallium", 4))
            cardDao.insert(Card( "Pb", "Lead", 4))
            cardDao.insert(Card( "Bi", "Bismuth", 4))
            cardDao.insert(Card( "Po", "Polonium", 4))
            cardDao.insert(Card( "At", "Astatine", 4))
            cardDao.insert(Card( "Rn", "Radon", 4))
            cardDao.insert(Card( "Fr", "Francium", 4))
            cardDao.insert(Card( "Ra", "Radium", 4))
            cardDao.insert(Card( "Lr", "Lawrencium", 4))
            cardDao.insert(Card( "Rf", "Rutherfordium", 4))
            cardDao.insert(Card( "Db", "Dubnium", 4))
            cardDao.insert(Card( "Sg", "Seaborgium", 4))
            cardDao.insert(Card( "Bh", "Bohrium", 4))
            cardDao.insert(Card( "Hs", "Hassium", 4))
            cardDao.insert(Card( "Mt", "Meitnerium", 4))
            cardDao.insert(Card( "Ds", "Darmstadtium", 4))
            cardDao.insert(Card( "Cn", "Copernicium", 4))
            cardDao.insert(Card( "Nh", "Nihonium", 4))
            cardDao.insert(Card( "Fl", "Flerovium", 4))
            cardDao.insert(Card( "Mc", "Moscovium", 4))
            cardDao.insert(Card( "Lv", "Livermorium", 4))
            cardDao.insert(Card( "Og", "Oganesson", 4))
        }
    }
}