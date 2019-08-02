package nl.bryanderidder.byheart.shared

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.FileProvider
import com.google.gson.GsonBuilder
import com.opencsv.CSVWriter
import nl.bryanderidder.byheart.card.Card
import nl.bryanderidder.byheart.pile.Pile
import java.io.BufferedReader
import java.io.File
import java.io.FileWriter
import java.io.InputStream

/**
 * Some util functions for importing and exporting content
 * @author Bryan de Ridder
 */
object IoUtils {
    fun createCSV(context: Context, cards: List<Card>, name: String) {
        val file = File(context.cacheDir, name)
        writeCSV(cards, file.path)
        val contentUri = FileProvider.getUriForFile(context, "nl.bryanderidder.byheart", file)
        file.deleteOnExit()
        exportData(context as Activity, contentUri)
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

    fun createJson(context: Context, pile: Pile, name: String) {
        val gson = GsonBuilder().addSerializationExclusionStrategy(JsonExclusionStrategy).setPrettyPrinting().create()
        val file = File(context.cacheDir, name)
        val writer = FileWriter(file.path)
        writer.write(gson.toJson(pile))
        writer.close()
        val contentUri = FileProvider.getUriForFile(context, "nl.bryanderidder.byheart", file)
        file.deleteOnExit()
        exportData(context as Activity, contentUri)
    }

    fun readJson(data: InputStream?): Pile {
        return data?.bufferedReader()?.use(BufferedReader::readText)?.let {
            val gson = GsonBuilder().addDeserializationExclusionStrategy(JsonExclusionStrategy).create()
            return gson.fromJson(it, Pile::class.java)
        }!!
    }

    private fun exportData(activity: Activity?, uri: Uri) {
        val extension = uri.toString().getExtension()
        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/$extension"
            putExtra(Intent.EXTRA_STREAM, uri)
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        }
        activity?.startActivity(intent)
    }

    fun readCSV(content: InputStream?) : List<Card> {
        val cards = mutableListOf<Card>()
        content?.bufferedReader()?.useLines { it.forEachIndexed { i, line ->
            if (i!=0 && line.contains(",")) {
                val splittedLine = line.split(',')
                cards.add(Card(splittedLine[0],splittedLine[1]))
            }
        }}
        return cards
    }
}
