package nl.bryanderidder.byheart.shared.database

import nl.bryanderidder.byheart.card.Card
import nl.bryanderidder.byheart.pile.Pile
import java.util.*

object InitialData {

    fun getFrench(color: Int): Pile {
        val langIsNL = Locale.getDefault().language == "nl"
        var cardIndex = 0
        val pile = if (langIsNL) Pile("Frans") else Pile("French")
        pile.id = 1
        pile.languageCardFront = if (langIsNL) "nl-NL" else "en-GB"
        pile.languageCardBack = "fr-FR"
        pile.color = color
        pile.listIndex = 0
        pile.cards = if (langIsNL) mutableListOf(
            Card("Hallo", "Bonjour", 1).apply { listIndex = cardIndex++ },
            Card("Hebben", "Avoir", 1).apply { listIndex = cardIndex++ },
            Card("Plezier", "Amusement", 1).apply { listIndex = cardIndex++ },
            Card("Gebruiken", "Utiliser", 1).apply { listIndex = cardIndex++ },
            Card("Dit", "Cette", 1).apply { listIndex = cardIndex++ },
            Card("Applicatie", "Application", 1).apply { listIndex = cardIndex++ })
        else mutableListOf(
            Card("Hello", "Bonjour", 1).apply { listIndex = cardIndex++ },
            Card("Have", "Avoir", 1).apply { listIndex = cardIndex++ },
            Card("Fun", "Amusement", 1).apply { listIndex = cardIndex++ },
            Card("Using", "Utiliser", 1).apply { listIndex = cardIndex++ },
            Card("This", "Cette", 1).apply { listIndex = cardIndex++ },
            Card("App", "Application", 1).apply { listIndex = cardIndex++ })
        return pile
    }

    fun getCapitals(color: Int): Pile {
        val langIsNL = Locale.getDefault().language == "nl"
        var cardIndex = 0
        val pile = if (langIsNL) Pile("Hoofdsteden") else Pile("Capitals")
        pile.id = 2
        pile.languageCardFront = if (langIsNL) "nl-NL" else "en-GB"
        pile.languageCardBack = if (langIsNL) "nl-NL" else "en-GB"
        pile.color = color
        pile.listIndex = 1
        pile.cards = if (langIsNL) mutableListOf(
            Card("Frankrijk", "Parijs", 2).apply { listIndex = cardIndex++ },
            Card("Duitsland", "Berlijn", 2).apply { listIndex = cardIndex++ },
            Card("Rusland", "Moskou", 2).apply { listIndex = cardIndex++ },
            Card("Turkije", "Istanbul", 2).apply { listIndex = cardIndex++ },
            Card("Verenigd Koninkrijk", "Londen", 2).apply { listIndex = cardIndex++ },
            Card("Brazilië", "Brasilia", 2).apply { listIndex = cardIndex++ },
            Card("Indonesië", "Jakarta", 2).apply { listIndex = cardIndex++ },
            Card("Japan", "Tokyo", 2).apply { listIndex = cardIndex++ },
            Card("Libië", "Tripoli", 2).apply { listIndex = cardIndex++ })
        else mutableListOf(
            Card("France", "Paris", 2).apply { listIndex = cardIndex++ },
            Card("Germany", "Berlin", 2).apply { listIndex = cardIndex++ },
            Card("Russia", "Moscow", 2).apply { listIndex = cardIndex++ },
            Card("Turkey", "Istanbul", 2).apply { listIndex = cardIndex++ },
            Card("United Kingdom", "London", 2).apply { listIndex = cardIndex++ },
            Card("Brazil", "Brasilia", 2).apply { listIndex = cardIndex++ },
            Card("Indonesia", "Jakarta", 2).apply { listIndex = cardIndex++ },
            Card("Japan", "Tokyo", 2).apply { listIndex = cardIndex++ },
            Card("Libya", "Tripoli", 2).apply { listIndex = cardIndex++ })
        return pile
    }

