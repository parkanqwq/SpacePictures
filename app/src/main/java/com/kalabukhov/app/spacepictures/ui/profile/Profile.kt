package com.kalabukhov.app.spacepictures.ui.profile

import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.androidx.AppNavigator
import com.kalabukhov.app.spacepictures.R
import com.kalabukhov.app.spacepictures.adapter.AdapterImagesDb
import com.kalabukhov.app.spacepictures.app
import com.kalabukhov.app.spacepictures.databinding.ActivityProfileBinding
import com.kalabukhov.app.spacepictures.domain.ImageSpaceRepo
import com.kalabukhov.app.spacepictures.domain.entity.ImageSpaceDbEntity
import com.kalabukhov.app.spacepictures.ui.open_image.OpenImage
import moxy.MvpAppCompatActivity
import moxy.ktx.moxyPresenter
import javax.inject.Inject

class Profile : MvpAppCompatActivity(), ProfileContract.View {

    @Inject
    lateinit var imageRepo: ImageSpaceRepo

    @Inject
    lateinit var navigatorHolder: NavigatorHolder

    private val presenter by moxyPresenter { ProfilePresenter() }

    private lateinit var binding: ActivityProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        app.appComponent.inject(this)
        initView()
        initViewToolBar()
    }

    private fun initView() {
        binding.imageSpaceRecyclerView.adapter = adapterImages
        presenter.onLoadingImageDb(imageRepo)
    }

    override fun setState(state: ProfileContract.ViewState) {
        when (state) {
            ProfileContract.ViewState.LOADING -> {

            }
            ProfileContract.ViewState.SUCCESSFUL -> {

            }
            ProfileContract.ViewState.ERROR -> {

            }
        }
    }

    override fun getImageForAdapter(imageSpaceDbEntity: List<ImageSpaceDbEntity>) {
        adapterImages.setImagesDbEntity(imageSpaceDbEntity.asReversed())
    }

    private val onObjectListener = object : OnItemViewClickListener {
        override fun onItemViewClick(imageSpaceDbEntity: ImageSpaceDbEntity) {
            startActivity(Intent(this@Profile, OpenImage::class.java)
                .putExtra(ID, imageSpaceDbEntity.id)
                .putExtra(IMAGE, imageSpaceDbEntity.url)
                .putExtra(TITTLE, imageSpaceDbEntity.title)
                .putExtra(TEXT, imageSpaceDbEntity.explanation),
            ActivityOptions.makeSceneTransitionAnimation(
                this@Profile,
            binding.imageSpaceRecyclerView,
                IMAGE_TAG)
                .toBundle())
        }
    }

    interface OnItemViewClickListener {
        fun onItemViewClick(imageSpaceDbEntity: ImageSpaceDbEntity)
    }

    private val adapterImages = AdapterImagesDb(onObjectListener)

    private val navigator by lazy { AppNavigator(this, binding.root.id) }

    override fun onResumeFragments() {
        super.onResumeFragments()
        navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        navigatorHolder.removeNavigator()
        super.onPause()
    }

    private fun initViewToolBar() {
        val toolbar: Toolbar = initToolbar()
    }

    private fun initToolbar(): Toolbar {
        val toolbar = findViewById<Toolbar>(R.id.profile_tool_bar)
        setSupportActionBar(toolbar)
        return toolbar
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        return if (navigateFragment(id)) {
            true
        } else super.onOptionsItemSelected(item)
    }

    private fun navigateFragment(id: Int): Boolean {
        when (id) {
            R.id.action_delete_all_image_menu -> {
                presenter.onDeleteAll(imageRepo)
                return true
            }
        }
        return false
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.profile_menu, menu)
        return true
    }

    companion object {
        const val ID = "id"
        const val IMAGE_TAG = "image_tag"
        const val IMAGE = "image"
        const val TITTLE = "tittle"
        const val TEXT = "text"

        fun profileScreen(context: Context): Intent {
            return Intent(context, Profile::class.java)
        }
    }
}