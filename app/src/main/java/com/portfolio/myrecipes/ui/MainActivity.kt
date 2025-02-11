package com.portfolio.myrecipes.ui

import android.content.Intent

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.recyclerview.widget.LinearLayoutManager
import com.portfolio.myrecipes.R
import com.portfolio.myrecipes.data.Recipes
import com.portfolio.myrecipes.databinding.ActivityMainBinding

@ExperimentalMaterial3Api
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val list = ArrayList<Recipes>()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.myToolbar)

        list.addAll(getListRecipes())
        showRecyclerList()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_about -> {
                val intent = Intent(this, AboutActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getListRecipes(): ArrayList<Recipes>{
        val dataName = resources.getStringArray(R.array.data_name)
        val dataTime = resources.getStringArray(R.array.data_time)
        val dataServing = resources.getStringArray(R.array.data_serving)
        val dataIngredients = resources.getStringArray(R.array.data_ingredients)
        val dataStep = resources.getStringArray(R.array.data_step)
        val dataPhoto = resources.getStringArray(R.array.data_photo)
        val listRecipes = ArrayList<Recipes>()
        for (i in dataName.indices) {
            val recipes = Recipes(dataName[i], dataTime[i], dataServing[i], dataIngredients[i], dataStep[i], dataPhoto[i])
            listRecipes.add(recipes)
        }
        return listRecipes
    }

    private fun showRecyclerList() {
        binding.rvRecipes.layoutManager = LinearLayoutManager(this)
        val listRecipesAdapter = ListRecipesAdapter(list)
        binding.rvRecipes.adapter = listRecipesAdapter
        listRecipesAdapter.setOnItemClickCallback(object : ListRecipesAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Recipes) {
                showSelectedRecipe(data)
            }
        })
    }

    private fun showSelectedRecipe(recipes: Recipes) {
        val intent = Intent(this@MainActivity, DetailActivity::class.java)
        intent.putExtra(DetailActivity.EXTRA_NAME, recipes.name)
        intent.putExtra(DetailActivity.EXTRA_TIME, recipes.time)
        intent.putExtra(DetailActivity.EXTRA_SERVING, recipes.serving)
        intent.putExtra(DetailActivity.EXTRA_INGREDIENT, recipes.ingredient)
        intent.putExtra(DetailActivity.EXTRA_STEP, recipes.step)
        intent.putExtra(DetailActivity.EXTRA_PHOTO, recipes.photo)
        startActivity(intent)
    }
}