    fun getPeriodicTable(color: Int): Pile {
        val langIsNL = Locale.getDefault().language == "nl"
        var cardIndex = 0
        val pile = if (langIsNL) Pile("Periodiek systeem") else Pile("Periodic table")
        pile.id = 3
        pile.languageCardFront = if (langIsNL) "nl-NL" else "en-GB"
        pile.languageCardBack = if (langIsNL) "nl-NL" else "en-GB"
        pile.color = color
        pile.listIndex = 2
        pile.cards = if (langIsNL) mutableListOf(
            Card("He", "Helium", 3).apply { listIndex = cardIndex++ },
            Card("Li", "Lythium", 3).apply { listIndex = cardIndex++ },
            Card("Be", "Beryllium", 3).apply { listIndex = cardIndex++ },
            Card("B", "Borium", 3).apply { listIndex = cardIndex++ },
            Card("C", "Koolstof", 3).apply { listIndex = cardIndex++ },
            Card("N", "Stikstof", 3).apply { listIndex = cardIndex++ },
            Card("O", "Zuurstof", 3).apply { listIndex = cardIndex++ },
            Card("F", "Fluorine", 3).apply { listIndex = cardIndex++ },
            Card("Ne", "Neon", 3).apply { listIndex = cardIndex++ },
            Card("Na", "Sodium", 3).apply { listIndex = cardIndex++ },
            Card("Mg", "Magnesium", 3).apply { listIndex = cardIndex++ },
            Card("Al", "Aluminium", 3).apply { listIndex = cardIndex++ },
            Card("Si", "Silicium", 3).apply { listIndex = cardIndex++ },
            Card("P", "Fosfor", 3).apply { listIndex = cardIndex++ },
            Card("S", "Zwavel", 3).apply { listIndex = cardIndex++ },
            Card("Ar", "Argon", 3).apply { listIndex = cardIndex++ },
            Card("K", "Kalium", 3).apply { listIndex = cardIndex++ })
        else mutableListOf(Card("H", "Hydrogen", 3).apply { listIndex = cardIndex++ },
            Card("He", "Helium", 3).apply { listIndex = cardIndex++ },
            Card("Li", "Lythium", 3).apply { listIndex = cardIndex++ },
            Card("Be", "Beryllium", 3).apply { listIndex = cardIndex++ },
            Card("B", "Boron", 3).apply { listIndex = cardIndex++ },
            Card("C", "Carbon", 3).apply { listIndex = cardIndex++ },
            Card("N", "Nitrogen", 3).apply { listIndex = cardIndex++ },
            Card("O", "Oxygen", 3).apply { listIndex = cardIndex++ },
            Card("F", "Fluorine", 3).apply { listIndex = cardIndex++ },
            Card("Ne", "Neon", 3).apply { listIndex = cardIndex++ },
            Card("Na", "Sodium", 3).apply { listIndex = cardIndex++ },
            Card("Mg", "Magnesium", 3).apply { listIndex = cardIndex++ },
            Card("Al", "Aluminium", 3).apply { listIndex = cardIndex++ },
            Card("Si", "Silicon", 3).apply { listIndex = cardIndex++ },
            Card("P", "Phosphorus", 3).apply { listIndex = cardIndex++ },
            Card("S", "Sulfur", 3).apply { listIndex = cardIndex++ },
            Card("Ar", "Argon", 3).apply { listIndex = cardIndex++ },
            Card("K", "Potassium", 3).apply { listIndex = cardIndex++ },
            Card("Ca", "Calcium", 3).apply { listIndex = cardIndex++ },
            Card("Sc", "Scandium", 3).apply { listIndex = cardIndex++ },
            Card("Ti", "Titanium", 3).apply { listIndex = cardIndex++ },
            Card("V", "Vanadium", 3).apply { listIndex = cardIndex++ },
            Card("Cr", "Chromium", 3).apply { listIndex = cardIndex++ },
            Card("Mn", "Manganese", 3).apply { listIndex = cardIndex++ },
            Card("Fe", "Iron", 3).apply { listIndex = cardIndex++ },
            Card("Co", "Cobalt", 3).apply { listIndex = cardIndex++ },
            Card("Ni", "Nickel", 3).apply { listIndex = cardIndex++ },
            Card("Cu", "Copper", 3).apply { listIndex = cardIndex++ },
            Card("Zn", "Zinc", 3).apply { listIndex = cardIndex++ },
            Card("Ga", "Gallium", 3).apply { listIndex = cardIndex++ },
            Card("Ge", "Germanium", 3).apply { listIndex = cardIndex++ },
            Card("As", "Arsenic", 3).apply { listIndex = cardIndex++ },
            Card("Se", "Selenium", 3).apply { listIndex = cardIndex++ },
            Card("Br", "Bromine", 3).apply { listIndex = cardIndex++ },
            Card("Kr", "Krypton", 3).apply { listIndex = cardIndex++ },
            Card("Rb", "Rubidium", 3).apply { listIndex = cardIndex++ },
            Card("Sr", "Strontium", 3).apply { listIndex = cardIndex++ },
            Card("Y", "Yttrium", 3).apply { listIndex = cardIndex++ },
            Card("Zr", "Zirconium", 3).apply { listIndex = cardIndex++ },
            Card("Nb", "Niobium", 3).apply { listIndex = cardIndex++ },
            Card("Mo", "Molybdenum", 3).apply { listIndex = cardIndex++ },
            Card("Tc", "Technetium", 3).apply { listIndex = cardIndex++ },
            Card("Ru", "Ruthenium", 3).apply { listIndex = cardIndex++ },
            Card("Rh", "Rhodium", 3).apply { listIndex = cardIndex++ },
            Card("Pd", "Palladium", 3).apply { listIndex = cardIndex++ },
            Card("Ag", "Silver", 3).apply { listIndex = cardIndex++ },
            Card("Cd", "Cadmium", 3).apply { listIndex = cardIndex++ },
            Card("In", "Indium", 3).apply { listIndex = cardIndex++ },
            Card("Sn", "Tin", 3).apply { listIndex = cardIndex++ },
            Card("Te", "Tellurium", 3).apply { listIndex = cardIndex++ },
            Card("I", "Iodine", 3).apply { listIndex = cardIndex++ },
            Card("Xe", "Xenon", 3).apply { listIndex = cardIndex++ },
            Card("Cs", "Caesium", 3).apply { listIndex = cardIndex++ },
            Card("Ba", "Barium", 3).apply { listIndex = cardIndex++ },
            Card("Lu", "Lutetium", 3).apply { listIndex = cardIndex++ },
            Card("Hf", "Hafnium", 3).apply { listIndex = cardIndex++ },
            Card("Ta", "Tantalum", 3).apply { listIndex = cardIndex++ },
            Card("W", "Tungsten", 3).apply { listIndex = cardIndex++ },
            Card("Re", "Rhenium", 3).apply { listIndex = cardIndex++ },
            Card("Os", "Osmium", 3).apply { listIndex = cardIndex++ },
            Card("Ir", "Iridium", 3).apply { listIndex = cardIndex++ },
            Card("Pt", "Platinum", 3).apply { listIndex = cardIndex++ },
            Card("Au", "Gold", 3).apply { listIndex = cardIndex++ },
            Card("Hg", "Mercury", 3).apply { listIndex = cardIndex++ },
            Card("Tl", "Thallium", 3).apply { listIndex = cardIndex++ },
            Card("Pb", "Lead", 3).apply { listIndex = cardIndex++ },
            Card("Bi", "Bismuth", 3).apply { listIndex = cardIndex++ },
            Card("Po", "Polonium", 3).apply { listIndex = cardIndex++ },
            Card("At", "Astatine", 3).apply { listIndex = cardIndex++ },
            Card("Rn", "Radon", 3).apply { listIndex = cardIndex++ },
            Card("Fr", "Francium", 3).apply { listIndex = cardIndex++ },
            Card("Ra", "Radium", 3).apply { listIndex = cardIndex++ },
            Card("Lr", "Lawrencium", 3).apply { listIndex = cardIndex++ },
            Card("Rf", "Rutherfordium", 3).apply { listIndex = cardIndex++ },
            Card("Db", "Dubnium", 3).apply { listIndex = cardIndex++ },
            Card("Sg", "Seaborgium", 3).apply { listIndex = cardIndex++ },
            Card("Bh", "Bohrium", 3).apply { listIndex = cardIndex++ },
            Card("Hs", "Hassium", 3).apply { listIndex = cardIndex++ },
            Card("Mt", "Meitnerium", 3).apply { listIndex = cardIndex++ },
            Card("Ds", "Darmstadtium", 3).apply { listIndex = cardIndex++ },
            Card("Cn", "Copernicium", 3).apply { listIndex = cardIndex++ },
            Card("Nh", "Nihonium", 3).apply { listIndex = cardIndex++ },
            Card("Fl", "Flerovium", 3).apply { listIndex = cardIndex++ },
            Card("Mc", "Moscovium", 3).apply { listIndex = cardIndex++ },
            Card("Lv", "Livermorium", 3).apply { listIndex = cardIndex++ },
            Card("Og", "Oganesson", 3).apply { listIndex = cardIndex++ })
        return pile
    }

    fun getPersonalPile(color: Int): Pile {
        val langIsNL = Locale.getDefault().language == "nl"
        val pile = if (langIsNL) Pile("Jouw eigen stapel") else Pile("Your pile")
        pile.id = 4
        pile.languageCardFront = if (langIsNL) "nl-NL" else "en-GB"
        pile.languageCardBack = if (langIsNL) "nl-NL" else "en-GB"
        pile.color = color
        pile.listIndex = 3
        return pile
    }
}