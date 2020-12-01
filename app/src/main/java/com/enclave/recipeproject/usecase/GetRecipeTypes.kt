package com.enclave.recipeproject.usecase

import android.content.Context
import com.enclave.recipeproject.model.RecipeType
import io.reactivex.Single
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import java.io.InputStream
import java.lang.Exception
import javax.inject.Inject
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory

class GetRecipeTypes @Inject constructor(
    private val context: Context
) {
    companion object {
        private const val RECIPE_TYPE = "recipetype"
        private const val XML_FILE_DIRECTORY = "xml/recipe_types.xml"
        private const val RECIPE_TYPE_ID = "id"
        private const val RECIPE_TYPE_TITLE = "title"
    }

    fun execute(): Single<List<RecipeType>> = Single.defer {
        val listRecipeTypes = mutableListOf<RecipeType>()
        try {
            val inputStream: InputStream = context.assets.open(XML_FILE_DIRECTORY)
            val dbFactory: DocumentBuilderFactory = DocumentBuilderFactory.newInstance()
            val dBuilder: DocumentBuilder = dbFactory.newDocumentBuilder()
            val doc: Document = dBuilder.parse(inputStream)
            val element: Element = doc.documentElement
            element.normalize()
            val nList: NodeList = doc.getElementsByTagName(RECIPE_TYPE)
            for (i in 0 until nList.length) {
                val node: Node = nList.item(i)
                if (node.nodeType == Node.ELEMENT_NODE) {
                    val element = node as Element
                    val id = getValue(RECIPE_TYPE_ID, element)
                    val title = getValue(RECIPE_TYPE_TITLE, element)
                    if (id != null && title != null)
                        listRecipeTypes.add(RecipeType(id.toInt(), title))
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        Single.just(listRecipeTypes)
    }

    private fun getValue(tag: String, element: Element): String? {
        val nodeList = element.getElementsByTagName(tag).item(0).childNodes
        val node: Node? = nodeList.item(0)
        return node?.nodeValue
    }
}