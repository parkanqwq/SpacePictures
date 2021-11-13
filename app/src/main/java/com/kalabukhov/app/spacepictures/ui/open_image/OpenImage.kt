package com.kalabukhov.app.spacepictures.ui.open_image

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.view.drawToBitmap
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import com.github.terrakok.cicerone.androidx.AppNavigator
import com.kalabukhov.app.spacepictures.R
import com.kalabukhov.app.spacepictures.app
import com.kalabukhov.app.spacepictures.databinding.ActivityOpenImageBinding
import com.kalabukhov.app.spacepictures.domain.ImageSpaceRepo
import com.kalabukhov.app.spacepictures.domain.entity.ImageSpaceDbEntity
import com.squareup.picasso.Picasso
import moxy.MvpAppCompatActivity
import moxy.ktx.moxyPresenter
import java.util.*
import javax.inject.Inject


class OpenImage : MvpAppCompatActivity(), OpenImageContract.View {

    @Inject
    lateinit var imageRepo: ImageSpaceRepo

    @Inject
    lateinit var navigatorHolder: NavigatorHolder

    @Inject
    lateinit var router: Router

    private val presenter by moxyPresenter { OpenImagePresenter(router, this@OpenImage) }

    private lateinit var binding: ActivityOpenImageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        app.appComponent.inject(this)
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
            OpenImageContract.ViewState.SAVE -> {
                Toast.makeText(this, resources.getText(R.string.image_save),
                    Toast.LENGTH_SHORT).show()
            }
            OpenImageContract.ViewState.NOT_SAVE -> {
                Toast.makeText(this, resources.getText(R.string.image_not_save),
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
                val bitmap = binding.spaceImageView.drawToBitmap()
                presenter.onSaveImageToPhone(bitmap, contentResolver, intent)
                return true
            }
            R.id.action_delete_image_menu -> {
                val imageSpaceDbEntity = ImageSpaceDbEntity(
                intent.getStringExtra(ID).toString(),
                intent.getStringExtra(TITTLE).toString(),
                intent.getStringExtra(IMAGE).toString(),
                intent.getStringExtra(TEXT).toString()
                )
                presenter.onDelete(imageRepo, imageSpaceDbEntity)
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
        navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        navigatorHolder.removeNavigator()
        super.onPause()
    }

    companion object {
        const val ID = "id"
        const val IMAGE = "image"
        const val TITTLE = "tittle"
        const val TEXT = "text"
        const val PERMISSION_CODE = 42
    }
}