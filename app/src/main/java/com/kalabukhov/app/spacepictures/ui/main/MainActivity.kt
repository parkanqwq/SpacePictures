package com.kalabukhov.app.spacepictures.ui.main

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import com.github.terrakok.cicerone.androidx.AppNavigator
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.kalabukhov.app.spacepictures.IScreens
import com.kalabukhov.app.spacepictures.R
import com.kalabukhov.app.spacepictures.app
import com.kalabukhov.app.spacepictures.databinding.ActivityMainBinding
import com.kalabukhov.app.spacepictures.domain.ImageSpaceRepo
import com.kalabukhov.app.spacepictures.domain.entity.ImageSpaceEntity
import com.kalabukhov.app.spacepictures.rest.SpaceImageApi
import com.squareup.picasso.Picasso
import moxy.MvpAppCompatActivity
import moxy.ktx.moxyPresenter
import java.time.LocalDate
import javax.inject.Inject

class MainActivity : MvpAppCompatActivity(), MainContract.View {

    private var dateDayMinus = 0L
    private val dateNow = LocalDate.now()
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    @Inject
    lateinit var imageRepo: ImageSpaceRepo

    @Inject
    lateinit var spaceImageApi: SpaceImageApi

    @Inject
    lateinit var navigatorHolder: NavigatorHolder

    @Inject
    lateinit var router: Router

    @Inject
    lateinit var screens: IScreens

    private val presenter by moxyPresenter { MainPresenter(router, screens) }

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        app.appComponent.inject(this)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViewToolBar()
        initView()
        setBottomSheetBehavior(binding.root.
        findViewById(R.id.bottom_sheet_container))
    }

    private fun setBottomSheetBehavior(bottomSheet: ConstraintLayout) {
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_COLLAPSED ->
                        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN }
            }
            override fun onSlide(bottomSheet: View, slideOffset: Float) {}})
    }

    private fun initView() {
        presenter.onLoadImageWeb(spaceImageApi, dateNow.toString())
        binding.nextImageBtnView.setOnClickListener {
            dateDayMinus++
            loadImageWeb(dateNow.minusDays(dateDayMinus).toString())
        }
    }

    override fun loadImageWeb(dateNow: String) {
        presenter.onLoadImageWeb(spaceImageApi, dateNow)
    }

    override fun showImageWed(imageSpaceEntity: ImageSpaceEntity) {
        val textSpaceTextView = binding.root.findViewById<TextView>(R.id.text_space_text_view)
        binding.nameTittleTextView.text = imageSpaceEntity.title
        textSpaceTextView.text = imageSpaceEntity.explanation
        Picasso.get()
            .load(imageSpaceEntity.url)
            .placeholder(R.drawable.ic_no_photo_vector)
            .error(R.drawable.ic_load_error_vector)
            .into(binding.spaceImageView)
    }

    override fun errorLoadImage(thr: String) {
        Toast.makeText(this, thr, Toast.LENGTH_SHORT).show()
    }

    override fun setState(state: MainContract.ViewState) {
        when (state) {
            MainContract.ViewState.SUCCESSFUL -> {
                binding.progressBar.visibility = View.GONE
            }
            MainContract.ViewState.ERROR -> {
                binding.progressBar.visibility = View.GONE
            }
            MainContract.ViewState.LOADING -> {
                binding.progressBar.visibility = View.VISIBLE
            }
            MainContract.ViewState.DOWNLOAD -> {
                Toast.makeText(this, resources.getText(R.string.successful),
                    Toast.LENGTH_SHORT).show()
            }
        }
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

    private fun initViewToolBar() {
        val toolbar: Toolbar = initToolbar()
    }

    private fun initToolbar(): Toolbar {
        val toolbar = findViewById<Toolbar>(R.id.main_tool_bar)
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
            R.id.action_profile_menu -> {
                presenter.onProfileActivity()
                return true
                }
            R.id.action_history_menu -> {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
                return true
            }
            R.id.action_save_menu -> {
                presenter.onSaveImageDb(imageRepo)
            }
        }
        return false
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }
}