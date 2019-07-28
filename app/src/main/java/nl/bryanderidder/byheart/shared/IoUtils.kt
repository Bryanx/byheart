package nl.bryanderidder.byheart.shared

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import com.google.gson.GsonBuilder
import com.opencsv.CSVWriter
import nl.bryanderidder.byheart.card.Card
import nl.bryanderidder.byheart.pile.Pile
import java.io.File
import java.io.FileWriter

/**
 * Some util functions for importing and exporting content
 * @author Bryan de Ridder
 */
object IoUtils {
    fun createCSV(context: Context, cards: MutableList<Card>, name: String): Uri? {
        val file = File(context.cacheDir, name)
        writeCSV(cards, file.path)
        val contentUri = FileProvider.getUriForFile(context, "nl.bryanderidder.byheart", file)
        file.deleteOnExit()
        return contentUri
    }

    private fun writeCSV(cards: List<Card>, path: String) {
        val header = arrayOf("front", "back")
        val writer = FileWriter(path)
        val csv = CSVWriter(writer)
        csv.writeNext(header, false)
        for (card in cards) {
            val columns = arrayOf(card.question, card.answer)
            csv.writeNext(columns, false)
        }
        csv.close()
        writer.close()
    }

    fun createJson(context: Context, pile: Pile, name: String): Uri? {
        val gson = GsonBuilder().addSerializationExclusionStrategy(JsonExclusionStrategy).setPrettyPrinting().create()
        val file = File(context.cacheDir, name)
        val writer = FileWriter(file.path)
        writer.write(gson.toJson(pile))
        writer.close()
        val contentUri = FileProvider.getUriForFile(context, "nl.bryanderidder.byheart", file)
        file.deleteOnExit()
        return contentUri
    }

    fun readJson(data: String): Pile {
        val gson = GsonBuilder().addDeserializationExclusionStrategy(JsonExclusionStrategy).create()
        return gson.fromJson(data, Pile::class.java)
    }
}