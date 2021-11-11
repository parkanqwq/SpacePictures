package com.kalabukhov.app.spacepictures.ui.open_image

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.github.terrakok.cicerone.androidx.AppNavigator
import com.kalabukhov.app.spacepictures.R
import com.kalabukhov.app.spacepictures.app
import com.kalabukhov.app.spacepictures.databinding.ActivityOpenImageBinding
import com.kalabukhov.app.spacepictures.domain.entity.ImageSpaceDbEntity
import com.kalabukhov.app.spacepictures.ui.profile.ProfileContract
import com.squareup.picasso.Picasso
import moxy.MvpAppCompatActivity
import moxy.ktx.moxyPresenter
import java.net.URI

class OpenImage : MvpAppCompatActivity(), OpenImageContract.View {

    private val presenter by moxyPresenter { OpenImagePresenter(app.router) }

    private lateinit var binding: ActivityOpenImageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOpenImageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViewToolBar()
        initView()
    }

    private fun initView() {
        Picasso.get()
            .load(intent.getStringExtra(IMAGE))
            .placeholder(R.drawable.ic_no_photo_vector)
            .error(R.drawable.ic_load_error_vector)
            .into(binding.spaceImageView)
        binding.nameTittleTextView.text = intent.getStringExtra(TITTLE)
        binding.textTextView.text = intent.getStringExtra(TEXT)
    }

    override fun setState(state: OpenImageContract.ViewState) {
        when (state) {
            OpenImageContract.ViewState.DELETE -> {
                Toast.makeText(this, resources.getText(R.string.delete),
                    Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initViewToolBar() {
        val toolbar: Toolbar = initToolbar()
    }

    private fun initToolbar(): Toolbar {
        val toolbar = findViewById<Toolbar>(R.id.open_image_tool_bar)
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
            R.id.action_download_image_menu -> {
                Toast.makeText(this, "Start download",
                    Toast.LENGTH_SHORT).show() // TODO: 11.11.2021
                return true
            }
            R.id.action_delete_image_menu -> {
                val imageSpaceDbEntity = ImageSpaceDbEntity(
                intent.getStringExtra(ID).toString(),
                intent.getStringExtra(TITTLE).toString(),
                intent.getStringExtra(IMAGE).toString(),
                intent.getStringExtra(TEXT).toString()
                )
                presenter.onDelete(app, imageSpaceDbEntity)
                return true
            }
        }
        return false
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.open_image_menu, menu)
        return true
    }

    private val navigator by lazy { AppNavigator(this, binding.root.id) }

    override fun onResumeFragments() {
        super.onResumeFragments()
        app.navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        app.navigatorHolder.removeNavigator()
        super.onPause()
    }

    companion object {
        const val ID = "id"
        const val IMAGE = "image"
        const val TITTLE = "tittle"
        const val TEXT = "text"
    }
}