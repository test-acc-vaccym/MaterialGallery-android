package com.numero.material_gallery.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import com.numero.material_gallery.R
import com.numero.material_gallery.fragment.ColorInfoBottomSheetDialog
import com.numero.material_gallery.model.Corner
import com.numero.material_gallery.model.Elevation
import com.numero.material_gallery.repository.IConfigRepository
import kotlinx.android.synthetic.main.activity_material_card.*
import org.koin.android.ext.android.inject

class MaterialCardActivity : AppCompatActivity() {

    private val configRepository by inject<IConfigRepository>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(configRepository.themeRes)
        setContentView(R.layout.activity_material_card)
        setSupportActionBar(toolbar)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_arrow_back)
        }

        elevationSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (progress >= Elevation.values().size) {
                    return
                }
                updateElevation(Elevation.values()[progress])
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })
        cornerSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                updateCorner(Corner.values()[progress])
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })

        updateElevation(Elevation.values()[elevationSeekBar.progress])
        updateCorner(Corner.values()[cornerSeekBar.progress])
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_common, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_info -> {
                ColorInfoBottomSheetDialog.newInstance().showIfNeed(supportFragmentManager)
                true
            }
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun updateElevation(elevation: Elevation) {
        val elevationSize = resources.getDimension(elevation.dimenRes)
        materialCardView.cardElevation = elevationSize
        elevationInfoTextView.text = getString(R.string.elevation_info_format).format(convertPxToDp(elevationSize).toInt())
    }

    private fun updateCorner(corner: Corner) {
        val cornerSize = resources.getDimension(corner.dimenRes)
        materialCardView.radius = cornerSize
        cornerInfoTextView.text = getString(R.string.corner_info_format).format(convertPxToDp(cornerSize).toInt())
    }

    private fun convertPxToDp(px: Float): Float {
        val metrics = resources.displayMetrics
        return px / metrics.density
    }

    companion object {
        fun createIntent(context: Context): Intent = Intent(context, MaterialCardActivity::class.java)
    }
}