package div.honwakadeveloper.searchrepo.ui

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import div.honwakadeveloper.searchrepo.R
import div.honwakadeveloper.searchrepo.databinding.ActivityMainBinding
import div.honwakadeveloper.searchrepo.ui.repos.RepoEpoxyController
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        DataBindingUtil.setContentView<ActivityMainBinding>(
            this, R.layout.activity_main
        ).apply {

            val controller = RepoEpoxyController()

            viewModel = mainViewModel.apply {

                lifecycleOwner = this@MainActivity

                reposData.observe(this@MainActivity, Observer {
                    // TODO リポジトリリスト更新処理
                    println("$reposData")

                    controller.setData(reposData.value?.items)
                })
            }

            mainReposView.setController(controller)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager

        val searchView = (menu.findItem(R.id.search).actionView as SearchView).apply {
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
            setIconifiedByDefault(false)
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                if (newText.isNullOrBlank()) return false

                mainViewModel.updateSearchWord(newText)
                return true
            }
        })
        return true
    }
}
